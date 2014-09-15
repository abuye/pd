 package com.abuye.core.jdbc.commonjdbcdao;
 
 import javax.sql.DataSource;

import com.abuye.core.jdbc.connectionpool.CommonDAOConnectionPool;
import com.abuye.core.jdbc.exception.CommonDAOException;
import com.abuye.core.jdbc.pobject.JDBCConnectParameterPO;
 
 public class CommonJDBCDAO_MySQL extends CommonJDBCDAO
 {
   private CommonJDBCDAO_MySQL()
   {
   }
 
   public CommonJDBCDAO_MySQL(DataSource someOneDataSource)
   {
     this.someOneDataSource = someOneDataSource;
 
     this.ConnectionTpye = 1;
 
     this.currentDataBaseType = 1;
   }
 
   public CommonJDBCDAO_MySQL(JDBCConnectParameterPO connectParameterPO)
     throws CommonDAOException
   {
     this.commonDAOConnectionPool = new CommonDAOConnectionPool(connectParameterPO, 
       1, this.resourceBundle);
 
     this.ConnectionTpye = 2;
 
     this.currentDataBaseType = 1;
   }
 
   public CommonJDBCDAO_MySQL(String driverClassName, String dbURL, String loginName, String loginPassWord)
     throws CommonDAOException
   {
     this.commonDAOConnectionPool = new CommonDAOConnectionPool(driverClassName, dbURL, 
       loginName, loginPassWord, 1, 
       this.resourceBundle);
 
     this.ConnectionTpye = 2;
 
     this.currentDataBaseType = 1;
   }
 
   public String buildDividPageSQLStatement(String targetTableName, String sqlStatementwhere, int firstElementIndex, int actualTotalCounterInTagetPage)
     throws CommonDAOException
   {
     if (firstElementIndex <= 0) {
       firstElementIndex = 1;
     }
     StringBuffer returnSqlStatementwhere = new StringBuffer();
     returnSqlStatementwhere.append("select * from ");
     returnSqlStatementwhere.append(targetTableName);
     returnSqlStatementwhere.append(" ");
     returnSqlStatementwhere.append(sqlStatementwhere);
     returnSqlStatementwhere.append(" limit ");
 
     returnSqlStatementwhere.append(firstElementIndex > 0 ? firstElementIndex - 1 : 0);
     returnSqlStatementwhere.append(",");
     returnSqlStatementwhere.append(actualTotalCounterInTagetPage);
     return returnSqlStatementwhere.toString();
   }
 }
