package io.github.callmeelle.pp.hash;

import java.security.MessageDigest;

/**
 * creates MD5 Hash using Message.Digest MD5 from Java to compare with my own MD5 implementation
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
