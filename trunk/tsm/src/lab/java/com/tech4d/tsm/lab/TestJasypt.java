package com.tech4d.tsm.lab;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.junit.Test;

public class TestJasypt {

    @Test public void testEncrypy() {
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword("cat");
        System.out.println(encryptedPassword);
    }
}
