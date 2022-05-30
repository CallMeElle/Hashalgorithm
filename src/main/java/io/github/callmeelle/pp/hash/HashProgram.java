package io.github.callmeelle.pp.hash;

import java.util.Scanner;

public class HashProgram {

  private static HashFunction newHashFunction() {

    // choose method to hash
    return new MyMD5Function();
    // return new MyHashFunction();
    // return new DigestHashFunction("MD5");
  }

  /**
   * @param args Argument from "Run Configurations" get Input, start hashFunction, output Hash
   */
  public static void main(String[] args) {

    try (Scanner myObj = new Scanner(System.in)) {
      System.out.println("Text to Hash: ");
      String input = myObj.nextLine();

      HashFunction hashFunction = newHashFunction();
      byte[] hash = hashFunction.hash(input);
      System.out.println("Hash: " + HashToHex.toHex(hash));
    }
  }

}
