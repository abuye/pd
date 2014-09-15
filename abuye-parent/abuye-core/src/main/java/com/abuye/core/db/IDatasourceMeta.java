package com.abuye.core.db;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;



public interface IDatasourceMeta {

  public enum ConnType {
    jndi("jndi"), jdbc("jdbc");
    private String name;

    private ConnType(String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }
  }

  public enum DbType {
    h2("h2"), oracle("oracle"), mysql("mysql");
    private String name;

    private DbType(String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }
  }

  /**
   * Java属性类型
   * 
   * @author chandler
   * 
   */
  public enum JavaType {
    STRING("string"), LONG("long"), DOUBLE("double"), DECIMAL("decimal"), DATE("date"), BYTES(
        "bytes");
    private String value;

    private JavaType(String value) {
      this.value = value;
    }

    public String v() {
      return this.value;
    }
  }

  /**
   * Database属性类型
   * 
   * @author chandler
   * 
   */
  public enum DataType {
    STRING("string"), LONG("long"), DECIMAL("decimal"), DATE("date"), CLOB("clob"), BLOB("blob");
    private String value;

    private DataType(String value) {
      this.value = value;
    }

    public String v() {
      return this.value;
    }
  }

  /**
   * hsqldb - select 1 from INFORMATION_SCHEMA.SYSTEM_USERS
   * Oracle - select 1 from dual
   * DB2 - select 1 from sysibm.sysdummy1
   * mysql - select 1
   * microsoft SQL Server - select 1 (tested on SQL-Server 9.0, 10.5 [2008])
   * postgresql - select 1
   * ingres - select 1
   * derby - values 1
   * H2 - select 1
   * Firebird - select 1 from rdb$database
   */
  String getValidationQuery();

  String getName();

  String getShowName();

  String getNotes();

  DbType getDbType();

  ConnType getConnType();

  String getJndi();

  String getJdbcUrl();

  String getDbUser();

  String getDbPass();

  String getDbDriver();

  String getDialectName();

  String getDbCatalog();

  String getDbSchema();

  String getTablePrefix();

  DataSource getDataSource();

  JdbcTemplate getJdbcTemplate();

  JavaType getJavaTypeByDataType(String dataType);

  List<TableMeta> getTableMetas() throws SQLException;


  List<ColumnMeta> getColumnMetas(String tableName) throws Exception;

  public abstract String getLimitString(String sql, boolean hasOffset);

}
