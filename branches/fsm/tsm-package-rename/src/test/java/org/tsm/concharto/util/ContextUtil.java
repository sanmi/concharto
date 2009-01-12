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
