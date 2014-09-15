package test.web;

public class Asingle {
  static {
    System.out.println("A is loaded.");
  }
  static class Aholder{
    static Asingle ins = createA();
  }
  public static Asingle get(){
    return Aholder.ins;
  }
  public static Asingle createA(){
    System.out.println("A is created.");
    return new Asingle();
  }
}
