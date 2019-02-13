import java.util.Random;
import java.util.Scanner;

/** 1. Generate 1000 random numbers between 1 and 1000, then save them to file.
    2. Read file of 1000 integers:
      a. calculate sum
      b. calculate average value
 */
public class file_io {
  public static void main(String[] args) {
  
    // generate values
    Random rand = new Random();
    int next, limit = 1000;
    for (int i = 0; i < limit; i++) {
      if (i % 10 == 0 && i != 0) System.out.println();
      next = rand.nextInt(1000);
      System.out.printf("%4d ", next);
    }

    System.out.println();

    // read values & operate
    Scanner input = new Scanner(System.in);
    int sum = 0;
    while (input.hasNext()) {
      sum += input.nextInt();
    }
    System.out.print(sum + " ");
    System.out.println(sum/1000);

    input.close();
  }
}
