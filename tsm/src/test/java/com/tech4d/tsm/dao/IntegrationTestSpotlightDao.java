package com.tech4d.tsm.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.tech4d.tsm.model.Spotlight;
import com.tech4d.tsm.model.user.User;
import com.tech4d.tsm.util.ContextUtil;

public class IntegrationTestSpotlightDao {
	private static final String LABLE = "Path of the  at [Kensington palace] ";
	private static final String LINK = "http://www.map4d.com/search/eventsearch.htm?_what=treasure&_zoom=8&_ll=26.667096,-80.760498&_maptype=0";
	private static SpotlightDao spotlightDao;
	private static User user;
	private static EventTesterDao eventTesterDao;
	private static UserDao userDao;

	/*
	 * x public Serializable save(Spotlight spotlight); public void
	 * x delete(Spotlight spotlight); 
	 * x public void delete(Long id); 
	 * x public Spotlight find(Long id); 
	 * public Spotlight getNextAfter(Long id); 
	 * x public List<Spotlight> find(int maxResults, int firstResult); 
	 * x public List<Spotlight> findAll();
	 * 
	 */

	@BeforeClass
	public static void init() {
		ApplicationContext appCtx = ContextUtil.getCtx();
		spotlightDao = (SpotlightDao) appCtx.getBean("spotlightDao");
		eventTesterDao = (EventTesterDao) appCtx.getBean("eventTesterDao");
		userDao = (UserDao) appCtx.getBean("userDao");
		eventTesterDao.deleteAll();
		user = new User("marina", "password", "sanmidude@noplace");
		userDao.save(user);
	}

	@Test
	public void saveGetDelete() {
		Spotlight spotlight = makeSpotlight();
		Long id1 = (Long) spotlightDao.save(spotlight);
		Long id2 = (Long) spotlightDao.save(spotlight);
		Spotlight returned = spotlightDao.find(id2);
		assertEquals(LINK, returned.getLink());
		
		spotlightDao.delete(id1);
		spotlightDao.delete(returned);
		assertEquals(null, spotlightDao.find(id1));
		assertEquals(null, spotlightDao.find(id2));
		
	}

	@Test
	public void find() {
		int numVisible = 10;
		int numInvisible = 3;
		int total = numInvisible + numVisible;
		List<Long> visible = makeSpotlights(numVisible-1, true);
		makeSpotlights(numInvisible, false);
		//make a gap in the id's
		List<Long> visible2 = makeSpotlights(1, true);
		
		
		assertEquals(total, spotlightDao.findAll().size());
		assertEquals(5, spotlightDao.find(5, 0).size());
		assertEquals(5, spotlightDao.find(5, 5).size());
		assertEquals(4, spotlightDao.find(5, total-4).size());
		
		assertEquals(new Integer(numVisible), spotlightDao.getTotalVisible());
		assertEquals(numVisible, spotlightDao.findVisible().size());
		
		for (int i=0; i<numVisible-1; i++) {
			assertEquals(visible.get(i), spotlightDao.getVisible(i).getId());
		}
		//now for the one with the gap in id's
		assertEquals(visible2.get(0), spotlightDao.getVisible(numVisible-1).getId());
	}

	private List<Long> makeSpotlights(int numVisible, boolean isVisible) {
		List<Long> spotlightIds = new ArrayList<Long>();
		for (int i=0; i<numVisible; i++) {
			spotlightIds.add((Long)spotlightDao.save(makeSpotlight(isVisible)));
		}
		return spotlightIds;
	}
	
	private Spotlight makeSpotlight() {
		return makeSpotlight(true);
	}
	
	private Spotlight makeSpotlight(boolean isVisible) {
		Spotlight spotlight = new Spotlight();
		spotlight.setLabel(LABLE);
		spotlight.setLink(LINK);
		spotlight.setVisible(isVisible);
		spotlight.setAddedByUser(user);
		return spotlight;
		
	}

}