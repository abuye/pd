package com.abuye.core.jdbc.connectionpool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

public class JDBCConnectionDynamicalProxy implements InvocationHandler {
  private CommonDAOConnectionPool currentJDBCConnectionPool = null;
  private Connection nextFreeJDBCConnection = null;

  public JDBCConnectionDynamicalProxy() {}

  public JDBCConnectionDynamicalProxy(CommonDAOConnectionPool currentJDBCConnectionPool) {
    this.currentJDBCConnectionPool = currentJDBCConnectionPool;
  }

  Connection bind(Connection nextFreeJDBCConnection) {
    this.nextFreeJDBCConnection = nextFreeJDBCConnection;

    Connection warpedJDBCConnetion =
        (Connection) Proxy.newProxyInstance(getClass().getClassLoader(),
            new Class[] {Connection.class}, this);

    return warpedJDBCConnetion;
  }

  public Object invoke(Object proxyTargetInstance, Method targetMethod, Object[] methodArguments)
      throws Throwable {
    String methodname = targetMethod.getName();
    if (methodname.equalsIgnoreCase("close")) {
      this.currentJDBCConnectionPool.reAddToConnectionPool(this.nextFreeJDBCConnection);
      return null;
    }

    return targetMethod.invoke(this.nextFreeJDBCConnection, methodArguments);
  }
}
