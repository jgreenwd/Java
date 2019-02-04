public class Exercises {
    // #1. Write a program that prints ‘Hello World’ to the screen.
    public static void ex1 {
        System.out.println("Hello, World!");
    }
    
    // #2. Write a program that asks the user for their name and greets them with their name.
    public static void ex2() {
        System.out.println("Enter your name: ");
        String name = input.nextLine();
        System.out.println("Hello, " + name);
    }
    
    // #3. Modify the previous program such that only the users Alice and Bob are greeted with their names.
    public static void ex3() {
        System.out.println("Enter your name: ");
        String name = input.nextLine();
        if (name.equals("Alice") || name.equals("Bob")) {
            System.out.println("Hello, " + name);
        } else {
            System.out.println("Hello.");
        }
    }

    // #4. Write a program that asks the user for a number n and prints the sum of the numbers 1 to n
    public static void ex4() {
        System.out.println("Enter a positive integer: ");
        int limit = input.nextInt();
        int num = 1;
        for(int i = 1; i <= limit; i++) {
            num *= i;
        }
        System.out.println(num);
    }
    public static void main(String[] args) {
    // ... insert call to exercise here ...
    }
}
