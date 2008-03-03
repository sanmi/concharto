package com.tech4d.tsm.lab.kml;

import java.io.FileNotFoundException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.dao.EventTesterDao;
import com.tech4d.tsm.dao.EventUtil;
import com.tech4d.tsm.lab.PopulateDummyData;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.util.ContextUtil;
import com.tech4d.tsm.util.LapTimer;
import com.vividsolutions.jts.geom.GeometryFactory;

public class Serialize {
    private static final String NAMESPACE = "timespacemap";
	private EventTesterDao eventTesterDao;
    private EventUtil eventUtil;
    private GeometryFactory gf = new GeometryFactory();
    protected static final Log logger = LogFactory.getLog(Serialize.class);
    private EventDao eventDao;

    @Before
    public void setUp() throws FileNotFoundException {
        ApplicationContext appCtx = ContextUtil.getCtx();
        
        SessionFactory sessionFactory = (SessionFactory) appCtx.getBean("myTestDbSessionFactory");
        eventTesterDao = (EventTesterDao) appCtx.getBean("eventTesterDao");
        eventDao = (EventDao) appCtx.getBean("eventDao");
        //replace the default with the test db
        eventTesterDao.setSessionFactory(sessionFactory);

        eventUtil = new EventUtil(eventTesterDao.getSessionFactory());        
    }
    
    
    @Test
    public void doSerialize() {

    	List<Event> events = eventTesterDao.findRecent(20,0);
    	System.out.println(events.size() + "======================");
    	
    	for(Event event : events) {
    		serialize(event);
    	}
    }


	private void serialize(Event event) {
		StringBuffer sb = new StringBuffer();
		//<Placemark id="timespacemap:1">
		sb.append("<Placemark id=\"").append(NAMESPACE).append(":").append(event.getId())
		  .append("\">");
		
		/*
		<Metadata>
	        <permalink>
	          <href>http://platial.com/post/1266119</href>
	        </permalink>
	        <author>
	          <name>thaonguyen</name>
	          <href>http://platial.com/thaonguyen</href>
	        </author>
	    </Metadata>		 
		 */
		//TODO
		//<name>Fort Myers, FL (RSW)</name>
		sb.append("<name>").append(event.getSummary()).append("</name>");
		//<Snippet maxLines="3">Thaonguyen says: Me/Mary/Olivia/Vanessa/Justin</Snippet>
//		sb.append("<snippet maxlines=\"3\">").append(event.getDescription()).append("</snippet>");
		sb.append("<description><![CDATA[")
			.append(event.getDescription())
			.append("]]></description>");
		sb.append("<visibility>1</visibility>");

		/*
	      <LookAt>
	       <longitude>-81.756073</longitude>
	       <latitude>26.541941</latitude>
	       <range>1500.0</range>
	       <tilt>0.0</tilt>
	       <heading>0.0</heading>
	      </LookAt>
	     */
		sb.append("<LookAt><longitude>")
		.append(event.getTsGeometry().getGeometry().getCentroid().getX())
		.append("</longitude")
		.append("<latitude>")
		.append(event.getTsGeometry().getGeometry().getCentroid().getX())
		.append("</latitude>")
		.append("</LookAt>");
		
		sb.append("</Placemark>");
		
		System.out.println(sb.toString());
		
		
	}

}
