package com.abuye.core.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class PropCache {
  private static final Map<String, Properties> cache = new ConcurrentHashMap<String, Properties>();

  public static Map<String, Properties> getCache(){
    return cache;
  }
  
  public static Properties get(String propertiesPath) throws IOException {
    Properties ps = null;
    synchronized (cache) {
      ps = cache.get(propertiesPath);
      if (ps == null) {
        ps = U.loadProperties(propertiesPath);
        cache.put(propertiesPath, ps);
      }
    }
    return ps;
  }

  public static Properties refresh(String propertiesPath) throws IOException {
    Properties ps = null;
    synchronized (cache) {
      ps = U.loadProperties(propertiesPath);
      cache.put(propertiesPath, ps);
    }
    return ps;
  }

}
