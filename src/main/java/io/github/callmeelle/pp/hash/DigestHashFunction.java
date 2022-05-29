package io.github.callmeelle.pp.hash;
import java.security.MessageDigest;

/**
 * TODO elle This type ...
 *
 */
public class DigestHashFunction implements HashFunction {

  private final String algorithm;

  /**
   * The constructor.
   */
  public DigestHashFunction(String algorithm) {

    super();
    this.algorithm = algorithm;
  }

  @Override
  public byte[] hash(byte[] input) {

    try {
      MessageDigest digest = MessageDigest.getInstance(this.algorithm);
      return digest.digest(input);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

}
