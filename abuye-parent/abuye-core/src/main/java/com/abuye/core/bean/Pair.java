package com.abuye.core.bean;

import java.io.Serializable;

/**
 * 常用的键值对Bean
 * 
 * @author chandler
 *
 * @param <K>
 * @param <V>
 */
public class Pair<K, V> implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -9013015623274677692L;
  
  public K name;
  public V value;

  public static Pair n() {
    return new Pair();
  }

  public static Pair create(String name, Object value) {
    Pair p = new Pair();
    p.setName(name);
    p.setValue(value);
    return p;
  }

  public Pair name(@SuppressWarnings("hiding") K name) {
    this.name = name;
    return this;
  }

  public Pair value(@SuppressWarnings("hiding") V value) {
    this.value = value;
    return this;
  }

  public K getName() {
    return name;
  }

  public void setName(K name) {
    this.name = name;
  }

  public V getValue() {
    return value;
  }

  public void setValue(V value) {
    this.value = value;
  }
}
