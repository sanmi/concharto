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
package com.tech4d.tsm.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.tech4d.tsm.model.Spotlight;
import com.tech4d.tsm.model.user.User;
import com.tech4d.tsm.util.ContextUtil;
import com.tech4d.tsm.web.util.CatalogUtil;

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

	@Before
	public void init() {
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
	
	@Test 
	public void testCatalog() {
		//some visible with the default catalog
		makeSpotlights(3, true);
		//some invisible with the default catalog
		makeSpotlights(2, false);
		//and a different catalog
		String catalog = "Sgffgf";
		Spotlight spotlight = makeSpotlight(true, catalog);
		spotlightDao.save(spotlight);
		assertEquals(5, spotlightDao.findAll(CatalogUtil.CATALOG_WWW).size());
		assertEquals(1, spotlightDao.findAll(catalog).size());
		assertEquals(3, spotlightDao.findVisible(CatalogUtil.CATALOG_WWW).size());
		assertEquals(1, spotlightDao.findVisible(catalog).size());
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
		return makeSpotlight(isVisible, CatalogUtil.CATALOG_WWW);
	}

	private Spotlight makeSpotlight(boolean isVisible, String catalog) {
		Spotlight spotlight = new Spotlight();
		spotlight.setLabel(LABLE);
		spotlight.setLink(LINK);
		spotlight.setVisible(isVisible);
		spotlight.setAddedByUser(user);
		spotlight.setCatalog(catalog);
		return spotlight;		
	}

}
