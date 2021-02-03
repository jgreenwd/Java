package cinema;

import java.util.IllegalFormatException;
import java.util.Scanner;

public class Cinema {
    boolean[] Cinema;
    int rows;
    int cols;
    boolean isLargeCinema;
    int[] prices = new int[] {10, 8};
    int currentIncome = 0;
    int maxIncome;
    int ticketCounter = 0;

    Cinema(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.Cinema = new boolean[rows * cols];
        this.isLargeCinema = this.rows * this.cols > 60;
        java.util.Arrays.fill(this.Cinema, true);

        if (this.isLargeCinema) {
            int front = this.rows / 2;
            this.maxIncome = front * this.cols * this.prices[0] + (this.rows - front) * this.cols * this.prices[1];
        } else {
            this.maxIncome = this.rows * this.cols * this.prices[0];
        }
    }

    public boolean isAvailable(int row, int col) {
        return this.Cinema[(row - 1) * this.cols + col - 1];
    }

    public void setSeat(int row, int col) {
        this.Cinema[(row - 1) * this.cols + col - 1] = false;
    }

    public int getTicketPrice(int row, int col) {
        if (!this.isLargeCinema || row <= this.rows / 2) {
            return 10;
        }
        return 8;
    }

    public String toString() {
        // build header
        StringBuilder result = new StringBuilder("\nCinema:\n  ");

        for (int i = 1; i <= this.cols; i++) {
            result.append(i).append(" ");
        }
        result.append("\n");

        // build body
        int row = 1;
        for (int i = 0; i < this.Cinema.length; i++) {
            boolean isLabel = i == 0 || i % this.cols == 0;
            boolean isRowEnd = i % this.cols == this.cols - 1;
            boolean isAvail = this.Cinema[i];
            String seat = isAvail && isLabel ? row++ + " S" : (!isAvail && isLabel) ? row++ + " B" : isAvail ? "S" : "B";

            result.append(seat).append(isRowEnd ? "\n" : " ");
        }

        return result.toString();
    }

    public void runPurchaseScript(Scanner scanner) throws IllegalFormatException {
        int row = 0;
        int col = 0;
        boolean finished = false;

        do {
            try {
                System.out.println("Enter a row number:");
                row = scanner.nextInt();
                System.out.println("Enter a seat number in that row:");
                col = scanner.nextInt();
                if (row == 0 || col == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                if (!this.isAvailable(row, col)) {
                    System.out.println("That ticket has already been purchased!\n");
                } else {
                    finished = true;
                }

            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Wrong input!\n");
            }
        } while (!finished);

        System.out.println("Ticket price: $" + this.getTicketPrice(row, col) + "\n");
        this.setSeat(row, col);
        this.currentIncome += this.getTicketPrice(row, col);
        this.ticketCounter++;
    }

    public void runStatisticsScript() {
        System.out.println("Number of purchased tickets: " + this.ticketCounter);
        String perc = String.format("Percentage: %.2f", this.ticketCounter / ((double)this.rows * this.cols) * 100);
        System.out.println(perc + "%");
        System.out.println("Current income: $" + this.currentIncome);
        System.out.println("Total income: $" + this.maxIncome + "\n");
    }

    public static void main(String[] args) {
        // Build the Cinema
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of rows:");
        int rows = scanner.nextInt();
        System.out.println("Enter the number of seats in each row:");
        int cols = scanner.nextInt();
        Cinema cinema = new Cinema(rows, cols);

        // Menu
        int response;
        do {
            System.out.println("1. Show the seats\n2. Buy a ticket\n3. Statistics\n0. Exit");
            response = scanner.nextInt();

            switch (response) {
                case 0:
                    break;
                case 1:
                    System.out.println(cinema);
                    break;
                case 2:
                    cinema.runPurchaseScript(scanner);
                    break;
                case 3:
                    cinema.runStatisticsScript();
                    break;
                default:
                    System.out.println("Wrong input!");
            }
        } while (response != 0);

    }
}
