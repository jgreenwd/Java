package converter;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    final public static int PRECISION = 5;

    static class InputValidator {
        int upperLimit = 36;
        int lowerLimit = 1;

        public boolean isInvalidRadix(int radix) {
            return radix < lowerLimit || radix > upperLimit;
        }

        public boolean isInvalidNum(String num, int radix) {
            char[] vals = num.toUpperCase(Locale.ROOT).toCharArray();
            int limit = radix < 11 ? '0' + radix : '0' + radix + 6;

            for (char x: vals) {
                if (x != '.' && x > limit) throw new NumberFormatException();
            }

            return false;
        }
    }

    /***
     * @param num : String representation of integer
     * @param radIn : radix used in input String
     * @param radOut : radix to be used in output String
     * @return : base[radOut] representation of the input String
     */
    public static String integerConverter(String num, int radIn, int radOut) throws NumberFormatException {
        int val = radIn == 1 ? Integer.toString(Integer.parseInt(num)).length() : Integer.valueOf(num, radIn);

        return radOut == 1 ? "1".repeat(val) : Integer.toString(val, radOut);
    }

    /***
     * @param num : String representation of mantissa
     * @param radIn : radix used in input String
     * @param radOut : radix to be used in output String
     * @return : base[radOut] representation of the input String
     */
    public static String mantissaConverter(String num, int radIn, int radOut) {
        if (radIn == 1) return "0";

        double deciResult = 0;

        for (int i = 1; i < num.length() + 1; i++) {
            deciResult += Integer.valueOf(String.valueOf(num.charAt(i - 1)), radIn) / Math.pow(radIn, i);
        }

        if (radOut == 10) return Double.toString(deciResult * PRECISION);

        int temp;
        StringBuilder resultOut = new StringBuilder();

        for (int i = 0; i < PRECISION; i++) {
            deciResult *= radOut;
            temp = (int) deciResult;
            resultOut.append(Integer.toString(temp, radOut));
            deciResult -= temp;
        }

        return resultOut.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            InputValidator validator = new InputValidator();

            int inRad = scanner.nextInt();
            if (validator.isInvalidRadix(inRad)) throw new NumberFormatException();

            String inNum = scanner.next();
            if (validator.isInvalidNum(inNum, inRad)) throw new NumberFormatException();
            String[] num = inNum.split("\\.");

            int outRad = scanner.nextInt();
            if (validator.isInvalidRadix(outRad)) throw new NumberFormatException();

            String whole = integerConverter(num[0], inRad, outRad);
            String fract = (inRad == 1 || num.length == 1) ? "0" : mantissaConverter(num[1], inRad, outRad);

            StringBuilder result = new StringBuilder(whole);

            if (outRad != 1 && fract.length() > 1) result.append(".").append(fract);

            System.out.println(result);
        } catch (NumberFormatException | InputMismatchException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}
