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
package org.tsm.concharto.web.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.tsm.concharto.util.PasswordUtil;


public class TestPasswordUtil {

    @Test 
    public void encrypt()  {
        String pwd = "cat";
        String hashed = PasswordUtil.encrypt(pwd);
        assertTrue(!pwd.equals(hashed));
        assertTrue(PasswordUtil.isPasswordValid(pwd, hashed));
        assertTrue(PasswordUtil.isPasswordValid(pwd, "/aPky1NqzLtdu/+W+x/Mr9gQTiUO0SeT"));
    }
    
    @Test public void handleNulls() {
        //trolling for an exception
        PasswordUtil.encrypt(null);
        assertTrue(!PasswordUtil.isPasswordValid("Cat", null));
        assertTrue(!PasswordUtil.isPasswordValid(null, "dog"));
    }
}
