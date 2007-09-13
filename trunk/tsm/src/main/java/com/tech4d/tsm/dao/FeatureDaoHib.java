package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.geometry.Feature;
import com.vividsolutions.jts.geom.Geometry;

@Transactional
public class FeatureDaoHib implements FeatureDao {
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.FeatureDao#save(com.tech4d.tsm.model.lab.Feature)
     */
    public Serializable save(Feature feature) {

        return this.sessionFactory.getCurrentSession().save(feature);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.FeatureDao#delete(com.tech4d.tsm.model.lab.Feature)
     */
    public void delete(Feature feature) {
        this.sessionFactory.getCurrentSession().delete(feature);
    }

    /*
     * 
     * @see com.tech4d.tsm.lab.FeatureDao#delete(java.lang.Long)
     */
    public void delete(Long id) {
        //TODO - figure out how to make this protected and still work within unit tests
        //TODO -  this is ugly, but it offers the best performance.  There don't
        //seem to be simpler cascade options using HQL delete
        this.sessionFactory
                .getCurrentSession()
                .createQuery(
                        "delete from TimePrimitive where id = (select feature.timePrimitive.id from Feature feature where id = ?)")
                .setLong(0, id).executeUpdate();
        this.sessionFactory
                .getCurrentSession()
                .createQuery(
                        "delete from TsGeometry where id = (select id from Feature where id = ?)")
                .setLong(0, id).executeUpdate();
        this.sessionFactory.getCurrentSession().createQuery(
                "delete Feature feature where feature.id = ?").setLong(0, id)
                .executeUpdate();
    }

    /**
     * Just for testing
     */
    public void deleteAll() {
        //TODO -  this is ugly, but it offers the best performance.  There don't
        //seem to be simpler cascade options using HQL delete
        this.sessionFactory.getCurrentSession().createQuery(
                "delete from TsGeometry where id in (select id from Feature)")
                .executeUpdate();
        this.sessionFactory
                .getCurrentSession()
                .createQuery(
                        "delete from TimePrimitive where id in (select feature.timePrimitive.id from Feature feature)")
                .executeUpdate();
        this.sessionFactory.getCurrentSession().createQuery(
                "delete from Feature").executeUpdate();

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.FeatureDao#findAll()
     */
    @SuppressWarnings("unchecked")
    public List<Feature> findAll() {
        return this.sessionFactory.getCurrentSession().createQuery(
                "select feature from Feature feature").list();
    }

    public Feature findById(Long id) {
        return (Feature) this.sessionFactory.getCurrentSession().get(
                Feature.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Feature> findWithinGeometry(Geometry geometry) {
        // String sql = "SELECT * FROM feature WHERE
        // MBRWithin(geom,Envelope(GeomFromText(:geom_text))) = 1";
        String sql = "SELECT * FROM feature f, tsgeometry g "
                + "WHERE f.tsgeometry_id = g.id "
                + "AND MBRWithin(geometryCollection, Envelope(GeomFromText(:geom_text)))";
        List<Feature> features = this.sessionFactory.getCurrentSession()
                .createSQLQuery(sql).addEntity(Feature.class).setString(
                        "geom_text", geometry.toText()).list();
        return features;
    }

}
