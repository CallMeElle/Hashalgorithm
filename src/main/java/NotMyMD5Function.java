import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import io.github.callmeelle.pp.hash.HashToHex;
import io.github.callmeelle.pp.hash.MyMD5Function;

// This code is mostly copy pasted from
// https://github.com/dongwonKim/MD5/blob/master/run.java/ (18.05.2022)
// and then edited to hash Sting inputs too

//This was a reference for my own code in case I had difficulties

public class NotMyMD5Function {

  static byte[] M;

  static long[] T = new long[64];

  static long[] X = new long[16];

  static long A = 0x67452301;

  static long B = 0xEFCDAB89;

  static long C = 0x98BADCFE;

  static long D = 0x10325476;

  static long temp_A;

  static long temp_B;

  static long temp_C;

  static long temp_D;

  public static long rotate_left(long x, long s) {

    System.out.println("F = " + x);
    return ((x) << (s)) | ((x) >>> (32 - s)) & 0xFFFFFFFFL;
  }

  public static long encode(long t) {

    return ((t >> 24) & 0xff) | ((t >> 16) & 0xff) << 8 | ((t >> 8) & 0xff) << 16 | (t & 0xff) << 24;
  }

  public static long ff(long a, long b, long c, long d, long k, long s, long i) {

    return (b + rotate_left(((a + ((b & c) | ((~b) & d)) + k + i) & 0xFFFFFFFFL), s)) & 0xFFFFFFFFL;
  }

  public static long gg(long a, long b, long c, long d, long k, long s, long i) {

    return (b + rotate_left(((a + ((b & d) | (c & (~d))) + k + i) & 0xFFFFFFFFL), s)) & 0xFFFFFFFFL;
  }

  public static long hh(long a, long b, long c, long d, long k, long s, long i) {

    return (b + rotate_left(((a + (b ^ c ^ d) + k + i) & 0xFFFFFFFFL), s)) & 0xFFFFFFFFL;
  }

  public static long ii(long a, long b, long c, long d, long k, long s, long i) {

    return (b + rotate_left(((a + (c ^ (b | (~d))) + k + i) & 0xFFFFFFFFL), s)) & 0xFFFFFFFFL;
  }

  public static byte[] longToByteArray(long value) {

    return new byte[] { (byte) (value), (byte) (value >> 8), (byte) (value >> 16), (byte) (value >> 24),
    (byte) (value >> 32), (byte) (value >> 40), (byte) (value >> 48), (byte) (value >> 56) };
  }

  public static void table_T() {

    for (int i = 0; i < 64; i++) {
      T[i] = (long) (Math.floor(Math.abs(Math.sin(i + 1)) * (long) Math.pow(2, 32)));
    }
  }

