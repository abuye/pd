package com.abuye.core.db;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * 数据库存取类
 * 1. crud功能
 * 2. SQL打印，要把参数嵌入SQL中打印出来
 * 3. 条件动态组合
 * 4. 视图解析
 *    EX：select x,y from a, b where a.create_date > '2014-09-09' and b.start_date > '2014-09-09'
 *    可定义为 select x,y from a, b where a.create_date > :date1 and b.start_date > :date1
 * 5. 事务处理
 * @author chandler
 *
 */
public class Db {
  /**
   * 根据主键取唯一记录
   * 
   * @param keyValues 主键值
   * @param tableMeta 表或视图的定义
   * @return
   */
  
  public static Map<String, Object> getByKeys(Map<String, Object> keyValues, TableMeta tableMeta,
      IDatasourceMeta datasourceMeta) throws Exception {
    return null;
  }

  public static Map<String, Object> getByKeys(Long idLongValue, TableMeta tableMeta,
      IDatasourceMeta datasourceMeta) throws Exception {
    return null;
  }

  public static List<Map<String, Object>> query(List<QueryClause> queryClauses, List<Order> orders,
      String tableName, IDatasourceMeta datasourceMeta, Logger log) {
    String sql = "";
    List<Object> sqlParams = new ArrayList<Object>();
    sql += "select * from " + tableName;
    if (queryClauses != null) {
      String s = "";
      try {
        s = QueryBuilder.getSql(queryClauses, sqlParams);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      if (s.length() > 0) {
        sql += " where" + s;
      }
    }
    JdbcTemplate jt = datasourceMeta.getJdbcTemplate();
    Object[] ps = sqlParams.toArray();
    logSql(sql, ps, log);
    return jt.query(sql, sqlParams.toArray(), cm);
  }

  private static void logSql(String sql, Object[] params, Logger log) {
    // TODO 参数嵌入到SQL，再打印
    log.info(sql);
    if(params == null || params.length < 1){
      return;
    }
    log.info("PARAMS AS LIST:");
    for (Object p : params) {
      log.info(p == null ? null : p.toString());
    }
  }

  public static List<Map<String, Object>> query(String sql, IDatasourceMeta datasourceMeta,
      Logger log) {
    return datasourceMeta.getJdbcTemplate().query(sql, cm);
  }

  public static List<Map<String, Object>> query(List<QueryClause> queryItems, List<Order> orders,
      String querySql, Map<String, Object> viewParams, List sqlParams,
      IDatasourceMeta datasourceMeta, Logger log) {
    return null;
  }

  public static List<Map<String, Object>> query(List<QueryClause> queryItems, List<Order> orders,
      TableMeta tableMeta, JdbcTemplate jdbcTemplate) throws Exception {
    return null;
  }

  public static String parseNameParams2sql(String sql, Map<String, Object> viewParams,
      List sqlParams) {
    return null;
  }

  public static List<Map<String, Object>> query(String sql, List<Object> sqlParams,
      JdbcTemplate jdbcTemplate) throws Exception {
    Object[] p = sqlParams == null ? new Object[] {} : sqlParams.toArray();
    return jdbcTemplate.query(sql, p, cm);
  }

  public static Map<String, Object> queryForFirst(String sql, List<Object> sqlParams,
      JdbcTemplate jdbcTemplate) throws Exception {
    return null;
  }

  public static List<Object> queryForSingleList(String sql, List<Object> sqlParams,
      JdbcTemplate jdbcTemplate) throws Exception {
    return null;
  }

  public static Object queryForObject(String sql, List<Object> sqlParams, JdbcTemplate jdbcTemplate)
      throws Exception {
    return null;
  }

  public static int update(String sql, List<Object> sqlParams, JdbcTemplate jdbcTemplate)
      throws Exception {
    return 0;
  }

  public static boolean execute(String sql, List<Object> sqlParams, JdbcTemplate jdbcTemplate)
      throws Exception {
    PreparedStatement ps = null;
    return false;
  }

  public static List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
    List<Map<String, Object>> r = new ArrayList<Map<String, Object>>();
    while (rs.next()) {
      r.add(resultSetRowToMap(rs));
    }
    return r;
  }

  public static Map<String, Object> resultSetRowToMap(ResultSet rs) throws SQLException {
    ResultSetMetaData rsmd = rs.getMetaData();
    int columnCount = rsmd.getColumnCount();
    Map<String, Object> mapOfColValues = new LinkedCaseInsensitiveMap<Object>(columnCount);
    for (int i = 1; i <= columnCount; i++) {
      String key = JdbcUtils.lookupColumnName(rsmd, i);
      Object obj = JdbcUtils.getResultSetValue(rs, i);
      mapOfColValues.put(key, obj);
    }
    return mapOfColValues;
  }

  public static Integer getInt(Object resultSetColumnValue) throws Exception {
    if (resultSetColumnValue == null) {
      return null;
    }
    if (resultSetColumnValue instanceof Number) {
      return ((Number) resultSetColumnValue).intValue();
    }
    throw new Exception("Data type is not Number.");
  }

  //
  // public static void closeResultSetAndConnection(ResultSet rs, Connection c) {
  // try {
  // rs.close();
  // } catch (SQLException e) {
  // e.printStackTrace();
  // }
  // try {
  // c.close();
  // } catch (SQLException e) {
  // e.printStackTrace();
  // }
  // }
  //
  // public static void closeConnection(Connection c) {
  // try {
  // c.close();
  // } catch (SQLException e) {
  // e.printStackTrace();
  // }
  // }
  //
  // public static void closeResultSet(ResultSet rs) {
  // try {
  // rs.close();
  // } catch (SQLException e) {
  // e.printStackTrace();
  // }
  // }

  public static class MyColumnMapRowMapper extends ColumnMapRowMapper implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4698735738567672070L;

    @Override
    protected String getColumnKey(String columnName) {
      return columnName == null ? null : columnName.toLowerCase();
    }
  }

  private static final MyColumnMapRowMapper cm = new MyColumnMapRowMapper();

}
