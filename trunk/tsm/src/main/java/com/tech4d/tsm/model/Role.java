package com.tech4d.tsm.model;

import javax.persistence.Entity;

@Entity
public class Role extends BaseAuditableEntity {
    private String name;

    public Role() {
    }
    public Role(String role) {
        this.name = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String role) {
        this.name = role;
    }
}
