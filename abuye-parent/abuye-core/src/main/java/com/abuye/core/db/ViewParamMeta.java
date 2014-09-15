package com.abuye.core.db;

import com.abuye.core.db.IDatasourceMeta.JavaType;



public class ViewParamMeta {

  private String datasourceName;
  private String catalog;
  private String schema;
  private String viewName;
  private String paramName;
  
  private JavaType javaType;
  
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
  public String getViewName() {
    return viewName;
  }
  public void setViewName(String viewName) {
    this.viewName = viewName;
  }
  public String getParamName() {
    return paramName;
  }
  public void setParamName(String paramName) {
    this.paramName = paramName;
  }
  public JavaType getJavaType() {
    return javaType;
  }
  public void setJavaType(JavaType javaType) {
    this.javaType = javaType;
  }
  public String getNotes() {
    return notes;
  }
  public void setNotes(String notes) {
    this.notes = notes;
  }
  
}
