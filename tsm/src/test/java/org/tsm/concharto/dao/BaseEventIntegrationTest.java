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
package org.tsm.concharto.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.tsm.concharto.OpenSessionInViewIntegrationTest;
import org.tsm.concharto.auth.ThreadLocalUserContext;
import org.tsm.concharto.auth.UserContext;
import org.tsm.concharto.util.ContextUtil;

/**
 * Base class for integration tests that use events as a means of testing 
 */
@Transactional
public class BaseEventIntegrationTest extends OpenSessionInViewIntegrationTest{

    private static EventDao eventDao;
    private static EventTesterDao eventTesterDao;
    private static EventUtil eventUtil;

    public static void baseSetUpClass() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        eventDao = (EventDao) appCtx.getBean("eventDao");
        eventTesterDao = (EventTesterDao) appCtx.getBean("eventTesterDao");
        eventUtil = new EventUtil(eventTesterDao.getSessionFactory());
        eventTesterDao.deleteAll();
        StyleUtil.setupStyle();
    }

    protected void setupUserContext(String username) {
        UserContext userContext = new UserContext();
        userContext.setUsername(username);
        ThreadLocalUserContext.setUserContext(userContext);
    }
    
    protected static EventDao getEventDao() {
        return eventDao;
    }

    protected static EventTesterDao getEventTesterDao() {
        return eventTesterDao;
    }

    protected static EventUtil getEventUtil() {
        return eventUtil;
    }

}
