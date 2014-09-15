package com.abuye.core.utils;

import java.util.Random;

public abstract class R {

  static Random r = new Random();

  /**
   * 根据一个范围，生成一个随机的整数
   * 
   * @param min
   *        最小值（包括）
   * @param max
   *        最大值（包括）
   * @return 随机数
   */
  public static int random(int min, int max) {
    return r.nextInt(max - min + 1) + min;
  }

  /**
   * 根据一个长度范围，生成一个随机的字符串，字符串内容为 [0-9a-zA-Z_]
   * 
   * @param min
   *        最小值（包括）
   * @param max
   *        最大值（包括）
   * @return 随机字符串
   */

  public static StringGenerator sg(int min, int max) {
    return new StringGenerator(min, max);
  }

  /**
   * 生成一个确定长度的随机字符串，字符串内容为 [0-9a-zA-Z_]
   * 
   * @param len
   *        字符串长度
   * @return 随机字符串
   */
  public static StringGenerator sg(int len) {
    return new StringGenerator(len, len);
  }
}
