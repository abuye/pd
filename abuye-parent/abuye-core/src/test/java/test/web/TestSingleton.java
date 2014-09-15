package test.web;

import com.abuye.core.utils.U;

import junit.framework.TestCase;

public class TestSingleton extends TestCase {
  public void test1() throws Exception{
//    U.CLASS_LOADER.loadClass("test.web.Asingle");
//    new Asingle();
//    Class.forName("test.web.Asingle");
    Asingle.get();
  }
}
