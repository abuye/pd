package com.abuye.core.bean;

/**
 * 常用的键值对Bean
 * 
 * @author chandler
 *
 * @param <T>
 */
public class PairStringObject<T> extends Pair<String, T> {

  /**
   * 
   */
  private static final long serialVersionUID = 8471440997466167707L;

  public static PairStringObject create() {
    return new PairStringObject();
  }

  public static PairStringObject n(String name, Object value) {
    PairStringObject p = new PairStringObject();
    p.setName(name);
    p.setValue(value);
    return p;
  }

  public PairStringObject name(@SuppressWarnings("hiding") String name) {
    this.name = name;
    return this;
  }

  public PairStringObject value(@SuppressWarnings("hiding") T value) {
    this.value = value;
    return this;
  }
}
