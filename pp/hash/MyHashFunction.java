package io.github.callmeelle.pp.hash;

/**
 * TODO elle This type ...
 *
 */
public class MyHashFunction implements HashFunction {

  private byte hash;

  @Override
  // create hash with digit sum
  public byte[] hash(byte[] input) {

    System.out.println("Text: " + input);
    for (int i = 0; i < input.length; i++) {
      this.hash = (byte) (this.hash + input[i]);
    }
    return new byte[] { this.hash };
  }

}
