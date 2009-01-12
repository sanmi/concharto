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
package org.tsm.concharto;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.tsm.concharto.audit.AuditInterceptor;
import org.tsm.concharto.util.ContextUtil;


/**
 * Implements the openSessionInView paradigm for integration tests.
 * 
 * Only extend this class if your unit test has to inspect objects that are lazy loaded. 
 * For example if you get an event using eventDao.find(23) and then call event.getFlags() 
 * you are getting collection of Flag objects that weren't loaded when eventDao.find was 
 * called because they are lazy loaded.  The session must be active.  If you don't
 * use this openSessionInView paradigm, your test will get a LazyLoaded exception.
 * 
 * Note that extending this class may require you to do more work in the test if 
 * you are creating an object, then modifying it, then comparing it with some
 * later object of the same id.  You will have to evict the earlier object.
 * TODO put an example here.  For now, look at IntegrationTestAuditEntry.
 *  
 * @see org.springframework.orm.hibernate3.support.OpenSessionInViewInterceptor
 * @see org.tsm.concharto.audit.IntegrationTestAuditEntry
 *  
 * @author frank
 *
 */
public class OpenSessionInViewIntegrationTest {
    private static SessionFactory sessionFactory;

    
    public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void setSessionFactory(SessionFactory sessionFactory) {
		OpenSessionInViewIntegrationTest.sessionFactory = sessionFactory;
	}

	@Before public void setupTransactionFactory() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        if (null == sessionFactory) {
            sessionFactory = (SessionFactory)  appCtx.getBean("mySessionFactory");
        }
        AuditInterceptor auditInterceptor = (AuditInterceptor) appCtx.getBean("auditInterceptor");
		Session session = SessionFactoryUtils.getSession(
				sessionFactory, auditInterceptor, null);
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
    }
    
    @After
    public void postProcessTransactionFactory() {
    	// single session mode
		SessionHolder sessionHolder =
				(SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
		SessionFactoryUtils.closeSession(sessionHolder.getSession()); 	
    }
}
