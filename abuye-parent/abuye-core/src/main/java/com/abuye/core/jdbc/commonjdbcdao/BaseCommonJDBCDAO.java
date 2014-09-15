 package com.abuye.core.jdbc.commonjdbcdao;
 
 import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentMap;

import javax.sql.DataSource;

import com.abuye.core.jdbc.connectionpool.CommonDAOConnectionPool;
import com.abuye.core.jdbc.exception.CommonDAOException;
import com.abuye.core.jdbc.util.CommonDAOUtil;
 
 public class BaseCommonJDBCDAO
 {
   protected int currentDataBaseType = 0;
 
   protected DataSource someOneDataSource = null;
 
   protected CommonDAOConnectionPool commonDAOConnectionPool = null;
 
   protected int ConnectionTpye = 1;
 
   protected static ConcurrentMap<String, List<Map<String, Object>>> querySomePOObjectCache = null;
 
   protected static ConcurrentMap<String, Object> queryByPrimaryKeyIDCache = null;
 
   protected static ConcurrentMap<String, List<Object>> queryCache = null;
 
   protected ResourceBundle resourceBundle = null;
 
   protected boolean executeUpdateSqlStatement(String someOneSQLStatement, Object[] argsInSomeOneSqlStatement)
     throws CommonDAOException
   {
     Connection jdbcConnection = null;
     PreparedStatement onePreparedStatement = null;
 
     int updateResultStatus = 0;
     boolean returnResultStatus = false;
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
 
       try
       {
         onePreparedStatement = jdbcConnection.prepareStatement(someOneSQLStatement);
       } catch (SQLException exception) {
         throw new CommonDAOException(this.resourceBundle.getString("jdbcConnection.updateOnePO.prepareStatementError") + 
           someOneSQLStatement + "。" + 
           CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), 
           exception);
       }
 
       try
       {
         for (int loopIndex = 0; loopIndex < argsInSomeOneSqlStatement.length; loopIndex++)
           onePreparedStatement.setObject(loopIndex + 1, argsInSomeOneSqlStatement[loopIndex]);
       }
       catch (SQLException exception) {
         throw new CommonDAOException(this.resourceBundle.getString("jdbcConnection.updateOnePO.parameterError") + 
           someOneSQLStatement + "。" + 
           CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), 
           exception);
       }
 
       try
       {
         updateResultStatus = onePreparedStatement.executeUpdate();
       } catch (SQLException exception) {
         throw new CommonDAOException(this.resourceBundle.getString("jdbcConnection.updateOnePO.executePrepareStatementError") + 
           someOneSQLStatement + "。" + 
           CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), 
           exception);
       }
 
     }
     finally
     {
       try
       {
         closeJDBCConnection(jdbcConnection);
       } catch (SQLException exception) {
         throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection") + "," + 
           someOneSQLStatement + "。" + this.resourceBundle.getString("common.orignalSQLExceptionInfo") + 
           CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), 
           exception);
       }
     }
     try
     {
       closeJDBCConnection(jdbcConnection);
     } catch (SQLException exception) {
       throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection") + "," + 
         someOneSQLStatement + "。" + this.resourceBundle.getString("common.orignalSQLExceptionInfo") + 
         CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), 
         exception);
     }
 
     if (updateResultStatus == 0) {
       returnResultStatus = false;
     }
     else {
       returnResultStatus = true;
     }
     return returnResultStatus;
   }
 
   protected Connection getJDBCConnection()
     throws SQLException
   {
     Connection newJDBCConnection = null;
     switch (this.ConnectionTpye) {
     case 1:
       newJDBCConnection = this.someOneDataSource.getConnection();
       break;
     case 2:
       newJDBCConnection = this.commonDAOConnectionPool.getConnection();
       break;
     default:
       throw new SQLException("系统中对连接池的使用方式不正确，请正确地选择数据库连接池的创建方式。");
     }
     return newJDBCConnection;
   }
 
   protected void closeJDBCConnection(Connection jdbcConnection)
     throws SQLException
   {
     if (jdbcConnection != null)
       switch (this.ConnectionTpye) {
       case 1:
         jdbcConnection.close();
         break;
       case 2:
         this.commonDAOConnectionPool.reAddToConnectionPool(jdbcConnection);
       }
   }
 }
