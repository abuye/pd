package org.abuye.core.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.abuye.core.jdbc.commonjdbcdao.CommonJDBCDAO;
import com.abuye.core.jdbc.exception.CommonDAOException;
import com.abuye.core.jdbc.factory.CommonJDBCDAOFactory;
import com.abuye.core.jdbc.pobject.JDBCConnectParameterPO;

public class TestCommonDao extends TestCase {
  CommonJDBCDAO c = null;

  public void setUp() throws Exception {
    super.setUp();
    JDBCConnectParameterPO jp = new JDBCConnectParameterPO();
    jp.setDbURL("jdbc:oracle:thin:@192.168.180.105:1521:POWERDESkex");
    jp.setDriverClassName("oracle.jdbc.driver.OracleDriver");
    jp.setLoginName("powerdesk");
    jp.setLoginPassWord("powerlong3");
    c = CommonJDBCDAOFactory.newCommonJDBCDAOInstance_Oracle(jp);
  }

  public void test1() throws CommonDAOException {
    String querySqlStatement =
        "select * from res_approve_content where approve_content is not null and rownum < 2";
    List args = new ArrayList();
    List<Map<String, Object>> r =
        c.querySomePOObjectByTargetSQLStatement(querySqlStatement, args.toArray());
    for (Object o : r.get(0).values()) {
      if (o == null)
        System.out.println("(null)");
      else
        System.out.println(o.getClass());
    }
    System.out.println(r.size());
  }
}
