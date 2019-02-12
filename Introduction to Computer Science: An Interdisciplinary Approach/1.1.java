public class UseArgument {

  // 1.1.1 Write a program that prints the Hello, World message 10 time
  public static void main(String[] args) {
    for(int i = 0; i < 10; i++)
      System.out.println("Hello, World");
  }
  
  /* 1.1.2 Describe what happens if you omit the following in HelloWorld.java:
    a. public - ** Error: Main method not found in class <classname>, please define the main method as: ...
    b. static - ** Error: Main method not found in class <classname>, please define the main method as: ...
    c. void   - ** error: invalid method declaration; return type required
    d. args   - ** error: <identifier> expected
   */

  /* 1.1.3 Describe what happens if you misspell (by, say, omitting the second letter) the following in HelloWorld.java:
    a. public - ** error: <identifier> expected
    b. static - ** error: <identifier> expected; error: invalid method declaration; return type required
    c. void   - ** error: cannot find symbol
    d. args   - ** error: cannot find symbol
   */
   
   /* 1.1.4 Describe what happens if you put the double quotes in the print statement of HelloWorld.java
    on different lines, as in this code fragment:
     System.out.println("Hello,
                         World");
     - Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 2 at...
    */
  
   /* 1.1.5 Describe what happens if you try to execute UseArgument with each of the following command lines:
    a. java UseArgument java        - prints "java" to screen
    b. java UseArgument @!&a%       - bash !: event not found
    c. java UseArgument 1234        - prints "1234" to screen
    d. java UseArgument.java Bob    - Error: Could not find or load main class exer.java
    e. java UseArgument Alice Bob   - prints "Alice" to screen
   */
}
  
public class UseThree {

  // 1.1.6 Modify UseArgument.java to make a program UseThree.java that takes three names and prints out a proper 
  // sentence with the names in the reverse of the order given, so that, for example, java UseThree Alice Bob Carol 
  // gives Hi Carol, Bob, and Alice.
  public static void main(String[] args) {
      System.out.println("Hi, " + args[2] + ", " + args[1] + ", and " + args[0] + ".");
  }

}
