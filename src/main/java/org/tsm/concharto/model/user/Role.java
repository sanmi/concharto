/*******************************************************************************
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tsm.concharto.model.user;

import javax.persistence.Entity;

import org.tsm.concharto.model.BaseAuditableEntity;


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
