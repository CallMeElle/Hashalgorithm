package io.github.callmeelle.pp.hash;

/**
 * Turns hash in form e.g. "[B@7de26db8" to text like "5c"
 *
 */
public class HashToHex {
  // all possible characters for hash as text
  private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

  /**
   *
   * @param data
   * @return hash as text
   */
  public static String toHex(byte[] data) {

    char[] buf = new char[data.length * 2];
    int i = 0;
    for (int j = 0; j < data.length; j++) {
      buf[i++] = HEX[(data[j] & 0xF0) >> 4];
      buf[i++] = HEX[data[j] & 0x0F];
    }
    return new String(buf);
  }
}
