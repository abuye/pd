package com.abuye.core.db;

import com.abuye.core.db.IDatasourceMeta.DataType;
import com.abuye.core.db.IDatasourceMeta.JavaType;



public class ColumnMeta {

  private String datasourceName;
  private String catalog;
  private String schema;
  private String tableName;
  private String columnName;
  
  private JavaType javaType;
  private DataType dataType;
  
  private Integer length;
  private Integer scale;
  private Integer columnIndex;
  /**
   * 是否可以为空值，默认false
   */
  private boolean nullable = true;
  /**
   * 参数 默认值，日期型的，格式以savePattern为准
   */
  private String defaultVal;
  /**
   * 是否主键
   */
  private boolean isPrimeryKey = false;
  /**
   * 不能重复
   */
  private boolean isUnique = false;
  private String notes;
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
  public Integer getLength() {
    return length;
  }
  public void setLength(Integer length) {
    this.length = length;
  }
  public Integer getScale() {
    return scale;
  }
  public void setScale(Integer scale) {
    this.scale = scale;
  }
  public boolean getNullable() {
    return nullable;
  }
  public void setNullable(boolean nullable) {
    this.nullable = nullable;
  }
  public String getDefaultVal() {
    return defaultVal;
  }
  public void setDefaultVal(String defaultVal) {
    this.defaultVal = defaultVal;
  }
  public boolean getIsPrimeryKey() {
    return isPrimeryKey;
  }
  public void setIsPrimeryKey(boolean isPrimeryKey) {
    this.isPrimeryKey = isPrimeryKey;
  }
  public boolean getIsUnique() {
    return isUnique;
  }
  public void setIsUnique(boolean isUnique) {
    this.isUnique = isUnique;
  }
  public String getNotes() {
    return notes;
  }
  public void setNotes(String notes) {
    this.notes = notes;
  }
  public String getTableName() {
    return tableName;
  }
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
  public JavaType getJavaType() {
    return javaType;
  }
  public void setJavaType(JavaType javaType) {
    this.javaType = javaType;
  }
  public DataType getDataType() {
    return dataType;
  }
  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }
  public Integer getColumnIndex() {
    return columnIndex;
  }
  public void setColumnIndex(Integer columnIndex) {
    this.columnIndex = columnIndex;
  }
  public String getColumnName() {
    return columnName;
  }
  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }
  
}
