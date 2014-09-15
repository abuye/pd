package com.abuye.core.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.abuye.core.db.IDatasourceMeta.JavaType;
import com.abuye.core.db.parser2.MapSqlParameterSource;
import com.abuye.core.db.parser2.NamedParameterUtils;
import com.abuye.core.db.parser2.ParsedSql;


public class ViewMetaCache {
  private static final Map<String, ViewMeta> cache = new ConcurrentHashMap<String, ViewMeta>();

  public static Map<String, ViewMeta> get() {
    return cache;
  }

  public static ViewMeta get(String sql) {
    return get(sql, DatasourceCache.getDefault());
  }

  public static ViewMeta get(String sql, final IDatasourceMeta datasourceMeta) {
    synchronized (cache) {
      ViewMeta vm = cache.get(sql);
      if (vm == null) {
        final String datasourceName = datasourceMeta.getName();
        final String viewName = sql;
        vm = new ViewMeta();
        vm.setDatasourceName(datasourceName);
        vm.setName(viewName);
        vm.setSqlScript(sql);
        ParsedSql r = NamedParameterUtils.parseSqlStatement(sql);
        vm.setParsedSql(r);
        // view params
        List<ViewParamMeta> ps = new ArrayList<ViewParamMeta>();
        for (String pn : r.getParameterNames()) {
          ViewParamMeta vpm = new ViewParamMeta();
          vpm.setDatasourceName(datasourceName);
          vpm.setViewName(viewName);
          vpm.setParamName(pn);
          vpm.setJavaType(JavaType.STRING);
          ps.add(vpm);
        }
        vm.setViewParamMetas(ps);
        // view columns
        JdbcTemplate jt = datasourceMeta.getJdbcTemplate();
        final List<ViewColumnMeta> columns = new ArrayList<ViewColumnMeta>();
        List<Object> sqlParams = new ArrayList<Object>();
        Map<String, Object> m = new HashMap<String, Object>();
        for (ViewParamMeta vpm : vm.getViewParamMetas()) {
          m.put(vpm.getParamName(), null);
        }
        MapSqlParameterSource paramSource = new MapSqlParameterSource(m);
        String sqlToUse = NamedParameterUtils.substituteNamedParameters(r, paramSource);
        Object[] params = NamedParameterUtils.buildValueArray(r, paramSource, null);
        CollectionUtils.addAll(sqlParams, params);
        // 分页设为0， 这里只需要取表头
        sqlToUse = datasourceMeta.getLimitString(sqlToUse, false);
        sqlParams.add(0);
        
        // TODO
        // oracle 不支持的特性
        jt.query(sqlToUse, sqlParams.toArray(), new ResultSetExtractor() {
          @Override
          public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
            ResultSetMetaData rsd = rs.getMetaData();
            for (int i = 1; i <= rsd.getColumnCount(); i++) {
              String columnName = rsd.getColumnName(i);
              ViewColumnMeta vcm = new ViewColumnMeta();
              vcm.setDatasourceName(datasourceName);
              vcm.setViewName(viewName);
              vcm.setColumnName(columnName);
              String columnType = rsd.getColumnTypeName(i);
              vcm.setJavaType(datasourceMeta.getJavaTypeByDataType(columnType));
              vcm.setLength(rsd.getColumnDisplaySize(i));
              vcm.setPrecision(rsd.getPrecision(i));
              vcm.setScale(rsd.getScale(i));
              columns.add(vcm);
            }
            return null;
          }
        });
        vm.setViewColumnMetas(columns);

        // put into cache
        cache.put(viewName, vm);
      }
    }
    return cache.get(sql);
  }


}
