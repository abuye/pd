package com.abuye.web.pd;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.abuye.core.db.DatasourceCache;
import com.abuye.core.db.Db;

@Controller
@RequestMapping(value = "/modifyNode")
public class ModifyNodeController {
  private static final Logger log = LoggerFactory.getLogger(ModifyNodeController.class);
  @RequestMapping(method = RequestMethod.GET)
  public String index() {
    return "modify-node";
  }

  @RequestMapping(value = "findNodes")
  public String findNodes(HttpServletRequest request, HttpServletResponse response) {
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
    Db.query(sql, DatasourceCache.getDefault(), log);
    return "aa";
  }
  
  @RequestMapping(value = "testMissView")
  public String testMissView() {
    return "viewMissing";
  }
  
  @RequestMapping(value = "testVoid")
  public void testVoid() {
  }

  @RequestMapping("testError")
  public void testError() throws Exception {
    throw new Exception("aa");
  }
}
