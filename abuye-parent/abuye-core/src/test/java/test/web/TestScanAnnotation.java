package test.web;

import java.io.IOException;

import junit.framework.TestCase;

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

import com.abuye.core.web.ControllerMappingCache;

public class TestScanAnnotation extends TestCase {
  private static final Logger log = LoggerFactory.getLogger(TestScanAnnotation.class);

  public void test2() throws Exception {
    scanAnnotation("test.web");
    log.info("" + ControllerMappingCache.get().size());
  }

  public void _test1() throws Exception {
    scanAnnotation("test.web");
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
}
