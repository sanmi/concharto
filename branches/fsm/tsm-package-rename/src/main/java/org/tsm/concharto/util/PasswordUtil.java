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
package org.tsm.concharto.util;

import org.jasypt.util.password.BasicPasswordEncryptor;

/**
 * Utility for one way encryption of passwords
 * 
 * @author frank
 */
public class PasswordUtil {

    public static String encrypt(String unencryptedPassword)    
    {
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        return passwordEncryptor.encryptPassword(unencryptedPassword);
    }

    public static boolean isPasswordValid(String checkPassword, String actualPassword) {
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();

        return passwordEncryptor.checkPassword(checkPassword, actualPassword); 
    }

}
