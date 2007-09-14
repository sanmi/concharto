package com.tech4d.tsm.model;

import javax.persistence.Entity;

@Entity
public class UserTag extends BaseEntity {
    String tag;

    public UserTag() {
        super();
    }

    public UserTag(String tag) {
        super();
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return this.tag;
    }

}
