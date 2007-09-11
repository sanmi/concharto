package com.tech4d.tsm.lab;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.lab.Address;
import com.vividsolutions.jts.geom.Geometry;

/**
 * TODO figure out how to use the plain old hibernate session syntax as
 * specified at
 * http://static.springframework.org/spring/docs/2.0.x/reference/orm.html#orm-hibernate
 * 
 * @author frank
 * 
 */
@Transactional
public class AddressDaoHib implements AddressDao {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.AddressDao#save(com.tech4d.tsm.model.lab.Address)
     */
    public Serializable save(Address address) {

        return this.sessionFactory.getCurrentSession().save(address);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.AddressDao#delete(com.tech4d.tsm.model.lab.Address)
     */
    public void delete(Address Address) {
        this.sessionFactory.getCurrentSession().delete(Address);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.AddressDao#delete(java.lang.Long)
     */
    public void delete(Long id) {
        this.sessionFactory.getCurrentSession().createQuery(
                "delete Address address where address.id = ?").setLong(0, id)
                .executeUpdate();
    }

    /**
     * Just for testing
     */
    public void deleteAll() {
        this.sessionFactory.getCurrentSession().createQuery(
                "delete from Address").executeUpdate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.AddressDao#findAll()
     */
    @SuppressWarnings("unchecked")
    public List<Address> findAll() {
        return this.sessionFactory.getCurrentSession().createQuery(
                "select address from Address address").list();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.AddressDao#findById(java.lang.Long)
     */
    public Address findById(Long id) {
        return (Address) this.sessionFactory.getCurrentSession().get(
                Address.class, id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.AddressDao#findWithinGeometry(com.vividsolutions.jts.geom.Geometry)
     */
    @SuppressWarnings("unchecked")
    public List<Address> findWithinGeometry(Geometry geometry) {
        String sql = "SELECT * FROM address WHERE MBRWithin(addressLocation,Envelope(GeomFromText(:geom_text))) = 1";
        List<Address> addresses = this.sessionFactory.getCurrentSession()
                .createSQLQuery(sql)
                .addEntity(Address.class)
                .setString("geom_text", geometry.toText())
                .list();
        return addresses;
    }

    @SuppressWarnings("unchecked")
    public List<Address> findGeomWithinGeometry(Geometry geometry) {
        String sql = "SELECT * FROM address WHERE MBRWithin(geom,Envelope(GeomFromText(:geom_text))) = 1";
        List<Address> addresses = this.sessionFactory.getCurrentSession()
                .createSQLQuery(sql)
                .addEntity(Address.class)
                .setString("geom_text", geometry.toText())
                .list();
        return addresses;
    }

}
