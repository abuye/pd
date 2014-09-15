package com.abuye.core.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 数据源描述
 * 
 * @author chandler
 * 
 */
public class H2DatasourceMeta extends AbstractDatasourceMeta {

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getDbType()
   */
  @Override
  public DbType getDbType() {
    return DbType.h2;
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
    return "org.h2.Driver";
  }

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getDialectName()
   */
  @Override
  public String getDialectName() {
    return "";
  }
  
  @Override
  public JavaType getJavaTypeByDataType(String dataType){
    if("VARCHAR".equals(dataType)){
      return JavaType.STRING;
    }
    if("BIGINT".equals(dataType)){
      return JavaType.LONG;
    }
    return null;
  }

  @Override
  public List<TableMeta> getTableMetas() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<ColumnMeta> getColumnMetas(String tableName) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

//  @Override
//  public List<TableMeta> getTableMeta(final String tableName) throws Exception {
//    List<TableMeta> ts = new ArrayList<TableMeta>();
//    List<Map<String, Object>> ms = null;
//    List<Map<String, Object>> ms2 = null;
//    JdbcTemplate jt = getJdbcTemplate();
//    jt.execute(new ConnectionCallback() {
//      @Override
//      public Object doInConnection(Connection c) throws SQLException, DataAccessException {
//        ResultSet rs = null;
////        ResultSet rs2 = null;
//        try {
//          DatabaseMetaData dmd = c.getMetaData();
//          rs = dmd.getTables(c.getCatalog(), "PUBLIC", tableName, new String[] {"TABLE"});
////          ms = Db.resultSetToList(rs);
////          rs2 = dmd.getColumns(c.getCatalog(), "PUBLIC", null, null);
////          // TODO 数据量过大，内存会溢出
////          ms2 = Db.resultSetToList(rs2);
//        } catch (Exception e) {
//          throw e;
//        } finally {
//          Db.closeResultSet(rs);
////          Db.closeResultSet(rs2);
////          Db.closeConnection(c);
//        }
//        return null;
//      }
//    });
//    for (Map<String, Object> m : ms) {
//      TableMeta t = new TableMeta();
//      // 数据示例如下：
//      // TABLE_CAT:ABUYE,TABLE_SCHEM:PUBLIC,TABLE_NAME:ABY_TABLE_PARAM,TABLE_TYPE:TABLE,REMARKS:,TYPE_CAT:null,TYPE_SCHEM:null,TYPE_NAME:null,SELF_REFERENCING_COL_NAME:null,REF_GENERATION:null,SQL:CREATE
//      // CACHED TABL...
//      String tableName = (String) m.get("TABLE_NAME");
//      t.setTableName(tableName);
//      t.setNotes((String) m.get("REMARKS"));
//      
////      List<ColumnMeta> cs = new ArrayList<ColumnMeta>();
////      for(Map<String, Object> m2 : ms2){
////        if(!tableName.equals((String) m2.get("TABLE_NAME"))){
////          continue;
////        }
////        ColumnMeta cm = new ColumnMeta();
////        // 数据示例
////        // TABLE_CAT:ABUYE,TABLE_SCHEM:PUBLIC,TABLE_NAME:ABY_TABLE_PARAM,COLUMN_NAME:NAME,DATA_TYPE:12,TYPE_NAME:VARCHAR,COLUMN_SIZE:255,BUFFER_LENGTH:255,DECIMAL_DIGITS:0,NUM_PREC_RADIX:10,NULLABLE:1,REMARKS:,COLUMN_DEF:null,SQL_DATA_TYPE:12,SQL_DATETIME_SUB:0,CHAR_OCTET_LENGTH:255,ORDINAL_POSITION:3,IS_NULLABLE:YES,SCOPE_CATALOG:null,SCOPE_SCHEMA:null,SCOPE_TABLE:null,SOURCE_DATA_TYPE:null,IS_AUTOINCREMENT:NO,SCOPE_CATLOG:null,
////        cm.setColumnName((String)m2.get("COLUMN_NAME"));
////        JavaType j = getJavaTypeByDataType((String)m2.get("TYPE_NAME"));
////        cm.setJavaType(j.v());
////        cm.setColLength((Integer)m2.get("COLUMN_SIZE"));
////        // TODO 
////        cm.setColScale((int)m2.get("DECIMAL_DIGITS"));
////        cm.setIsRequired(!"YES".equals(m2.get("IS_NULLABLE")));
////        cm.setNotes((String)m2.get("REMARKS"));
////        cm.setTableMeta(t);
////        cs.add(cm);
////      }
////      t.setColumnMetas(cs);
//      
//      ts.add(t);
//    }
//    return ts;
//  }


}
