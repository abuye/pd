package test.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abuye.core.web.ControllerMappingAnnotation;
import com.abuye.core.web.ControllerMappingCache;

@ControllerMappingAnnotation("/aaa")
public class AaaAction {
  private static final Logger log = LoggerFactory.getLogger(AaaAction.class);
  static{
    ControllerMappingCache.addControllerClass(AaaAction.class);
  }
  @ControllerMappingAnnotation("/exe.do")
  public void exe(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    log.info("aaa called.");
  }
}
