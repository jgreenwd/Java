import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;

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
        int num = 0;
        for(int i = 1; i <= limit; i++) {
            num += i;
        }
        System.out.println(num);
    }
    
    
    // #5. Modify the previous program such that only multiples of three or five are considered in the sum, 
    //     e.g. 3, 5, 6, 9, 10, 12, 15 for n=17
    public static void ex5() {
        System.out.println("Enter a positive integer: ");
        int limit = input.nextInt();
        int num = 0;
        for(int i = 1; i <= limit; i++) {
            if (i % 3 == 0 || i % 5 == 0) {
                num += i;
            }
        }
        System.out.println(num);
    }
    
    
    // #6. Write a program that asks the user for a number n and gives them the possibility to choose between 
    //     computing the sum and computing the product of 1,...,n.
    public static void ex6() {
        System.out.println("Enter a positive integer: ");
        int limit = input.nextInt();
        System.out.println("Calculate (P)roduct or (S)um: ");
        String calc = input.next();
        
        int num = 1;
        if (calc.equals("P")) {
            for(int i = 1; i <= limit; i++) {
                num *= i;
            }
        } else if (calc.equals("S")) {
            for(int i = 2; i <= limit; i++) {
                num += i;
            }
        }
        System.out.println(num);
    }
    
    
    // #7. Write a program that prints a multiplication table for numbers up to 12.
    public static void ex7() {
        System.out.println("Enter an integer between 1 & 12: ");
        int limit = input.nextInt();
        if (limit > 12 || limit < 1) return;
        
        for(int i = 1; i <= limit; i++) {
            for(int j = 1; j <= limit; j++) {
                System.out.print(i*j + "\t");
            }
            System.out.println();
        }
    }
    
    
   // #8. Write a program that prints all prime numbers. (Note: if your programming language does not support 
   //     arbitrary size numbers, printing all primes up to the largest number you can easily represent is fine too.)
    public static void ex8(int max_) {
        ArrayList<Integer> primes = new ArrayList<Integer>();
        primes.add(2);
        primes.add(3);
        primes.add(5);

        for(int i = 6; i < max_; i++) {
            boolean prime = true;
            for(int j = 0; j < primes.size() && primes.get(j)*primes.get(j) <= i; j++) {
                if(i % primes.get(j) == 0) {
                    prime = false;
                    break;
                }
            }
            if (prime) primes.add(i);
        }
        
        for(int i = 0; i < primes.size(); ++i) {
            if (i % 10 == 0) System.out.println();
            System.out.print(primes.get(i) + "\t");
        }
    }
    
    
    // #9. Write a guessing game where the user has to guess a secret number. After every guess the program
    //     tells the user whether their number was too large or too small. At the end the number of tries needed 
    //     should be printed. I counts only as one try if they input the same number multiple times consecutively.
    public static void ex9() {
        int min_ = 1, max_ = 100;
        System.out.print("Pick a number between 1 and 100: ");
        
        Random rand = new Random();
        int num = rand.nextInt(max_) + min_;
        ArrayList<Integer> count = new ArrayList<Integer>();
             
        int guess;
        do {
            guess = input.nextInt();
            if (!count.contains(guess)) count.add(guess);
            if (guess == num) {
                System.out.println("Congratulations! You guessed " + num + " correctly!");
            } else if (guess > num) {
                System.out.print("Lower! ");
            } else if (guess < num) {
                System.out.print("Higher! ");
            }
        } while (guess != num);
        System.out.println("Correct answer guessed in " + count.size() + " tries!");
    }
    
    
    // #10. Write a program that prints the next 20 leap years.
    public static void ex10(int first_yr_) {
        ArrayList<Integer> output = new ArrayList<Integer>();
        
        int years_ = first_yr_;
        
        while (output.size() < 20) {
            boolean leap_year = false;
            if (years_ % 4 == 0) leap_year = true;
            if (years_ % 100 == 0) leap_year = false;
            if (years_ % 400 == 0) leap_year = true;
            if (leap_year) output.add(years_);
            years_++;
        }
        for(int i = 0; i < 20; i++) {
            if (i % 5 == 0) System.out.println();
            System.out.print(output.get(i) + "\t"); 
        }
    }
    
    
/* ------------------------------------------------------------------------------------ */
 public static void main(String[] args) {
        // ... insert function call to exercise here ...
    }    
}
