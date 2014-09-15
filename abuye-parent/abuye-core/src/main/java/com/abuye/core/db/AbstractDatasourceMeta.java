package com.abuye.core.db;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;


public abstract class AbstractDatasourceMeta implements IDatasourceMeta {
  /**
   * 唯一标识符
   */
  private String name;
  private String showName;
  private String notes;
  private ConnType connType = ConnType.jdbc;
  private String jndi;
  private String jdbcUrl;
  private String dbUser;
  private String dbPass;
  private String dbCatalog;
  private String dbSchema;
  /**
   * 表前缀
   */
  private String tablePrefix;

  private DataSource dataSource;
  private JdbcTemplate jdbcTemplate;

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getName()
   */
  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getShowName()
   */
  @Override
  public String getShowName() {
    return showName;
  }

  public void setShowName(String showName) {
    this.showName = showName;
  }

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getNotes()
   */
  @Override
  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getConnType()
   */
  @Override
  public ConnType getConnType() {
    return connType;
  }

  public void setConnType(ConnType connType) {
    this.connType = connType;
  }

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getJndi()
   */
  @Override
  public String getJndi() {
    return jndi;
  }

  public void setJndi(String jndi) {
    this.jndi = jndi;
  }

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getJdbcUrl()
   */
  @Override
  public String getJdbcUrl() {
    return jdbcUrl;
  }

  public void setJdbcUrl(String jdbcUrl) {
    this.jdbcUrl = jdbcUrl;
  }

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getDbUser()
   */
  @Override
  public String getDbUser() {
    return dbUser;
  }

  public void setDbUser(String dbUser) {
    this.dbUser = dbUser;
  }

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getDbPass()
   */
  @Override
  public String getDbPass() {
    return dbPass;
  }

  public void setDbPass(String dbPass) {
    this.dbPass = dbPass;
  }

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getDbCatalog()
   */
  @Override
  public String getDbCatalog() {
    return dbCatalog;
  }

  public void setDbCatalog(String dbCatalog) {
    this.dbCatalog = dbCatalog;
  }

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getDbSchema()
   */
  @Override
  public String getDbSchema() {
    return dbSchema;
  }

  public void setDbSchema(String dbSchema) {
    this.dbSchema = dbSchema;
  }

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getTablePrefix()
   */
  @Override
  public String getTablePrefix() {
    return tablePrefix;
  }

  public void setTablePrefix(String tablePrefix) {
    this.tablePrefix = tablePrefix;
  }

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getDataSource()
   */
  @Override
  public DataSource getDataSource() {
    return dataSource;
  }

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getJdbcTemplate()
   */
  @Override
  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public String getLimitString(String sql, boolean hasOffset) {
    return null;
  }


}
