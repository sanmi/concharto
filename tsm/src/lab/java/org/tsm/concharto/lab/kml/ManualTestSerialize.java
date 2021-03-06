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
package org.tsm.concharto.lab.kml;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.tsm.concharto.OpenSessionInViewIntegrationTest;
import org.tsm.concharto.dao.EventTesterDao;
import org.tsm.concharto.model.Event;
import org.tsm.concharto.util.ContextUtil;
import org.tsm.concharto.util.KmlFormat;


public class ManualTestSerialize extends OpenSessionInViewIntegrationTest {
	private static EventTesterDao eventTesterDao;
    protected static final Log logger = LogFactory.getLog(ManualTestSerialize.class);
	

    @BeforeClass
    public static void setUp() {

        ApplicationContext appCtx = ContextUtil.getCtx();       
        SessionFactory sessionFactory = (SessionFactory) appCtx.getBean("myTestDbSessionFactory");
        //different session factory - the test context!!
        setSessionFactory(sessionFactory);
        
        eventTesterDao = (EventTesterDao) appCtx.getBean("eventTesterDao");
        //replace the default with the test db
        eventTesterDao.setSessionFactory(sessionFactory);
    }
  
    @Test
    public void doSerialize() throws ParserConfigurationException {

    	List<Event> events = eventTesterDao.findRecent(20,0);
    	System.out.println(events.size() + "======================");
		//serializeToKML(events);
    	
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream("tmp3.kml");
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
			long start = System.currentTimeMillis();
			KmlFormat.toKML(events, outputStreamWriter, "recent events from concharto.com", "Recent events from concharto!");
			logger.debug("time: " + (System.currentTimeMillis() - start));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		try {
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
