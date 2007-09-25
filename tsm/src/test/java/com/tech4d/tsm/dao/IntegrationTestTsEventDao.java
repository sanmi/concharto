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

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.tech4d.tsm.model.TsEvent;
import com.tech4d.tsm.model.geometry.TimeRange;
import com.tech4d.tsm.util.ContextUtil;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.util.GeometricShapeFactory;

public class IntegrationTestTsEventDao {
    private static TsEventDao tsEventDao;
    
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
        tsEventUtil = new TsEventUtil(tsEventDao.getSessionFactory());
        tsEventDao.deleteAll();
        StyleUtil.setupStyle();
    }

    /**
     * Runs the first time
     */
    @Test
    public void testInitFindAll() {
        tsEventDao.deleteAll();
        Collection<TsEvent> tsEvents = tsEventDao.findAll();
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
  
    public void findAll() throws ParseException {
        tsEventDao.save(tsEventUtil.createTsEvent());
        List<TsEvent> tsEvents = tsEventDao.findAll();
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
