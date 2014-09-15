package com.abuye.core.web;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.abuye.core.bean.PairStringObject;

public abstract class ControllerMappingCache {
  private static Map<String, ControllerMapping> cache =
      new HashMap<String, ControllerMapping>();

  public static Map<String, ControllerMapping> get(){
    return cache;
  }
  
  /**
   * simple mappings cache
   * 
   * @param clazz
   */
  public static void addControllerClass(Class clazz) {
    // RequestMapping to RequestMappingInfo
    ControllerMappingAnnotation rm = (ControllerMappingAnnotation) clazz.getAnnotation(ControllerMappingAnnotation.class);
    String[] classPatterns = rm.value();
    // TODO validation

    Method[] ms = clazz.getMethods();
    List<PairStringObject<Method>> methodMappings = new ArrayList<PairStringObject<Method>>();
    for (Method m : ms) {
      if (m.isAnnotationPresent(ControllerMappingAnnotation.class)) {
        ControllerMappingAnnotation mrm = m.getAnnotation(ControllerMappingAnnotation.class);
        String[] mvs = mrm.value();
        // TODO validation method pattern

        // assembly method RequestMappingInfo
        for (String mv : mvs) {
          PairStringObject<Method> p = PairStringObject.n(mv, m);
          methodMappings.add(p);
        }
      }
    }
    // assembly RequestMappingInfo
    ControllerMapping rmi = new ControllerMapping();
    String key = clazz.getName();
    rmi.setControllerName(key);
    rmi.setControllerClass(clazz);
    rmi.setClassPatterns(classPatterns);
    rmi.setOperationMappings(methodMappings);
    // put into cache
    cache.put(key, rmi);
  }
}
