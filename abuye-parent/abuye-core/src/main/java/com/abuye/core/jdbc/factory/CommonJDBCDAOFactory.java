 package com.abuye.core.jdbc.factory;
 
 import javax.sql.DataSource;

import com.abuye.core.jdbc.commonjdbcdao.CommonJDBCDAO;
import com.abuye.core.jdbc.commonjdbcdao.CommonJDBCDAO_MSSQLServer;
import com.abuye.core.jdbc.commonjdbcdao.CommonJDBCDAO_MySQL;
import com.abuye.core.jdbc.commonjdbcdao.CommonJDBCDAO_Oracle;
import com.abuye.core.jdbc.exception.CommonDAOException;
import com.abuye.core.jdbc.pobject.JDBCConnectParameterPO;
import com.abuye.core.jdbc.util.CommonDAOUtil;
 
 public class CommonJDBCDAOFactory
 {
   public static CommonJDBCDAO newCommonJDBCDAOInstance_MySQL(DataSource someOneDataSource)
     throws CommonDAOException
   {
     if (someOneDataSource == null) {
       throw new CommonDAOException("所提供的数据库连接池的DataSource对象为空，请检查数据库连接池的数据库连接状态是否正常。");
     }
     return new CommonJDBCDAO_MySQL(someOneDataSource);
   }
 
   public static CommonJDBCDAO newCommonJDBCDAOInstance_MySQL(JDBCConnectParameterPO jDBCConnectParameterPO)
     throws CommonDAOException
   {
     if (jDBCConnectParameterPO == null) {
       throw new CommonDAOException("所提供的数据库连接参数为空对象，请提供有效的数据库连接工作参数。");
     }
     return new CommonJDBCDAO_MySQL(jDBCConnectParameterPO);
   }
 
   public static CommonJDBCDAO newCommonJDBCDAOInstance_MySQ(String driverClassName, String dbURL, String loginName, String loginPassWord)
     throws CommonDAOException
   {
     CommonDAOUtil.checkJDBCConnectionParameterValid(driverClassName, dbURL, 
       loginName, loginPassWord);
     return new CommonJDBCDAO_MySQL(driverClassName, dbURL, loginName, loginPassWord);
   }
 
   public static CommonJDBCDAO newCommonJDBCDAOInstance_Oracle(DataSource someOneDataSource)
     throws CommonDAOException
   {
     if (someOneDataSource == null) {
       throw new CommonDAOException("所提供的数据库连接池的DataSource对象为空，请检查数据库连接池的数据库连接状态是否正常。");
     }
     return new CommonJDBCDAO_Oracle(someOneDataSource);
   }
 
   public static CommonJDBCDAO newCommonJDBCDAOInstance_Oracle(JDBCConnectParameterPO jDBCConnectParameterPO)
     throws CommonDAOException
   {
     if (jDBCConnectParameterPO == null) {
       throw new CommonDAOException("所提供的数据库连接参数为空对象，请提供有效的数据库连接工作参数。");
     }
     return new CommonJDBCDAO_Oracle(jDBCConnectParameterPO);
   }
 
   public static CommonJDBCDAO newCommonJDBCDAOInstance_Oracle(String driverClassName, String dbURL, String loginName, String loginPassWord)
     throws CommonDAOException
   {
     CommonDAOUtil.checkJDBCConnectionParameterValid(driverClassName, dbURL, 
       loginName, loginPassWord);
     return new CommonJDBCDAO_Oracle(driverClassName, dbURL, loginName, loginPassWord);
   }
 
   public static CommonJDBCDAO newCommonJDBCDAOInstance_MSSQLServer2000(DataSource someOneDataSource)
     throws CommonDAOException
   {
     if (someOneDataSource == null) {
       throw new CommonDAOException("所提供的数据库连接池的DataSource对象为空，请检查数据库连接池的数据库连接状态是否正常。");
     }
     return new CommonJDBCDAO_MSSQLServer(someOneDataSource, 3);
   }
 
   public static CommonJDBCDAO newCommonJDBCDAOInstance_MSSQLServer2005(DataSource someOneDataSource)
     throws CommonDAOException
   {
     if (someOneDataSource == null) {
       throw new CommonDAOException("所提供的数据库连接池的DataSource对象为空，请检查数据库连接池的数据库连接状态是否正常。");
     }
     return new CommonJDBCDAO_MSSQLServer(someOneDataSource, 4);
   }
 
   public static CommonJDBCDAO newCommonJDBCDAOInstance_MSSQLServer2000(JDBCConnectParameterPO jDBCConnectParameterPO)
     throws CommonDAOException
   {
     if (jDBCConnectParameterPO == null) {
       throw new CommonDAOException("所提供的数据库连接参数为空对象，请提供有效的数据库连接工作参数。");
     }
     return new CommonJDBCDAO_MSSQLServer(jDBCConnectParameterPO, 3);
   }
 
   public static CommonJDBCDAO newCommonJDBCDAOInstance_MSSQLServer2005(JDBCConnectParameterPO jDBCConnectParameterPO)
     throws CommonDAOException
   {
     if (jDBCConnectParameterPO == null) {
       throw new CommonDAOException("所提供的数据库连接参数为空对象，请提供有效的数据库连接工作参数。");
     }
     return new CommonJDBCDAO_MSSQLServer(jDBCConnectParameterPO, 4);
   }
 
   public static CommonJDBCDAO newCommonJDBCDAOInstance_MSSQLServer2000(String driverClassName, String dbURL, String loginName, String loginPassWord)
     throws CommonDAOException
   {
     CommonDAOUtil.checkJDBCConnectionParameterValid(driverClassName, dbURL, 
       loginName, loginPassWord);
     return new CommonJDBCDAO_MSSQLServer(driverClassName, dbURL, loginName, loginPassWord, 3);
   }
 
   public static CommonJDBCDAO newCommonJDBCDAOInstance_MSSQLServer2005(String driverClassName, String dbURL, String loginName, String loginPassWord)
     throws CommonDAOException
   {
     CommonDAOUtil.checkJDBCConnectionParameterValid(driverClassName, dbURL, 
       loginName, loginPassWord);
     return new CommonJDBCDAO_MSSQLServer(driverClassName, dbURL, loginName, loginPassWord, 4);
   }
 }

