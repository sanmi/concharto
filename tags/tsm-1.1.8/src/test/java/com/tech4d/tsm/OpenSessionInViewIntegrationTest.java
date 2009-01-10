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
package com.tech4d.tsm;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.tech4d.tsm.audit.AuditInterceptor;
import com.tech4d.tsm.util.ContextUtil;

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
 * @see com.tech4d.tsm.audit.IntegrationTestAuditEntry
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
