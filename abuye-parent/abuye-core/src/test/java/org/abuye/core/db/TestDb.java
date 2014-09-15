package org.abuye.core.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.abuye.core.db.ColumnMeta;
import com.abuye.core.db.DatasourceCache;
import com.abuye.core.db.Db;
import com.abuye.core.db.IDatasourceMeta;
import com.abuye.core.db.QueryBuilder;
import com.abuye.core.db.QueryClause;
import com.abuye.core.db.QueryClause.Op;
import com.abuye.core.db.TableMeta;
import com.abuye.core.db.ViewColumnMeta;
import com.abuye.core.db.ViewMeta;
import com.abuye.core.db.ViewMetaCache;
import com.abuye.core.utils.Constants;

public class TestDb extends TestCase {
  private static final Logger logger = LoggerFactory.getLogger(TestDb.class);
  /**
   * 测试数据库连接
   * 
   * @throws Exception
   */
  public void _testConnect() throws Exception {
    DatasourceCache.loadConfig("abuye-test.properties");
    IDatasourceMeta dm = DatasourceCache.get(Constants.DEFAULT);
    JdbcTemplate jt = dm.getJdbcTemplate();
    int i = jt.queryForInt(dm.getValidationQuery());
    assertEquals(1, i);
  }

  public void testQuerySql() throws Exception {
    DatasourceCache.loadConfig("abuye-test.properties");
    String sql = "";
    sql += " SELECT *";
    sql += " FROM";
    sql += "     res_approve_info a";
    sql += " WHERE";
    sql += "  a.display_no = :queryNo";
    ViewMeta vm = ViewMetaCache.get(sql);
    for(ViewColumnMeta vcm : vm.getViewColumnMetas()){
      System.out.println(vcm.getColumnName());
    }
  }
  
  public void _testQueryTable() throws Exception {
    DatasourceCache.loadConfig("abuye-test.properties");
    IDatasourceMeta dm = DatasourceCache.get(Constants.DEFAULT);
    List<QueryClause> qis = QueryBuilder.start().and("RES_APPROVE_INFO_ID", Op.EQ, "ff808081405040ce01405055a6140012").end();
    List<Map<String, Object>> l = Db.query(qis, null, "RES_APPROVE_INFO", dm, logger);
    printListMap(l);
  }
  
  public void printListMap(List<Map<String, Object>> l ){
    System.out.println("===" + l.size());
    for(Map<String, Object> m : l){
      String x = "";
      for(String k : m.keySet()){
        x += k + ":" + m.get(k) + ", ";
      }
      System.out.println(x);
    }
  }
  
  public void _testGetColumnMetas() throws Exception {
    DatasourceCache.loadConfig("abuye-test.properties");
    IDatasourceMeta dm = DatasourceCache.get(Constants.DEFAULT);
    JdbcTemplate jt = dm.getJdbcTemplate();
    List<ColumnMeta> ts = dm.getColumnMetas("RES_APPROVE_INFO");
    for(ColumnMeta t : ts){
      System.out.println(t.getColumnName());
    }
  }
  
  public void _testGetTableMetas() throws Exception {
    DatasourceCache.loadConfig("abuye-test.properties");
    IDatasourceMeta dm = DatasourceCache.get(Constants.DEFAULT);
    JdbcTemplate jt = dm.getJdbcTemplate();
    List<TableMeta> ts = dm.getTableMetas();
    for(TableMeta t : ts){
      System.out.println(t.getTableName());
    }
  }
  
