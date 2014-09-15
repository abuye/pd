package com.abuye.core.web;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import com.abuye.core.bean.PairStringObject;
import com.abuye.core.utils.U;

public class ControllerServlet extends HttpServlet {

  private static final long serialVersionUID = -930386857509367419L;
  private static final Logger log = LoggerFactory.getLogger(ControllerServlet.class);

  static {
    try {
       Properties ps = U.loadProperties("abuye.properties");
       String scanBasePackage = ps.getProperty("scanBasePackage");
       scanAnnotation(scanBasePackage);
       log.info("scanBasePackage=" + scanBasePackage);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  

  private static void scanAnnotation(String basePackage) throws Exception {
    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    MetadataReaderFactory metadataReaderFactory =
        new CachingMetadataReaderFactory(resourcePatternResolver);
    String packageSearchPath =
        ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
            + ClassUtils.convertClassNameToResourcePath(basePackage) + "/**/*.class";
    Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
    for (Resource resource : resources) {
      if (resource.isReadable()) {
        try {
          MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
          if (isCandidateComponent(metadataReader)) {
            String cn = metadataReader.getClassMetadata().getClassName();
            Class.forName(cn);
          }
        } catch (Throwable ex) {
          throw new Exception("Failed to read candidate component class: " + resource, ex);
        }
      }
    }
  }

  protected static boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
    AnnotationMetadata am = metadataReader.getAnnotationMetadata();
    return am.hasAnnotation("com.abuye.core.web.ControllerMappingAnnotation");
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    control(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    control(request, response);
  }

  protected void control(HttpServletRequest request, HttpServletResponse response) {
    // get request path
    // cause config in web.xml
    // <url-pattern>/</url-pattern>
    // we can use the servletPath as requestPath
    String requestPath = getServletPath(request);
    // TODO if("/".equals(requestPath))
    log.info("requestPath:" + requestPath);

    // find requestmappinginfo
    ControllerMapping requestMappingInfo = null;
    String methodPath = null;
    for (ControllerMapping rm : ControllerMappingCache.get().values()) {
      // match class pattern
      String[] classPatterns = rm.getClassPatterns();
      // get mappingInfo from mappings
      boolean in = false;
      for (String cp : classPatterns) {
        if (requestPath.startsWith(cp)) {
          requestMappingInfo = rm;
          methodPath = requestPath.substring(cp.length());
          in = true;
          break;
        }
      }
      if (in) {
        break;
      }
    }
    if (requestMappingInfo == null) {
      log.info("Cannot find controller for path [" + requestPath + "].");
      sendError(HttpServletResponse.SC_NOT_FOUND, response);
      return;
    }
    Method m = null;
    m = getActionMethod(methodPath, requestMappingInfo);
    if (m == null) {
      log.info("Cannot find operation for path [" + requestPath + "] of controller ["
          + requestMappingInfo.getControllerName() + "].");
      sendError(HttpServletResponse.SC_NOT_FOUND, response);
      return;
    }
    Object action = getClassInstance(requestMappingInfo.getControllerClass());
    try {
      // no result
      m.invoke(action, request, response);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
    }

  }

  public static void sendError(int code, HttpServletResponse response) {
    try {
      response.sendError(code);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public static Object getClassInstance(Class clazz) {
    try {
      return clazz.newInstance();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  private static Method getActionMethod(String methodPath, ControllerMapping mappingInfo) {
    for (PairStringObject<Method> mp : mappingInfo.getOperationMappings()) {
      // match method patterns
      if (methodPath.equals(mp.getName())) {
        return mp.getValue();
      }
    }
    return null;
  }

  /**
   * Return the servlet path for the given request, regarding an include request
   * URL if called within a RequestDispatcher include.
   * <p>
   * As the value returned by <code>request.getServletPath()</code> is already decoded by the
   * servlet container, this method will not attempt to decode it.
   * 
   * @param request current HTTP request
   * @return the servlet path
   */
  private static String getServletPath(HttpServletRequest request) {
    String servletPath = request.getServletPath();
    // simply fix WebSphere bug
    if (servletPath.length() > 1 && servletPath.endsWith("/")) {
      // On WebSphere, in non-compliant mode, for a "/foo/" case that would be "/foo"
      // on all other servlet containers: removing trailing slash, proceeding with
      // that remaining slash as final lookup path...
      servletPath = servletPath.substring(0, servletPath.length() - 1);
    }
    return servletPath;
  }
}
