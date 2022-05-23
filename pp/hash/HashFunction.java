package io.github.callmeelle.pp.hash;

import java.nio.charset.StandardCharsets;

/**
 * Interface for a hash function (or algorithm).
 */
public interface HashFunction {

  /**
   *
   * @param input
   * @return
   */
  default byte[] hash(String input) {

    byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
    return hash(bytes);
  }

  /**
   * @return the computed hash of the data that has been provided by one or multiple calls to
   *         {@link #update(byte[], int, int)}.
   */
  byte[] hash(byte[] input);

}
