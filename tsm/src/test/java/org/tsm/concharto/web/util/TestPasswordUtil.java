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
