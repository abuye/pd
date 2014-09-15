package com.abuye.core.jdbc.commonjdbcdao;

import javax.sql.DataSource;

import com.abuye.core.jdbc.connectionpool.CommonDAOConnectionPool;
import com.abuye.core.jdbc.exception.CommonDAOException;
import com.abuye.core.jdbc.pobject.JDBCConnectParameterPO;

public class CommonJDBCDAO_Oracle extends CommonJDBCDAO {
  private CommonJDBCDAO_Oracle() {}

  public CommonJDBCDAO_Oracle(DataSource someOneDataSource) {
    this.someOneDataSource = someOneDataSource;

    this.ConnectionTpye = 1;

    this.currentDataBaseType = 2;
  }

  public CommonJDBCDAO_Oracle(JDBCConnectParameterPO connectParameterPO) throws CommonDAOException {
    this.commonDAOConnectionPool =
        new CommonDAOConnectionPool(connectParameterPO, 2, this.resourceBundle);

    this.ConnectionTpye = 2;

    this.currentDataBaseType = 2;
  }

  public CommonJDBCDAO_Oracle(String driverClassName, String dbURL, String loginName,
      String loginPassWord) throws CommonDAOException {
    this.commonDAOConnectionPool =
        new CommonDAOConnectionPool(driverClassName, dbURL, loginName, loginPassWord, 2,
            this.resourceBundle);

    this.ConnectionTpye = 2;

    this.currentDataBaseType = 2;
  }

  public String buildDividPageSQLStatement(String targetTableName, String sqlStatementwhere,
      int firstElementIndex, int actualTotalCounterInTagetPage) throws CommonDAOException {
    if (firstElementIndex < 0) {
      firstElementIndex = 0;
    }
    StringBuffer returnSqlStatementwhere = new StringBuffer();
    returnSqlStatementwhere
        .append("select * from (select tempQueryResult.*, rownum as rownumTempColumn  FROM");
    returnSqlStatementwhere.append(" (select * FROM ");
    returnSqlStatementwhere.append(targetTableName);
    returnSqlStatementwhere.append(" ");
    returnSqlStatementwhere.append(sqlStatementwhere);
    returnSqlStatementwhere.append(" ) ");
    returnSqlStatementwhere.append(" tempQueryResult where rownum <= ");
    returnSqlStatementwhere.append(firstElementIndex + actualTotalCounterInTagetPage);
    returnSqlStatementwhere.append(") where rownumTempColumn >= ");
    returnSqlStatementwhere.append(firstElementIndex);
    return returnSqlStatementwhere.toString();
  }
}
