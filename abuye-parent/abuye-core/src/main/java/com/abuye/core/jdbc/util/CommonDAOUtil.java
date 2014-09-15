package com.abuye.core.jdbc.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import com.abuye.core.jdbc.exception.CommonDAOException;

public class CommonDAOUtil {
  public static boolean testJDBCConnectionValid(Connection jdbcConnection)
      throws CommonDAOException {
    PreparedStatement preparedStatement = null;
    ResultSet oneResultSet = null;

    String dualSqlStatement = "SELECT COUNT(*) FROM DUAL";
    try {
      preparedStatement = jdbcConnection.prepareStatement(dualSqlStatement);
    } catch (SQLException exception) {
      throw new CommonDAOException(dealSQLState(exception), exception);
    }

    try {
      oneResultSet = preparedStatement.executeQuery();
    } catch (SQLException exception) {
      throw new CommonDAOException(dealSQLState(exception), exception);
    }
    boolean isJDBCConnectionOK = false;
    try {
      isJDBCConnectionOK = oneResultSet.next();
    } catch (SQLException exception) {
      throw new CommonDAOException(dealSQLState(exception), exception);
    }

    return isJDBCConnectionOK;
  }

  public static void buildReturnResultMapList(ResultSet oneResultSet,
      List<Map<String, Object>> queryReturnResultList) throws CommonDAOException {
    ResultSetMetaData resultSetMetaData = null;
    try {
      resultSetMetaData = oneResultSet.getMetaData();
    } catch (SQLException exception) {
      throw new CommonDAOException(dealSQLState(exception), exception);
    }

    int totalColumnCounter = 0;
    try {
      totalColumnCounter = resultSetMetaData.getColumnCount();
    } catch (SQLException exception) {
      throw new CommonDAOException(dealSQLState(exception), exception);
    }

    String[] allColumnNameArray = new String[totalColumnCounter];
    for (int loopIndex = 0; loopIndex < totalColumnCounter; loopIndex++) {
      String someOneColumnName = null;
      try {
        someOneColumnName = resultSetMetaData.getColumnLabel(loopIndex + 1);
      } catch (SQLException exception) {
        throw new CommonDAOException(dealSQLState(exception), exception);
      }

      allColumnNameArray[loopIndex] = someOneColumnName;
    }

    try {
      while (oneResultSet.next()) {
        Map allColumnValueInOneRecord = new HashMap();
        for (int loopIndex = 0; loopIndex < totalColumnCounter; loopIndex++) {
          Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);
          allColumnValueInOneRecord.put(allColumnNameArray[loopIndex], someOneColumnValue);
        }
        queryReturnResultList.add(allColumnValueInOneRecord);
      }
    } catch (SQLException exception) {
      throw new CommonDAOException(dealSQLState(exception), exception);
    }
  }

  public static <T> List<T> buildReturnResultList(Connection jdbcConnection,
      ResultSet oneResultSet, Class<T> poClassName_class, int currentDataBaseType)
      throws CommonDAOException {
    List allFieldNameList_InTargetPOObject = new ArrayList();

    Field[] allFieldsInTargetPOObject = poClassName_class.getDeclaredFields();

    Field.setAccessible(allFieldsInTargetPOObject, true);

    for (Field someOneFieldInTargetPOObject : allFieldsInTargetPOObject) {
      String someOneFieldName = someOneFieldInTargetPOObject.getName();

      allFieldNameList_InTargetPOObject.add(someOneFieldName);
    }
    ResultSetMetaData resultSetMetaData = null;
    try {
      resultSetMetaData = oneResultSet.getMetaData();
    } catch (SQLException exception) {
      throw new CommonDAOException(dealSQLState(exception), exception);
    }

    int totalColumnCounter = 0;
    try {
      totalColumnCounter = resultSetMetaData.getColumnCount();
    } catch (SQLException exception) {
      throw new CommonDAOException(dealSQLState(exception), exception);
    }

    String[] allColumnNameArray = new String[totalColumnCounter];
    Integer[] allColumnDataTypeArray = new Integer[totalColumnCounter];
    for (int loopIndex = 0; loopIndex < totalColumnCounter; loopIndex++) {
      String someOneColumnName = null;
      Integer someOneColumnDataType = null;
      try {
        someOneColumnName = resultSetMetaData.getColumnLabel(loopIndex + 1);
        someOneColumnDataType = Integer.valueOf(resultSetMetaData.getColumnType(loopIndex + 1));

        Iterator allFieldNameListItems = allFieldNameList_InTargetPOObject.iterator();
        while (allFieldNameListItems.hasNext()) {
          String someOneFieldName_InTargetPOObject = (String) allFieldNameListItems.next();

          if (someOneFieldName_InTargetPOObject.equalsIgnoreCase(someOneColumnName)) {
            someOneColumnName = someOneFieldName_InTargetPOObject;
          }

        }

        allColumnNameArray[loopIndex] = someOneColumnName;
        allColumnDataTypeArray[loopIndex] = someOneColumnDataType;
      } catch (SQLException exception) {
        throw new CommonDAOException(dealSQLState(exception), exception);
      }

    }

    List returnAllPOObjectResultList = new ArrayList();
    Object someOneTargetPOObject = null;
    try {
      while (oneResultSet.next()) {
        someOneTargetPOObject = poClassName_class.newInstance();

        for (int loopIndex = 0; loopIndex < totalColumnCounter; loopIndex++) {
          String someOneColumnName = allColumnNameArray[loopIndex];

          boolean isOracleDivPageQuery_RNTempColumn =
              (currentDataBaseType == 2)
                  && ("rownumTempColumn".equalsIgnoreCase(someOneColumnName));
          if (!isOracleDivPageQuery_RNTempColumn) {
            Field someOneFieldInTargetPOObject =
                poClassName_class.getDeclaredField(someOneColumnName);
            someOneFieldInTargetPOObject.setAccessible(true);

            Integer someOneColumnDataType = allColumnDataTypeArray[loopIndex];
            if (2003 == someOneColumnDataType.intValue()) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (-5 == someOneColumnDataType.intValue()) {
              Long someOneColumnLongValue =
                  Long.valueOf(oneResultSet.getLong(allColumnNameArray[loopIndex]));

              if (someOneColumnLongValue == null) {
                someOneFieldInTargetPOObject.set(someOneTargetPOObject, new Long(0L));
              } else {
                someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnLongValue);
              }
            } else if (-2 == someOneColumnDataType.intValue()) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (-7 == someOneColumnDataType.intValue()) {
              Boolean someOneColumnBooleanValue =
                  Boolean.valueOf(oneResultSet.getBoolean(allColumnNameArray[loopIndex]));

              if (someOneColumnBooleanValue == null) {
                someOneFieldInTargetPOObject.set(someOneTargetPOObject, new Boolean(false));
              } else {
                someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnBooleanValue);
              }
            } else if (2004 == someOneColumnDataType.intValue()) {
              Blob someOneColumnValue_Blob = oneResultSet.getBlob(allColumnNameArray[loopIndex]);

              if (someOneColumnValue_Blob == null) {
                someOneFieldInTargetPOObject.set(someOneTargetPOObject, null);
              } else {
                InputStream someOneColumnValue_InputStream =
                    someOneColumnValue_Blob.getBinaryStream();
                byte[] someOneColumnValue_ByteArray = null;
                try {
                  someOneColumnValue_ByteArray =
                      new byte[someOneColumnValue_InputStream.available()];
                } catch (IOException exception) {
                  throw new CommonDAOException(dealSQLState(exception), exception);
                }
                try {
                  someOneColumnValue_InputStream.read(someOneColumnValue_ByteArray);
                } catch (IOException exception) {
                  throw new CommonDAOException(dealSQLState(exception), exception);
                }

                someOneFieldInTargetPOObject.set(someOneTargetPOObject,
                    someOneColumnValue_ByteArray);
              }
            } else if (16 == someOneColumnDataType.intValue()) {
              Boolean someOneColumnBooleanValue =
                  Boolean.valueOf(oneResultSet.getBoolean(allColumnNameArray[loopIndex]));

              if (someOneColumnBooleanValue == null) {
                someOneFieldInTargetPOObject.set(someOneTargetPOObject, new Boolean(false));
              } else {
                someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnBooleanValue);
              }
            } else if (1 == someOneColumnDataType.intValue()) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (2005 == someOneColumnDataType.intValue()) {
              Clob someOneColumnValue = oneResultSet.getClob(allColumnNameArray[loopIndex]);

              if (someOneColumnValue == null) {
                someOneFieldInTargetPOObject.set(someOneTargetPOObject, null);
              } else {
                Reader someOneColumnValue_Reader = someOneColumnValue.getCharacterStream();
                char[] someOneColumnValue_ByteArray = new char[2000];
                try {
                  someOneColumnValue_Reader.read(someOneColumnValue_ByteArray);
                } catch (IOException e) {
                  e.printStackTrace();
                }
                String someOneColumnValue_String = new String(someOneColumnValue_ByteArray);

                someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue_String);
              }
            } else if (70 == someOneColumnDataType.intValue()) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (91 == someOneColumnDataType.intValue()) {
              Date someOneColumnValue = oneResultSet.getDate(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (3 == someOneColumnDataType.intValue()) {
              BigDecimal someOneColumnBigDecimalValue =
                  oneResultSet.getBigDecimal(allColumnNameArray[loopIndex]);

              if (someOneColumnBigDecimalValue == null) {
                someOneFieldInTargetPOObject.set(someOneTargetPOObject, new BigDecimal(0.0D));
              } else {
                someOneFieldInTargetPOObject.set(someOneTargetPOObject,
                    someOneColumnBigDecimalValue);
              }
            } else if (2001 == someOneColumnDataType.intValue()) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (8 == someOneColumnDataType.intValue()) {
              if (currentDataBaseType == 3) {
                Float someOneColumnFloatValue =
                    Float.valueOf(oneResultSet.getFloat(allColumnNameArray[loopIndex]));

                if (someOneColumnFloatValue == null) {
                  someOneFieldInTargetPOObject.set(someOneTargetPOObject, new Float(0.0D));
                } else {
                  someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnFloatValue);
                }
              } else {
                Double someOneColumnDoubleValue =
                    Double.valueOf(oneResultSet.getDouble(allColumnNameArray[loopIndex]));

                if (someOneColumnDoubleValue == null) {
                  someOneFieldInTargetPOObject.set(someOneTargetPOObject, new Double(0.0D));
                } else {
                  someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnDoubleValue);
                }
              }
            } else if (6 == someOneColumnDataType.intValue()) {
              Float someOneColumnFloatValue =
                  Float.valueOf(oneResultSet.getFloat(allColumnNameArray[loopIndex]));

              if (someOneColumnFloatValue == null) {
                someOneFieldInTargetPOObject.set(someOneTargetPOObject, new Float(0.0D));
              } else {
                someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnFloatValue);
              }
            } else if (4 == someOneColumnDataType.intValue()) {
              if (currentDataBaseType == 2) {
                BigDecimal someOneColumnBigDecimalValue =
                    oneResultSet.getBigDecimal(allColumnNameArray[loopIndex]);

                if (someOneColumnBigDecimalValue == null) {
                  someOneFieldInTargetPOObject.set(someOneTargetPOObject, new BigDecimal(0.0D));
                } else {
                  someOneFieldInTargetPOObject.set(someOneTargetPOObject,
                      someOneColumnBigDecimalValue);
                }

              } else {
                Integer someOneColumnIntegerValue =
                    Integer.valueOf(oneResultSet.getInt(allColumnNameArray[loopIndex]));

                if (someOneColumnIntegerValue == null) {
                  someOneFieldInTargetPOObject.set(someOneTargetPOObject, new Integer(0));
                } else {
                  someOneFieldInTargetPOObject
                      .set(someOneTargetPOObject, someOneColumnIntegerValue);
                }
              }
            } else if (2000 == someOneColumnDataType.intValue()) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (-16 == someOneColumnDataType.intValue()) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (-4 == someOneColumnDataType.intValue()) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (-1 == someOneColumnDataType.intValue()) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (-15 == someOneColumnDataType.intValue()) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (2011 == someOneColumnDataType.intValue()) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (someOneColumnDataType.intValue() == 0) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (2 == someOneColumnDataType.intValue()) {
              BigDecimal someOneColumnBigDecimalValue =
                  oneResultSet.getBigDecimal(allColumnNameArray[loopIndex]);

              if (someOneColumnBigDecimalValue == null) {
                someOneFieldInTargetPOObject.set(someOneTargetPOObject, new BigDecimal(0.0D));
              } else {
                someOneFieldInTargetPOObject.set(someOneTargetPOObject,
                    someOneColumnBigDecimalValue);
              }
            } else if (-9 == someOneColumnDataType.intValue()) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (1111 == someOneColumnDataType.intValue()) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (7 == someOneColumnDataType.intValue()) {
              Float someOneColumnFloatValue =
                  Float.valueOf(oneResultSet.getFloat(allColumnNameArray[loopIndex]));

              if (someOneColumnFloatValue == null) {
                someOneFieldInTargetPOObject.set(someOneTargetPOObject, new Float(0.0D));
              } else {
                someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnFloatValue);
              }
            } else if (2006 == someOneColumnDataType.intValue()) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (-8 == someOneColumnDataType.intValue()) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (5 == someOneColumnDataType.intValue()) {
              Short someOneColumnShortValue =
                  Short.valueOf(oneResultSet.getShort(allColumnNameArray[loopIndex]));

              if (someOneColumnShortValue == null) {
                short defaultValue = 0;
                someOneFieldInTargetPOObject.set(someOneTargetPOObject, new Short(defaultValue));
              } else {
                someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnShortValue);
              }
            } else if (2009 == someOneColumnDataType.intValue()) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (2002 == someOneColumnDataType.intValue()) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (2002 == someOneColumnDataType.intValue()) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (92 == someOneColumnDataType.intValue()) {
              Time someOneColumnValue = oneResultSet.getTime(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (93 == someOneColumnDataType.intValue()) {
              Timestamp someOneColumnValue =
                  oneResultSet.getTimestamp(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (-6 == someOneColumnDataType.intValue()) {
              Byte someOneColumnByteValue =
                  Byte.valueOf(oneResultSet.getByte(allColumnNameArray[loopIndex]));

              if (someOneColumnByteValue == null) {
                byte defaultValue = 0;
                someOneFieldInTargetPOObject.set(someOneTargetPOObject, new Byte(defaultValue));
              } else {
                someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnByteValue);
              }
            } else if (-3 == someOneColumnDataType.intValue()) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else if (12 == someOneColumnDataType.intValue()) {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            } else {
              Object someOneColumnValue = oneResultSet.getObject(allColumnNameArray[loopIndex]);

              someOneFieldInTargetPOObject.set(someOneTargetPOObject, someOneColumnValue);
            }
          }

        }

        returnAllPOObjectResultList.add(someOneTargetPOObject);
      }
    } catch (SQLException exception) {
      throw new CommonDAOException(dealSQLState(exception), exception);
    } catch (InstantiationException exception) {
      throw new CommonDAOException(dealSQLState(exception), exception);
    } catch (IllegalAccessException exception) {
      throw new CommonDAOException(dealSQLState(exception), exception);
    } catch (NoSuchFieldException exception) {
      throw new CommonDAOException(dealSQLState(exception), exception);
    }

    return returnAllPOObjectResultList;
  }

  public static void setPreparedStatementParameter(Integer someOneColumnDataType,
      Object someOneColumnValue, PreparedStatement preparedStatement, int parameterIndex,
      int currentDataBaseType) throws CommonDAOException {
    if (2004 == someOneColumnDataType.intValue()) {
      if ((currentDataBaseType == 3) || (currentDataBaseType == 4)) {
        if (someOneColumnValue == null) {
          throw new CommonDAOException("不能对MS SQLServer数据库表中的Blob字段赋null空值。");
        }
        byte[] someOneBolbColumnValue = (byte[]) someOneColumnValue;
        Blob someOneBolbColumn_BlobValue_SerialBlob = null;
        try {
          someOneBolbColumn_BlobValue_SerialBlob = new SerialBlob(someOneBolbColumnValue);
        } catch (SerialException exception) {
          throw new CommonDAOException(dealSQLState(exception), exception);
        } catch (SQLException exception) {
          throw new CommonDAOException(dealSQLState(exception), exception);
        }

        try {
          preparedStatement.setBlob(parameterIndex, someOneBolbColumn_BlobValue_SerialBlob);
        } catch (SQLException exception) {
          throw new CommonDAOException(dealSQLState(exception), exception);
        }

      } else if (currentDataBaseType == 2) {
        byte[] someOneBolbColumnValue = (byte[]) someOneColumnValue;
        try {
          preparedStatement.setBytes(parameterIndex, someOneBolbColumnValue);
        } catch (SQLException exception) {
          throw new CommonDAOException(dealSQLState(exception), exception);
        }

      } else {
        byte[] someOneBolbColumnValue = (byte[]) someOneColumnValue;
        try {
          preparedStatement.setBytes(parameterIndex, someOneBolbColumnValue);
        } catch (SQLException exception) {
          throw new CommonDAOException(dealSQLState(exception), exception);
        }
      }
    } else
      try {
        preparedStatement.setObject(parameterIndex, someOneColumnValue);
      } catch (SQLException exception) {
        throw new CommonDAOException(dealSQLState(exception), exception);
      }
  }

  public static String dealSQLState(Exception exception) {
    StringBuffer errorExplainText = new StringBuffer();

    if ((exception instanceof SQLException)) {
      SQLException targetException = (SQLException) exception;
      String currentSQLState = targetException.getSQLState();
      int currentErrorCode = targetException.getErrorCode();
      errorExplainText.append("SQLException异常的编码：");
      errorExplainText.append(currentErrorCode);
      errorExplainText.append("，错误状态码：");
      errorExplainText.append(currentSQLState);
      errorExplainText.append("，请查询所用的JDBC驱动程序相关的技术参考文档可以了解详细的错误原因。原始的异常错误信息如下：");
    } else if ((exception instanceof IllegalArgumentException)) {
      errorExplainText.append("在调用系统中的某个方法时，传递了不合法或不正确的参数值。原始的异常错误信息如下：");
    } else if ((exception instanceof IllegalAccessException)) {
      errorExplainText.append("对不允许调用的方法进行了强制调用，如强制对类中的private类型的方法进行调用。原始的异常错误信息如下：");
    } else if ((exception instanceof ClassNotFoundException)) {
      errorExplainText
          .append("指定的JDBC驱动程序类没有找到，因此不能正确地加载目标驱动程序类，请检查在项目中是否添加了JDBC驱动程序的*.jar包文件。原始的异常错误信息如下：");
    }

    return errorExplainText.toString();
  }

  public static String dealSQLExceptionState(SQLException targetException,
      ResourceBundle resourceBundle) {
    StringBuffer errorExplainText = new StringBuffer();
    String currentSQLState = targetException.getSQLState();
    int currentErrorCode = targetException.getErrorCode();
    errorExplainText.append(resourceBundle.getString("common.sQLExceptionCode"));
    errorExplainText.append(currentErrorCode);
    errorExplainText.append(resourceBundle.getString("common.sQLStatusCode"));
    errorExplainText.append(currentSQLState);
    errorExplainText.append(resourceBundle.getString("common.sQLExceptionAdviceInfo"));
    return errorExplainText.toString();
  }

  public static String getExceptionErrorInfoText(String errorInfoTextKey) {
    ResourceBundle resourceBundle = null;

    Locale currentLocale = Locale.getDefault();

    resourceBundle =
        ResourceBundle.getBundle("com.abuye.core.jdbc.resource.ExceptionInfo", currentLocale);

    return resourceBundle.getString(errorInfoTextKey);
  }

  public static Class getJavaType(int sqlType, int columnSize, int decimalDigits) {
    Class javaDataType = null;
    if ((sqlType == 1) || (sqlType == 12)) {
      javaDataType = String.class;
    } else if ((sqlType == 6) || (sqlType == 7)) {
      javaDataType = Float.class;
    } else if (sqlType == 4) {
      javaDataType = Integer.class;
    } else if (sqlType == 8) {
      javaDataType = Double.class;
    } else if (sqlType == 91) {
      javaDataType = String.class;
    } else if (sqlType == 93) {
      javaDataType = String.class;
    } else if (sqlType == 92) {
      javaDataType = String.class;
    } else if (sqlType == 5) {
      javaDataType = Short.class;
    } else if (sqlType == -7) {
      javaDataType = Integer.class;
    } else if (sqlType == -5) {
      javaDataType = Long.class;
    } else if ((sqlType == 2) || (sqlType == 3)) {
      if (decimalDigits == 0) {
        if (columnSize == 1) {
          javaDataType = Integer.class;
        } else if (columnSize < 5) {
          javaDataType = Short.class;
        } else if (columnSize < 10) {
          javaDataType = Integer.class;
        } else {
          javaDataType = Long.class;
        }

      } else if (columnSize < 9) {
        javaDataType = Float.class;
      } else {
        javaDataType = Double.class;
      }
    }

    return javaDataType;
  }

  public static boolean checkJDBCConnectionParameterValid(String driverClassName, String dbURL,
      String loginName, String loginPassWord) throws CommonDAOException {
    if ((driverClassName == null) || (driverClassName.length() == 0)) {
      throw new CommonDAOException("没有提供有效的JDBC驱动程序类名称的字符串，请根据所使用的JDBC驱动程序的类型和版本提供有效的类名称字符串。");
    }

    if ((dbURL == null) || (dbURL.length() == 0)) {
      throw new CommonDAOException("没有提供有效的数据库的URL地址字符串，请根据所使用的数据库类型提供有效的数据库的URL地址字符串。");
    }

    if ((loginName == null) || (loginName.length() == 0)) {
      throw new CommonDAOException("没有提供有效的连接数据库的登录帐号字符串，请根据所使用的数据库类型提供有效的登录帐号字符串（咨询DBA获知）。");
    }

    if ((loginPassWord == null) || (loginPassWord.length() == 0)) {
      throw new CommonDAOException("没有提供有效的连接数据库的登录密码字符串，请根据所使用的数据库类型提供有效的登录密码字符串（咨询DBA获知）。");
    }

    return true;
  }

  public static List getCatalogs(Connection jdbcConnection) throws SQLException {
    DatabaseMetaData oneDatabaseMetaData = jdbcConnection.getMetaData();
    ResultSet oneResultSet = null;
    try {
      oneResultSet = oneDatabaseMetaData.getCatalogs();
      List oneLinkedList = new LinkedList();
      while (oneResultSet.next()) {
        oneLinkedList.add(oneResultSet.getString(1));
      }
      return oneLinkedList;
    } finally {
      if (oneResultSet != null) oneResultSet.close();
    }
  }

  public static Map getSchemas(Connection jdbcConnection) throws SQLException {
    DatabaseMetaData oneDatabaseMetaData = jdbcConnection.getMetaData();
    ResultSet oneResultSet = null;
    try {
      oneResultSet = oneDatabaseMetaData.getSchemas();
      Map map = new HashMap();

      while (oneResultSet.next()) {
        String schema = oneResultSet.getString(1);
        String catalog = null;
        if (oneResultSet.getMetaData().getColumnCount() > 1) {
          catalog = oneResultSet.getString(2);
        }
        List oneList = (List) map.get(catalog);
        if (oneList == null) {
          oneList = new LinkedList();
          map.put(catalog, oneList);
        }
        oneList.add(schema);
      }
      return map;
    } finally {
      if (oneResultSet != null) oneResultSet.close();
    }
  }

  public static List getTables(Connection c, String catalog, String schema, String tablePattern)
      throws SQLException {
    DatabaseMetaData dmd = c.getMetaData();
    ResultSet rs = null;
    try {
      rs =
          dmd.getTables(catalog, schema, tablePattern, new String[] {"TABLE", "VIEW", "SYNONYM",
              "ALIAS"});
      List l = new LinkedList();
      while (rs.next()) {
        l.add(rs.getString(3));
      }
      return l;
    } finally {
      if (rs != null) rs.close();
    }
  }

  public static Set getForeignKeyColumns(Connection c, String catalog, String schema, String table)
      throws SQLException {
    DatabaseMetaData dmd = c.getMetaData();
    ResultSet rs = null;
    try {
      rs = dmd.getImportedKeys(catalog, schema, table);
      HashSet columns = new HashSet();
      while (rs.next()) {
        columns.add(rs.getString(8));
      }
      return columns;
    } finally {
      if (rs != null) rs.close();
    }
  }
}
