package tictactoe;

import java.util.Scanner;

public class Main {
    static char EMPTY = '_';

    static class Square {
        char value = EMPTY;

        void set(char value) { this.value = value; }

        char get() { return this.value; }
    }

    static class Board {
        int xCount = 0;
        int oCount = 0;
        int emptyCount = 9;
        boolean xTurn = true;
        java.util.Set<Character> winner = new java.util.HashSet<>();

        Square[] brd = new Square[] { new Square(), new Square(), new Square(), new Square(), new Square(),
                                      new Square(), new Square(), new Square(), new Square()};

        /** @return : Is the winner list not empty or no turns remaining */
        boolean isFinished() {
            return !winner.isEmpty() || emptyCount == 0;
        }

        /** Determine winner(s) */
        void solve() {
            boolean row1 = brd[0].get() == brd[1].get() && brd[0].get() == brd[2].get() && brd[0].get() != EMPTY;
            boolean row2 = brd[3].get() == brd[4].get() && brd[3].get() == brd[5].get() && brd[3].get() != EMPTY;
            boolean row3 = brd[6].get() == brd[7].get() && brd[6].get() == brd[8].get() && brd[6].get() != EMPTY;
            boolean col1 = brd[0].get() == brd[3].get() && brd[0].get() == brd[6].get() && brd[0].get() != EMPTY;
            boolean col2 = brd[1].get() == brd[4].get() && brd[1].get() == brd[7].get() && brd[1].get() != EMPTY;
            boolean col3 = brd[2].get() == brd[5].get() && brd[2].get() == brd[8].get() && brd[2].get() != EMPTY;
            boolean dia1 = brd[0].get() == brd[4].get() && brd[0].get() == brd[8].get() && brd[0].get() != EMPTY;
            boolean dia2 = brd[2].get() == brd[4].get() && brd[2].get() == brd[6].get() && brd[2].get() != EMPTY;

            // set winning character
            if (row1 || col1 || dia1) {
                winner.add(brd[0].get());
            } else if (col2) {
                winner.add(brd[1].get());
            } else if (col3 || dia2) {
                winner.add(brd[2].get());
            } else if (row2) {
                winner.add(brd[3].get());
            } else if (row3) {
                winner.add(brd[6].get());
            }
        }

        void play(int target) {
            char value = xTurn ? 'X' : 'O';
            brd[target].set(value);
            if (xTurn) {
                xCount++;
                emptyCount--;
                xTurn = false;
            } else if (value == 'O') {
                oCount++;
                emptyCount--;
                xTurn = true;
            }
            this.solve();
        }

        public String toString() {
            StringBuilder output = new StringBuilder();
            String line = "---------\n";
            output.append(line);
            for(int i = 0; i < 9; i += 3){
                String entry = String.format("| %c %c %c |\n", brd[i].get(), brd[i + 1].get(), brd[i + 2].get());
                output.append(entry);
            }
            output.append(line);
            return output.toString();
        }
    }

    public static int decodeCoordinate(int row, int col) {
        if (row == 1 && col == 1) {
            return 0;
        } else if (row == 1 && col == 2) {
            return 1;
        } else if (row == 1 && col == 3) {
            return 2;
        } else if (row == 2 && col == 1) {
            return 3;
        } else if (row == 2 && col == 2) {
            return 4;
        } else if (row == 2 && col == 3) {
            return 5;
        } else if (row == 3 && col == 1) {
            return 6;
        } else if (row == 3 && col == 2) {
            return 7;
        } else if (row == 3 && col == 3) {
            return 8;
        } else {
            return -1;
        }
    }

    public static boolean playGame(Board board) {
        Scanner scanner = new Scanner(System.in);
        boolean validCoords = false;
        System.out.print("Enter the coordinates: ");

        String[] input = scanner.nextLine().split(" ");
        try {
            int row = Integer.parseInt(input[0]);
            int col = Integer.parseInt(input[1]);

            int index = decodeCoordinate(row, col);

            if (col > 3 || row > 3 || col < 1 || row < 1) {
                System.out.println("Coordinates should be from 1 to 3!");
            } else if (board.brd[index].get() != EMPTY) {
                System.out.println("This cell is occupied! Choose another one!");
            } else {
                board.play(index);
                validCoords = true;
            }
        } catch (NumberFormatException e) {
            System.out.println("You should enter numbers!");
        }

        return validCoords;
    }

    public static void main(String[] args) {
        // build the board
        Board board = new Board();

        // display initial board state
        System.out.println(board.toString());

        // gameplay
        while (!board.isFinished()) {
            boolean validCoords = false;
            while (!validCoords) {
                validCoords = playGame(board);
            }

            // refresh current board state
            System.out.println(board.toString());
        }

        // game winner determination mechanics
        StringBuilder output = new StringBuilder();

        if (board.winner.size() == 0){
            output.append("Draw");
        } else {
            output.append(board.winner.toString()
                    .replace("[", "")
                    .replace("]", "") + " wins");
        }
        System.out.println(output.toString());
    }
}
