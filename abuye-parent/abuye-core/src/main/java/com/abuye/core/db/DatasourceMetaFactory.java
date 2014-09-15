package com.abuye.core.db;

import org.apache.commons.lang3.StringUtils;


public class DatasourceMetaFactory {
  public static IDatasourceMeta createDatasourceMetaByDriver(String driver) throws Exception {
    if ("oracle.jdbc.driver.OracleDriver".equals(driver)) {
      return new OracleDatasourceMeta();
    }
    if ("com.mysql.jdbc.Driver".equals(driver)) {
      return new MysqlDatasourceMeta();
    }
    if ("org.h2.Driver".equals(driver)) {
      return new H2DatasourceMeta();
    }
    throw new Exception("Cannot find this driver ["+driver+"].");
    // TODO 还有更多数据库类型要处理
  }
  public static IDatasourceMeta createDatasourceMetaByUrl(String url) throws Exception{
    if (StringUtils.startsWith(url, "jdbc:h2:")) {
      return new H2DatasourceMeta();
    }
    if (StringUtils.startsWith(url, "jdbc:mysql:")) {
      return new MysqlDatasourceMeta();
    }
    if (StringUtils.startsWith(url, "jdbc:oracle:")) {
      return new OracleDatasourceMeta();
    }
    // TODO 还有更多数据库类型要处理
    throw new Exception("Cannot handle this jdbc url ["+url+"].");
  }
  
}
