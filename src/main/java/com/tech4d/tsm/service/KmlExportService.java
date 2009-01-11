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
package com.tech4d.tsm.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.tech4d.tsm.audit.AuditInterceptor;
import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.util.KmlFormat;

/**
 * Periodically writes all Concharto events into KML file(s)
 */
public class KmlExportService extends QuartzJobBean {
    private static final String CHARACTER_ENCODING = "UTF-8";
	private final Log log = LogFactory.getLog(getClass());
	private EventDao eventDao;
	private EventSearchService eventSearchService;
	private AuditInterceptor auditInterceptor;
    private SessionFactory sessionFactory;
	private String kmlFileName;

	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}
	public void setEventSearchService(EventSearchService eventSearchService) {
		this.eventSearchService = eventSearchService;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public void setAuditInterceptor(AuditInterceptor auditInterceptor) {
		this.auditInterceptor = auditInterceptor;
	}
	public void setKmlFileName(String kmlFileName) {
		this.kmlFileName = kmlFileName;
	}


	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		setupTransactionFactory();
		
		Integer total = eventDao.getTotalCount();
		log.debug("running export task for " + total + " events");
		//TODO FIXME!! 5-4-08
		//TODO we need to split this up later! otherwise we will run out of ram
		List<Event> events = eventSearchService.search(2000, 0, null, 
				new SearchParams(null, null, Visibility.NORMAL, true, null, null));
		log.info("retrieved " + total + " events");

		FileOutputStream fileOutputStream = null;
		try {
			//TODO FIXME hack..
			fileOutputStream = new FileOutputStream("../webapps/ROOT/" + kmlFileName);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, CHARACTER_ENCODING);
			KmlFormat.toKML(events, outputStreamWriter);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (UnsupportedEncodingException e) {
			log.error(e);
		} finally {
			try {
				fileOutputStream.close();
			} catch (IOException e) {
				log.error(e);
			}
		}
		log.info("wrote kml for " + events.size() + " events");
		
		postProcessTransactionFactory();
	}
	
	public void setupTransactionFactory() {
		Session session = SessionFactoryUtils.getSession(
				sessionFactory, auditInterceptor, null);
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
    }
    
    public void postProcessTransactionFactory() {
    	// single session mode
		SessionHolder sessionHolder =
				(SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
		SessionFactoryUtils.closeSession(sessionHolder.getSession()); 	
    }

}
