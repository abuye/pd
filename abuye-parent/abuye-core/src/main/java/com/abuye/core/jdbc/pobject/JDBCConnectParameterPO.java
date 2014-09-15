package com.abuye.core.jdbc.pobject;

public class JDBCConnectParameterPO {
  private int initJDBCConnectionCount = 5;
  private int maxJDBCConnectionCount = 10;

  private int countBuildedConnection = 0;
  private String driverClassName;
  private String dbURL;
  private String loginName;
  private String loginPassWord;
  private int currentDBType;

  public int getInitJDBCConnectionCount() {
    return this.initJDBCConnectionCount;
  }

  public void setInitJDBCConnectionCount(int initJDBCConnectionCount) {
    this.initJDBCConnectionCount = initJDBCConnectionCount;
  }

  public int getMaxJDBCConnectionCount() {
    return this.maxJDBCConnectionCount;
  }

  public void setMaxJDBCConnectionCount(int maxJDBCConnectionCount) {
    this.maxJDBCConnectionCount = maxJDBCConnectionCount;
  }

  public int getCountBuildedConnection() {
    return this.countBuildedConnection;
  }

  public void setCountBuildedConnection(int currentCount) {
    this.countBuildedConnection = currentCount;
  }

  public String getDriverClassName() {
    return this.driverClassName;
  }

  public void setDriverClassName(String driverClassName) {
    this.driverClassName = driverClassName;
  }

  public String getDbURL() {
    return this.dbURL;
  }

  public void setDbURL(String dbURL) {
    this.dbURL = dbURL;
  }

  public String getLoginName() {
    return this.loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public String getLoginPassWord() {
    return this.loginPassWord;
  }

  public void setLoginPassWord(String loginPassWord) {
    this.loginPassWord = loginPassWord;
  }

  public int getCurrentDBType() {
    return this.currentDBType;
  }

  public void setCurrentDBType(int currentDBType) {
    this.currentDBType = currentDBType;
  }
}
