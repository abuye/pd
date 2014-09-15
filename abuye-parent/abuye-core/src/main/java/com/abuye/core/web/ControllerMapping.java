package com.abuye.core.web;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.abuye.core.bean.PairStringObject;

public class ControllerMapping {
  private String controllerName;
  private Class controllerClass;
  private String[] controllerPaths;
  private List<PairStringObject<Method>> operationMappings = new ArrayList<PairStringObject<Method>>();

  public String getControllerName() {
    return controllerName;
  }

  public void setControllerName(String className) {
    this.controllerName = className;
  }

  public Class getControllerClass() {
    return controllerClass;
  }

  public void setControllerClass(Class clazz) {
    this.controllerClass = clazz;
  }

  public List<PairStringObject<Method>> getOperationMappings() {
    return operationMappings;
  }

  public void setOperationMappings(List<PairStringObject<Method>> methodMappings) {
    this.operationMappings = methodMappings;
  }

  public String[] getClassPatterns() {
    return controllerPaths;
  }

  public void setClassPatterns(String[] classPatterns) {
    this.controllerPaths = classPatterns;
  }

}
