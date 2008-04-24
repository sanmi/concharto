package com.tech4d.tsm.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.util.KmlFormat;

/**
 * Periodically writes all Concharto events into KML file(s)
 */
public class KmlExportService extends TimerTask {
    private static final String CHARACTER_ENCODING = "UTF-8";
	private final Log log = LogFactory.getLog(getClass());
	private EventDao eventDao;
	private EventSearchService eventSearchService;
	private String kmlFileName;
	
	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}
	public void setEventSearchService(EventSearchService eventSearchService) {
		this.eventSearchService = eventSearchService;
	}
	public void setKmlFileName(String kmlFileName) {
		this.kmlFileName = kmlFileName;
	}

	@Override
	public void run() {
		Integer total = eventDao.getTotalCount();
		log.debug("running export task for " + total + " events");
		//TODO we need to split this up later! otherwise we will run out of ram
//		List<Event> events = eventSearchService.search(total, 0, null, 
//				new SearchParams(null, null, Visibility.NORMAL, true));
		List<Event> events = eventDao.findRecent(10, 0);
		log.debug("retrieved " + total + " events");
		System.out.println("sdfsdfsdf --------------------------------------");

		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(kmlFileName);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, CHARACTER_ENCODING);
			KmlFormat.toKML(events, outputStreamWriter);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (UnsupportedEncodingException e) {
			log.error(e);
		} 
		log.debug("wrote kml for " + total + " events");
	
	}

}
