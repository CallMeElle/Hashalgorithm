import java.util.Scanner;

/**
 * TODO elle This type ...
 *
 */

public class StringHashFunction {

  private static int stringHashCode(byte[] value) {

    int h = 0;
    for (int i = 0; i < value.length; i++) {
      h = 31 * h + (value[i] & 0xff);
    }
    return h;
  }

  public static void main(String[] args) {

    try (Scanner myObj = new Scanner(System.in)) {
      System.out.println("Text to Hash: ");
      String input = myObj.nextLine();

      System.out.println("String Hash Function: " + input.hashCode());

      // System.out.println("String Hash Function2: " + stringHashCode(input));

    }
  }
}