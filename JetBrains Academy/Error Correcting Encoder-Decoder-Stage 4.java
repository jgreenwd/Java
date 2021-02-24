package correcter;

import java.io.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static class Message {
        final public static int BYTE_SIZE = 8;
        final public static int BITS_PER_BYTE = 3;
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

        public byte[] getMessage() {
            return this.message;
        }

        public int getBit(int bytePos, int bitPos) {
            return (this.message[bytePos] >> bitPos) & 1;
        }

        public void setBit(int bytePos, int bitPos, int val) {
            int temp = this.message[bytePos];
            temp = val == 0 ? temp & ~(1 << bitPos) : temp | (1 << bitPos);
            this.message[bytePos] = (byte) temp;
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

        public void bitCorrection() {
            if (this.isEncoded) {
                for (int i = 0; i < this.length; i++) {
                    int bit2_7 = getBit(i, 7);
                    int bit2_6 = getBit(i, 6);
                    int bit2_5 = getBit(i, 5);
                    int bit2_4 = getBit(i, 4);
                    int bit2_3 = getBit(i, 3);
                    int bit2_2 = getBit(i, 2);
                    int bit2_1 = getBit(i, 1);

                    int value, bitPos;
                    if (bit2_7 != bit2_6) {
                        value = bit2_5 ^ bit2_3 ^ bit2_1;
                        bitPos = 7;
                    } else if (bit2_5 != bit2_4) {
                        value = bit2_7 ^ bit2_3 ^ bit2_1;
                        bitPos = 5;
                    } else if (bit2_3 != bit2_2) {
                        value = bit2_7 ^ bit2_5 ^ bit2_1;
                        bitPos = 3;
                    } else {
                        value = bit2_7 ^ bit2_5 ^ bit2_3;
                        bitPos = 1;
                    }

                    setBit(i, bitPos--, value);
                    setBit(i, bitPos, value);
                }
            }
        }

        public void encode() {
            if (!this.isEncoded) {
                // 8 bits for every 3 bits of input bit-string -> 2 per bit + 2 parity bits per set of 3
                Message output = new Message(new byte[(int) Math.ceil(((double) this.length / BITS_PER_BYTE) * BYTE_SIZE)]);
                int bitCount = this.length * BYTE_SIZE;

                int inputByteIndex, inputBitIndex, outputByteIndex, outputBitIndex;
                inputByteIndex = outputByteIndex = 0;
                inputBitIndex = outputBitIndex = BYTE_SIZE - 1;

                while (inputByteIndex < this.length) {
                    int temp = this.getBit(inputByteIndex, inputBitIndex--);

                    if (inputBitIndex == -1) {
                        inputBitIndex = BYTE_SIZE - 1;
                        inputByteIndex++;
                    }

                    output.setBit(outputByteIndex, outputBitIndex--, temp);
                    output.setBit(outputByteIndex, outputBitIndex--, temp);

                    // calculate parity bit
                    if (--bitCount == 0 || outputBitIndex == 1) {
                        int bit0 = output.getBit(outputByteIndex, BYTE_SIZE - 1);
                        int bit2 = output.getBit(outputByteIndex, BYTE_SIZE - 3);
                        int bit4 = output.getBit(outputByteIndex, BYTE_SIZE - 5);
                        int parity = bit0 ^ bit2 ^ bit4;

                        output.setBit(outputByteIndex, 1, parity);
                        output.setBit(outputByteIndex, 0, parity);
                        outputBitIndex = BYTE_SIZE - 1;
                        outputByteIndex++;
                    }
                }

                this.setMessage(output.getMessage(), true);
            }
        }

        public void decode() {
            if (this.isEncoded) {
                Message output = new Message(new byte[(int) Math.floor(((double) this.length / BYTE_SIZE) * BITS_PER_BYTE)]);

                int inputByteIndex, inputBitIndex, outputByteIndex, outputBitIndex;
                inputByteIndex = outputByteIndex = 0;
                inputBitIndex = outputBitIndex = BYTE_SIZE - 1;

                while (outputByteIndex < output.length) {
                    int a = this.getBit(inputByteIndex, inputBitIndex);

                    output.setBit(outputByteIndex, outputBitIndex--, a);
                    if (outputBitIndex == -1) {
                        outputBitIndex = BYTE_SIZE - 1;
                        outputByteIndex++;
                    }

                    inputBitIndex -= 2;
                    if (inputBitIndex == 1) {
                        inputBitIndex = BYTE_SIZE - 1;
                        inputByteIndex++;
                    }
                }

                this.setMessage(output.getMessage(), false);
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
            this.writer.write(this.message.getMessage());
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
            this.message.isEncoded = true;
            this.message = garbleTransmission(this.message);
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

    public static Message garbleTransmission(Message message) {
        Message xFormedMessage = new Message(message.getMessage().clone());
        xFormedMessage.isEncoded = message.isEncoded;

        if (xFormedMessage.isEncoded) {
            int length = xFormedMessage.getMessage().length;

            Random r = new Random();

            for (int i = 0; i < length; i++) {
                int index = r.nextInt(8);
                int flipBit = xFormedMessage.getBit(i, index) == 0 ? 1 : 0;
                xFormedMessage.setBit(i, index, flipBit);
            }
        }

        return xFormedMessage;
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
