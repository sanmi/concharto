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
package org.tsm.concharto.dao;

import java.io.Serializable;
import java.util.List;

import org.tsm.concharto.model.user.Role;
import org.tsm.concharto.model.user.User;


public interface UserDao {
    public Serializable save(Object obj);
    public User find(Long id);
    public User find(String username);
    public List<Role> getRolesForUser(Long id);
    public void delete(User user);
    public void delete(Long id);
    public List<Role> getRoles();
    public Role getRole(String role);
	public User getUserFromPasswordRetrievalKey(String retrievalKey);
	public User getUserFromRememberMeKey(String value);
}
