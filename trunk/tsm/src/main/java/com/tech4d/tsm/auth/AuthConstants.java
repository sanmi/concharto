/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version 
 * 1.1 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 * 
 * The Original Code is Concharto.
 * 
 * The Initial Developer of the Original Code is
 * Time Space Map, LLC
 * Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 * the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s):
 * Time Space Map, LLC
 * 
 * ***** END LICENSE BLOCK *****
 ******************************************************************************/
package com.tech4d.tsm.auth;

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
