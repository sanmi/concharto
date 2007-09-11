package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;

import com.tech4d.tsm.model.geometry.Feature;
import com.tech4d.tsm.model.geometry.TsGeometry;
import com.tech4d.tsm.util.ContextUtil;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.util.GeometricShapeFactory;

public class IntegrationTestFeatureDao extends TestCase {
    private FeatureDao featureDao;

    public void setUp() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        featureDao = (FeatureDao) appCtx.getBean("featureDao");
    }

    /**
     * Runs the first time
     */
    public void testInitFindAll() {
        featureDao.deleteAll();
        Collection<Feature> features = featureDao.findAll();
        assertEquals(0, features.size());
    }

    public void testSaveAndFindById() throws ParseException {
        Feature feature = createFeature();
        Serializable id = featureDao.save(feature);
        assertNotNull(id);
        Feature returned = featureDao.findById((Long) id);
        if (returned.getTsGeometry().getGeometry() instanceof Point) {
            Point point = (Point) returned.getTsGeometry().getGeometry();
            System.out.println("point: " + point.toText());
            assertTrue((feature.getTsGeometry()).getGeometry().equals(point));
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
        Feature feature = new Feature();
        TsGeometry tsPoint = new TsGeometry(geometry);
        feature.setAddress("17 Mockinbird Ln, Nameless, TN, 60606");
        feature.setTsGeometry(tsPoint);
        return feature;
    }

    public void findAll() throws ParseException {
        featureDao.save(createFeature());
        Collection<Feature> features = featureDao.findAll();
        assertTrue(features.size() >= 1);
    }

    public void testSaveOrUpdate() {
    }

    public void testDelete() throws ParseException {
        Feature feature = createFeature();
        Serializable id = featureDao.save(feature);
        featureDao.delete(feature);
        assertNull(featureDao.findById((Long) id));
    }

    public void testDeleteById() throws ParseException {
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
    public void testFindWithinGeometry() throws ParseException {
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
