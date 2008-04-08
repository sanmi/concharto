package com.tech4d.tsm.model.user;

import javax.persistence.Entity;

import com.tech4d.tsm.model.BaseAuditableEntity;

@Entity
public class Role extends BaseAuditableEntity {
	public static final Role ROLE_EDIT = new Role("edit");
	public static final Role ROLE_ANONYMOUS = new Role("anonymous");
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
