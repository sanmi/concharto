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
package org.tsm.concharto.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ContextUtil {
    static private ClassPathXmlApplicationContext ctx = null;
    
    protected static final String[] XML_CONFIG_FILES = new String[]{
        "/tsm-integrationtest-datasource.xml",
        "/tsm-integrationtest-dao.xml",
        "/tsm-application.xml",
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
