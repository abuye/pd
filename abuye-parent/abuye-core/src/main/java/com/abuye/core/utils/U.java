package com.abuye.core.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class U {
  private static final Logger log = LoggerFactory.getLogger(U.class);
  public static final ClassLoader CLASS_LOADER = U.class.getClassLoader();
  public static final String DEFAULT_PROPERTIES_PATH = "abuye.properties";
  static {
    if(CLASS_LOADER == null){
      log.error("CLASS_LOADER cannot be null.");
    }
  }
  
  public static Properties getDefaultProperties() throws IOException{
    return PropCache.get(DEFAULT_PROPERTIES_PATH);
  }

  public static File loadFile(String name) throws URISyntaxException {
    URL u = CLASS_LOADER.getResource(name);
    URI ui = u.toURI();
    File f = new File(ui);
    return f;
  }

  public static Properties loadProperties(String name) throws IOException {
    InputStream inStream = CLASS_LOADER.getResourceAsStream(name);
    Properties ps = new Properties();
    ps.load(inStream);
    return ps;
  }


}
