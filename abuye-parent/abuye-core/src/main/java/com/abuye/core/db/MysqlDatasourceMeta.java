package com.abuye.core.db;

import java.util.List;


/**
 * 数据源描述
 * 
 * @author chandler
 * 
 */
public class MysqlDatasourceMeta extends AbstractDatasourceMeta {

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getDbType()
   */
  @Override
  public DbType getDbType() {
    return DbType.mysql;
  }

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getValidationQuery()
   */
  @Override
  public String getValidationQuery() {
    return "select 1";
  }

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getDbDriver()
   */
  @Override
  public String getDbDriver() {
    return "com.mysql.jdbc.Driver";
  }

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getDialectName()
   */
  @Override
  public String getDialectName() {
    return "";
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
