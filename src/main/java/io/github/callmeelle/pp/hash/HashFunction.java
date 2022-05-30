package io.github.callmeelle.pp.hash;

import java.nio.charset.StandardCharsets;

/**
 * Interface for a hash function (or algorithm).
 */
public interface HashFunction {

  /**
   * Hash String input
   *
   * @param input User-Input/Message
   * @return hash byte[] input
   */
  default byte[] hash(String input) {

    byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
    return hash(bytes);
  }

  /*
   * hash input in byte[]-form
   */
  byte[] hash(byte[] input);

}
