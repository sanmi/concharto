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
import org.springframework.transaction.annotation.Transactional;
import org.tsm.concharto.model.Event;
import org.tsm.concharto.model.PositionalAccuracy;
import org.tsm.concharto.model.UserTag;
import org.tsm.concharto.model.wiki.WikiText;
import org.tsm.concharto.web.util.CatalogUtil;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;

@Transactional
public class IntegrationTestEventDao extends BaseEventIntegrationTest{
    private static final int MAX_RESULTS = 200;

    private Date begin;
    private Date end;

    @Before
    public void setUp() {
        Calendar cal = new GregorianCalendar(107 + 1900, 8, 22, 12, 22, 3);
        cal.set(Calendar.MILLISECOND, 750);
        begin = cal.getTime();
        cal.set(Calendar.SECOND, 35);
        end = cal.getTime();
        getEventTesterDao().deleteAll();
    }

    @BeforeClass
    public static void setUpClass() {
        baseSetUpClass();
    }

    public void testFindRecent() {
    	
    }
    /**
     * Runs the first time
     */
    @Test
    public void testInitFindAll() {
        getEventTesterDao().deleteAll();
        Collection<Event> events = getEventDao().findRecent(MAX_RESULTS,0);
        assertEquals(0, events.size());
    }

    @Test
    public void testSaveAndFindById() throws ParseException {
        Event event = getEventUtil().createEvent(begin, end);
        Serializable id = getEventDao().save(event);
        assertNotNull(id);
        Event returned = getEventDao().findById((Long) id);
        if (returned.getTsGeometry().getGeometry() instanceof Point) {
            Point point = (Point) returned.getTsGeometry().getGeometry();
            assertTrue((event.getTsGeometry()).getGeometry().equals(point));
            
        } else {
            fail("should have been a point");
        }
        
        getEventUtil().assertEquivalent(event, returned);
    }

    /*
     * Tests Auditing
     */
    @Test
    public void testSaveAndResave() throws ParseException, InterruptedException {
        Event event = getEventUtil().createEvent(begin, end);
        Serializable id = getEventDao().save(event);
        getSessionFactory().getCurrentSession().evict(event);  //only for unit testing since we are using OpenSessionInView paradigm

        Event returned = getEventDao().findById((Long) id);
        returned.setDescription("sdfsdf");
        Thread.sleep(1000);
        getEventDao().saveOrUpdate(returned);
        getSessionFactory().getCurrentSession().evict(returned);
        
        Event returned2 = getEventDao().findById((Long) id);
        assertEquals(EventUtil.filterMilliseconds(event.getCreated()), returned.getCreated());
        //make sure the last modified dates are different for the two instances we edited
        assertTrue(returned.getLastModified().compareTo(returned2.getLastModified()) != 0);
    }
  
    @Test
    public void findAll() throws ParseException, InterruptedException {
    	//create 3, one is invisible, so it shouldn't count in the resutls
    	//pause in between so the created dates are different
        Event event = getEventUtil().createEvent();
        event.setVisible(false);
        getEventDao().save(event);
        getEventDao().save(getEventUtil().createEvent());
        Thread.sleep(1000);
        getEventDao().save(getEventUtil().createEvent());
        List<Event> events = getEventDao().findRecent(MAX_RESULTS,0);
        assertEquals(2, events.size());
        //ensure they are sorted in date order, newest first
        Event earlier = events.get(0);
        for (int i=1; i<events.size(); i++) {
        	event = events.get(i);
        	assertTrue(earlier.getCreated() + "is not later than " + event.getCreated(), 
        			earlier.getCreated().after(event.getCreated()));
        	earlier = event;
        }        
        assertEquals(2, (int)getEventDao().getTotalCount());

    }

    @Test
    public void testSaveOrUpdate() throws ParseException {
        Event event = getEventUtil().createEvent(begin, end);
        getEventDao().save(event);
        String newDescription = "Sdfsdf ";
        event.setDescription(newDescription);
        getEventDao().saveOrUpdate(event);
        Event returned = getEventDao().findById(event.getId());
        getEventUtil().assertEquivalent(event, returned);
    }

