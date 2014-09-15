package com.abuye.auth.web;

import com.abuye.core.web.ControllerMappingCache;

public class InitMapping {
  static{
    ControllerMappingCache.addControllerClass(LoginAction.class);
  }
}
