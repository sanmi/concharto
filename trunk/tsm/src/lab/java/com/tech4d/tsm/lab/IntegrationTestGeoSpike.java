package com.tech4d.tsm.lab;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;

import com.tech4d.tsm.model.lab.Address;
import com.tech4d.tsm.util.ContextUtil;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.util.GeometricShapeFactory;

public class IntegrationTestGeoSpike extends TestCase {
    private AddressDao addressDao;

    public void setUp() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        addressDao = (AddressDao) appCtx.getBean("addressDao");
    }

    /**
     * Runs the first time
     */
    public void testInitFindAll() {
        addressDao.deleteAll();
        Collection<Address> addresses = addressDao.findAll();
        assertEquals(0, addresses.size());
    }

    public void testSaveAndFindById() throws ParseException {
        Address address = createAddess();
        Serializable id = addressDao.save(address);
        assertNotNull(id);
        Address returned = addressDao.findById((Long) id);
        System.out.println("Location: " + returned.getAddress_loc().toText());
        assertTrue(address.getAddress_loc().equals(returned.getAddress_loc()));
    }

    private Address createAddess() throws ParseException {
        return createAddress("POINT (20 20)");
    }

    private Address createAddress(String addressStr) throws ParseException {
        return createAddress(addressStr, addressStr);
    }

    private Address createAddress(String addressStr, String geomStr)
            throws ParseException {
        Point point = (Point) new WKTReader().read(addressStr);
        Address addr = new Address();
        addr.setAddress("17 Mockinbird Ln, Nameless, TN, 60606");
        addr.setAddress_loc(point);
        // GeometryCollection geom = (GeometryCollection) new WKTReader()
        // .read("GEOMETRYCOLLECTION(" + geomStr + ")");
        // addr.setGeom(geom);

        Geometry geom = new WKTReader().read(geomStr);
        addr.setGeometry(geom);
        return addr;
    }

    private Address createAddress(Geometry geometry) throws ParseException {
        Address addr = new Address();
        Point point = (Point) new WKTReader().read("POINT(20 20)");
        addr.setAddress_loc(point);
        addr.setAddress("17 Mockinbird Ln, Nameless, TN, 60606");
        addr.setGeometry(geometry);
        return addr;
    }

    public void findAll() throws ParseException {
        addressDao.save(createAddess());
        Collection<Address> addresses = addressDao.findAll();
        assertTrue(addresses.size() >= 1);
    }

    public void testSaveOrUpdate() {
    }

    public void testDelete() throws ParseException {
        Address address = createAddess();
        Serializable id = addressDao.save(address);
        addressDao.delete(address);
        assertNull(addressDao.findById((Long) id));
    }

    public void testDeleteById() throws ParseException {
        Serializable id = addressDao.save(createAddess());
        addressDao.delete((Long) id);
        Address addr = addressDao.findById((Long) id);
        assertNull(addr);
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
        String insidePoint = "POINT (330 330)";
        String insidePoly = "POLYGON ((330 330, 340 330, 340 340, 330 340, 330 330))";
        String outsidePoint = "POINT (130 130)";
        String outsidePoly = "POLYGON ((30 30, 40 30, 40 40, 30 40, 30 30))";
        Address insideTheBox = createAddress(insidePoint);
        addressDao.save(insideTheBox);

        // outside of the box
        Long start = System.currentTimeMillis();
        Random random = new Random(System.currentTimeMillis());
//        for (int i = 0; i < 1000000; i++) {
//            if (i % 2000 == 0) {
//                System.out.println(i);
//            }
//            if (i % 20000 == 0) {
//                addressDao.save(createAddress(insidePoint, insidePoly));
//                addressDao.save(createAddress(insidePoint)); // a point in both fields
//            } else {
//                Double x = random.nextDouble()*150;
//                Double y = random.nextDouble()*150;
//                gsf.setBase(new Coordinate(x, y));
//                addressDao.save(createAddress(gsf.createRectangle()));
//            }
//        }
        System.out.println("save " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        List<Address> addresses = addressDao.findWithinGeometry(rect);
        System.out.println("findWithinPoint "
                + (System.currentTimeMillis() - start));
        System.out.println("results: " + addresses.size());
        // assertEquals(51, addresses.size());
        Address returned = addresses.get(0);
        assertTrue(insideTheBox.getAddress_loc().equals(
                returned.getAddress_loc()));

        // Now check the geometry collections
        start = System.currentTimeMillis();
        addresses = addressDao.findGeomWithinGeometry(rect);
        System.out.println("findWithinGeometry "
                + (System.currentTimeMillis() - start));

        System.out.println("results: " + addresses.size());
        // assertEquals(51, addresses.size());
        returned = addresses.get(0);

        assertTrue(insideTheBox.getAddress_loc().equals(
                returned.getAddress_loc()));
    }

    public void testGetAndDisplay() throws ParseException {
        // Create some stuff
        addressDao.save(createAddress("POINT (1330 1330)"));
        addressDao
                .save(createAddress("POINT (1330 1330)",
                        "POLYGON ((1330 1330, 1340 1330, 1340 1340, 1330 1340, 1330 1330))"));

        // get it
        Collection<Address> addresses = addressDao
                .findGeomWithinGeometry(new WKTReader()
                        .read("POLYGON ((1300 1300, 1440 1300, 1440 1440, 1300 1440, 1300 1300))"));

        // iterate
        for (Address address : addresses) {
            // the ugly way
            // printGeometry(address.getGeom().getGeometryN(0));

            // the nice way
            printGeometry(address.getGeometry());

        }
    }

    private void printGeometry(Geometry geometry) {
        System.out.println(geometry.getGeometryType() + ", "
                + geometry.getClass() + ", " + geometry.toText());
    }

}
