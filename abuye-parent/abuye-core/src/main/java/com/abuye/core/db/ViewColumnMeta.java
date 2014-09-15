package com.abuye.core.db;

import com.abuye.core.db.IDatasourceMeta.JavaType;



public class ViewColumnMeta {

  private String datasourceName;
  private String catalog;
  private String schema;
  private String viewName;
  private String columnName;
  
  private JavaType javaType;
  
  private Integer length;
  private Integer precision;
  private Integer scale;
  private Integer columnIndex;
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
  public String getViewName() {
    return viewName;
  }
  public void setViewName(String viewName) {
    this.viewName = viewName;
  }
  public String getColumnName() {
    return columnName;
  }
  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }
  public JavaType getJavaType() {
    return javaType;
  }
  public void setJavaType(JavaType javaType) {
    this.javaType = javaType;
  }
  public Integer getColumnIndex() {
    return columnIndex;
  }
  public void setColumnIndex(Integer columnIndex) {
    this.columnIndex = columnIndex;
  }
  public String getNotes() {
    return notes;
  }
  public void setNotes(String notes) {
    this.notes = notes;
  }
  public Integer getPrecision() {
    return precision;
  }
  public void setPrecision(Integer precision) {
    this.precision = precision;
  }
  
}
