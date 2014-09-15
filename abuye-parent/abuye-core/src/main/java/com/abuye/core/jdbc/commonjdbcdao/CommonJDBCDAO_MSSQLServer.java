 package com.abuye.core.jdbc.commonjdbcdao;
 
 import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.abuye.core.jdbc.connectionpool.CommonDAOConnectionPool;
import com.abuye.core.jdbc.exception.CommonDAOException;
import com.abuye.core.jdbc.pobject.JDBCConnectParameterPO;
import com.abuye.core.jdbc.util.CommonDAOUtil;
 
 public class CommonJDBCDAO_MSSQLServer extends CommonJDBCDAO
 {
   private CommonJDBCDAO_MSSQLServer()
   {
   }
 
   public CommonJDBCDAO_MSSQLServer(DataSource someOneDataSource, int msSQlServerType)
   {
     this.someOneDataSource = someOneDataSource;
 
     this.ConnectionTpye = 1;
 
     this.currentDataBaseType = msSQlServerType;
   }
 
   public CommonJDBCDAO_MSSQLServer(JDBCConnectParameterPO connectParameterPO, int msSQlServerType)
     throws CommonDAOException
   {
     this.commonDAOConnectionPool = new CommonDAOConnectionPool(connectParameterPO, 
       3, this.resourceBundle);
 
     this.ConnectionTpye = 2;
 
     this.currentDataBaseType = msSQlServerType;
   }
 
   public CommonJDBCDAO_MSSQLServer(String driverClassName, String dbURL, String loginName, String loginPassWord, int msSQlServerType)
     throws CommonDAOException
   {
     this.commonDAOConnectionPool = new CommonDAOConnectionPool(driverClassName, dbURL, 
       loginName, loginPassWord, 3, 
       this.resourceBundle);
 
     this.ConnectionTpye = 2;
 
     this.currentDataBaseType = msSQlServerType;
   }
 
   public String buildDividPageSQLStatement(String targetTableName, String sqlStatementwhere, int firstElementIndex, int actualTotalCounterInTagetPage)
     throws CommonDAOException
   {
     Connection jdbcConnection = null;
     String primaryKeyColumnName = null;
     try
     {
       try
       {
         jdbcConnection = getJDBCConnection();
       } catch (SQLException exception) {
         throw new CommonDAOException(this.resourceBundle.getString("jdbcConnection.noJDBCConnectionAndCheck") + 
           CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), 
           exception);
       }
 
       ResultSet primaryKeyResultSet = null;
       try {
         primaryKeyResultSet = jdbcConnection.getMetaData().getPrimaryKeys(null, null, targetTableName);
       } catch (SQLException exception) {
         throw new CommonDAOException(this.resourceBundle.getString("jdbcConnection.databaseMetaData.getMetaDataError") + 
           CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), 
           exception);
       }
 
       try
       {
         do
         {
           primaryKeyColumnName = primaryKeyResultSet.getString("Column_name");
         }
         while (primaryKeyResultSet.next());
       }
       catch (SQLException exception)
       {
         throw new CommonDAOException(this.resourceBundle.getString("jdbcConnection.databaseMetaData.primaryKeyResultSetError") + 
           CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), 
           exception);
       }
 
       boolean hasNotPrimaryKeyColumn = (primaryKeyColumnName == null) || 
         ("".equals(primaryKeyColumnName));
       if (hasNotPrimaryKeyColumn) {
         throw new CommonDAOException(targetTableName + 
           "数据库表中没有提供有主键，该数据库表不满足数据表设计的规范。");
       }
 
     }
     finally
     {
       try
       {
         closeJDBCConnection(jdbcConnection);
       } catch (SQLException exception) {
         throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection") + 
           this.resourceBundle.getString("common.orignalSQLExceptionInfo") + 
           CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), 
           exception);
       }
     }
     try
     {
       closeJDBCConnection(jdbcConnection);
     } catch (SQLException exception) {
       throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection") + 
         this.resourceBundle.getString("common.orignalSQLExceptionInfo") + 
         CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), 
         exception);
     }
 
     StringBuffer returnSqlStatementwhere = new StringBuffer();
     returnSqlStatementwhere.append("select * from ( select top ");
     returnSqlStatementwhere.append(actualTotalCounterInTagetPage);
     returnSqlStatementwhere.append(" * from ( select top ");
     returnSqlStatementwhere.append(firstElementIndex + actualTotalCounterInTagetPage);
     returnSqlStatementwhere.append(" * from ");
     returnSqlStatementwhere.append(targetTableName);
     returnSqlStatementwhere.append(" ");
     returnSqlStatementwhere.append(sqlStatementwhere);
     returnSqlStatementwhere.append(" order by ");
     returnSqlStatementwhere.append(primaryKeyColumnName);
     returnSqlStatementwhere.append(" asc ) as tempTableOne order by ");
     returnSqlStatementwhere.append(primaryKeyColumnName);
     returnSqlStatementwhere.append(" desc ) as tempTableTwo order by ");
     returnSqlStatementwhere.append(primaryKeyColumnName);
     returnSqlStatementwhere.append(" asc ");
 
     return returnSqlStatementwhere.toString();
   }
 }
