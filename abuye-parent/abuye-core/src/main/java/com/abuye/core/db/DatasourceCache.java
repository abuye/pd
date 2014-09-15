package com.abuye.core.db;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.abuye.core.db.IDatasourceMeta.ConnType;
import com.abuye.core.utils.Constants;
import com.abuye.core.utils.U;

public class DatasourceCache {
  private static final Map<String, IDatasourceMeta> cache =
      new ConcurrentHashMap<String, IDatasourceMeta>();

  public static final String DATASOURCE_PREFIX = "datasource";

  public static Map<String, IDatasourceMeta> get() {
    return cache;
  }

  public static IDatasourceMeta getDefault() {
    return cache.get(Constants.DEFAULT);
  }
  
  public static IDatasourceMeta get(String datasourceName) {
    return cache.get(datasourceName);
  }

  public static void put(IDatasourceMeta datasourceMeta) {
    cache.put(datasourceMeta.getName(), datasourceMeta);
  }

  public static void refresh() throws Exception {
    cache.clear();
    loadConfig();
    loadDb();
  }

  public static void loadConfig() throws Exception {
    loadConfig(U.DEFAULT_PROPERTIES_PATH);
  }

  /**
   * e.g.
   * datasource.default.jdbc.url=jdbc:h2:tcp://localhost/test
   * datasource.default.jdbc.username=sa
   * datasource.default.jdbc.password=
   * datasource.default.jdbc.driver=org.h2.Driver # 可选
   * 
   * or
   * 
   * datasource.default.jndi=java:comp/env/test
   * datasource.default.jdbc.driver=org.h2.Driver
   * @throws Exception 
   */
  public static void loadConfig(String propertiesPath) throws Exception {
    Properties ps = U.loadProperties(propertiesPath);
    Enumeration x = ps.propertyNames();
    Map<String, DatasourceConfig> m = new HashMap<String, DatasourceConfig>();
    while (x.hasMoreElements()) {
      String name = (String) x.nextElement();
      String[] ns = name.split("\\.");
      if (ns != null && (ns.length == 3 || ns.length == 4) && DATASOURCE_PREFIX.equals(ns[0])) {
        String dn = ns[1];
        // 初始化
        DatasourceConfig ds = null;
        if (m.containsKey(dn)) {
          ds = m.get(dn);
        } else {
          ds = new DatasourceConfig();
          m.put(dn, ds);
        }
        String value = ps.getProperty(name);
        // jdbc
        if (ns.length == 4 && ConnType.jdbc.getName().equals(ns[2])) {
          ds.setConnType(ConnType.jdbc);
          if ("url".equals(ns[3])) {
            ds.setJdbcUrl(value);
          }
          if ("username".equals(ns[3])) {
            ds.setDbUser(value);
          }
          if ("password".equals(ns[3])) {
            ds.setDbPass(value);
          }
        }
        // jndi
        if (ns.length == 3 && ConnType.jndi.getName().equals(ns[2])) {
          ds.setConnType(ConnType.jndi);
          if ("jndi".equals(ns[2])) {
            ds.setJndi(value);
          }
        }
        // driver
        if (ns.length == 4 && "driver".equals(ns[3])) {
          ds.setDbDriver(value);
        }
      }
    }

    // 初始化
    // 补全driver, dbType, datasourc, jdbctemplate
    // 放入CACHE
    for (String k : m.keySet()) {
      DatasourceConfig d = m.get(k);
      AbstractDatasourceMeta idm = null;
      if (ConnType.jndi.equals(d.getConnType())) {
        idm =
            (AbstractDatasourceMeta) DatasourceMetaFactory.createDatasourceMetaByDriver(d
                .getDbDriver());
      } else {
        // 优先认driver
        if (!StringUtils.isBlank(d.getDbDriver())) {
          idm =
              (AbstractDatasourceMeta) DatasourceMetaFactory.createDatasourceMetaByDriver(d
                  .getDbDriver());
        } else {
          idm =
              (AbstractDatasourceMeta) DatasourceMetaFactory.createDatasourceMetaByUrl(d
                  .getJdbcUrl());
        }
        idm.setJdbcUrl(d.getJdbcUrl());
        idm.setDbUser(d.getDbUser());
        idm.setDbPass(d.getDbPass());
        // TODO 更多配置信息
      }
      
      // TODO 数据连接池
      BasicDataSource ds = new BasicDataSource();
      ds.setDriverClassName(idm.getDbDriver());
      ds.setUsername(idm.getDbUser());
      ds.setPassword(idm.getDbPass());
      ds.setUrl(idm.getJdbcUrl());

      idm.setDataSource(ds);

      // jdbctemplate
      JdbcTemplate jt = new JdbcTemplate(ds);
      idm.setJdbcTemplate(jt);

      cache.put(k, idm);
    }

    // TODO 如果一个数据库都没配，加一个默认H2数据库
  }

  private static void loadDb() {
    // TODO
  }

}
