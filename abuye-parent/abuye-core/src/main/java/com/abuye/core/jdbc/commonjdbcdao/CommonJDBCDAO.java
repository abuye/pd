package com.abuye.core.jdbc.commonjdbcdao;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.LRUMap;

import com.abuye.core.jdbc.exception.CommonDAOException;
import com.abuye.core.jdbc.util.CommonDAOUtil;


public abstract class CommonJDBCDAO extends BaseCommonJDBCDAO {
  protected CommonJDBCDAO() {
    Locale currentLocale = Locale.getDefault();
    this.resourceBundle =
        ResourceBundle.getBundle("com.abuye.core.jdbc.resource.ExceptionInfo", currentLocale);
  }

  public boolean callProcedure_NoReturnValue(String calledProcedureSqlstatement,
      Object[] inputArgumentsArray) throws CommonDAOException {
    Connection jdbcConnection = null;
    CallableStatement oneCallableStatement = null;
    try {
      try {
        jdbcConnection = getJDBCConnection();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnection")
                + calledProcedureSqlstatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      try {
        oneCallableStatement = jdbcConnection.prepareCall(calledProcedureSqlstatement, 1005, 1008);
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noCallableStatement")
                + calledProcedureSqlstatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      if ((inputArgumentsArray != null) && (inputArgumentsArray.length > 0)) {
        for (int loopIndex = 0; loopIndex < inputArgumentsArray.length; loopIndex++) {
          try {
            oneCallableStatement.setObject(loopIndex + 1, inputArgumentsArray[loopIndex]);
          } catch (SQLException exception) {
            throw new CommonDAOException(
                this.resourceBundle.getString("jdbcConnection.callableStatement.parameterError")
                    + calledProcedureSqlstatement + "。"
                    + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle),
                exception);
          }
        }

      }

      try {
        oneCallableStatement.execute();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.callableStatement.executeError")
                + calledProcedureSqlstatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + "," + calledProcedureSqlstatement + "，"
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
    }
    try {
      closeJDBCConnection(jdbcConnection);
    } catch (SQLException exception) {
      throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
          + "," + calledProcedureSqlstatement + "，"
          + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
          + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
    }

    return true;
  }

  public Object[] callProcedure_HasReturnSomeValue(String calledProcedureSqlstatement,
      Object[] inputArgumentsArray, Integer[] outputArgumentsDataTypeArray)
      throws CommonDAOException {
    Connection jdbcConnection = null;
    CallableStatement oneCallableStatement = null;
    Object[] returnOutputResultArray = (Object[]) null;
    try {
      try {
        jdbcConnection = getJDBCConnection();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnection")
                + calledProcedureSqlstatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      try {
        oneCallableStatement = jdbcConnection.prepareCall(calledProcedureSqlstatement, 1005, 1008);
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noCallableStatement")
                + calledProcedureSqlstatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      if ((inputArgumentsArray != null) && (inputArgumentsArray.length > 0)) {
        for (int loopIndex = 0; loopIndex < inputArgumentsArray.length; loopIndex++) {
          try {
            oneCallableStatement.setObject(loopIndex + 1, inputArgumentsArray[loopIndex]);
          } catch (SQLException exception) {
            throw new CommonDAOException(
                this.resourceBundle.getString("jdbcConnection.callableStatement.parameterError")
                    + calledProcedureSqlstatement + "。"
                    + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle),
                exception);
          }

        }

      }

      if ((outputArgumentsDataTypeArray != null) && (outputArgumentsDataTypeArray.length > 0)) {
        for (int loopIndex = 0; loopIndex < outputArgumentsDataTypeArray.length; loopIndex++) {
          try {
            oneCallableStatement.registerOutParameter(inputArgumentsArray.length + loopIndex + 1,
                outputArgumentsDataTypeArray[loopIndex].intValue());
          } catch (SQLException exception) {
            throw new CommonDAOException(
                this.resourceBundle.getString("jdbcConnection.callableStatement.outputParameterError")
                    + calledProcedureSqlstatement
                    + "。"
                    + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle),
                exception);
          }
        }

      }

      try {
        oneCallableStatement.execute();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.callableStatement.executeError")
                + calledProcedureSqlstatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      if ((outputArgumentsDataTypeArray != null) && (outputArgumentsDataTypeArray.length > 0)) {
        returnOutputResultArray = new Object[outputArgumentsDataTypeArray.length];
        for (int loopIndex = 0; loopIndex < inputArgumentsArray.length; loopIndex++) {
          try {
            returnOutputResultArray[loopIndex] =
                oneCallableStatement.getObject(inputArgumentsArray.length + loopIndex + 1);
          } catch (SQLException exception) {
            throw new CommonDAOException(
                this.resourceBundle.getString("jdbcConnection.callableStatement.getOutputParameterError")
                    + calledProcedureSqlstatement
                    + "。"
                    + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle),
                exception);
          }
        }
      }

    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + "," + calledProcedureSqlstatement + "，"
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
    }
    return returnOutputResultArray;
  }

  public List<List> callProcedure_returnSomeResultSet_MySQL(String calledProcedureSqlstatement,
      Object[] inputArgumentsArray, Class[] poClassName_class) throws CommonDAOException {
    Connection jdbcConnection = null;
    CallableStatement oneCallableStatement = null;
    ResultSet oneResultSet = null;
    List returnAllResultSetPOObjectList = null;
    try {
      try {
        jdbcConnection = getJDBCConnection();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnection")
                + calledProcedureSqlstatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      try {
        oneCallableStatement = jdbcConnection.prepareCall(calledProcedureSqlstatement);
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noCallableStatement")
                + calledProcedureSqlstatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      if ((inputArgumentsArray != null) && (inputArgumentsArray.length > 0)) {
        for (int loopIndex = 0; loopIndex < inputArgumentsArray.length; loopIndex++) {
          try {
            oneCallableStatement.setObject(loopIndex + 1, inputArgumentsArray[loopIndex]);
          } catch (SQLException exception) {
            throw new CommonDAOException(
                this.resourceBundle.getString("jdbcConnection.callableStatement.parameterError")
                    + calledProcedureSqlstatement + "。"
                    + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle),
                exception);
          }

        }

      }

      boolean isReturnResultSet = false;
      try {
        isReturnResultSet = oneCallableStatement.execute();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.callableStatement.executeError")
                + calledProcedureSqlstatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      returnAllResultSetPOObjectList = new ArrayList();

      int currentResultSetIndex = 0;
      while (isReturnResultSet) {
        try {
          oneResultSet = oneCallableStatement.getResultSet();
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.callableStatement.executeError")
                  + calledProcedureSqlstatement + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        returnAllResultSetPOObjectList.add(CommonDAOUtil.buildReturnResultList(jdbcConnection,
            oneResultSet, poClassName_class[currentResultSetIndex], this.currentDataBaseType));
        try {
          isReturnResultSet = oneCallableStatement.getMoreResults();
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.callableStatement.resultSetError")
                  + calledProcedureSqlstatement + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }
        currentResultSetIndex++;
      }

    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + "," + calledProcedureSqlstatement + "，"
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
    }
    return returnAllResultSetPOObjectList;
  }

  public List<List> callProcedure_returnSomeResultSet_MSSqlServer(
      String calledProcedureSqlstatement, Object[] inputArgumentsArray, Class[] poClassName_class)
      throws CommonDAOException {
    return callProcedure_returnSomeResultSet_MySQL(calledProcedureSqlstatement,
        inputArgumentsArray, poClassName_class);
  }

  public List<List> callProcedure_returnSomeResultSet_Oracle(String calledProcedureSqlstatement,
      Object[] inputArgumentsArray, Integer[] outputArgumentsDataTypeArray,
      Class[] poClassName_class) throws CommonDAOException {
    Connection jdbcConnection = null;
    CallableStatement oneCallableStatement = null;
    ResultSet oneResultSet = null;
    List returnAllResultSetPOObjectList = null;
    try {
      try {
        jdbcConnection = getJDBCConnection();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnection")
                + calledProcedureSqlstatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      try {
        oneCallableStatement = jdbcConnection.prepareCall(calledProcedureSqlstatement, 1005, 1008);
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noCallableStatement")
                + calledProcedureSqlstatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      if ((inputArgumentsArray != null) && (inputArgumentsArray.length > 0)) {
        for (int loopIndex = 0; loopIndex < inputArgumentsArray.length; loopIndex++) {
          try {
            oneCallableStatement.setObject(loopIndex + 1, inputArgumentsArray[loopIndex]);
          } catch (SQLException exception) {
            throw new CommonDAOException(
                this.resourceBundle.getString("jdbcConnection.callableStatement.parameterError")
                    + calledProcedureSqlstatement + "。"
                    + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle),
                exception);
          }

        }

      }

      if ((outputArgumentsDataTypeArray != null) && (outputArgumentsDataTypeArray.length > 0)) {
        for (int loopIndex = 0; loopIndex < outputArgumentsDataTypeArray.length; loopIndex++) {
          try {
            oneCallableStatement.registerOutParameter(inputArgumentsArray.length + loopIndex + 1,
                outputArgumentsDataTypeArray[loopIndex].intValue());
          } catch (SQLException exception) {
            throw new CommonDAOException(
                this.resourceBundle.getString("jdbcConnection.callableStatement.outputParameterError")
                    + calledProcedureSqlstatement
                    + "。"
                    + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle),
                exception);
          }

        }

      }

      try {
        oneCallableStatement.execute();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.callableStatement.executeError")
                + calledProcedureSqlstatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      returnAllResultSetPOObjectList = new ArrayList();

      for (int currentResultSetIndex = 0; currentResultSetIndex < outputArgumentsDataTypeArray.length; currentResultSetIndex++) {
        try {
          oneResultSet =
              (ResultSet) oneCallableStatement.getObject(inputArgumentsArray.length
                  + currentResultSetIndex + 1);
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.callableStatement.resultSetError")
                  + calledProcedureSqlstatement + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        if (oneResultSet != null) {
          returnAllResultSetPOObjectList.add(CommonDAOUtil.buildReturnResultList(jdbcConnection,
              oneResultSet, poClassName_class[currentResultSetIndex], this.currentDataBaseType));
        }
      }
    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + "," + calledProcedureSqlstatement + "，"
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
    }
    return returnAllResultSetPOObjectList;
  }

  public <T> List<T> queryAllPOObjectList(Class<T> poClassName_class, String sqlStatementWhere)
      throws CommonDAOException {
    String targetTableName = poClassName_class.getSimpleName();
    return queryAllPOObjectList_ByDBTableName(poClassName_class, targetTableName, sqlStatementWhere);
  }

  public <T> List<T> dividPageQueryAllPOObjectList(Class<T> poClassName_class,
      String sqlStatementWhere, int firstElementIndex, int onePageSize) throws CommonDAOException {
    String targetTableName = poClassName_class.getSimpleName();
    return dividPageQueryAllPOObjectList_ByDBTableName(poClassName_class, targetTableName,
        sqlStatementWhere, firstElementIndex, onePageSize);
  }

  public <T> List<T> dividPageQueryAllPOObjectList_ByDBTableName(Class<T> poClassName_class,
      String targetTableName, String sqlStatementWhere, int firstElementIndex, int onePageSize)
      throws CommonDAOException {
    String dividPageSQLStatement =
        buildDividPageSQLStatement(targetTableName, sqlStatementWhere, firstElementIndex,
            onePageSize);

    return queryAllPOObjectListForDividPageQuer_ByDBTableName(poClassName_class,
        dividPageSQLStatement);
  }

  private <T> List<T> queryAllPOObjectListForDividPageQuer_ByDBTableName(
      Class<T> poClassName_class, String dividPageQuerySQLStatement) throws CommonDAOException {
    Connection jdbcConnection = null;
    PreparedStatement onePreparedStatement = null;
    ResultSet oneResultSet = null;
    List returnAllPOObjectResultList = null;
    try {
      try {
        jdbcConnection = getJDBCConnection();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnectionAndCheck")
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      try {
        onePreparedStatement =
            jdbcConnection.prepareStatement(dividPageQuerySQLStatement, 1005, 1008);
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.divPageAllPO.prepareStatementError")
                + poClassName_class.getName() + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      try {
        oneResultSet = onePreparedStatement.executeQuery();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.divPageAllPO.executePrepareStatementError")
                + poClassName_class.getName()
                + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      boolean checkResultIsNotEmpty = false;
      try {
        checkResultIsNotEmpty = oneResultSet.next();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.divPageAllPO.nextResultError")
                + poClassName_class.getName() + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      if (checkResultIsNotEmpty) {
        try {
          oneResultSet.previous();
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.divPageAllPO.nextResultError")
                  + poClassName_class.getName() + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        returnAllPOObjectResultList =
            CommonDAOUtil.buildReturnResultList(jdbcConnection, oneResultSet, poClassName_class,
                this.currentDataBaseType);
      }

    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + "," + poClassName_class.getName() + "。"
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

    }

    return returnAllPOObjectResultList;
  }

  public <T> List<T> queryAllPOObjectList_ByDBTableName(Class<T> poClassName_class,
      String targetTableName, String sqlStatementWhere) throws CommonDAOException {
    Connection jdbcConnection = null;
    PreparedStatement onePreparedStatement = null;
    ResultSet oneResultSet = null;
    List returnAllPOObjectResultList = null;

    if (sqlStatementWhere == null) {
      sqlStatementWhere = "";
    }

    StringBuffer sqlStatementStringBuffer = new StringBuffer();
    sqlStatementStringBuffer.append("select * from ");
    sqlStatementStringBuffer.append(targetTableName);
    sqlStatementStringBuffer.append(" ");
    sqlStatementStringBuffer.append(sqlStatementWhere);
    try {
      try {
        jdbcConnection = getJDBCConnection();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnectionAndCheck")
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      try {
        onePreparedStatement =
            jdbcConnection.prepareStatement(sqlStatementStringBuffer.toString(), 1005, 1008);
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.queryAllPO.prepareStatementError")
                + poClassName_class.getName() + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      try {
        oneResultSet = onePreparedStatement.executeQuery();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.queryAllPO.executePrepareStatementError")
                + poClassName_class.getName() + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      boolean checkResultIsNotEmpty = false;
      try {
        checkResultIsNotEmpty = oneResultSet.next();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.queryAllPO.nextResultError")
                + poClassName_class.getName() + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      if (checkResultIsNotEmpty) {
        try {
          oneResultSet.previous();
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.queryAllPO.nextResultError")
                  + poClassName_class.getName() + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        returnAllPOObjectResultList =
            CommonDAOUtil.buildReturnResultList(jdbcConnection, oneResultSet, poClassName_class,
                this.currentDataBaseType);
      } else {
        returnAllPOObjectResultList = new ArrayList();
      }

    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + "," + poClassName_class.getName() + "。"
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

    }

    return returnAllPOObjectResultList;
  }

  public <T> List<T> queryAllPOObjectList_ByDBTableName_WithCache(Class<T> poClassName_class,
      String targetTableName, String sqlStatementWhere, int queryCacheMaxSize)
      throws CommonDAOException {
    if (sqlStatementWhere == null) {
      sqlStatementWhere = "";
    }

    StringBuffer sqlStatementStringBuffer = new StringBuffer();
    sqlStatementStringBuffer.append("select * from ");
    sqlStatementStringBuffer.append(targetTableName);
    sqlStatementStringBuffer.append(" ");
    sqlStatementStringBuffer.append(sqlStatementWhere);

    if ((queryCache == null) || (queryCache.size() == 0)) {
      queryCache = new ConcurrentHashMap(new LRUMap(queryCacheMaxSize));
    } else if (queryCache.containsKey(sqlStatementStringBuffer.toString())) {
      return (List) queryCache.get(sqlStatementStringBuffer.toString());
    }

    List returnAllPOObjectResultList =
        queryAllPOObjectList_ByDBTableName(poClassName_class, targetTableName, sqlStatementWhere);

    queryCache.put(sqlStatementStringBuffer.toString(), returnAllPOObjectResultList);

    return returnAllPOObjectResultList;
  }

  public <T> List<T> dividPageQueryAllPOObjectList_ByDBTableName_WithCache(
      Class<T> poClassName_class, String targetTableName, String sqlStatementWhere,
      int firstElementIndex, int onePageSize, int queryCacheMaxSize) throws CommonDAOException {
    String dividPageQuerySQLStatement =
        buildDividPageSQLStatement(targetTableName, sqlStatementWhere, firstElementIndex,
            onePageSize);

    return queryAllPOObjectListForDividPageQuer_ByDBTableName_WithCache(poClassName_class,
        dividPageQuerySQLStatement, queryCacheMaxSize);
  }

  private <T> List<T> queryAllPOObjectListForDividPageQuer_ByDBTableName_WithCache(
      Class<T> poClassName_class, String dividPageQuerySQLStatement, int queryCacheMaxSize)
      throws CommonDAOException {
    if ((queryCache == null) || (queryCache.size() == 0)) {
      queryCache = new ConcurrentHashMap(new LRUMap(queryCacheMaxSize));
    } else if (queryCache.containsKey(dividPageQuerySQLStatement)) {
      return (List) queryCache.get(dividPageQuerySQLStatement);
    }

    List returnAllPOObjectResultList =
        queryAllPOObjectListForDividPageQuer_ByDBTableName(poClassName_class,
            dividPageQuerySQLStatement);

    queryCache.put(dividPageQuerySQLStatement, returnAllPOObjectResultList);

    return returnAllPOObjectResultList;
  }

  public <T> List<T> dividPageQueryAllPOObjectList_WithCache(Class<T> poClassName_class,
      String sqlStatementWhere, int firstElementIndex, int onePageSize, int queryCacheMaxSize)
      throws CommonDAOException {
    String targetTableName = poClassName_class.getSimpleName();
    return dividPageQueryAllPOObjectList_ByDBTableName_WithCache(poClassName_class,
        targetTableName, sqlStatementWhere, firstElementIndex, onePageSize, queryCacheMaxSize);
  }

  public <T> T queryByPrimaryKeyID(Class<T> poClassName_class, Object primaryKeyID)
      throws CommonDAOException {
    String targetTableName = poClassName_class.getSimpleName();
    return queryByPrimaryKeyID_ByDBTableName(poClassName_class, targetTableName, primaryKeyID);
  }

  public <T> T queryByPrimaryKeyID_ByDBTableName(Class<T> poClassName_class,
      String targetTableName, Object primaryKeyIDValue) throws CommonDAOException {
    Connection jdbcConnection = null;
    PreparedStatement onePreparedStatement = null;
    ResultSet oneResultSet = null;
    T returnResultPOObject = null;
    try {
      try {
        jdbcConnection = getJDBCConnection();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnectionAndCheck")
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      ResultSet primaryKeyResultSet = null;
      DatabaseMetaData databaseMetaData = null;
      try {
        databaseMetaData = jdbcConnection.getMetaData();

        if (this.currentDataBaseType == 2) {
          primaryKeyResultSet =
              databaseMetaData.getPrimaryKeys(null, null, targetTableName.toUpperCase());
        } else
          primaryKeyResultSet = databaseMetaData.getPrimaryKeys(null, null, targetTableName);
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.databaseMetaData.getMetaDataError")
                + targetTableName + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
      String primaryKeyColumnName = null;
      try {
        while (primaryKeyResultSet.next())
          primaryKeyColumnName = primaryKeyResultSet.getString("Column_name");
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.databaseMetaData.primaryKeyResultSetError")
                + targetTableName
                + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
      boolean hasPrimaryKeyColumn =
          (!"".equals(primaryKeyColumnName)) || (primaryKeyColumnName != null);
      if (hasPrimaryKeyColumn) {
        StringBuffer sqlStatementStringBuffer = new StringBuffer();
        sqlStatementStringBuffer.append("select * from ");
        sqlStatementStringBuffer.append(targetTableName);
        sqlStatementStringBuffer.append(" where ");
        sqlStatementStringBuffer.append(primaryKeyColumnName);
        sqlStatementStringBuffer.append("=?");
        try {
          onePreparedStatement =
              jdbcConnection.prepareStatement(sqlStatementStringBuffer.toString(), 1005, 1008);
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.queryAllPO.prepareStatementError")
                  + targetTableName + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        try {
          onePreparedStatement.setObject(1, primaryKeyIDValue);
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.queryStatement.parameterError")
                  + targetTableName + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        try {
          oneResultSet = onePreparedStatement.executeQuery();
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.queryAllPO.executePrepareStatementError")
                  + targetTableName
                  + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        boolean checkResultIsNotEmpty = false;
        try {
          checkResultIsNotEmpty = oneResultSet.next();
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.queryAllPO.nextResultError")
                  + targetTableName + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        if (checkResultIsNotEmpty) {
          try {
            oneResultSet.previous();
          } catch (SQLException exception) {
            throw new CommonDAOException(
                this.resourceBundle.getString("jdbcConnection.queryAllPO.nextResultError")
                    + targetTableName + "。"
                    + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle),
                exception);
          }

          List<T> returnAllPOObjectResultList =
              CommonDAOUtil.buildReturnResultList(jdbcConnection, oneResultSet, poClassName_class,
                  this.currentDataBaseType);

          if ((returnAllPOObjectResultList == null) || (returnAllPOObjectResultList.size() == 0)) {
            returnResultPOObject = null;
          } else {
            returnResultPOObject = returnAllPOObjectResultList.get(0);
          }

        } else {
          returnResultPOObject = null;
        }
      } else {
        throw new CommonDAOException(targetTableName + "数据库表中没有提供有主键，该数据库表不满足数据表设计的规范。");
      }

    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + "," + targetTableName + "。"
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
    }
    try {
      closeJDBCConnection(jdbcConnection);
    } catch (SQLException exception) {
      throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
          + "," + targetTableName + "。"
          + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
          + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
    }

    return returnResultPOObject;
  }

  public <T> T queryByPrimaryKeyID_ByDBTableName_WithCache(Class<T> poClassName_class,
      String targetTableName, Object primaryKeyIDValue) throws CommonDAOException {
    Connection jdbcConnection = null;
    PreparedStatement onePreparedStatement = null;
    ResultSet oneResultSet = null;

    T returnResultPOObject = null;
    StringBuffer sqlStatementStringBuffer = null;
    StringBuffer querySqlStatementCacheKey = null;
    try {
      try {
        jdbcConnection = getJDBCConnection();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnectionAndCheck")
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      ResultSet primaryKeyResultSet = null;
      DatabaseMetaData databaseMetaData = null;
      try {
        databaseMetaData = jdbcConnection.getMetaData();

        if (this.currentDataBaseType == 2) {
          primaryKeyResultSet =
              databaseMetaData.getPrimaryKeys(null, null, targetTableName.toUpperCase());
        } else
          primaryKeyResultSet = databaseMetaData.getPrimaryKeys(null, null, targetTableName);
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.databaseMetaData.getMetaDataError")
                + targetTableName + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
      String primaryKeyColumnName = null;
      try {
        while (primaryKeyResultSet.next())
          primaryKeyColumnName = primaryKeyResultSet.getString("Column_name");
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.databaseMetaData.primaryKeyResultSetError")
                + targetTableName
                + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
      boolean hasPrimaryKeyColumn =
          (!"".equals(primaryKeyColumnName)) || (primaryKeyColumnName != null);
      if (hasPrimaryKeyColumn) {
        sqlStatementStringBuffer = new StringBuffer();
        sqlStatementStringBuffer.append("select * from ");
        sqlStatementStringBuffer.append(targetTableName);
        sqlStatementStringBuffer.append(" where ");
        sqlStatementStringBuffer.append(primaryKeyColumnName);
        sqlStatementStringBuffer.append("=?");

        querySqlStatementCacheKey = new StringBuffer();
        querySqlStatementCacheKey.append("select * from ");
        querySqlStatementCacheKey.append(targetTableName);
        querySqlStatementCacheKey.append(" where ");
        querySqlStatementCacheKey.append(primaryKeyColumnName);
        querySqlStatementCacheKey.append("=");
        querySqlStatementCacheKey.append(primaryKeyIDValue.toString());

        int queryCacheMaxSize = 1;
        if ((queryByPrimaryKeyIDCache == null) || (queryByPrimaryKeyIDCache.size() == 0)) {
          queryByPrimaryKeyIDCache = new ConcurrentHashMap(new LRUMap(queryCacheMaxSize));
        } else if (queryByPrimaryKeyIDCache.containsKey(querySqlStatementCacheKey.toString())) {
          try {
            closeJDBCConnection(jdbcConnection);
          } catch (SQLException exception) {
            throw new CommonDAOException(
                this.resourceBundle.getString("common.notCloseJDBCConnection") + ","
                    + targetTableName + "。"
                    + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
                    + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle),
                exception);
          }
          return (T)queryByPrimaryKeyIDCache.get(querySqlStatementCacheKey.toString());
        }

        try {
          onePreparedStatement =
              jdbcConnection.prepareStatement(sqlStatementStringBuffer.toString(), 1005, 1008);
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.queryAllPO.prepareStatementError")
                  + targetTableName + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        try {
          onePreparedStatement.setObject(1, primaryKeyIDValue);
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.queryStatement.parameterError")
                  + targetTableName + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        try {
          oneResultSet = onePreparedStatement.executeQuery();
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.queryAllPO.executePrepareStatementError")
                  + targetTableName
                  + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        boolean checkResultIsNotEmpty = false;
        try {
          checkResultIsNotEmpty = oneResultSet.next();
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.queryAllPO.nextResultError")
                  + targetTableName + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        if (checkResultIsNotEmpty) {
          try {
            oneResultSet.previous();
          } catch (SQLException exception) {
            throw new CommonDAOException(
                this.resourceBundle.getString("jdbcConnection.queryAllPO.nextResultError")
                    + targetTableName + "。"
                    + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle),
                exception);
          }

          List<T> returnAllPOObjectResultList =
              CommonDAOUtil.buildReturnResultList(jdbcConnection, oneResultSet, poClassName_class,
                  this.currentDataBaseType);

          if ((returnAllPOObjectResultList == null) || (returnAllPOObjectResultList.size() == 0)) {
            returnResultPOObject = null;
          } else {
            returnResultPOObject = returnAllPOObjectResultList.get(0);
          }

          queryByPrimaryKeyIDCache.put(querySqlStatementCacheKey.toString(), returnResultPOObject);
        } else {
          returnResultPOObject = null;
        }
      } else {
        throw new CommonDAOException(targetTableName + "数据库表中没有提供有主键，该数据库表不满足数据表设计的规范。");
      }

    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + "," + targetTableName + "。"
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
    }
    try {
      closeJDBCConnection(jdbcConnection);
    } catch (SQLException exception) {
      throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
          + "," + targetTableName + "。"
          + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
          + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
    }

    return returnResultPOObject;
  }

  public <T> T queryByPrimaryKeyID_WithCache(Class<T> poClassName_class, Object primaryKeyID)
      throws CommonDAOException {
    String targetTableName = poClassName_class.getSimpleName();
    return queryByPrimaryKeyID_ByDBTableName_WithCache(poClassName_class, targetTableName,
        primaryKeyID);
  }

  public boolean insertOnePOObject(Object someOneTargetPOObject) throws CommonDAOException {
    Class poClassName_class = someOneTargetPOObject.getClass();
    String targetTableName = poClassName_class.getSimpleName();
    return insertOnePOObject_ByDBTableName(someOneTargetPOObject, targetTableName);
  }

  public boolean insertOnePOObject_ByDBTableName(Object someOneTargetPOObject,
      String targetTableName) throws CommonDAOException {
    Connection jdbcConnection = null;
    boolean returnStatus = false;
    try {
      Map allFieldsNameAndValueMap = new HashMap();

      Field[] allFieldsInTargetPOObject = someOneTargetPOObject.getClass().getDeclaredFields();

      Field.setAccessible(allFieldsInTargetPOObject, true);
      for (Field someOneFieldInTargetPOObject : allFieldsInTargetPOObject) {
        Object someOneFieldValue = null;
        try {
          someOneFieldValue = someOneFieldInTargetPOObject.get(someOneTargetPOObject);
        } catch (IllegalArgumentException exception) {
          throw new CommonDAOException(targetTableName + "，"
              + this.resourceBundle.getString("IllegalArgumentExceptionError"), exception);
        } catch (IllegalAccessException exception) {
          throw new CommonDAOException(targetTableName + "，"
              + this.resourceBundle.getString("IllegalAccessException"), exception);
        }

        String someOneFieldName = someOneFieldInTargetPOObject.getName();
        allFieldsNameAndValueMap.put(someOneFieldName.toLowerCase(), someOneFieldValue);
      }

      try {
        jdbcConnection = getJDBCConnection();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnectionAndCheck")
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
      ResultSet allColumnNameResultSetInTargetTable = null;

      PreparedStatement preparedStatement = null;
      try {
        preparedStatement =
            jdbcConnection.prepareStatement("select * from " + targetTableName, 1004, 1008);
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.queryAllPO.prepareStatementError")
                + targetTableName + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      try {
        allColumnNameResultSetInTargetTable = preparedStatement.executeQuery();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.queryAllPO.executePrepareStatementError")
                + targetTableName + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
      ResultSetMetaData resultSetMetaData = null;
      try {
        resultSetMetaData = allColumnNameResultSetInTargetTable.getMetaData();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.databaseMetaData.getMetaDataError")
                + targetTableName + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
      int totalColumnCounter = 0;
      try {
        totalColumnCounter = resultSetMetaData.getColumnCount();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.databaseMetaData.getColumnCountError")
                + targetTableName + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      StringBuffer allColumnNameStringBuffer = new StringBuffer();
      allColumnNameStringBuffer.append("insert into ");
      allColumnNameStringBuffer.append(targetTableName);
      allColumnNameStringBuffer.append("(");

      StringBuffer allColumnValueStringBuffer = new StringBuffer(" values(");

      Object[] allColumnValueObjectArray = new Object[totalColumnCounter];
      Integer[] allColumnDataTypeArray = new Integer[totalColumnCounter];

      for (int loopIndex = 1; loopIndex <= totalColumnCounter; loopIndex++) {
        String someOneColumnName = null;
        Integer someOneColumnDataType = null;
        try {
          someOneColumnName = resultSetMetaData.getColumnName(loopIndex);

          someOneColumnDataType = Integer.valueOf(resultSetMetaData.getColumnType(loopIndex));
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.databaseMetaData.getColumnTypeError")
                  + targetTableName + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        if (loopIndex == totalColumnCounter) {
          allColumnNameStringBuffer.append(someOneColumnName + ")");
          allColumnValueStringBuffer.append("?)");
        } else {
          allColumnNameStringBuffer.append(someOneColumnName + ",");
          allColumnValueStringBuffer.append("?,");
        }

        allColumnValueObjectArray[(loopIndex - 1)] =
            allFieldsNameAndValueMap.get(someOneColumnName.toLowerCase());

        allColumnDataTypeArray[(loopIndex - 1)] = someOneColumnDataType;
      }

      String insertSQLStatement = allColumnValueStringBuffer.toString();
      try {
        preparedStatement = jdbcConnection.prepareStatement(insertSQLStatement);
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.insertOnePO.prepareStatementError")
                + targetTableName + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      for (int loopIndex = 1; loopIndex <= allColumnDataTypeArray.length; loopIndex++) {
        Integer someOneColumnDataType = allColumnDataTypeArray[(loopIndex - 1)];
        Object someOneColumnValue = allColumnValueObjectArray[(loopIndex - 1)];

        CommonDAOUtil.setPreparedStatementParameter(someOneColumnDataType, someOneColumnValue,
            preparedStatement, loopIndex, this.currentDataBaseType);
      }

      int insertExecuteResult = 0;
      try {
        insertExecuteResult = preparedStatement.executeUpdate();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.insertOnePOByPrimaryKey.executePrepareStatementError")
                + targetTableName
                + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      if (insertExecuteResult > 0) {
        returnStatus = true;
      } else {
        returnStatus = false;
      }

    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + "," + targetTableName + "。"
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
    }
    return returnStatus;
  }

  public boolean updateSomeFieldsInPOObject_ByDBTableName(Object someOneTargetPOObject,
      String targetTableName, String sqlStatementWhere) throws CommonDAOException {
    boolean returnStatus = false;
    Connection jdbcConnection = null;
    PreparedStatement onePreparedStatement = null;

    StringBuffer updateSQLStatementStringBuffer = new StringBuffer();
    updateSQLStatementStringBuffer.append("update ");
    updateSQLStatementStringBuffer.append(targetTableName);
    updateSQLStatementStringBuffer.append(" set ");
    try {
      Field[] allFieldsInTargetPOObject = someOneTargetPOObject.getClass().getDeclaredFields();
      Field.setAccessible(allFieldsInTargetPOObject, true);

      Object[] allColumnParameterValueObjectArray = new Object[allFieldsInTargetPOObject.length];
      for (int loopIndex = 1; loopIndex <= allFieldsInTargetPOObject.length; loopIndex++) {
        Field someOneFieldInTargetPOObject = allFieldsInTargetPOObject[(loopIndex - 1)];

        Object someOneFieldValue = null;
        try {
          someOneFieldValue = someOneFieldInTargetPOObject.get(someOneTargetPOObject);
        } catch (IllegalArgumentException exception) {
          throw new CommonDAOException(targetTableName + "，"
              + this.resourceBundle.getString("IllegalArgumentExceptionError"), exception);
        } catch (IllegalAccessException exception) {
          throw new CommonDAOException(targetTableName + "，"
              + this.resourceBundle.getString("IllegalAccessException"), exception);
        }

        allColumnParameterValueObjectArray[(loopIndex - 1)] = someOneFieldValue;

        String someOneFieldName = someOneFieldInTargetPOObject.getName();
        if (loopIndex == allFieldsInTargetPOObject.length)
          updateSQLStatementStringBuffer.append(someOneFieldName.toLowerCase() + "=? ");
        else {
          updateSQLStatementStringBuffer.append(someOneFieldName.toLowerCase() + "=?, ");
        }

      }

      if (sqlStatementWhere == null) {
        sqlStatementWhere = "";
      }
      String updateSQLStatement = sqlStatementWhere;
      try {
        jdbcConnection = getJDBCConnection();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnectionAndCheck")
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      try {
        onePreparedStatement = jdbcConnection.prepareStatement(updateSQLStatement);
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.updateOnePO.prepareStatementError")
                + targetTableName + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      for (int loopIndex = 0; loopIndex < allColumnParameterValueObjectArray.length; loopIndex++) {
        try {
          onePreparedStatement.setObject(loopIndex + 1,
              allColumnParameterValueObjectArray[loopIndex]);
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.updateOnePO.parameterError")
                  + targetTableName + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

      }

      int updateExecuteResult = 0;
      try {
        updateExecuteResult = onePreparedStatement.executeUpdate();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.updateOnePO.executePrepareStatementError")
                + targetTableName
                + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      if (updateExecuteResult > 0) {
        returnStatus = true;
      } else {
        returnStatus = false;
      }

    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + "," + targetTableName + "。"
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
    }
    return returnStatus;
  }

  public <T> boolean deleteSomePOObject_ByDBTableName(String targetTableName,
      String sqlStatementWhere) throws CommonDAOException {
    Connection jdbcConnection = null;
    PreparedStatement onePreparedStatement = null;
    boolean returnStatus = false;
    try {
      StringBuffer deleteStringBuffer = new StringBuffer();
      deleteStringBuffer.append("delete from ");
      deleteStringBuffer.append(targetTableName);
      deleteStringBuffer.append(" ");
      deleteStringBuffer.append(sqlStatementWhere);
      try {
        jdbcConnection = getJDBCConnection();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnectionAndCheck")
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      try {
        onePreparedStatement = jdbcConnection.prepareStatement(deleteStringBuffer.toString());
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.deleteOnePO.prepareStatementError")
                + targetTableName + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      int deleteResultStatus = 0;
      try {
        deleteResultStatus = onePreparedStatement.executeUpdate();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.deleteOnePO.executePrepareStatementError")
                + targetTableName
                + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      if (deleteResultStatus > 0) {
        returnStatus = true;
      } else {
        returnStatus = false;
      }

    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + "," + targetTableName + "。"
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
    }
    return returnStatus;
  }

  public <T> boolean deleteSomePOObject(Class<T> poClassName_class, String sqlStatementWhere)
      throws CommonDAOException {
    String targetTableName = poClassName_class.getSimpleName();
    return deleteSomePOObject_ByDBTableName(targetTableName, sqlStatementWhere);
  }

  public boolean insertOnePOObjectByTargetSQLStatement(String insertSqlStatement,
      Object[] argsInsertSqlStatement) throws CommonDAOException {
    return executeUpdateSqlStatement(insertSqlStatement, argsInsertSqlStatement);
  }

  public boolean deletePOObjectByTargetSQLStatement(String deleteSqlStatement,
      Object[] argsDeleteSqlStatement) throws CommonDAOException {
    return executeUpdateSqlStatement(deleteSqlStatement, argsDeleteSqlStatement);
  }

  public boolean updatePOObjectByTargetSQLStatement(String updateSqlStatement,
      Object[] argsUpdateSqlStatement) throws CommonDAOException {
    return executeUpdateSqlStatement(updateSqlStatement, argsUpdateSqlStatement);
  }

  public List<Map<String, Object>> querySomePOObjectByTargetSQLStatement(String querySqlStatement,
      Object[] argsQuerySqlStatement) throws CommonDAOException {
    Connection jdbcConnection = null;
    PreparedStatement onePreparedStatement = null;
    ResultSet oneResultSet = null;
    List queryReturnResultList = new ArrayList();
    try {
      try {
        jdbcConnection = getJDBCConnection();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnectionAndCheck")
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      try {
        onePreparedStatement = jdbcConnection.prepareStatement(querySqlStatement, 1005, 1008);
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.queryAllPO.prepareStatementError")
                + querySqlStatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      if (argsQuerySqlStatement != null) {
        for (int loopIndex = 0; loopIndex < argsQuerySqlStatement.length; loopIndex++) {
          try {
            onePreparedStatement.setObject(loopIndex + 1, argsQuerySqlStatement[loopIndex]);
          } catch (SQLException exception) {
            throw new CommonDAOException(
                this.resourceBundle.getString("jdbcConnection.queryStatement.parameterError")
                    + querySqlStatement + "。"
                    + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle),
                exception);
          }

        }

      }

      try {
        oneResultSet = onePreparedStatement.executeQuery();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.queryAllPO.executePrepareStatementError")
                + querySqlStatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      boolean checkResultIsNotEmpty = false;
      try {
        checkResultIsNotEmpty = oneResultSet.next();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.queryAllPO.nextResultError")
                + querySqlStatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      if (checkResultIsNotEmpty) {
        try {
          oneResultSet.previous();
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.queryAllPO.nextResultError")
                  + querySqlStatement + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        CommonDAOUtil.buildReturnResultMapList(oneResultSet, queryReturnResultList);
      }

    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + "," + querySqlStatement + "。"
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
    }
    return queryReturnResultList;
  }

  public List<Map<String, Object>> querySomePOObjectByTargetSQLStatement_WithCache(
      String querySqlStatement, int queryCacheMaxSize, Object[] argsQuerySqlStatement)
      throws CommonDAOException {
    Connection jdbcConnection = null;
    PreparedStatement onePreparedStatement = null;
    ResultSet oneResultSet = null;

    if ((querySomePOObjectCache == null) || (querySomePOObjectCache.size() == 0)) {
      querySomePOObjectCache = new ConcurrentHashMap(new LRUMap(queryCacheMaxSize));
    } else if (querySomePOObjectCache.containsKey(querySqlStatement)) {
      return (List) querySomePOObjectCache.get(querySqlStatement);
    }

    List queryReturnResultList = new ArrayList();
    try {
      try {
        jdbcConnection = getJDBCConnection();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnectionAndCheck")
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      try {
        onePreparedStatement = jdbcConnection.prepareStatement(querySqlStatement, 1005, 1008);
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.queryAllPO.prepareStatementError")
                + querySqlStatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      if (argsQuerySqlStatement != null) {
        for (int loopIndex = 0; loopIndex < argsQuerySqlStatement.length; loopIndex++) {
          try {
            onePreparedStatement.setObject(loopIndex + 1, argsQuerySqlStatement[loopIndex]);
          } catch (SQLException exception) {
            throw new CommonDAOException(
                this.resourceBundle.getString("jdbcConnection.queryStatement.parameterError")
                    + querySqlStatement + "。"
                    + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle),
                exception);
          }

        }

      }

      try {
        oneResultSet = onePreparedStatement.executeQuery();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.queryAllPO.executePrepareStatementError")
                + querySqlStatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      boolean checkResultIsNotEmpty = false;
      try {
        checkResultIsNotEmpty = oneResultSet.next();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.queryAllPO.nextResultError")
                + querySqlStatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      if (checkResultIsNotEmpty) {
        try {
          oneResultSet.previous();
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.queryAllPO.nextResultError")
                  + querySqlStatement + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        CommonDAOUtil.buildReturnResultMapList(oneResultSet, queryReturnResultList);
      }

    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + "," + querySqlStatement + "。"
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

    }

    querySomePOObjectCache.put(querySqlStatement, queryReturnResultList);

    return queryReturnResultList;
  }

  public int[] batchUpdateBySameSQLStatement_NoTransaction(String batchUpdateSqlStatement,
      Object[][] argsbatchUpdateSqlStatement) throws CommonDAOException {
    Connection jdbcConnection = null;
    PreparedStatement onePreparedStatement = null;

    int[] returnResultStatusArray = (int[]) null;
    try {
      try {
        jdbcConnection = getJDBCConnection();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnectionAndCheck")
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      try {
        onePreparedStatement = jdbcConnection.prepareStatement(batchUpdateSqlStatement);
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.updateOnePO.prepareStatementError")
                + batchUpdateSqlStatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
      try {
        for (int outLoopIndex = 0; outLoopIndex < argsbatchUpdateSqlStatement.length; outLoopIndex++) {
          for (int innerLoopIndex = 0; innerLoopIndex < argsbatchUpdateSqlStatement[outLoopIndex].length; innerLoopIndex++) {
            onePreparedStatement.setObject(innerLoopIndex + 1,
                argsbatchUpdateSqlStatement[outLoopIndex][innerLoopIndex]);
          }
          onePreparedStatement.addBatch();
        }
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.updateOnePO.parameterError")
                + batchUpdateSqlStatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
      try {
        returnResultStatusArray = onePreparedStatement.executeBatch();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.updateOnePO.executePrepareStatementError")
                + batchUpdateSqlStatement
                + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + "," + batchUpdateSqlStatement + "。"
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
    }
    try {
      closeJDBCConnection(jdbcConnection);
    } catch (SQLException exception) {
      throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
          + "," + batchUpdateSqlStatement + "。"
          + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
          + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
    }

    return returnResultStatusArray;
  }

  public int[] batchUpdateBySameSQLStatement_WithTransaction(String batchUpdateSqlStatement,
      Object[][] argsbatchUpdateSqlStatement) throws CommonDAOException {
    Connection jdbcConnection = null;
    PreparedStatement onePreparedStatement = null;
    int[] returnResultStatusArray = (int[]) null;
    try {
      try {
        jdbcConnection = getJDBCConnection();
        jdbcConnection.setAutoCommit(false);
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnectionAndCheck")
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      try {
        onePreparedStatement = jdbcConnection.prepareStatement(batchUpdateSqlStatement);
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.updateOnePO.prepareStatementError")
                + batchUpdateSqlStatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
      try {
        for (int outLoopIndex = 0; outLoopIndex < argsbatchUpdateSqlStatement.length; outLoopIndex++) {
          for (int innerLoopIndex = 0; innerLoopIndex < argsbatchUpdateSqlStatement[outLoopIndex].length; innerLoopIndex++) {
            onePreparedStatement.setObject(innerLoopIndex + 1,
                argsbatchUpdateSqlStatement[outLoopIndex][innerLoopIndex]);
          }
          onePreparedStatement.addBatch();
        }
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.updateOnePO.parameterError")
                + batchUpdateSqlStatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
      try {
        returnResultStatusArray = onePreparedStatement.executeBatch();
        jdbcConnection.commit();
      } catch (SQLException exception) {
        try {
          jdbcConnection.rollback();
        } catch (SQLException exception1) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.updateOnePO.rollbackError")
                  + batchUpdateSqlStatement + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.updateOnePO.executeBatchError")
                + batchUpdateSqlStatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + "," + batchUpdateSqlStatement + "。"
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
    }
    try {
      closeJDBCConnection(jdbcConnection);
    } catch (SQLException exception) {
      throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
          + "," + batchUpdateSqlStatement + "。"
          + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
          + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
    }

    return returnResultStatusArray;
  }

  public int[] batchUpdateByNotSameSQLStatement_NoTransaction(
      String[] notSameSqlStatementStringArray, Object[][] allArgsForPerSQLStatement)
      throws CommonDAOException {
    Connection jdbcConnection = null;
    int[] returnResultStatusArray = new int[notSameSqlStatementStringArray.length];
    try {
      try {
        jdbcConnection = getJDBCConnection();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnectionAndCheck")
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
      for (int outLoopIndex = 0; outLoopIndex < notSameSqlStatementStringArray.length; outLoopIndex++) {
        PreparedStatement onePreparedStatement = null;
        try {
          onePreparedStatement =
              jdbcConnection.prepareStatement(notSameSqlStatementStringArray[outLoopIndex]);
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.updateOnePO.prepareStatementError")
                  + notSameSqlStatementStringArray[outLoopIndex] + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        try {
          for (int innerLoopIndex = 0; innerLoopIndex < allArgsForPerSQLStatement[outLoopIndex].length; innerLoopIndex++)
            onePreparedStatement.setObject(innerLoopIndex + 1,
                allArgsForPerSQLStatement[outLoopIndex][innerLoopIndex]);
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.updateOnePO.parameterError")
                  + notSameSqlStatementStringArray[outLoopIndex] + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }
        int someOneSQLExecuteResultStatus;
        try {
          someOneSQLExecuteResultStatus = onePreparedStatement.executeUpdate();
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.updateOnePO.executeBatchError")
                  + notSameSqlStatementStringArray[outLoopIndex] + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }
        
        returnResultStatusArray[outLoopIndex] = someOneSQLExecuteResultStatus;
      }

    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
    }
    return returnResultStatusArray;
  }

  public int[] batchUpdateByNotSameSQLStatement_WithTransaction(
      String[] notSameSqlStatementStringArray, Object[][] allArgsForPerSQLStatement)
      throws CommonDAOException {
    Connection jdbcConnection = null;
    int[] returnResultStatusArray = new int[notSameSqlStatementStringArray.length];
    try {
      try {
        jdbcConnection = getJDBCConnection();
        jdbcConnection.setAutoCommit(false);
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnectionAndCheck")
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
      for (int outLoopIndex = 0; outLoopIndex < notSameSqlStatementStringArray.length; outLoopIndex++) {
        PreparedStatement onePreparedStatement = null;
        try {
          onePreparedStatement =
              jdbcConnection.prepareStatement(notSameSqlStatementStringArray[outLoopIndex]);
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.updateOnePO.prepareStatementError")
                  + notSameSqlStatementStringArray[outLoopIndex] + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        try {
          for (int innerLoopIndex = 0; innerLoopIndex < allArgsForPerSQLStatement[outLoopIndex].length; innerLoopIndex++)
            onePreparedStatement.setObject(innerLoopIndex + 1,
                allArgsForPerSQLStatement[outLoopIndex][innerLoopIndex]);
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.updateOnePO.parameterError")
                  + notSameSqlStatementStringArray[outLoopIndex] + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        int someOneSQLExecuteResultStatus;
        try {
          someOneSQLExecuteResultStatus = onePreparedStatement.executeUpdate();
        } catch (SQLException exception) {
          throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.updateOnePO.executeBatchError")
            + notSameSqlStatementStringArray[outLoopIndex] + "。"
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }
        returnResultStatusArray[outLoopIndex] = someOneSQLExecuteResultStatus;
      }

      try {
        jdbcConnection.commit();
      } catch (SQLException exception) {
        try {
          jdbcConnection.rollback();
        } catch (SQLException exception1) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.updateOnePO.rollbackError")
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.updateOnePO.executeBatchError")
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
    }
    try {
      closeJDBCConnection(jdbcConnection);
    } catch (SQLException exception) {
      throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
          + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
          + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
    }

    return returnResultStatusArray;
  }

  public List<List<Map<String, Object>>> batchQueryAndReturnListListMap(
      String[] allQuerySqlStatementStringArray, Object[][] allArgsForPerQuerySQLStatement)
      throws CommonDAOException {
    Connection jdbcConnection = null;
    List returnAllListListMapResult = new ArrayList();
    try {
      try {
        jdbcConnection = getJDBCConnection();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnectionAndCheck")
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
      for (int outLoopIndex = 0; outLoopIndex < allQuerySqlStatementStringArray.length; outLoopIndex++) {
        PreparedStatement onePreparedStatement = null;
        ResultSet oneResultSet = null;
        List queryReturnResultList_ForSomeOneSQLStatement = new ArrayList();
        try {
          onePreparedStatement =
              jdbcConnection.prepareStatement(allQuerySqlStatementStringArray[outLoopIndex], 1005,
                  1008);
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.queryAllPO.prepareStatementError")
                  + allQuerySqlStatementStringArray[outLoopIndex] + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        if ((allArgsForPerQuerySQLStatement[outLoopIndex] != null)
            && (allArgsForPerQuerySQLStatement[outLoopIndex].length > 0)) {
          for (int innerLoopIndex = 0; innerLoopIndex < allArgsForPerQuerySQLStatement[outLoopIndex].length; innerLoopIndex++) {
            try {
              onePreparedStatement.setObject(innerLoopIndex + 1,
                  allArgsForPerQuerySQLStatement[outLoopIndex][innerLoopIndex]);
            } catch (SQLException exception) {
              throw new CommonDAOException(
                  this.resourceBundle.getString("jdbcConnection.queryStatement.parameterError")
                      + allQuerySqlStatementStringArray[outLoopIndex] + "。"
                      + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle),
                  exception);
            }

          }

        }

        try {
          oneResultSet = onePreparedStatement.executeQuery();
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.queryAllPO.executePrepareStatementError")
                  + allQuerySqlStatementStringArray[outLoopIndex]
                  + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        boolean checkResultIsNotEmpty = false;
        try {
          checkResultIsNotEmpty = oneResultSet.next();
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.queryAllPO.nextResultError")
                  + allQuerySqlStatementStringArray[outLoopIndex] + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        if (checkResultIsNotEmpty) {
          try {
            oneResultSet.previous();
          } catch (SQLException exception) {
            throw new CommonDAOException(
                this.resourceBundle.getString("jdbcConnection.queryAllPO.nextResultError")
                    + allQuerySqlStatementStringArray[outLoopIndex] + "。"
                    + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle),
                exception);
          }

          CommonDAOUtil.buildReturnResultMapList(oneResultSet,
              queryReturnResultList_ForSomeOneSQLStatement);
        }

        returnAllListListMapResult.add(queryReturnResultList_ForSomeOneSQLStatement);
      }

    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
    }
    return returnAllListListMapResult;
  }

  public List<List> batchQueryAndReturnListListPO(Class[] poClassName_class,
      String[] allQuerySqlStatementStringArray, Object[][] allArgsForPerQuerySQLStatement)
      throws CommonDAOException {
    Connection jdbcConnection = null;
    List returnAllListPOObjectResultList = new ArrayList();
    try {
      try {
        jdbcConnection = getJDBCConnection();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnectionAndCheck")
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      for (int outLoopIndex = 0; outLoopIndex < allQuerySqlStatementStringArray.length; outLoopIndex++) {
        PreparedStatement onePreparedStatement = null;
        ResultSet oneResultSet = null;
        List queryReturnResultList_ForSomeOneSQLStatement = null;
        try {
          onePreparedStatement =
              jdbcConnection.prepareStatement(allQuerySqlStatementStringArray[outLoopIndex], 1005,
                  1008);
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.queryAllPO.prepareStatementError")
                  + allQuerySqlStatementStringArray[outLoopIndex] + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        if ((allArgsForPerQuerySQLStatement[outLoopIndex] != null)
            && (allArgsForPerQuerySQLStatement[outLoopIndex].length > 0)) {
          for (int innerLoopIndex = 0; innerLoopIndex < allArgsForPerQuerySQLStatement[outLoopIndex].length; innerLoopIndex++) {
            try {
              onePreparedStatement.setObject(innerLoopIndex + 1,
                  allArgsForPerQuerySQLStatement[outLoopIndex][innerLoopIndex]);
            } catch (SQLException exception) {
              throw new CommonDAOException(
                  this.resourceBundle.getString("jdbcConnection.queryStatement.parameterError")
                      + allQuerySqlStatementStringArray[outLoopIndex] + "。"
                      + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle),
                  exception);
            }

          }

        }

        try {
          oneResultSet = onePreparedStatement.executeQuery();
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.queryAllPO.executePrepareStatementError")
                  + allQuerySqlStatementStringArray[outLoopIndex]
                  + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        boolean checkResultIsNotEmpty = false;
        try {
          checkResultIsNotEmpty = oneResultSet.next();
        } catch (SQLException exception) {
          throw new CommonDAOException(
              this.resourceBundle.getString("jdbcConnection.queryAllPO.nextResultError")
                  + allQuerySqlStatementStringArray[outLoopIndex] + "。"
                  + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
        }

        if (checkResultIsNotEmpty) {
          try {
            oneResultSet.previous();
          } catch (SQLException exception) {
            throw new CommonDAOException(
                this.resourceBundle.getString("jdbcConnection.queryAllPO.nextResultError")
                    + allQuerySqlStatementStringArray[outLoopIndex] + "。"
                    + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle),
                exception);
          }

          queryReturnResultList_ForSomeOneSQLStatement =
              CommonDAOUtil.buildReturnResultList(jdbcConnection, oneResultSet,
                  poClassName_class[outLoopIndex], this.currentDataBaseType);

          returnAllListPOObjectResultList.add(queryReturnResultList_ForSomeOneSQLStatement);
        }

      }

    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
    }
    return returnAllListPOObjectResultList;
  }

  public boolean dropDBTable(Object someOneTargetPOObject) throws CommonDAOException {
    Connection jdbcConnection = null;
    PreparedStatement preparedStatement = null;
    String dropDBTableSQLStatement = null;

    Class poClassName_class = someOneTargetPOObject.getClass();
    String targetTableName = poClassName_class.getSimpleName();
    try {
      try {
        jdbcConnection = getJDBCConnection();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnectionAndCheck")
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
      dropDBTableSQLStatement = "Drop table " + targetTableName;
      try {
        preparedStatement = jdbcConnection.prepareStatement(dropDBTableSQLStatement);
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.dropTable.prepareStatementError")
                + dropDBTableSQLStatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
      try {
        preparedStatement.executeUpdate();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.dropTable.executeError")
                + dropDBTableSQLStatement + "。"
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + "," + dropDBTableSQLStatement + "。"
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
    }
    try {
      closeJDBCConnection(jdbcConnection);
    } catch (SQLException exception) {
      throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
          + "," + dropDBTableSQLStatement + "。"
          + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
          + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
    }

    return true;
  }

  public Map<String, String> dumpDataBaseMetaData() throws CommonDAOException {
    Connection jdbcConnection = null;
    Map returnDataBaseMetaDataMap = null;
    DatabaseMetaData currentDatabaseMetaData = null;
    try {
      try {
        jdbcConnection = getJDBCConnection();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.noJDBCConnectionAndCheck")
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      try {
        currentDatabaseMetaData = jdbcConnection.getMetaData();
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.databaseMetaData.getMetaDataError")
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

      returnDataBaseMetaDataMap = new HashMap();
      try {
        returnDataBaseMetaDataMap.put("dbURL", currentDatabaseMetaData.getURL());

        returnDataBaseMetaDataMap.put("loginName", currentDatabaseMetaData.getUserName());

        returnDataBaseMetaDataMap.put("databaseProductName",
            currentDatabaseMetaData.getDatabaseProductName());

        returnDataBaseMetaDataMap.put("productVersion",
            currentDatabaseMetaData.getDatabaseProductVersion());

        returnDataBaseMetaDataMap.put("driverClassName", currentDatabaseMetaData.getDriverName());

        returnDataBaseMetaDataMap.put("driverVersion", currentDatabaseMetaData.getDriverVersion());
      } catch (SQLException exception) {
        throw new CommonDAOException(
            this.resourceBundle.getString("jdbcConnection.databaseMetaData.getMetaDataError")
                + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }

    } finally {
      try {
        closeJDBCConnection(jdbcConnection);
      } catch (SQLException exception) {
        throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
            + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
            + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
      }
    }
    try {
      closeJDBCConnection(jdbcConnection);
    } catch (SQLException exception) {
      throw new CommonDAOException(this.resourceBundle.getString("common.notCloseJDBCConnection")
          + this.resourceBundle.getString("common.orignalSQLExceptionInfo")
          + CommonDAOUtil.dealSQLExceptionState(exception, this.resourceBundle), exception);
    }

    return returnDataBaseMetaDataMap;
  }

  public abstract String buildDividPageSQLStatement(String paramString1, String paramString2,
      int paramInt1, int paramInt2) throws CommonDAOException;
}
