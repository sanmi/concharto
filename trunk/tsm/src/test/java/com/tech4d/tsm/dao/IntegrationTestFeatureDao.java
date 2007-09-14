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
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.tech4d.tsm.model.geometry.Feature;
import com.tech4d.tsm.model.geometry.Style;
import com.tech4d.tsm.model.geometry.StyleSelector;
import com.tech4d.tsm.model.geometry.TimeRange;
import com.tech4d.tsm.util.ContextUtil;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.util.GeometricShapeFactory;

public class IntegrationTestFeatureDao {
    private static FeatureDao featureDao;

    private Date begin = new Date();

    private Date end = new Date();

    private static boolean initialized;

    @BeforeClass
    public static void setUp() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        featureDao = (FeatureDao) appCtx.getBean("featureDao");

        if (!initialized) {
            initialized = true;
            StyleUtil.setupStyle();
        }
    }

    /**
     * Runs the first time
     */
    @Test
    public void testInitFindAll() {
        featureDao.deleteAll();
        Collection<Feature> features = featureDao.findAll();
        assertEquals(0, features.size());
    }

    @Test
    public void testSaveAndFindById() throws ParseException {
        Feature feature = FeatureUtil.createFeature();
        Serializable id = featureDao.save(feature);
        assertNotNull(id);
        Feature returned = featureDao.findById((Long) id);
        if (returned.getTsGeometry().getGeometry() instanceof Point) {
            Point point = (Point) returned.getTsGeometry().getGeometry();
            assertTrue((feature.getTsGeometry()).getGeometry().equals(point));

            // NOTE: MySQL doesn't store dates with millisecond precision, so we
            // need to strip out
            // the msec in order to compare
            Calendar cal = Calendar.getInstance();
            cal.setTime(begin);
            cal.set(Calendar.MILLISECOND, 0);
            Date correctedBegin = cal.getTime();
            Date newBegin = ((TimeRange) returned.getTimePrimitive())
                    .getBegin();
            assertEquals(correctedBegin, newBegin);

            StyleSelector styleS = returned.getStyleSelector();
            Style style = (Style) styleS;
            assertEquals(style.getBaloonStyle().getBgColor(), style
                    .getBaloonStyle().getBgColor());
        } else {
            fail("should have been a point");
        }
    }

    public void findAll() throws ParseException {
        featureDao.save(FeatureUtil.createFeature());
        Collection<Feature> features = featureDao.findAll();
        assertTrue(features.size() >= 1);
    }

    @Test
    public void testSaveOrUpdate() {
    }

    @Test
    public void testDelete() throws ParseException {
        Feature feature = FeatureUtil.createFeature();
        Serializable id = featureDao.save(feature);
        featureDao.delete(feature);
        assertNull(featureDao.findById((Long) id));
    }

    @Test
    public void testDeleteById() throws ParseException {
        Serializable id = featureDao.save(FeatureUtil.createFeature());
        featureDao.delete((Long) id);
        Feature feature = featureDao.findById((Long) id);
        assertNull(feature);
    }

    /**
     * Tests a spatially indexed query!! Finding a point within a bounding box
     * 
     * @throws ParseException
     */
    @Test
    public void testFindWithinGeometry() throws ParseException {
        // bounding box
        GeometricShapeFactory gsf = new GeometricShapeFactory();
        gsf.setSize(100);
        gsf.setNumPoints(4);
        gsf.setBase(new Coordinate(300, 300)); // pretend these are lat/longs
        Polygon rect = gsf.createRectangle();

        // inside the box
        String insideWKT = "POINT (330 330)";
        Feature insideTheBox = FeatureUtil.createFeature(
                (Point) new WKTReader().read(insideWKT),
                new TimeRange(begin, end)
                );
        featureDao.save(insideTheBox);

        // outside of the box
        featureDao.save(FeatureUtil.createFeature(
                (Point) new WKTReader().read("POINT (130 130)"),
                new TimeRange(begin, end)
                ));

        List<Feature> features = featureDao.findWithinGeometry(rect);
        assertEquals(1, features.size());
        Feature returned = features.get(0);

        assertEquals(insideWKT, (returned.getTsGeometry()).getGeometry()
                .toText());
    }

}
