package com.tech4d.tsm.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ContextUtil {
    static private ClassPathXmlApplicationContext ctx = null;
    
    protected static final String[] XML_CONFIG_FILES = new String[]{
        "/tsm-integrationtest-datasource.xml",
        "/tsm-dao.xml"
    };

    /**
     * Get the {@link ApplicationContext}.
     * @return applicationContext {@link ApplicationContext} initialized by the current <var>xmlContextFiles</var>
     */
    public static ApplicationContext getCtx() {
        if (ctx == null) {
            ctx = new ClassPathXmlApplicationContext(XML_CONFIG_FILES);
        }
        return ctx; 
        
    }
}
