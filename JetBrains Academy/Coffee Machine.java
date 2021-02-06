package machine;

import java.util.Scanner;

public class CoffeeMachine {
    static class Machine {
        int water;
        int milk;
        int beans;
        int cups;
        int money;

        public Machine(int water, int milk, int beans, int cups, int money) {
            this.water = water;
            this.milk = milk;
            this.beans = beans;
            this.cups = cups;
            this.money = money;
        }

        public void getState() {
            System.out.printf("\nThe coffee machine has:\n%d of water\n", this.water);
            System.out.printf("%d of milk\n%d of coffee beans\n", this.milk, this.beans);
            System.out.printf("%d of disposable cups\n$%d of money\n", this.cups, this.money);
            System.out.println();
        }

        public void purchase(Scanner s) {
            System.out.println("\nWhat do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:");
            String order = s.next();

            switch (order) {
                case "1": this.makeEspresso(); break;
                case "2": this.makeLatte(); break;
                case "3": this.makeCappuccino(); break;
                case "back": break;
            }
        }

        public void makeEspresso() {
            if (this.water < 250 || this.beans < 16 || this.cups < 1) {
                System.out.println("Sorry, not enough ingredients!");
            } else {
                this.water -= 250;
                this.beans -= 16;
                this.cups -= 1;
                this.money += 4;
                System.out.println("I have enough resources, making you a coffee!\n");
            }
        }

        public void makeLatte() {
            if (this.water < 350 || this.milk < 75 || this.beans < 20 || this.cups < 1) {
                System.out.println("Sorry, not enough ingredients!");
            } else {
                this.water -= 350;
                this.milk -= 75;
                this.beans -= 20;
                this.cups -= 1;
                this.money += 7;
                System.out.println("I have enough resources, making you a coffee!\n");
            }
        }

        public void makeCappuccino() {
            if (this.water < 200 || this.milk < 100 || this.beans < 12 || this.cups < 1) {
                System.out.println("Sorry, not enough ingredients!");
            } else {
                this.water -= 200;
                this.milk -= 100;
                this.beans -= 12;
                this.cups -= 1;
                this.money += 6;
                System.out.println("I have enough resources, making you a coffee!\n");
            }
        }

        public void add(Scanner s) {
            System.out.println("Write how many ml of water do you want to add:");
            this.water += s.nextInt();
            System.out.println("Write how many ml of milk do you want to add:");
            this.milk += s.nextInt();
            System.out.println("Write how many grams of coffee beans do you want to add:");
            this.beans += s.nextInt();
            System.out.println("Write how many disposable cups of coffee do you want to add:");
            this.cups += s.nextInt();
        }

        public void empty() {
            System.out.printf("I gave you $%d\n\n", this.money);
            this.money = 0;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Machine machine = new Machine(400, 540, 120, 9, 550);

        String input;
        do {
            System.out.println("Write action (buy, fill, take, remaining, exit):");
            input = scanner.next();
            switch (input) {
                case "buy": machine.purchase(scanner); break;
                case "fill": machine.add(scanner); break;
                case "take": machine.empty(); break;
                case "remaining": machine.getState(); break;
                default: break;
            }
        } while (!input.equals("exit"));
    }
}
