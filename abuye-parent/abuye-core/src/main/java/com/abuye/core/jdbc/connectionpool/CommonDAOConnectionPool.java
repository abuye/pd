package com.abuye.core.jdbc.connectionpool;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.abuye.core.jdbc.exception.CommonDAOException;
import com.abuye.core.jdbc.pobject.JDBCConnectParameterPO;

public class CommonDAOConnectionPool implements DataSource {
  private ConcurrentLinkedQueue<Connection> jdbcConnectionPool_LinkedList =
      new ConcurrentLinkedQueue();

  private JDBCConnectParameterPO jDBCConnectParameterPO = new JDBCConnectParameterPO();

  protected ResourceBundle resourceBundle = null;

  public CommonDAOConnectionPool() {}

  public CommonDAOConnectionPool(JDBCConnectParameterPO jDBCConnectParameterPO, int currentDBType,
      ResourceBundle resourceBundle) throws CommonDAOException {
    this.jDBCConnectParameterPO = jDBCConnectParameterPO;
    this.resourceBundle = resourceBundle;
    jDBCConnectParameterPO.setCurrentDBType(currentDBType);
    loadJDBCDriverAndInitConnectionPool();
  }

  public CommonDAOConnectionPool(String driverClassName, String dbURL, String loginName,
      String loginPassWord, int currentDBType, ResourceBundle resourceBundle)
      throws CommonDAOException {
    this.resourceBundle = resourceBundle;

    this.jDBCConnectParameterPO = new JDBCConnectParameterPO();
    this.jDBCConnectParameterPO.setDriverClassName(driverClassName);
    this.jDBCConnectParameterPO.setDbURL(dbURL);
    this.jDBCConnectParameterPO.setLoginName(loginName);
    this.jDBCConnectParameterPO.setLoginPassWord(loginPassWord);
    this.jDBCConnectParameterPO.setCurrentDBType(currentDBType);
    loadJDBCDriverAndInitConnectionPool();
  }

  public synchronized Connection getConnection() throws SQLException {
    synchronized (this.jdbcConnectionPool_LinkedList) {
      if (this.jdbcConnectionPool_LinkedList.size() > 0) {
        Connection nextFreeJDBCConnection = (Connection) this.jdbcConnectionPool_LinkedList.poll();

        JDBCConnectionDynamicalProxy proxy = new JDBCConnectionDynamicalProxy(this);

        return proxy.bind(nextFreeJDBCConnection);
      }

      if (this.jDBCConnectParameterPO.getCountBuildedConnection() < this.jDBCConnectParameterPO
          .getMaxJDBCConnectionCount()) {
        Connection newJDBCConnection = createJDBCConnection();

        int currentCount = this.jDBCConnectParameterPO.getCountBuildedConnection();
        this.jDBCConnectParameterPO.setCountBuildedConnection(currentCount + 1);
        return newJDBCConnection;
      }

      throw new SQLException("目前系统中的JDBC数据库连接对象的数量已经达到了规定的最大连接数量，系统的数据访问请求将要暂停。");
    }
  }

  public void reAddToConnectionPool(Connection jdbcConnection) {
    this.jdbcConnectionPool_LinkedList.add(jdbcConnection);
  }

  private void loadJDBCDriverAndInitConnectionPool() throws CommonDAOException {
    try {
      Class.forName(this.jDBCConnectParameterPO.getDriverClassName());
    } catch (ClassNotFoundException exception) {
      throw new CommonDAOException(
          this.resourceBundle.getString("common.connectionPool.driverClassName")
              + this.jDBCConnectParameterPO.getDriverClassName()
              + this.resourceBundle.getString("common.orignalSQLExceptionInfo"), exception);
    }

    for (int i = 0; i < this.jDBCConnectParameterPO.getInitJDBCConnectionCount(); i++) {
      Connection newJDBCConnection = null;
      try {
        newJDBCConnection = createJDBCConnection();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("common.connectionPool.createJDBCConnection")
                + this.resourceBundle.getString("common.orignalSQLExceptionInfo"), exception);
      }
      this.jdbcConnectionPool_LinkedList.add(newJDBCConnection);

      int currentCount = this.jDBCConnectParameterPO.getCountBuildedConnection();
      this.jDBCConnectParameterPO.setCountBuildedConnection(currentCount + 1);
    }
  }

  private Connection createJDBCConnection() throws SQLException {
    Connection jdbcConnection = null;

    Properties jdbcConnectionPropertiesInfo = new Properties();
    jdbcConnectionPropertiesInfo.setProperty("user", this.jDBCConnectParameterPO.getLoginName());
    jdbcConnectionPropertiesInfo.setProperty("password",
        this.jDBCConnectParameterPO.getLoginPassWord());
    switch (this.jDBCConnectParameterPO.getCurrentDBType()) {
      case 1:
        break;
      case 3:
        break;
      case 2:
        jdbcConnectionPropertiesInfo.setProperty("includeSynonyms", "true");
        jdbcConnectionPropertiesInfo.setProperty("remarksReporting", "true");
    }

    jdbcConnection =
        DriverManager.getConnection(this.jDBCConnectParameterPO.getDbURL(),
            jdbcConnectionPropertiesInfo);

    return jdbcConnection;
  }

  public Connection getConnection(String username, String password) throws SQLException {
    this.jDBCConnectParameterPO.setLoginName(username);
    this.jDBCConnectParameterPO.setLoginPassWord(password);

    return getConnection();
  }

  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return false;
  }

  public <T> T unwrap(Class<T> iface) throws SQLException {
    return null;
  }

  public PrintWriter getLogWriter() throws SQLException {
    return null;
  }

  public int getLoginTimeout() throws SQLException {
    return 0;
  }

  public void setLogWriter(PrintWriter arg0) throws SQLException {}

  public void setLoginTimeout(int arg0) throws SQLException {}

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    // TODO Auto-generated method stub
    return null;
  }
}
