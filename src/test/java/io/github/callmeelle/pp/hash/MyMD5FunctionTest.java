package io.github.callmeelle.pp.hash;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link MyMD5Function}.
 */
public class MyMD5FunctionTest extends Assertions {
  @Test
  void testHash() {

    // given
    String text = "The quick brown fox jumps over the lazy dog";
    HashFunction hashFunction = new MyMD5Function();
    // when
    byte[] hash = hashFunction.hash(text);
    String hashcode = HashToHex.toHex(hash);
    // then
    // https://en.wikipedia.org/wiki/MD5#MD5_hashes
    assertEquals("9e107d9d372bb6826bd81d3542a419d6", hashcode);
  }

  @Test
  void testHash2() {

    // given
    String text = "The quick brown fox jumps over the lazy dog.";
    HashFunction hashFunction = new MyMD5Function();
    // when
    byte[] hash = hashFunction.hash(text);
    String hashcode = HashToHex.toHex(hash);
    // then
    // https://en.wikipedia.org/wiki/MD5#MD5_hashes
    assertEquals("e4d909c290d0fb1ca068ffaddf22cbd0", hashcode);
  }

  @Test
  void testHash3() {

    // given
    String text = "";
    HashFunction hashFunction = new MyMD5Function();
    // when
    byte[] hash = hashFunction.hash(text);
    String hashcode = HashToHex.toHex(hash);
    // then
    // https://en.wikipedia.org/wiki/MD5#MD5_hashes
    assertEquals("d41d8cd98f00b204e9800998ecf8427e", hashcode);
  }

  @Test
  void testHash4() {

    // given
    String text = "The 128-bit (16-byte) MD5 hashes (also termed message digests) are typically represented as a sequence of 32 hexadecimal digits. The following demonstrates a 43-byte ASCII input and the corresponding MD5 hash";
    HashFunction hashFunction = new MyMD5Function();
    // when
    byte[] hash = hashFunction.hash(text);
    String hashcode = HashToHex.toHex(hash);
    // then
    // https://en.wikipedia.org/wiki/MD5#MD5_hashes
    assertEquals("0f4cfc9873b7ce6e6e20320a5614cb18", hashcode);

  }
}
