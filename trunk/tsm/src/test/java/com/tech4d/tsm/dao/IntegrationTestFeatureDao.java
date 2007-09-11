package com.tech4d.tsm.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.tech4d.tsm.model.geometry.BalloonStyle;
import com.tech4d.tsm.model.geometry.Feature;
import com.tech4d.tsm.model.geometry.IconStyle;
import com.tech4d.tsm.model.geometry.LabelStyle;
import com.tech4d.tsm.model.geometry.LineStyle;
import com.tech4d.tsm.model.geometry.PolyStyle;
import com.tech4d.tsm.model.geometry.Style;
import com.tech4d.tsm.model.geometry.TimePrimitive;
import com.tech4d.tsm.model.geometry.TimeRange;
import com.tech4d.tsm.model.geometry.TsGeometry;
import com.tech4d.tsm.util.ContextUtil;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.util.GeometricShapeFactory;

public class IntegrationTestFeatureDao  {
    private static FeatureDao featureDao;

    private static StyleDao styleDao;

    private static Style style = new Style();

    private Date begin = new Date();

    private Date end = new Date();
    
    private static boolean initialized;
    
    static {
        style.setLabelStyle(new LabelStyle(2.3f));
        style.setLineStyle(new LineStyle(2));
        style.setIconStyle(new IconStyle(.1f, .2f, null, null));
        style.setBaloonStyle(new BalloonStyle(Color.BLACK, Color.WHITE,
                "Some text", BalloonStyle.DISPLAY_MODE_DEFAULT));
        style.setPolyStyle(new PolyStyle(true, true));        
    }

    @BeforeClass public static void setUp() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        featureDao = (FeatureDao) appCtx.getBean("featureDao");
        styleDao = (StyleDao) appCtx.getBean("styleDao");
        
        if (!initialized) {
            initialized = true;
            styleDao.deleteAll();
            // The style has to be created before you create any features. It is a
            // many to one mapping.
            styleDao.save(style);
        }
    }

    /**
     * Runs the first time
     */
    @Test public void testInitFindAll() {
        featureDao.deleteAll();
        Collection<Feature> features = featureDao.findAll();
        assertEquals(0, features.size());
        Collection<Style> styles = styleDao.findAll();
        assertEquals(1, styles.size());

    }

    @Test public void testSaveAndFindById() throws ParseException {
        Feature feature = createFeature();
        Serializable id = featureDao.save(feature);
        assertNotNull(id);
        Feature returned = featureDao.findById((Long) id);
        if (returned.getTsGeometry().getGeometry() instanceof Point) {
            Point point = (Point) returned.getTsGeometry().getGeometry();
            System.out.println("point: " + point.toText());
            assertTrue((feature.getTsGeometry()).getGeometry().equals(point));

            //NOTE: MySQL doesn't store dates with millisecond precision, so we need to strip out
            //the msec in order to compare
            Calendar cal = Calendar.getInstance();
            cal.setTime(begin);
            cal.set(Calendar.MILLISECOND,0);
            Date correctedBegin = cal.getTime(); 
            Date newBegin = ((TimeRange)returned.getTimePrimitive()).getBegin();            
            assertEquals(correctedBegin, newBegin);
           
        } else {
            fail("should have been a point");
        }
    }

    private Feature createFeature() throws ParseException {
        Feature feature = createFeature((Point) new WKTReader()
                .read("POINT (20 20)"));
        return feature;
    }

    private Feature createFeature(Geometry geometry) {
        return CreateFeature(geometry, new TimeRange(begin, end));
    }

    private Feature CreateFeature(Geometry geometry, TimePrimitive timePrimitive) {
        Feature feature = new Feature();
        feature.setAddress("17 Mockinbird Ln, Nameless, TN, 60606");
        feature.setSnippet("This is like some sort of small description yo");
        feature.setDescription("This is like the full sort of small description yo with a bunch of stuff");

        TsGeometry tsPoint = new TsGeometry(geometry);
        feature.setTsGeometry(tsPoint);
        feature.setTimePrimitive(timePrimitive);
        feature.setStyleSelector(style);

        return feature;
    }

    public void findAll() throws ParseException {
        featureDao.save(createFeature());
        Collection<Feature> features = featureDao.findAll();
        assertTrue(features.size() >= 1);
    }

    @Test public void testSaveOrUpdate() {
    }

    @Test public void testDelete() throws ParseException {
        Feature feature = createFeature();
        Serializable id = featureDao.save(feature);
        featureDao.delete(feature);
        assertNull(featureDao.findById((Long) id));
    }

    @Test public void testDeleteById() throws ParseException {
        Serializable id = featureDao.save(createFeature());
        featureDao.delete((Long) id);
        Feature feature = featureDao.findById((Long) id);
        assertNull(feature);
    }

    /**
     * Tests a spatially indexed query!! Finding a point within a bounding box
     * 
     * @throws ParseException
     */
    @Test public void testFindWithinGeometry() throws ParseException {
        // bounding box
        GeometricShapeFactory gsf = new GeometricShapeFactory();
        gsf.setSize(100);
        gsf.setNumPoints(4);
        gsf.setBase(new Coordinate(300, 300)); // pretend these are lat/longs
        Polygon rect = gsf.createRectangle();

        // inside the box
        String insideWKT = "POINT (330 330)";
        Feature insideTheBox = createFeature((Point) new WKTReader()
                .read(insideWKT));
        featureDao.save(insideTheBox);

        // outside of the box
        featureDao.save(createFeature((Point) new WKTReader()
                .read("POINT (130 130)")));

        List<Feature> features = featureDao.findWithinGeometry(rect);
        assertEquals(1, features.size());
        Feature returned = features.get(0);

        assertEquals(insideWKT, (returned.getTsGeometry()).getGeometry()
                .toText());
    }

}
