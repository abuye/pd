package org.abuye.core.db;

import com.abuye.core.db.DatasourceCache;
import com.abuye.core.db.ViewMetaCache;
import com.abuye.core.db.ViewParamMeta;

import junit.framework.TestCase;

public class TestParser extends TestCase {
  public void test1(){
    String sql = "";
    sql += " SELECT";
    sql += "     node.node_name,";
    sql += "     a.user_name,";
    sql += "     a.user_cd,";
    sql += "     a.approve_rank,";
    sql += "     dictdata.dict_name approve_option,";
    sql += "     a.remark,";
    sql += "     a.approve_date";
    sql += " FROM";
    sql += "     res_approve_node a,";
    sql += "     res_node node,";
    sql += "     app_dict_data dictdata,";
    sql += "     app_dict_type dicttype";
    sql += " WHERE";
    sql += "     a.node_cd = node.node_cd";
    sql += " AND a.approve_option_cd = dictdata.dict_cd";
    sql += " AND dictdata.app_dict_type_id = dicttype.app_dict_type_id";
    sql += " AND dicttype.dict_type_cd = 'SP_OPTION'";
    sql += " AND a.display_no = :queryNo";
    sql += " ORDER BY";
    sql += "     a.approve_level";
    ViewMetaCache.get(sql);
  }
}