    @Test
    public void testDelete() throws ParseException {
        Event event = getEventUtil().createEvent();
        Serializable id = getEventDao().save(event);
        getEventDao().delete(event);
        assertNull(getEventDao().findById((Long) id));
    }

    @Test
    public void testDeleteById() throws ParseException {
    	Event event = getEventUtil().createEvent();
        Serializable id = getEventDao().save(event);
        getSessionFactory().getCurrentSession().evict(event);

        getEventDao().delete((Long) id);
        event = getEventDao().findById((Long) id);
        assertNull(event);
    }
    
    @Test
    public void saveUpdateGetDiscussion() throws ParseException {
    	Event event = getEventUtil().createEvent();
    	WikiText discussion = new WikiText();
    	String text = "sdfgsdfgsdfgsdfg sdfg sdf gsdfg ";
    	discussion.setText(text);
    	event.setDiscussion(discussion);
        getEventDao().saveOrUpdate(event);
        Event returned = getEventDao().findById(event.getId());
        assertEquals(text, returned.getDiscussion().getText());    
        
        //update
        String updated = "gfdfgdfg 333";
        discussion.setText(updated);
        getEventDao().saveOrUpdate(discussion);
        returned = getEventDao().findById(event.getId());
        assertEquals(updated, returned.getDiscussion().getText());
        
        //get
        discussion = getEventDao().getDiscussion(returned.getId());
        assertEquals(updated, discussion.getText());
    }
    
    @Test
    public void positionalAccuracies() throws ParseException {
    	String name = "pretty good";
    	PositionalAccuracy pa = new PositionalAccuracy("first", true);
    	getEventDao().save(pa);
		pa = new PositionalAccuracy(name, true);
    	getEventDao().save(pa);
    	assertEquals(2,getEventDao().getPositionalAccuracies().size());
    	
    	Event event = getEventUtil().createEvent();
    	event.setPositionalAccuracy(pa);
    	getEventDao().saveOrUpdate(event);
    	Event returned = getEventDao().findById(event.getId());
    	assertEquals(name, returned.getPositionalAccuracy().getName());
    }

    @Test
    public void setUserTagsAsString() throws ParseException {
    	Event event = getEventUtil().createEvent();
    	
		event.setUserTagsAsString("tag a, tag b, tag c, tag d");
    	Long id = (Long) getEventDao().save(event);
    	Event returned = getEventDao().findById(id);
    	getEventDao().saveOrUpdate(returned);
    	assertEquals(new Long(4), getEventTesterDao().getCount(UserTag.class));
    	
    	boolean found = false;
    	for(UserTag tag : returned.getUserTags()) {
    		if (tag.getTag().equals("tag b")) {
    			found = true;
    		}
    	}
    	assertTrue(found);

    	//now make sure we don't create orphaned UserTags
    	event.setUserTagsAsString("tag d, tag a, tag b, tag c");
    	getEventDao().saveOrUpdate(event);
    	assertEquals(new Long(4), getEventTesterDao().getCount(UserTag.class));

    	event.setUserTagsAsString("tag d, tag a, tag b");
    	getEventDao().saveOrUpdate(event);
    	assertEquals(new Long(3), getEventTesterDao().getCount(UserTag.class));
    }
    
    @Test
    public void testCatalog() throws ParseException {
    	for (int i=0; i<3; i++) {
            getEventDao().save(getEventUtil().createEvent());
    	}
    	Event event = getEventUtil().createEvent();
    	String catalog = "SDfsdf";
    	event.setCatalog(catalog);
    	getEventDao().save(event);
    	assertEquals(1, getEventDao().findRecent(catalog, 10, 0).size());
    	assertEquals(3, getEventDao().findRecent(CatalogUtil.CATALOG_WWW, 10, 0).size());    	
    	assertEquals(new Integer(1), getEventDao().getTotalCount(catalog));
    	assertEquals(new Integer(3), getEventDao().getTotalCount(CatalogUtil.CATALOG_WWW));
    }
}
