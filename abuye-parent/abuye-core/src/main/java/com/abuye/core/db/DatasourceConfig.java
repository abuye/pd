package com.abuye.core.db;

import java.util.List;

public class DatasourceConfig extends AbstractDatasourceMeta {
  
  private String dbDriver;

  @Override
  public String getValidationQuery() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DbType getDbType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getDbDriver() {
    return this.dbDriver;
  }

  @Override
  public String getDialectName() {
    // TODO Auto-generated method stub
    return dbDriver;
  }

  public void setDbDriver(String dbDriver) {
    this.dbDriver = dbDriver;
  }

  @Override
  public List<TableMeta> getTableMetas() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JavaType getJavaTypeByDataType(String dataType) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<ColumnMeta> getColumnMetas(String tableName) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }
  
}
