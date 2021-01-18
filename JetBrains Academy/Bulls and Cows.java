package bullscows;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    final private static String symbols = "0123456789abcdefghijklmnopqrstuvwxyz";

    /**
     * Randomly generate a code with n- unique digits from c- possible characters
     * @param n: int between 1 and 36
     * @param c: int between n and 36
     * @return : String
     * */
    public static String generateCode(int n, int c) throws IllegalArgumentException {
        // input validation
        if (n > c || n > 35 || c > 36 || n == 0) {
            throw new IllegalArgumentException();
        }

        char[] code = new char[n];
        Random random = new Random();

        // minimum of 10 characters (0-9) to be used in code
        int limit = Math.max(c, 10);
        ArrayList<Character> symbolList = symbols.substring(0, limit).chars().mapToObj(ch -> (char) ch).collect(
                Collectors.toCollection(ArrayList::new));

        // randomly select code elements from list
        for (int i = 0; i < n; i++) {
            code[i] = symbolList.remove(random.nextInt(symbolList.size() - 1));
        }

        return new String(code);
    }

    /**
     * Return message indicating characters used in the code
     * @param code: String of code characters
     * @param count: int number of possible valid code characters
     * @return : String explanation
     */
    public static String codeSummary(String code, int count) {
        StringBuilder result = new StringBuilder("The secret code is prepared: ");

        // add * for each character of code & start valid character indication
        result.append("*".repeat(code.length())).append(" ").append("(0-");

        // complete valid character indication
        if (count < 10) {
            result.append("9).");
        } else if (count == 11) {
            result.append("9, a)");
        } else {
            result.append("9, a-").append(symbols.charAt(count-1)).append(").");
        }
        return result.toString();
    }

    /** Compare 2 strings for equality & common characters
     *
     * @param user: String dependent variable
     * @param code: String independent variable
     * @return : int array of 2 elements - number of bulls, number of cows
     * */
    public static int[] compareCode(String user, String code) {
        char[] response = user.toCharArray();
        int[] result = new int[] {0, 0};            // {bulls, cows}

        for (int i = 0; i < response.length; i++) {
            int index = code.indexOf(response[i]);
            if (index == i) {
                result[0]++;
            } else if (index > -1) {
                result[1]++;
            }
        }

        return result;
    }

    /**
     * Interpret results of compareCode & build a String response
     * @param bovines: int array of 2 elements {bulls, cows}
     * @return : appropriate String response for output
     */
    public static String interpretCode(int[] bovines) {
        StringBuilder output = new StringBuilder();

        boolean bullPlural = bovines[0] > 1;
        boolean cowPlural = bovines[1] > 1;

        if (bovines[0] > 0 && bovines[1] > 0) {
            output.append(bovines[0]).append(bullPlural ? " bulls" : " bull");
            output.append(" and ");
            output.append(bovines[1]).append(cowPlural ? " cows." : " cow.");
        } else if (bovines[0] > 0) {
            output.append(bovines[0]).append(bullPlural ? " bulls." : " bull.");
        } else if (bovines[1] > 0) {
            output.append(bovines[1]).append(cowPlural ? " cows." : " cow.");
        } else {
            output.append(" None.");
        }

        return output.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // 1.a Prompt for code length
            System.out.println("Input the length of the secret code: ");
            int codeLength = scanner.nextInt();
            if (codeLength == 0) {
                throw new IllegalArgumentException();
            }

            // 1.b Prompt for code char range
            System.out.println("Input the number of possible symbols in the code: ");
            int symbolCount = scanner.nextInt();
            if (symbolCount < codeLength) {
                throw new IllegalArgumentException();
            }

            // 2. Generate code
            String code = generateCode(codeLength, symbolCount);
            String summary = codeSummary(code, symbolCount);
            System.out.println(summary);

            System.out.println("Okay, let's start a game!");

            // 2. Gameplay
            int turn = 0;
            int[] result;
            String response;

            do {
                System.out.println("Turn " + ++turn + ":");
                response = scanner.next();
                if (response.equals("x")) {
                    System.out.println("Exiting game");
                    break;
                } else if (response.length() != codeLength) {
                    throw new IllegalArgumentException();
                }

                result = compareCode(response, code);

                String output = interpretCode(result);

                System.out.println("Grade: " + output);
            } while (result[0] != codeLength);

            System.out.println("Congratulations! You guessed the secret code.");
        } catch (IllegalArgumentException | InputMismatchException e) {
            System.out.println("error");
        }
    }
}
