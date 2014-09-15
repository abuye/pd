package com.abuye.core.db.parser;

import java.util.List;

public class ConditionItem {
    String logic;
    String field;
    String operator;
    List items;
    public String getLogic() {
        return logic;
    }
    public void setLogic(String logic) {
        this.logic = logic;
    }
    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
    public List getItems() {
        return items;
    }
    public void setItems(List items) {
        this.items = items;
    }
    
}
