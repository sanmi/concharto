package com.tech4d.tsm.dao;

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

import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.util.ContextUtil;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;

@Transactional
public class IntegrationTestEventDao {
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

    /**
     * Runs the first time
     */
    @Test
    public void testInitFindAll() {
        eventTesterDao.deleteAll();
        Collection<Event> events = eventDao.findAll(MAX_RESULTS);
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

    /**
     * Tests Auditing
     * @throws ParseException
     * @throws InterruptedException
     */
    @Test
    public void testSaveAndResave() throws ParseException, InterruptedException {
        Event event = eventUtil.createEvent(begin, end);
        Serializable id = eventDao.save(event);
        Event returned = eventDao.findById((Long) id);
        event.setDescription("sdfsdf");
        Thread.sleep(1000);
        eventDao.saveOrUpdate(event);
        Event returned2 = eventDao.findById((Long) id);
        assertEquals(EventUtil.filterMilliseconds(event.getCreated()), returned.getCreated());
        //make sure the last modified dates are different for the two instances we edited
        assertTrue(returned.getLastModified().compareTo(returned2.getLastModified()) != 0);
    }
  
    public void findAll() throws ParseException {
        eventDao.save(eventUtil.createEvent());
        List<Event> events = eventDao.findAll(MAX_RESULTS);
        assertTrue(events.size() >= 1);
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
        Serializable id = eventDao.save(eventUtil.createEvent());
        eventDao.delete((Long) id);
        Event event = eventDao.findById((Long) id);
        assertNull(event);
    }
    

}
