package com.tech4d.tsm.util;

import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;

import sun.misc.BASE64Encoder;

/**
 * Utility for one way encryption of passwords
 * 
 * @author frank
 */
public class PasswordUtil {

    public static String encrypt(String x) throws NoSuchAlgorithmException   
    {
       java.security.MessageDigest digester;
       digester = java.security.MessageDigest.getInstance("SHA-1");
       digester.reset();
       digester.update(x.getBytes());
       String secret = new String(digester.digest());
       
       //Now encode it as text.  This makes things easier for the administration via sql
       BASE64Encoder encoder = new BASE64Encoder();
       
       return encoder.encode(secret.getBytes());
    }

    public static boolean isPasswordValid(Log log, String checkPassword, String actualPassword) {
        String hashedPassword = null;
        try {
            hashedPassword = PasswordUtil.encrypt(checkPassword);            
        } catch (NoSuchAlgorithmException e) {
            log.error("Exception while encrypting: " + e);
        }
        return !((hashedPassword == null) || (actualPassword == null) ||
                !actualPassword.equals(hashedPassword));

    }

}
