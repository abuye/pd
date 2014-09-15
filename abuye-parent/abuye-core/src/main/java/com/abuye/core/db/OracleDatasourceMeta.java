package com.abuye.core.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;

import com.abuye.core.db.IDatasourceMeta.JavaType;


/**
 * 数据源描述
 * 
 * @author chandler
 * 
 */
public class OracleDatasourceMeta extends AbstractDatasourceMeta {

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getDbType()
   */
  @Override
  public DbType getDbType() {
    return DbType.oracle;
  }

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getValidationQuery()
   */
  @Override
  public String getValidationQuery() {
    return "select 1 from dual";
  }

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getDbDriver()
   */
  @Override
  public String getDbDriver() {
    return "oracle.jdbc.driver.OracleDriver";
  }

  /* (non-Javadoc)
   * @see com.abuye.core.db.IDatasourceMeta#getDialectName()
   */
  @Override
  public String getDialectName() {
    return "";
  }

  @Override
  public List<TableMeta> getTableMetas() throws SQLException {
    JdbcTemplate jt = getJdbcTemplate();
    List<Map<String, Object>> ms = jt.execute(new ConnectionCallback() {
      @Override
      public List<Map<String, Object>> doInConnection(Connection c) throws SQLException,
          DataAccessException {
        ResultSet rs = null;
        try {
          DatabaseMetaData dmd = c.getMetaData();
          rs =
              dmd.getTables(c.getCatalog(), getDbUser().toUpperCase(), null, new String[] {"TABLE"});
          return Db.resultSetToList(rs);
        } catch (Exception e) {
          throw e;
        } finally {
          JdbcUtils.closeResultSet(rs);
        }
      }
    });
    // 数据示例如下：
    // TABLE_CAT:null,TABLE_SCHEM:POWERDESK,TABLE_NAME:TP_BIS_REPORT_STORE,TABLE_TYPE:TABLE,REMARKS:null,
    List<TableMeta> ts = new ArrayList<TableMeta>();
    for (Map<String, Object> m : ms) {
      TableMeta t = new TableMeta();
      t.setCatalog((String) m.get("TABLE_CAT"));
      t.setSchema((String) m.get("TABLE_SCHEM"));
      t.setTableName((String) m.get("TABLE_NAME"));
      t.setNotes((String) m.get("REMARKS"));
      ts.add(t);
    }
    return ts;
  }

  @Override
  public List<ColumnMeta> getColumnMetas(final String tableName) throws Exception {
    JdbcTemplate jt = getJdbcTemplate();
    List<Map<String, Object>> ms = jt.execute(new ConnectionCallback() {
      @Override
      public List<Map<String, Object>> doInConnection(Connection c) throws SQLException,
          DataAccessException {
        ResultSet rs = null;
        try {
          DatabaseMetaData dmd = c.getMetaData();
          rs = dmd.getColumns(c.getCatalog(), getDbUser().toUpperCase(), tableName, null);
          return Db.resultSetToList(rs);
        } catch (Exception e) {
          throw e;
        } finally {
          JdbcUtils.closeResultSet(rs);
        }
      }
    });
    // 数据示例如下：
    // TABLE_CAT:null,TABLE_SCHEM:POWERDESK,TABLE_NAME:ADDR_BOOK_CHANGE,COLUMN_NAME:APPLICANT,DATA_TYPE:12,TYPE_NAME:VARCHAR2,COLUMN_SIZE:510,BUFFER_LENGTH:0,DECIMAL_DIGITS:null,NUM_PREC_RADIX:10,NULLABLE:0,REMARKS:null,COLUMN_DEF:null,SQL_DATA_TYPE:0,SQL_DATETIME_SUB:0,CHAR_OCTET_LENGTH:510,ORDINAL_POSITION:1,IS_NULLABLE:NO,
    List<ColumnMeta> ts = new ArrayList<ColumnMeta>();
    for (Map<String, Object> m : ms) {
      ColumnMeta cm = new ColumnMeta();
      cm.setDatasourceName(getName());
      cm.setCatalog((String) m.get("TABLE_CAT"));
      cm.setSchema((String) m.get("TABLE_SCHEM"));
      cm.setTableName((String) m.get("TABLE_NAME"));
      cm.setColumnName((String) m.get("COLUMN_NAME"));
      String dataType = (String) m.get("TYPE_NAME");
      cm.setJavaType(getJavaTypeByDataType(dataType));
      cm.setLength(Db.getInt(m.get("COLUMN_SIZE")));
      cm.setNullable("YES".equals((String) m.get("IS_NULLABLE")));
      cm.setColumnIndex(Db.getInt(m.get("ORDINAL_POSITION")));
      cm.setDefaultVal((String) m.get("COLUMN_DEF"));
      cm.setScale(Db.getInt(m.get("DECIMAL_DIGITS")));
      cm.setNotes((String) m.get("REMARKS"));
      ts.add(cm);
    }
    return ts;
  }

  @Override
  public JavaType getJavaTypeByDataType(String dataType) {
    if ("VARCHAR2".equals(dataType)) {
      return JavaType.STRING;
    }
    if ("LONG".equals(dataType)) {
      return JavaType.LONG;
    }
    // TODO
    return null;
  }

  @Override
  public String getLimitString(String sql, boolean hasOffset) {
    sql = sql.trim();

    StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
    if (hasOffset) {
      pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
    } else {
      pagingSelect.append("select * from ( ");
    }
    pagingSelect.append(sql);
    if (hasOffset) {
      pagingSelect.append(" ) row_ where rownum <= ?) where rownum_ > ?");
    } else {
      pagingSelect.append(" ) where rownum <= ?");
    }

    return pagingSelect.toString();
  }

}
