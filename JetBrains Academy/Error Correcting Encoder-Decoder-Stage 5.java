package correcter;

import java.io.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Main {
    /***
     * Get value of bit within a Message
     * @param byteValue: selected byte
     * @param bitPos: index of selected bit within selected byte
     * @return : integer value of bit at selected position
     */
    public static int getBit(byte byteValue, int bitPos) {
        return (byteValue >> bitPos) & 1;
    }

    /***
     * Set value of bit within a Message
     * @param byteValue: selected byte to modify
     * @param bitPos: index of selected bit within selected byte
     * @param val: bit value to be inserted at selected position
     */
    public static byte setBit(byte byteValue, int bitPos, int val) {
        return (byte) (val == 0 ? byteValue & ~(1 << bitPos) : byteValue | (1 << bitPos));
    }

    /***
     * Flip 1 randomly selected bit in every byte of the received Message
     * @param message: encoded Message
     */
    public static void garbleTransmission(Message message) {
        Random r = new Random();
        int bitIndex, flipBit, messageIndex;

        messageIndex = 0;
        for (byte b: message.message) {
            bitIndex = r.nextInt(7);
            flipBit = getBit(b, bitIndex) == 0 ? 1 : 0;
            message.message[messageIndex++] = setBit(b, bitIndex, flipBit);
        }
    }

    static class Message {
        final public static int[] GENERATOR_MATRIX = {
                0b1101, 0b1011, 0b1000, 0b0111, 0b0100, 0b0010, 0b0001 };

        byte[] message;
        boolean isEncoded = false;
        int length;

        Message(byte[] message) {
            this.message = message;
            this.length = message.length;
        }

        public void setMessage(byte[] message, boolean encoded) {
            this.message = message;
            this.length = message.length;
            this.isEncoded = encoded;
        }

        public String toString() {
            return Arrays.toString(this.message);
        }

        public String toString(String mode) {
            if (mode.equals("text")) {
                return "text view: " + new String(this.message);
            } else {
                StringBuilder result = new StringBuilder();
                String preface;

                switch (mode) {
                    case "hex": preface = "hex view: "; break;
                    case "dec": preface = "decode: "; break;
                    case "exp": preface = "expand: "; break;
                    case "bin": preface = "bin view: "; break;
                    case "par": preface = "parity: "; break;
                    case "cor": preface = "correct: "; break;
                    case "rem": preface = "remove: "; break;
                    default: preface = "Unsupported argument: " + mode;
                }

                for (Byte b:this.message) {
                    String value;
                    if (mode.equals("hex")) {
                        value = String.format("%02x", b).toUpperCase(Locale.ROOT);
                    } else {
                        value = String.format("%8s", Integer.toBinaryString(b)).replace(" ", "0");
                        value = value.substring(value.length() - 8);
                    }

                    if (mode.equals("exp")) value = value.replaceAll("(..)$", "..");

                    result.append(value).append(" ");
                }

                return preface + result.toString();
            }
        }

        private void bitCorrection() {
            if (this.isEncoded) {
                int messageIndex = 0;
                for (byte b: this.message) {
                    int bit2_7 = getBit(b, 7);
                    int bit2_6 = getBit(b, 6);
                    int bit2_5 = getBit(b, 5);
                    int bit2_4 = getBit(b, 4);
                    int bit2_3 = getBit(b, 3);
                    int bit2_2 = getBit(b, 2);
                    int bit2_1 = getBit(b, 1);

                    int x = bit2_7 ^ bit2_5 ^ bit2_3 ^ bit2_1;
                    int y = bit2_6 ^ bit2_5 ^ bit2_2 ^ bit2_1;
                    int z = bit2_4 ^ bit2_3 ^ bit2_2 ^ bit2_1;

                    if (x == 1 && y == 0 && z == 0) b = setBit(b, 7, bit2_7 == 0 ? 1 : 0);
                    if (x == 0 && y == 1 && z == 0) b = setBit(b, 6, bit2_6 == 0 ? 1 : 0);
                    if (x == 0 && y == 0 && z == 1) b = setBit(b, 4, bit2_4 == 0 ? 1 : 0);
                    if (x == 1 && y == 1 && z == 0) b = setBit(b, 5, bit2_5 == 0 ? 1 : 0);
                    if (x == 1 && y == 0 && z == 1) b = setBit(b, 3, bit2_3 == 0 ? 1 : 0);
                    if (x == 0 && y == 1 && z == 1) b = setBit(b, 2, bit2_2 == 0 ? 1 : 0);
                    if (x == 1 && y == 1 && z == 1) b = setBit(b, 1, bit2_1 == 0 ? 1 : 0);

                    this.message[messageIndex++] = b;
                }
            }
        }

        /***
         * @param segment: 4-bit int to be encoded
         * @return : Hamming(7,4) encoded byte of 4-bit input
         */
        private byte hiLoEncoder(int segment) {
            byte result = 0;
            int el;
            for (int i = 7; i > 0; i--) {
                el = Integer.bitCount(GENERATOR_MATRIX[7-i] & segment) & 1;
                result = setBit(result, i, el);
            }

            return result;
        }

        public void encode() {
            if (!this.isEncoded) {
                // 2 bytes encoded for every 1 byte provided
                Message output = new Message(new byte[this.length * 2]);

                int outputIndex = 0;
                for (Byte b: this.message) {
                    output.message[outputIndex++] = hiLoEncoder(b >> 4);    // 4 hi bits
                    output.message[outputIndex++] = hiLoEncoder(b & 15);    // 4 lo bits
                }
                this.setMessage(output.message, true);
            }
        }

        public void decode() {
            if (this.isEncoded) {
                Message output = new Message(new byte[this.length / 2]);

                int currentByte;
                int outputByteIndex = 0;
                for (int i = 0; i < this.length; i++) {
                    currentByte = (this.message[i] >> 1 & 0b00000111) | (this.message[i] >> 2 & 0b00001000);
                    if (i % 2 == 0) {
                        output.message[outputByteIndex] = (byte) (currentByte << 4); // 4 hi bits
                    } else {
                        output.message[outputByteIndex++] |= currentByte;            // 4 lo bits
                    }
                }

                this.setMessage(output.message, false);
            }
        }
    }

    abstract static class Strategy {
        FileInputStream reader;
        FileOutputStream writer;
        byte[] input;
        Message message;

        Strategy(String inStr, String outStr) throws IOException {
            this.reader = new FileInputStream(inStr);
            this.writer = new FileOutputStream(outStr);
            this.input = reader.readAllBytes();
            this.message = new Message(this.input);
        }

        void cleanUp() throws IOException {
            this.writer.write(this.message.message);
            this.reader.close();
            this.writer.close();
        }

        abstract void execute();
    }

    public static class MessageEncoder extends Strategy {
        public MessageEncoder(String inStr, String outStr) throws IOException {
            super(inStr, outStr);
        }

        public void execute() {
            System.out.print("\nsend.txt:\n" + this.message.toString("text") + "\n");
            System.out.println(this.message.toString("hex") + "\n" + this.message.toString("bin"));
            this.message.encode();
            System.out.print("\nencoded.txt:\n" + this.message.toString("exp") + "\n");
            System.out.println(this.message.toString("par") + "\n" + this.message.toString("hex"));

            try {
                this.cleanUp();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static class MessageSender extends Strategy {
        public MessageSender(String inStr, String outStr) throws IOException {
            super(inStr, outStr);
        }

        public void execute() {
            System.out.print("\nencoded.txt:\n" + this.message.toString("hex") + "\n");
            System.out.println(this.message.toString("bin"));
            garbleTransmission(this.message);
            System.out.print("\nreceived.txt:\n" + this.message.toString("bin") + "\n");
            System.out.println(this.message.toString("hex"));

            try {
                this.cleanUp();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static class MessageDecoder extends Strategy {
        public MessageDecoder(String inStr, String outStr) throws IOException {
            super(inStr, outStr);
        }

        public void execute() {
            System.out.print("\nreceived.txt:\n" + this.message.toString("hex") + "\n");
            System.out.println(this.message.toString("bin"));
            this.message.isEncoded = true;
            this.message.bitCorrection();
            System.out.print("\ndecoded.txt:\n" + this.message.toString("cor") + "\n");
            this.message.decode();
            System.out.print(this.message.toString("dec") + "\n" + this.message.toString("rem") + "\n");
            System.out.println(this.message.toString("hex") + "\n" + this.message.toString("text"));
            try {
                this.cleanUp();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try {
            System.out.print("Write a mode: > ");
            String mode = new Scanner(System.in).nextLine();

            Strategy strategy;

            switch (mode) {
                case "encode": strategy = new MessageEncoder("send.txt","encoded.txt"); break;
                case "decode": strategy = new MessageDecoder("received.txt","decoded.txt"); break;
                case "send":
                default: strategy = new MessageSender("encoded.txt","received.txt"); break;
            }

            strategy.execute();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
