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

import com.tech4d.tsm.model.TsEvent;
import com.tech4d.tsm.util.ContextUtil;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;

@Transactional
public class IntegrationTestTsEventDao {
    private static final int MAX_RESULTS = 200;

    private static TsEventDao tsEventDao;
    private static TsEventTesterDao tsEventTesterDao;

    private static TsEventUtil tsEventUtil;

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
        tsEventDao = (TsEventDao) appCtx.getBean("tsEventDao");
        tsEventTesterDao = (TsEventTesterDao) appCtx.getBean("tsEventTesterDao");
        tsEventUtil = new TsEventUtil(tsEventTesterDao.getSessionFactory());
        tsEventTesterDao.deleteAll();
        StyleUtil.setupStyle();
    }

    /**
     * Runs the first time
     */
    @Test
    public void testInitFindAll() {
        tsEventTesterDao.deleteAll();
        Collection<TsEvent> tsEvents = tsEventDao.findAll(MAX_RESULTS);
        assertEquals(0, tsEvents.size());
    }

    @Test
    public void testSaveAndFindById() throws ParseException {
        TsEvent tsEvent = tsEventUtil.createTsEvent(begin, end);
        Serializable id = tsEventDao.save(tsEvent);
        assertNotNull(id);
        TsEvent returned = tsEventDao.findById((Long) id);
        if (returned.getTsGeometry().getGeometry() instanceof Point) {
            Point point = (Point) returned.getTsGeometry().getGeometry();
            assertTrue((tsEvent.getTsGeometry()).getGeometry().equals(point));
            
        } else {
            fail("should have been a point");
        }
        
        tsEventUtil.assertEquivalent(tsEvent, returned);
    }

    /**
     * Tests Auditing
     * @throws ParseException
     * @throws InterruptedException
     */
    @Test
    public void testSaveAndResave() throws ParseException, InterruptedException {
        TsEvent event = tsEventUtil.createTsEvent(begin, end);
        Serializable id = tsEventDao.save(event);
        TsEvent returned = tsEventDao.findById((Long) id);
        event.setDescription("sdfsdf");
        Thread.sleep(1000);
        tsEventDao.saveOrUpdate(event);
        TsEvent returned2 = tsEventDao.findById((Long) id);
        assertEquals(TsEventUtil.filterMilliseconds(event.getCreated()), returned.getCreated());
        //make sure the last modified dates are different for the two instances we edited
        assertTrue(returned.getLastModified().compareTo(returned2.getLastModified()) != 0);
    }
  
    public void findAll() throws ParseException {
        tsEventDao.save(tsEventUtil.createTsEvent());
        List<TsEvent> tsEvents = tsEventDao.findAll(MAX_RESULTS);
        assertTrue(tsEvents.size() >= 1);
    }

    @Test
    public void testSaveOrUpdate() throws ParseException {
        TsEvent tsEvent = tsEventUtil.createTsEvent(begin, end);
        tsEventDao.save(tsEvent);
        String newDescription = "Sdfsdf ";
        tsEvent.setDescription(newDescription);
        tsEventDao.saveOrUpdate(tsEvent);
        TsEvent returned = tsEventDao.findById(tsEvent.getId());
        tsEventUtil.assertEquivalent(tsEvent, returned);
    }

    @Test
    public void testDelete() throws ParseException {
        TsEvent tsEvent = tsEventUtil.createTsEvent();
        Serializable id = tsEventDao.save(tsEvent);
        tsEventDao.delete(tsEvent);
        assertNull(tsEventDao.findById((Long) id));
    }

    @Test
    public void testDeleteById() throws ParseException {
        Serializable id = tsEventDao.save(tsEventUtil.createTsEvent());
        tsEventDao.delete((Long) id);
        TsEvent tsEvent = tsEventDao.findById((Long) id);
        assertNull(tsEvent);
    }

}
