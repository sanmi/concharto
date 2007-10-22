package com.tech4d.tsm.web.util;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import com.tech4d.tsm.util.PasswordUtil;

public class TestPasswordUtil {

    @Test 
    public void encrypt() throws NoSuchAlgorithmException {
        String pwd = "cat";
        String hashed = PasswordUtil.encrypt(pwd);
        System.out.println("hashed = " + hashed);
        assertTrue(!pwd.equals(hashed));
        assertEquals(hashed, PasswordUtil.encrypt(pwd));
    }
}
