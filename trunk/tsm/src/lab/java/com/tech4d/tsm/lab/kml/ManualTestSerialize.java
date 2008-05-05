package com.tech4d.tsm.lab.kml;

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

import com.tech4d.tsm.OpenSessionInViewIntegrationTest;
import com.tech4d.tsm.dao.EventTesterDao;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.util.ContextUtil;
import com.tech4d.tsm.util.KmlFormat;

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
			KmlFormat.toKML(events, outputStreamWriter, "recent events from concharto.com", "Recent events from concharto!");
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