  public void _testSqlMeta() throws Exception {
    DatasourceCache.loadConfig("abuye-test.properties");
    IDatasourceMeta dm = DatasourceCache.get(Constants.DEFAULT);
    JdbcTemplate jt = dm.getJdbcTemplate();
    jt.query("select * from (select * from res_approve_info) where rownum < 1", new ResultSetExtractor() {

      @Override
      public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
        ResultSetMetaData rsd = rs.getMetaData();
        for(int i = 1; i <= rsd.getColumnCount(); i++){
          System.out.println(rsd.getColumnName(i));
        }
        return null;
      }
    });
  }

  public void _testLoadTableMeta() throws Exception {
    DatasourceCache.loadConfig("abuye-test.properties");
    IDatasourceMeta dm = DatasourceCache.get(Constants.DEFAULT);
    DataSource ds = dm.getDataSource();
    Connection c = ds.getConnection();
    DatabaseMetaData dmd = c.getMetaData();
    // dmd.getCatalogs();
    // dmd.getSchemas();
    // dmd.getTables(catalog, schemaPattern, tableNamePattern, types);
    // ResultSet rs = dmd.getTables(c.getCatalog(), dm.getDbUser().toUpperCase(), null, new
    // String[]{"TABLE"});
    ResultSet rs = dmd.getTables(c.getCatalog(), null, null, new String[] {"TABLE"});
    while (rs.next()) {
      Map<String, Object> m = Db.resultSetRowToMap(rs);
      String x = "";
      for (String k : m.keySet()) {
        x += k + ":" + m.get(k) + ",";
      }
      System.out.println(x);
    }
  }

  public void _testLoadColumnMeta() throws Exception {
    DatasourceCache.loadConfig("abuye-test.properties");
    IDatasourceMeta dm = DatasourceCache.get(Constants.DEFAULT);
    DataSource ds = dm.getDataSource();
    Connection c = ds.getConnection();
    DatabaseMetaData dmd = c.getMetaData();
    // 数据示例
    // TABLE_CAT:ABUYE,TABLE_SCHEM:PUBLIC,TABLE_NAME:ABY_TABLE_PARAM,COLUMN_NAME:NAME,DATA_TYPE:12,TYPE_NAME:VARCHAR,COLUMN_SIZE:255,BUFFER_LENGTH:255,DECIMAL_DIGITS:0,NUM_PREC_RADIX:10,NULLABLE:1,REMARKS:,COLUMN_DEF:null,SQL_DATA_TYPE:12,SQL_DATETIME_SUB:0,CHAR_OCTET_LENGTH:255,ORDINAL_POSITION:3,IS_NULLABLE:YES,SCOPE_CATALOG:null,SCOPE_SCHEMA:null,SCOPE_TABLE:null,SOURCE_DATA_TYPE:null,IS_AUTOINCREMENT:NO,SCOPE_CATLOG:null,
    // ResultSet rs = dmd.getColumns(c.getCatalog(), null, "ABY_TABLE_PARAM", null);
    // TABLE_CAT:null,TABLE_SCHEM:POWERDESK,TABLE_NAME:ADDR_BOOK_CHANGE,COLUMN_NAME:APPLICANT,DATA_TYPE:12,TYPE_NAME:VARCHAR2,COLUMN_SIZE:510,BUFFER_LENGTH:0,DECIMAL_DIGITS:null,NUM_PREC_RADIX:10,NULLABLE:0,REMARKS:null,COLUMN_DEF:null,SQL_DATA_TYPE:0,SQL_DATETIME_SUB:0,CHAR_OCTET_LENGTH:510,ORDINAL_POSITION:1,IS_NULLABLE:NO,
    ResultSet rs = dmd.getColumns(c.getCatalog(), dm.getDbUser().toUpperCase(), null, null);
    while (rs.next()) {
      Map<String, Object> m = Db.resultSetRowToMap(rs);
      String x = "";
      for (String k : m.keySet()) {
        x += k + ":" + m.get(k) + ",";
      }
      System.out.println(x);
    }
  }

  public void _testLoadReferMeta() throws Exception {
    DatasourceCache.loadConfig("abuye-test.properties");
    IDatasourceMeta dm = DatasourceCache.get(Constants.DEFAULT);
    DataSource ds = dm.getDataSource();
    Connection c = ds.getConnection();
    DatabaseMetaData dmd = c.getMetaData();
    // 数据示例
    // PKTABLE_CAT:null,PKTABLE_SCHEM:POWERDESK,PKTABLE_NAME:ADDR_BOOK_GROUP,PKCOLUMN_NAME:ADDR_BOOK_GROUP_ID,FKTABLE_CAT:null,FKTABLE_SCHEM:POWERDESK,FKTABLE_NAME:ADDR_BOOK_MEMBER,FKCOLUMN_NAME:ADDR_BOOK_GROUP_ID,KEY_SEQ:1,UPDATE_RULE:null,DELETE_RULE:1,FK_NAME:REFADDR_BOOK_GROUP865,PK_NAME:PK_ADDR_BOOK_GROUP,DEFERRABILITY:7,
    // ResultSet rs = dmd.getCrossReference(c.getCatalog(), null, null, c.getCatalog(), null, null);
    ResultSet rs =
        dmd.getCrossReference(c.getCatalog(), dm.getDbUser().toUpperCase(), null, c.getCatalog(),
            dm.getDbUser().toUpperCase(), null);
    while (rs.next()) {
      Map<String, Object> m = Db.resultSetRowToMap(rs);
      String x = "";
      for (String k : m.keySet()) {
        x += k + ":" + m.get(k) + ",";
      }
      System.out.println(x);
    }
  }

  public void _testLoadUniqueMeta() throws Exception {
    DatasourceCache.loadConfig("abuye-test.properties");
    IDatasourceMeta dm = DatasourceCache.get(Constants.DEFAULT);
    DataSource ds = dm.getDataSource();
    Connection c = ds.getConnection();
    DatabaseMetaData dmd = c.getMetaData();
    // 数据示例
    // PKTABLE_CAT:null,PKTABLE_SCHEM:POWERDESK,PKTABLE_NAME:ADDR_BOOK_GROUP,PKCOLUMN_NAME:ADDR_BOOK_GROUP_ID,FKTABLE_CAT:null,FKTABLE_SCHEM:POWERDESK,FKTABLE_NAME:ADDR_BOOK_MEMBER,FKCOLUMN_NAME:ADDR_BOOK_GROUP_ID,KEY_SEQ:1,UPDATE_RULE:null,DELETE_RULE:1,FK_NAME:REFADDR_BOOK_GROUP865,PK_NAME:PK_ADDR_BOOK_GROUP,DEFERRABILITY:7,
    // ResultSet rs = dmd.getCrossReference(c.getCatalog(), null, null, c.getCatalog(), null, null);
    ResultSet rs =
        dmd.getCrossReference(c.getCatalog(), dm.getDbUser().toUpperCase(), null, c.getCatalog(),
            dm.getDbUser().toUpperCase(), null);
    while (rs.next()) {
      Map<String, Object> m = Db.resultSetRowToMap(rs);
      String x = "";
      for (String k : m.keySet()) {
        x += k + ":" + m.get(k) + ",";
      }
      System.out.println(x);
    }
  }
}
