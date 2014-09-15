package com.abuye.core.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.abuye.core.db.QueryClause.Logic;
import com.abuye.core.db.QueryClause.Op;
import com.abuye.core.utils.Constants;

public class QueryBuilder {
  private List<QueryClause> list = new ArrayList<QueryClause>();
  private Stack<List<QueryClause>> stack = new Stack<List<QueryClause>>();

  public static QueryBuilder start() {
    return new QueryBuilder();
  }

  public List<QueryClause> end() {
    return list;
  }

  public QueryBuilder left() {
    return left(Logic.AND);
  }

  public QueryBuilder left(Logic logic) {
    QueryClause q = new QueryClause();
    q.setLogic(logic);
    q.setMulti(true);
    q.setItems(list);
    list.add(q);
    stack.add(list);
    list = new ArrayList<QueryClause>();
    return this;
  }

  public QueryBuilder right() {
    List<QueryClause> parent = stack.pop();
    QueryClause q = parent.get(parent.size() - 1);
    q.setItems(list);
    list = parent;
    return this;
  }

  public QueryBuilder and(String columnName, Op operator, Object value) {
    QueryClause q = new QueryClause();
    q.setOperator(operator);
    q.setColumn(columnName);
    q.setValue(value);
    list.add(q);
    return this;
  }

  public QueryBuilder or(String columnName, Op operator, Object value) {
    QueryClause q = new QueryClause();
    q.setLogic(Logic.OR);
    q.setOperator(operator);
    q.setColumn(columnName);
    q.setValue(value);
    list.add(q);
    return this;
  }

  public QueryBuilder like(String columnName, Object value) {
    return and(columnName, Op.LIKE, value);
  }


  public static String getSql(List<QueryClause> queryClauses, List sqlParams) throws Exception {
    String sql = "";
    boolean isFirst = true;
    for (QueryClause p : queryClauses) {
      // TODO 括号处理
      if (isFirst) {
        isFirst = false;
      } else {
        sql += Constants.BLANK + logicToSql(p.getLogic());
      }
      sql += Constants.BLANK + p.getColumn() + operatorToSql(p.getOperator());
      sqlParams.add(p.getValue());
    }
    return sql;
  }

  public static String operatorToSql(Op operator) throws Exception {
    switch (operator) {
      case EQ:
        return " = ?";
      case LIKE:
        return " like ?";
      case GT:
        return " > ?";
      case LT:
        return " < ?";
      case GTE:
        return " >= ?";
      case LTE:
        return " <= ?";
        // TODO
      default:
        throw new Exception("Wrong operator.");
    }
  }

  public static String logicToSql(Logic logic) throws Exception {
    switch (logic) {
      case AND:
        return "and";
      case OR:
        return "or";

      default:
        throw new Exception("Wrong logic.");
    }
  }
}
