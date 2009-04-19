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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.tsm.concharto.OpenSessionInViewIntegrationTest;
import org.tsm.concharto.dao.EventDao;
import org.tsm.concharto.model.Event;
import org.tsm.concharto.model.PositionalAccuracy;
import org.tsm.concharto.model.UserTag;
import org.tsm.concharto.model.wiki.WikiText;
import org.tsm.concharto.util.ContextUtil;
import org.tsm.concharto.web.util.CatalogUtil;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;

@Transactional
public class IntegrationTestEventDao extends OpenSessionInViewIntegrationTest{
    private static final int MAX_RESULTS = 200;

    private static EventDao eventDao;
    private static EventTesterDao eventTesterDao;

    private static EventUtil eventUtil;

    private Date begin;
    private Date end;

    @Before
    public void setUp() {
        Calendar cal = new GregorianCalendar(107 + 1900, 8, 22, 12, 22, 3);
        cal.set(Calendar.MILLISECOND, 750);
        begin = cal.getTime();
        cal.set(Calendar.SECOND, 35);
        end = cal.getTime();
        eventTesterDao.deleteAll();
    }

    @BeforeClass
    public static void setUpClass() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        eventDao = (EventDao) appCtx.getBean("eventDao");
        eventTesterDao = (EventTesterDao) appCtx.getBean("eventTesterDao");
        eventUtil = new EventUtil(eventTesterDao.getSessionFactory());
        eventTesterDao.deleteAll();
        StyleUtil.setupStyle();
    }

    public void testFindRecent() {
    	
    }
    /**
     * Runs the first time
     */
    @Test
    public void testInitFindAll() {
        eventTesterDao.deleteAll();
        Collection<Event> events = eventDao.findRecent(MAX_RESULTS,0);
        assertEquals(0, events.size());
    }

    @Test
    public void testSaveAndFindById() throws ParseException {
        Event event = eventUtil.createEvent(begin, end);
        Serializable id = eventDao.save(event);
        assertNotNull(id);
        Event returned = eventDao.findById((Long) id);
        if (returned.getTsGeometry().getGeometry() instanceof Point) {
            Point point = (Point) returned.getTsGeometry().getGeometry();
            assertTrue((event.getTsGeometry()).getGeometry().equals(point));
            
        } else {
            fail("should have been a point");
        }
        
        eventUtil.assertEquivalent(event, returned);
    }

    /*
     * Tests Auditing
     */
    @Test
    public void testSaveAndResave() throws ParseException, InterruptedException {
        Event event = eventUtil.createEvent(begin, end);
        Serializable id = eventDao.save(event);
        getSessionFactory().getCurrentSession().evict(event);  //only for unit testing since we are using OpenSessionInView paradigm

        Event returned = eventDao.findById((Long) id);
        returned.setDescription("sdfsdf");
        Thread.sleep(1000);
        eventDao.saveOrUpdate(returned);
        getSessionFactory().getCurrentSession().evict(returned);
        
        Event returned2 = eventDao.findById((Long) id);
        assertEquals(EventUtil.filterMilliseconds(event.getCreated()), returned.getCreated());
        //make sure the last modified dates are different for the two instances we edited
        assertTrue(returned.getLastModified().compareTo(returned2.getLastModified()) != 0);
    }
  
    @Test
    public void findAll() throws ParseException, InterruptedException {
    	//create 3, one is invisible, so it shouldn't count in the resutls
    	//pause in between so the created dates are different
        Event event = eventUtil.createEvent();
        event.setVisible(false);
        eventDao.save(event);
        eventDao.save(eventUtil.createEvent());
        Thread.sleep(1000);
        eventDao.save(eventUtil.createEvent());
        List<Event> events = eventDao.findRecent(MAX_RESULTS,0);
        assertEquals(2, events.size());
        //ensure they are sorted in date order, newest first
        Event earlier = events.get(0);
        for (int i=1; i<events.size(); i++) {
        	event = events.get(i);
        	assertTrue(earlier.getCreated() + "is not later than " + event.getCreated(), 
        			earlier.getCreated().after(event.getCreated()));
        	earlier = event;
        }        
        assertEquals(2, (int)eventDao.getTotalCount());

    }

    @Test
    public void testSaveOrUpdate() throws ParseException {
        Event event = eventUtil.createEvent(begin, end);
        eventDao.save(event);
        String newDescription = "Sdfsdf ";
        event.setDescription(newDescription);
        eventDao.saveOrUpdate(event);
        Event returned = eventDao.findById(event.getId());
        eventUtil.assertEquivalent(event, returned);
    }

    @Test
    public void testDelete() throws ParseException {
        Event event = eventUtil.createEvent();
        Serializable id = eventDao.save(event);
        eventDao.delete(event);
        assertNull(eventDao.findById((Long) id));
    }

    @Test
    public void testDeleteById() throws ParseException {
    	Event event = eventUtil.createEvent();
        Serializable id = eventDao.save(event);
        getSessionFactory().getCurrentSession().evict(event);

        eventDao.delete((Long) id);
        event = eventDao.findById((Long) id);
        assertNull(event);
    }
    
    @Test
    public void saveUpdateGetDiscussion() throws ParseException {
    	Event event = eventUtil.createEvent();
    	WikiText discussion = new WikiText();
    	String text = "sdfgsdfgsdfgsdfg sdfg sdf gsdfg ";
    	discussion.setText(text);
    	event.setDiscussion(discussion);
        eventDao.saveOrUpdate(event);
        Event returned = eventDao.findById(event.getId());
        assertEquals(text, returned.getDiscussion().getText());    
        
        //update
        String updated = "gfdfgdfg 333";
        discussion.setText(updated);
        eventDao.saveOrUpdate(discussion);
        returned = eventDao.findById(event.getId());
        assertEquals(updated, returned.getDiscussion().getText());
        
        //get
        discussion = eventDao.getDiscussion(returned.getId());
        assertEquals(updated, discussion.getText());
    }
    
    @Test
    public void positionalAccuracies() throws ParseException {
    	String name = "pretty good";
    	PositionalAccuracy pa = new PositionalAccuracy("first", true);
    	eventDao.save(pa);
		pa = new PositionalAccuracy(name, true);
    	eventDao.save(pa);
    	assertEquals(2,eventDao.getPositionalAccuracies().size());
    	
    	Event event = eventUtil.createEvent();
    	event.setPositionalAccuracy(pa);
    	eventDao.saveOrUpdate(event);
    	Event returned = eventDao.findById(event.getId());
    	assertEquals(name, returned.getPositionalAccuracy().getName());
    }

    @Test
    public void setUserTagsAsString() throws ParseException {
    	Event event = eventUtil.createEvent();
    	
		event.setUserTagsAsString("tag a, tag b, tag c, tag d");
    	Long id = (Long) eventDao.save(event);
    	Event returned = eventDao.findById(id);
    	eventDao.saveOrUpdate(returned);
    	assertEquals(new Long(4), eventTesterDao.getCount(UserTag.class));
    	
    	boolean found = false;
    	for(UserTag tag : returned.getUserTags()) {
    		if (tag.getTag().equals("tag b")) {
    			found = true;
    		}
    	}
    	assertTrue(found);

    	//now make sure we don't create orphaned UserTags
    	event.setUserTagsAsString("tag d, tag a, tag b, tag c");
    	eventDao.saveOrUpdate(event);
    	assertEquals(new Long(4), eventTesterDao.getCount(UserTag.class));

    	event.setUserTagsAsString("tag d, tag a, tag b");
    	eventDao.saveOrUpdate(event);
    	assertEquals(new Long(3), eventTesterDao.getCount(UserTag.class));
    }
    
    @Test
    public void testCatalog() throws ParseException {
    	for (int i=0; i<3; i++) {
            eventDao.save(eventUtil.createEvent());
    	}
    	Event event = eventUtil.createEvent();
    	String catalog = "SDfsdf";
    	event.setCatalog(catalog);
    	eventDao.save(event);
    	assertEquals(1, eventDao.findRecent(catalog, 10, 0).size());
    	assertEquals(3, eventDao.findRecent(CatalogUtil.CATALOG_WWW, 10, 0).size());    	
    	assertEquals(new Integer(1), eventDao.getTotalCount(catalog));
    	assertEquals(new Integer(3), eventDao.getTotalCount(CatalogUtil.CATALOG_WWW));
    }
}
