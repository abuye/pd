package com.abuye.core.db;

import java.util.List;

import com.abuye.core.db.parser2.ParsedSql;


public class ViewMeta {
  /**
   * 唯一命名
   */
  private String name;
  private String datasourceName;
  private String sqlScript;
  private ParsedSql parsedSql;
  private String notes;
  private List<ViewColumnMeta> viewColumnMetas;
  private List<ViewParamMeta> viewParamMetas;
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDatasourceName() {
    return datasourceName;
  }
  public void setDatasourceName(String datasourceName) {
    this.datasourceName = datasourceName;
  }
  public String getSqlScript() {
    return sqlScript;
  }
  public void setSqlScript(String sqlScript) {
    this.sqlScript = sqlScript;
  }
  public ParsedSql getParsedSql() {
    return parsedSql;
  }
  public void setParsedSql(ParsedSql parsedSql) {
    this.parsedSql = parsedSql;
  }
  public String getNotes() {
    return notes;
  }
  public void setNotes(String notes) {
    this.notes = notes;
  }
  public List<ViewColumnMeta> getViewColumnMetas() {
    return viewColumnMetas;
  }
  public void setViewColumnMetas(List<ViewColumnMeta> viewColumnMetas) {
    this.viewColumnMetas = viewColumnMetas;
  }
  public List<ViewParamMeta> getViewParamMetas() {
    return viewParamMetas;
  }
  public void setViewParamMetas(List<ViewParamMeta> viewParamMetas) {
    this.viewParamMetas = viewParamMetas;
  }
  
}
