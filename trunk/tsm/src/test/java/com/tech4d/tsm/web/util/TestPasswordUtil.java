package com.tech4d.tsm.web.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.tech4d.tsm.util.PasswordUtil;

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
