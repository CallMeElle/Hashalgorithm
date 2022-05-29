package io.github.callmeelle.pp.hash;

/**
 * My version of the MD5 hashing algorithm
 *
 */
public class MyMD5Function implements HashFunction {

  // s specifies the per-round shift amounts
  private static final byte[] S = new byte[] { //
  7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, //
  5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, //
  4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, //
  6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21 };

  // constants for Hash-Algorithm
  // calculated with:
  // for (int i = 0; i < 64; i++) {
  /// long ki = (long) (Math.floor(Math.abs(Math.sin(i + 1)) * (long) Math.pow(2, 32)));
  // this.K[i] = ki;
  // }
  private static final long[] K = new long[] { mask(0xd76aa478), mask(0xe8c7b756), mask(0x242070db), mask(0xc1bdceee), //
  mask(0xf57c0faf), mask(0x4787c62a), mask(0xa8304613), mask(0xfd469501), //
  mask(0x698098d8), mask(0x8b44f7af), mask(0xffff5bb1), mask(0x895cd7be), //
  mask(0x6b901122), mask(0xfd987193), mask(0xa679438e), mask(0x49b40821), //
  mask(0xf61e2562), mask(0xc040b340), mask(0x265e5a51), mask(0xe9b6c7aa), //
  mask(0xd62f105d), mask(0x02441453), mask(0xd8a1e681), mask(0xe7d3fbc8), //
  mask(0x21e1cde6), mask(0xc33707d6), mask(0xf4d50d87), mask(0x455a14ed), //
  mask(0xa9e3e905), mask(0xfcefa3f8), mask(0x676f02d9), mask(0x8d2a4c8a), //
  mask(0xfffa3942), mask(0x8771f681), mask(0x6d9d6122), mask(0xfde5380c), //
  mask(0xa4beea44), mask(0x4bdecfa9), mask(0xf6bb4b60), mask(0xbebfbc70), //
  mask(0x289b7ec6), mask(0xeaa127fa), mask(0xd4ef3085), mask(0x04881d05), //
  mask(0xd9d4d039), mask(0xe6db99e5), mask(0x1fa27cf8), mask(0xc4ac5665), //
  mask(0xf4292244), mask(0x432aff97), mask(0xab9423a7), mask(0xfc93a039), //
  mask(0x655b59c3), mask(0x8f0ccc92), mask(0xffeff47d), mask(0x85845dd1), //
  mask(0x6fa87e4f), mask(0xfe2ce6e0), mask(0xa3014314), mask(0x4e0811a1), //
  mask(0xf7537e82), mask(0xbd3af235), mask(0x2ad7d2bb), mask(0xeb86d391) };

  @Override
  public byte[] hash(byte[] input) {

    // initialize Variables
    Payload payload = new Payload(input);
    long[] M = new long[16];

    // set Buffers
    long a0 = 0x67452301; // A
    long b0 = 0xefcdab89; // B
    long c0 = 0x98badcfe; // C
    long d0 = 0x10325476; // D

    // divide padded message in 64 byte blocks
    for (int payloadIndex = 0; payloadIndex < payload.getPaddedLength(); payloadIndex = payloadIndex + 64) {

      // break down in 4 byte blocks
      for (int i = 0; i < M.length; i++) {
        M[i] = payload.getLong(payloadIndex + i * 4);
      }

      // Begin calculate Hash
      long a = a0;
      long b = b0;
      long c = c0;
      long d = d0;

      // for each 64 byte block do
      for (int i = 0; i < 64; i++) {
        long F = 0;
        int g = 0;

        if (i <= 15) {
          F = (b & c) | ((~b) & d);
          g = i;
        } else if (i <= 31) {
          F = (d & b) | ((~d) & c);
          g = (5 * i + 1) % 16;
        } else if (i <= 47) {
          F = b ^ c ^ d;
          g = (3 * i + 5) % 16;
        } else if (i <= 63) {
          F = c ^ (b | (~d));
          g = (7 * i) % 16;
        }

        F = F + a + K[i] + M[g];

        a = d;
        d = c;
        c = b;
        b = b + leftrotate(mask(F), S[i]); // is it a rotate or a shift?????

      }

      // new buffers for next 64-byte block of the message or final hash
      a0 = a0 + a;
      b0 = b0 + b;
      c0 = c0 + c;
      d0 = d0 + d;
    }

    // return final hash as byte[]
    byte[] hash = new byte[16];
    fillBytes(a0, hash, 0);
    fillBytes(b0, hash, 4);
    fillBytes(c0, hash, 8);
    fillBytes(d0, hash, 12);
    return hash;
  }

  private static long mask(long value) {

    return value & 0xFFFFFFFFL;
  }

  /**
   * @param value
   * @param bits
   * @return
   */
  private static long leftrotate(long value, byte bits) {

    return mask((value << bits) | value >>> (32 - bits));
  }

  private static long maskByte(byte b) {

    return (b & 0xFF);
  }

  private static long convertBytesToLong(byte b1, byte b2, byte b3, byte b4) {

    return maskByte(b1) | maskByte(b2) << 8 | maskByte(b3) << 16 | maskByte(b4) << 24;
  }

  /**
   *
   * @param value
   * @param hash
   * @param i
   */
  public static void fillBytes(long value, byte[] hash, int i) {

    hash[i + 3] = (byte) (value >> 24);
    hash[i + 2] = (byte) (value >> 16);
    hash[i + 1] = (byte) (value >> 8);
    hash[i] = (byte) (value);
  }

  private static class Payload {

    private final byte[] data;

    private final int zeroPaddingEnd;

    /**
     * The constructor.
     */
    public Payload(byte[] data) {

      // determine at which byte number the padding with 0s ends and the message length follows
      super();
      this.data = data;
      int pad = data.length % 64;
      if (pad < 56) {
        this.zeroPaddingEnd = data.length + (56 - pad);
      } else if (pad == 56) {
        this.zeroPaddingEnd = data.length + 56;
      } else {
        this.zeroPaddingEnd = data.length + (120 - pad);
      }
    }

    /**
     *
     * @return complete length of message with padding (+length)
     */
    public int getPaddedLength() {

      return this.zeroPaddingEnd + 8;
    }

    /**
     *
     * @param i number of specific byte from padded message
     * @return byte i
     */
    // receive byte for hashing algorithm
    public byte getByte(int i) {

      if (i < this.data.length) { // return data from message
        return this.data[i];
      } else if (i == this.data.length) { // return 10000000 as divider between message and padding
        return (byte) 128;
      } else if (i < this.zeroPaddingEnd) { // return 0s
        return (byte) 0;
      } else { // return message length
        int shiftBytes = i - this.zeroPaddingEnd;
        long result = (this.data.length * 8);
        result = result >> (shiftBytes * 8);
        return (byte) result;
      }
    }

    /**
     *
     * @param i byte from padded message
     * @return long consisting of 4 bytes from message
     */
    public long getLong(int i) {

      return convertBytesToLong(getByte(i), getByte(i + 1), getByte(i + 2), getByte(i + 3));
    }

  }

}
