package com.abuye.core.db;

import java.util.List;

public class TableMeta {
  /**
   * 唯一命名
   */
  private String name;
  private String datasourceName;
  private String catalog;
  private String schema;
  private String tableName;
  private boolean isView = false;
  private String sqlScript;
  private String notes;
  private List<ColumnMeta> columnMetas;
  
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getTableName() {
    return tableName;
  }
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
  public boolean getIsView() {
    return isView;
  }
  public void setIsView(boolean isView) {
    this.isView = isView;
  }
  public String getSqlScript() {
    return sqlScript;
  }
  public void setSqlScript(String sqlScript) {
    this.sqlScript = sqlScript;
  }
  public String getNotes() {
    return notes;
  }
  public void setNotes(String notes) {
    this.notes = notes;
  }
  public List<ColumnMeta> getColumnMetas() {
    return columnMetas;
  }
  public void setColumnMetas(List<ColumnMeta> columnMetas) {
    this.columnMetas = columnMetas;
  }
  public String getDatasourceName() {
    return datasourceName;
  }
  public void setDatasourceName(String datasourceName) {
    this.datasourceName = datasourceName;
  }
  public String getCatalog() {
    return catalog;
  }
  public void setCatalog(String catalog) {
    this.catalog = catalog;
  }
  public String getSchema() {
    return schema;
  }
  public void setSchema(String schema) {
    this.schema = schema;
  }
  
}
