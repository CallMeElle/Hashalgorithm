package io.github.callmeelle.pp.hash;

/**
 * TODO elle This type ...
 *
 */
public class HashProgram {

  private static HashFunction newHashFunction() {

    // choose method to hash
    return new MyMD5Function();
    // return new DigestHashFunction("MD5");
  }

  public static void main(String[] args) {

    if (args.length != 1) {
      System.err.println("ERROR: Text to hash has to be provided as single argument!");
      System.out.println("Usage: " + HashProgram.class.getName() + " \"<your text to hash>\"");
      System.exit(-1);
    }
    HashFunction hashFunction = newHashFunction();
    byte[] hash = hashFunction.hash(args[0]);
    System.out.println(hash);
    System.out.println("Hash: " + HashToBinary.formatHex(hash));

  }

}
