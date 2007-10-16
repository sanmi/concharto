package com.tech4d.tsm.web.util;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import com.tech4d.tsm.util.PasswordUtil;

public class TestPasswordUtil {

    @Test 
    public void encrypt() throws NoSuchAlgorithmException {
        String pwd = "somepassword";
        String hashed = PasswordUtil.encrypt(pwd);
        assertTrue(!pwd.equals(hashed));
        assertEquals(hashed, PasswordUtil.encrypt(pwd));
    }
}
