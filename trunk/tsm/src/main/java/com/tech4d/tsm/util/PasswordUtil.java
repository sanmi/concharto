package com.tech4d.tsm.util;

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
