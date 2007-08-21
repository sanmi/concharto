package com.tech4d.tsm.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ContextUtil {
    protected static final String[] XML_CONFIG_FILES = new String[]{
        "tsm-dao.xml",
        "tsm-datasource.xml"
    };

    /**
     * Get the {@link ApplicationContext}.
     * @return applicationContext {@link ApplicationContext} initialized by the current <var>xmlContextFiles</var>
     */
    public static ApplicationContext getCtx() {
        return new ClassPathXmlApplicationContext(XML_CONFIG_FILES);
        
    }
}
