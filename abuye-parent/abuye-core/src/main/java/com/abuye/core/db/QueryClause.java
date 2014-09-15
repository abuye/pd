package com.abuye.core.db;

import java.util.List;

// TODO api
/**
 * query.like("name","%aa%")
  .pl().eq("gender", "1").or().eq("gender", "0").pr()
  .end()
 * @author chandler
 *
 */
public class QueryClause {
  Logic logic = Logic.AND;
  String property;
  String column;
  Object value;
  Op operator;
  boolean multi = false;
  List<QueryClause> items;

  public enum Logic {
    AND, OR
  }

  public enum Op {
    // TODO 更多类型
    EQ("eq"), LIKE("like"), GT("gt"), LT("lt"), GTE("gte"), LTE("lte");
    private String value;

    private Op(String value) {
      this.value = value;
    }

    public String v() {
      return this.value;
    }

    public static Op n(String value) {
      if (EQ.v().equals(value)) {
        return EQ;
      }
      if (LIKE.v().equals(value)) {
        return LIKE;
      }
      if (GT.v().equals(value)) {
        return GT;
      }
      if (LT.v().equals(value)) {
        return LT;
      }
      if (GTE.v().equals(value)) {
        return GTE;
      }
      if (LTE.v().equals(value)) {
        return LTE;
      }
      return null;
    }
  }
//
//  public static QueryItem n(String property, String value) {
//    QueryItem r = new QueryItem();
//    r.property = property;
//    r.value = value;
//    r.operator = Op.LIKE;
//    return r;
//  }
//
//  public static QueryItem n(String property, String operatorString, Object value) {
//    QueryItem r = new QueryItem();
//    r.property = property;
//    r.value = value;
//    r.operator = Op.n(operatorString);
//    return r;
//  }
//
//  public static QueryItem n(String property, Op operator, Object value) {
//    QueryItem r = new QueryItem();
//    r.property = property;
//    r.value = value;
//    r.operator = operator;
//    return r;
//  }
//
//  // /**
//  // * 自定义查询条件时可先省略，不写column
//  // *
//  // * @param operator
//  // * @param value
//  // * @return
//  // */
//  // public static QueryItem n(Op operator, Object value) {
//  // QueryItem r = new QueryItem();
//  // r.value = value;
//  // r.operator = operator;
//  // return r;
//  // }
//
//  public static QueryItem n(String column, Op operator) {
//    QueryItem r = new QueryItem();
//    r.property = column;
//    r.operator = operator;
//    return r;
//  }
//
//
//  public QueryItem column(String column) {
//    this.column = column;
//    return this;
//  }
//
//
//  public QueryItem logic(Logic logic) {
//    this.logic = logic;
//    return this;
//  }
//
//  public QueryItem operator(Op operator) {
//    this.operator = operator;
//    return this;
//  }
//
//  public QueryItem items(List<QueryItem> items) {
//    this.items = items;
//    return this;
//  }

  /**************************************************************/
  public Logic getLogic() {
    return logic;
  }

  public void setLogic(Logic logic) {
    this.logic = logic;
  }

  public String getProperty() {
    return property;
  }

  public void setProperty(String column) {
    this.property = column;
  }

  public Op getOperator() {
    return operator;
  }

  public void setOperator(Op operator) {
    this.operator = operator;
  }

  public boolean isMulti() {
    return multi;
  }

  public void setMulti(boolean multi) {
    this.multi = multi;
  }

  public List<QueryClause> getItems() {
    return items;
  }

  public void setItems(List<QueryClause> items) {
    this.items = items;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public String getColumn() {
    return column;
  }

  public void setColumn(String column) {
    this.column = column;
  }

}
