package com.abuye.core.utils;

public class CharGenerator {
  private CharGenerator() {}

  private static final char[] src =
      "1234567890_ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

  public static char next() {
    return src[Math.abs(R.r.nextInt(src.length))];
  }
}