  public static void main(String[] args) throws IOException {

    String inputString = args[0];
    byte[] bytes = inputString.getBytes(StandardCharsets.UTF_8);
    System.out.println(HashToHex.toHex(bytes));
    long length = 0;
    int padding_length = 0;
    int paddingIndex, i;
    byte[] pad;
    int g = 0, div16, k = 0;

    length = bytes.length;

    if (length % 64 < 56) {
      padding_length = (int) (56 - length % 64);
    } else if (length % 64 > 56) {
      padding_length = (int) (64 - (length % 64 - 56));
    } else if (length % 64 == 56) {
      padding_length = 64;
    }
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
      BufferedInputStream bis = new BufferedInputStream(bais);
      DataInputStream dis = new DataInputStream(bis);

      M = new byte[(int) (length + padding_length + 8)];

      for (paddingIndex = 0; paddingIndex < length + padding_length; paddingIndex++) {
        if (paddingIndex < length) {
          M[paddingIndex] = (byte) dis.read();
        } else if (paddingIndex == length) {
          M[paddingIndex] = (byte) 128;
        } else {
          M[paddingIndex] = 0;
        }
      }

      pad = longToByteArray(length * 8);

      for (paddingIndex = 0; paddingIndex < 8; paddingIndex++) {
        M[(int) (paddingIndex + length + padding_length)] = pad[paddingIndex];
      }

      table_T();
      for (paddingIndex = 0; paddingIndex < (length + padding_length + 8) / 64; paddingIndex++) {
        byte[] debug = new byte[4];
        for (i = 0, k = 0; i < 16; i++, k += 4) {
          X[i] = (M[paddingIndex * 64 + k] & 0xFF) | (M[paddingIndex * 64 + k + 1] & 0xFF) << 8
              | (M[paddingIndex * 64 + k + 2] & 0xFF) << 16 | (M[paddingIndex * 64 + k + 3] & 0xFF) << 24;
          MyMD5Function.fillBytes(X[i], debug, 0);
          System.out.println("X[" + i + "]=" + HashToHex.toHex(debug));
        }
        temp_A = A;
        temp_B = B;
        temp_C = C;
        temp_D = D;

        for (i = 0; i < 64; i++) {
          System.out.println("A = " + A);
          System.out.println("B = " + B);
          System.out.println("C = " + C);
          System.out.println("D = " + D);
          div16 = i >>> 4;

          switch (div16) {
            case 0:
              g = i;
              if (i % 4 == 0) {
                A = ff(A, B, C, D, X[g], 7, T[i]);
              } else if (i % 4 == 1) {
                D = ff(D, A, B, C, X[g], 12, T[i]);
              } else if (i % 4 == 2) {
                C = ff(C, D, A, B, X[g], 17, T[i]);
              } else if (i % 4 == 3) {
                B = ff(B, C, D, A, X[g], 22, T[i]);
              }
              break;
            case 1:
              g = (i * 5 + 1) % 16;
              if (i % 4 == 0) {
                A = gg(A, B, C, D, X[g], 5, T[i]);
              } else if (i % 4 == 1) {
                D = gg(D, A, B, C, X[g], 9, T[i]);
              } else if (i % 4 == 2) {
                C = gg(C, D, A, B, X[g], 14, T[i]);
              } else if (i % 4 == 3) {
                B = gg(B, C, D, A, X[g], 20, T[i]);
              }
              break;
            case 2:
              g = (i * 3 + 5) % 16;
              if (i % 4 == 0) {
                A = hh(A, B, C, D, X[g], 4, T[i]);
              } else if (i % 4 == 1) {
                D = hh(D, A, B, C, X[g], 11, T[i]);
              } else if (i % 4 == 2) {
                C = hh(C, D, A, B, X[g], 16, T[i]);
              } else if (i % 4 == 3) {
                B = hh(B, C, D, A, X[g], 23, T[i]);
              }
              break;

            case 3:
              g = (i * 7) % 16;
              if (i % 4 == 0) {
                A = ii(A, B, C, D, X[g], 6, T[i]);
              } else if (i % 4 == 1) {
                D = ii(D, A, B, C, X[g], 10, T[i]);
              } else if (i % 4 == 2) {
                C = ii(C, D, A, B, X[g], 15, T[i]);
              } else if (i % 4 == 3) {
                B = ii(B, C, D, A, X[g], 21, T[i]);
              }
              break;
          }
        }
        System.out.println("i = " + i);
        System.out.println("g = " + g);
        A = (A + temp_A) & 0xFFFFFFFFL;
        B = (B + temp_B) & 0xFFFFFFFFL;
        C = (C + temp_C) & 0xFFFFFFFFL;
        D = (D + temp_D) & 0xFFFFFFFFL;
        System.out.println("A0 = " + A);
        System.out.println("B0 = " + B);
        System.out.println("C0 = " + C);
        System.out.println("D0 = " + D);
      }

      A = encode(A);
      B = encode(B);
      C = encode(C);
      D = encode(D);

      System.out.format("%x%x%x%x\n", A, B, C, D);
      bis.close();
      dis.close();
    } catch (IOException e) {
      System.out.println("There is no such an file.");
    }
  }
}