package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class Main {
    enum Mode { ENCODE, DECODE, ERROR }

    enum Algorithm { SHIFT, UNICODE, ERROR }

    interface AlgorithmImplementation {
        char[] encode(char[] strIn, int key);
        char[] decode(char[] strIn, int key);
    }

    static class Shift implements AlgorithmImplementation {
        public char[] encode(char[] strIn, int key) {
            char[] result = strIn.clone();

            for (int i = 0; i < result.length; i++) {
                if (result[i] > 96 && result[i] < 123) {
                    result[i] += key;
                    if (result[i] > 122) {
                        result[i] -= 26;
                    }
                } else if (result[i] > 64 && result[i] < 90) {
                    result[i] += key;
                    if (result[i] > 90) {
                        result[i] -= 26;
                    }
                }
            }
            return result;
        }

        public char[] decode(char[] strIn, int key) {
            char[] result = strIn.clone();

            for (int i = 0; i < result.length; i++) {
                if (result[i] > 96 && result[i] < 123) {
                    result[i] -= key;
                    if (result[i] < 97) {
                        result[i] += 26;
                    }
                } else if (result[i] > 64 && result[i] < 90) {
                    result[i] -= key;
                    if (result[i] < 65) {
                        result[i] += 26;
                    }
                }
            }
            return result;
        }
    }

    static class Unicode implements AlgorithmImplementation {
        public char[] encode(char[] strIn, int key) {
            char[] result = strIn.clone();

            for (int i = 0; i < strIn.length; i++) {
                result[i] += key;
            }

            return result;
        }

        public char[] decode(char[] strIn, int key) {
            char[] result = strIn.clone();

            for (int i = 0; i < strIn.length; i++) {
                result[i] -= key;
            }

            return result;
        }
    }

    static class CipherContext {
        Mode mode;
        Algorithm algo;
        char[] strIn;
        char[] strOut;
        int key;

        public CipherContext(Mode m, Algorithm a, int key) {
            this.mode = m;
            this.algo = a;
            this.key = key;
        }

        public void execute() {
            AlgorithmImplementation method;

            switch (this.algo) {
                case UNICODE: method = new Unicode(); break;
                case SHIFT: method = new Shift(); break;
                default: method = null;
            }

            switch (this.mode) {
                case DECODE: this.strOut = method.decode(this.strIn, this.key); break;
                case ENCODE: this.strOut = method.encode(this.strIn, this.key); break;
                default: this.strOut = null;
            }
        }

        public void setString(File input) throws IOException {
            this.strIn = new String(Files.readAllBytes(input.toPath())).toCharArray();
        }

        public void setString(String input) {
            this.strIn = input.toCharArray();
        }
    }

    static String modeStr;
    static String algoStr;
    static String keyStr;
    static String strIn = "";
    static File fileIn = null;
    static File fileOut = null;

    public static void main(String[] args) {
        if (args.length == 0) System.exit(0);

        try {
            /* -------------------- Read CLI -------------------- */
            for (int i = 0; i < args.length; i += 2) {
                if (args[i].startsWith("-") && !args[i+1].startsWith("-")) {
                    switch (args[i]) {
                        case "-mode": modeStr = args[i+1]; break;
                        case "-key": keyStr = args[i+1]; break;
                        case "-data": strIn = args[i+1]; break;
                        case "-in": fileIn = new File(args[i+1]); break;
                        case "-out": fileOut = new File(args[i+1]); break;
                        case "-alg": algoStr = args[i+1]; break;
                        default: throw new IllegalArgumentException();
                    }
                } else {
                    throw new IllegalArgumentException("Illegal arguments: " + args[i] + " " + args[i+1]);
                }
            }

            /* ----------------- Validate Input ----------------- */
            Mode mode = modeStr.equals("dec") ? Mode.DECODE : modeStr.equals("enc") ? Mode.ENCODE : Mode.ERROR;
            if (mode.equals(Mode.ERROR)) {
                throw new IllegalArgumentException("Illegal argument -mode: " + modeStr);
            }

            Algorithm algo = algoStr.equals("unicode") ? Algorithm.UNICODE : algoStr.equals("shift") ?
                    Algorithm.SHIFT : Algorithm.ERROR;
            if (algo.equals(Algorithm.ERROR)) {
                throw new IllegalArgumentException("Illegal argument -alg: " + algoStr);
            }

            int key;
            try {
                key = Integer.parseInt(keyStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Illegal argument -key: " + keyStr);
            }

            if (Objects.nonNull(fileIn) && !fileIn.exists()) {
                throw new FileNotFoundException("File not found for -in");
            }

            /* ----------------- Create Context ----------------- */
            CipherContext ctx = new CipherContext(mode, algo, key);

            if (strIn.length() == 0 && Objects.nonNull(fileIn)){
                ctx.setString(fileIn);
            } else {
                ctx.setString(strIn);
            }

            /* ------------------- Get Result ------------------- */
            ctx.execute();

            // output results
            if (Objects.nonNull(fileOut)) {
                FileWriter f = new FileWriter(fileOut);
                f.write(ctx.strOut);
                f.close();
            } else {
                for (char el: ctx.strOut) {
                    System.out.print(el);
                }
            }

        } catch (IllegalArgumentException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}
