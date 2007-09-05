package com.tech4d.tsm.lab;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;

import com.tech4d.tsm.model.lab.Address;
import com.tech4d.tsm.util.ContextUtil;
import com.vividsolutions.jts.geom.Coordinate;
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

    private Address createAddress(Point address_loc) {
        Address addr = new Address();
        addr.setAddress("17 Mockinbird Ln, Nameless, TN, 60606");
        addr.setAddress_loc(address_loc);
        return addr;        
    }
    private Address createAddess() throws ParseException {
        Address addr = createAddress((Point) new WKTReader().read("POINT (20 20)"));
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
        assertNull(addressDao.findById(address.getId()));
    }

    public void testDeleteById() throws ParseException {
        Serializable id = addressDao.save(createAddess());
        addressDao.delete((Long)id);
        Address addr = addressDao.findById((Long) id);
        assertNull(addr);
    }
    
    /**
     * Tests a spatially indexed query!!  Finding a point within a bounding box
     * @throws ParseException
     */
    public void testFindWithinGeometry() throws ParseException {
        //bounding box
        GeometricShapeFactory gsf = new GeometricShapeFactory();
        gsf.setSize(100);
        gsf.setNumPoints(4);
        gsf.setBase(new Coordinate(300, 300));  //pretend these are lat/longs
        Polygon rect = gsf.createRectangle();

        //inside the box
        Address insideTheBox = createAddress((Point) new WKTReader().read("POINT (330 330)"));
        addressDao.save(insideTheBox);

        // outside of the box
        addressDao.save(createAddress((Point) new WKTReader().read("POINT (130 130)"))); 
        
        List<Address> addresses = addressDao.findWithinGeometry(rect);
        assertEquals(1, addresses.size());
        Address returned = addresses.get(0);
        
        assertTrue(insideTheBox.getAddress_loc().equals(returned.getAddress_loc()));
    }

}
