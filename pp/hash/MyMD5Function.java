package io.github.callmeelle.pp.hash;

/**
 * TODO elle This type ...
 *
 */
public class MyMD5Function implements HashFunction {
  // s specifies the per-round shift amounts
  private static final byte[] S = new byte[] { //
  7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, //
  5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, //
  4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, //
  6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21 };

  private final long[] K = new long[] { 0xd76aa478, 0xe8c7b756, 0x242070db, 0xc1bdceee, //
  0xf57c0faf, 0x4787c62a, 0xa8304613, 0xfd469501, //
  0x698098d8, 0x8b44f7af, 0xffff5bb1, 0x895cd7be, //
  0x6b901122, 0xfd987193, 0xa679438e, 0x49b40821, //
  0xf61e2562, 0xc040b340, 0x265e5a51, 0xe9b6c7aa, //
  0xd62f105d, 0x02441453, 0xd8a1e681, 0xe7d3fbc8, //
  0x21e1cde6, 0xc33707d6, 0xf4d50d87, 0x455a14ed, //
  0xa9e3e905, 0xfcefa3f8, 0x676f02d9, 0x8d2a4c8a, //
  0xfffa3942, 0x8771f681, 0x6d9d6122, 0xfde5380c, //
  0xa4beea44, 0x4bdecfa9, 0xf6bb4b60, 0xbebfbc70, //
  0x289b7ec6, 0xeaa127fa, 0xd4ef3085, 0x04881d05, //
  0xd9d4d039, 0xe6db99e5, 0x1fa27cf8, 0xc4ac5665, //
  0xf4292244, 0x432aff97, 0xab9423a7, 0xfc93a039, //
  0x655b59c3, 0x8f0ccc92, 0xffeff47d, 0x85845dd1, //
  0x6fa87e4f, 0xfe2ce6e0, 0xa3014314, 0x4e0811a1, //
  0xf7537e82, 0xbd3af235, 0x2ad7d2bb, 0xeb86d391 };

  @Override
  public byte[] hash(byte[] input) {

    System.out.println(HashToBinary.formatHex(input));
    Payload payload = new Payload(input);
    int length = input.length;
    // long[] K = new long[64];
    long[] M = new long[16];

    // set Buffers
    long a0 = 0x67452301; // A
    long b0 = 0xefcdab89; // B
    long c0 = 0x98badcfe; // C
    long d0 = 0x10325476; // D

    // calculate set values for K[]
    // for (int i = 0; i < 64; i++) {
    // K[i] = (long) (Math.floor(Math.pow(2, 32) * Math.abs(Math.sin(i + 1))));
    // }

    // divide padded message in 512-bit blocks and these into 16 32-bit blocks (M[j])
    for (int payloadIndex = 0; payloadIndex < payload.getPaddedLength(); payloadIndex = payloadIndex + 64) {

      byte[] debug = new byte[4];
      for (int i = 0; i < M.length; i++) {
        M[i] = payload.getLong(payloadIndex + i * 4);
        fillBytes(M[i], debug, 0);
        System.out.println("M[" + i + "]=" + HashToBinary.formatHex(debug));
      }

      // Begin calculate Hash
      long a = a0;
      long b = b0;
      long c = c0;
      long d = d0;

      for (int i = 0; i < 64; i++) {
        System.out.println("A = " + a);
        System.out.println("B = " + b);
        System.out.println("C = " + c);
        System.out.println("D = " + d);
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
        } else {
          System.out.println("Error: more than 4 Rounds in non-linear logarithm Function");
        }

        F = F + a + this.K[i] + M[g];

        a = d;
        d = c;
        c = b;
        b = b + leftrotate(F, S[i]);

      }

      a0 = mask(a0 + a);
      b0 = mask(b0 + b);
      c0 = mask(c0 + c);
      d0 = mask(d0 + d);
      System.out.println("A0 = " + a0);
      System.out.println("B0 = " + b0);
      System.out.println("C0 = " + c0);
      System.out.println("D0 = " + d0);

    }

    byte[] hash = new byte[16];
    fillBytes(a0, hash, 0);
    fillBytes(b0, hash, 4);
    fillBytes(c0, hash, 8);
    fillBytes(d0, hash, 12);
    return hash;
  }

  private long mask(long value) {

    return value & 0xFFFFFFFFL;
  }

  /**
   * @param value
   * @param bits
   * @return
   */
  private long leftrotate(long value, byte bits) {

    return ((value) << (bits)) | ((value) >>> (32 - bits)) & 0xFFFFFFFFL;
  }

  /**
   * @param a0
   * @param hash
   * @param i
   */
  public static void fillBytes(long value, byte[] hash, int i) {

    hash[i] = (byte) (value >> 24);
    hash[i + 1] = (byte) (value >> 16);
    hash[i + 2] = (byte) (value >> 8);
    hash[i + 3] = (byte) (value);
  }

  private static class Payload {

    private final byte[] data;

    private final int zeroPaddingEnd;

    /**
     * The constructor.
     */
    public Payload(byte[] data) {

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

    public int getPaddedLength() {

      return this.zeroPaddingEnd + 8;
    }

    // receive byte for
    public byte getByte(int i) {

      if (i < this.data.length) {
        return this.data[i];
      } else if (i == this.data.length) {
        return (byte) 128;
      } else if (i < this.zeroPaddingEnd) {
        return (byte) 0;
      } else {
        // length
        int shiftBytes = i - this.zeroPaddingEnd;
        long result = (this.data.length * 8);
        result = result >> (shiftBytes * 8);
        return (byte) result;
      }
      // // TODO 100...... message length
      //
      // int padding_length = 0;
      //
      // System.out.println("Laenge der Eingabe: " + this.data.length);
      //
      // byte[] rest;
      // int length = this.data.length;
      // if (this.data.length % 64 < 56) {
      // padding_length = 56 - this.data.length % 64;
      // for (int j = 0; j < (padding_length - 8); j++) {
      // // keine neuen 64 byte nötig
      // }
      //
      // } else if (length % 64 > 56) {
      // padding_length = 64 - (length % 64 - 56);
      // // neue 64 byte nötig
      // } else if (length % 64 == 56) {
      // padding_length = 64;
      // // neue 64 byte nötig
      // }
      //
      // throw new IllegalStateException("TODO");
    }

    public long getLong(int i) {

      return getLong(getByte(i), getByte(i + 1), getByte(i + 2), getByte(i + 3));
    }

    private static long getInt(byte b) {

      return (b & 0xFF);
    }

    private static long getLong(byte b1, byte b2, byte b3, byte b4) {

      return getInt(b1) | getInt(b2) << 8 | getInt(b3) << 16 | getInt(b4) << 24;
    }

  }

}
