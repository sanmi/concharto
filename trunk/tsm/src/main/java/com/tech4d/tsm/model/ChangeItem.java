package com.tech4d.tsm.model;

import javax.persistence.Entity;

@Entity
public class ChangeItem extends BaseEntity {
    String fieldName;
    String diff;

    public ChangeItem(String fieldName, String diff) {
        super();
        this.fieldName = fieldName;
        this.diff = diff;
    }

    public ChangeItem() {
        super();
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    

}
