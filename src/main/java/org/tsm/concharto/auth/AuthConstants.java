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
package org.tsm.concharto.auth;

public class AuthConstants {
    public static final String SESSION_AUTH_USERNAME = "username";
    public static final String SESSION_AUTH_TARGET_URI = "targetURI";
    public static final String SESSION_AUTH_ROLES = "roles";
    public static final String[] PATTERN_REQUIRES_AUTHENTICATION = 
    	{"admin","member","private"};
    //TODO this is simplistic, probably want some sort of map or properties file
    public static final String ROLE_EDIT = "edit";
    public static final String ROLE_ADMIN = "admin";
    public static final String[] PATTERN_REQUIRES_AUTHORIZATION = {ROLE_EDIT,ROLE_ADMIN};
}
