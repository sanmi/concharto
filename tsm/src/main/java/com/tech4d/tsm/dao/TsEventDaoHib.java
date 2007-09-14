package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Geometry;
import com.tech4d.tsm.model.TsEvent;

@Transactional
public class TsEventDaoHib implements TsEventDao {
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
     * @see com.tech4d.tsm.lab.TsEventDao#save(com.tech4d.tsm.model.lab.TsEvent)
     */
    public Serializable save(TsEvent tsEvent) {
        return this.sessionFactory.getCurrentSession().save(tsEvent);
    }

    public void saveOrUpdate(TsEvent tsEvent) {
         this.sessionFactory.getCurrentSession().saveOrUpdate(tsEvent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.TsEventDao#delete(com.tech4d.tsm.model.lab.TsEvent)
     */
    public void delete(TsEvent tsEvent) {
        this.sessionFactory.getCurrentSession().delete(tsEvent);
    }

    /*
     * 
     * @see com.tech4d.tsm.lab.TsEventDao#delete(java.lang.Long)
     */
    public void delete(Long id) {
        //TODO - figure out how to make this protected and still work within unit tests
        //TODO -  this is ugly, but it offers the best performance.  There don't
        //seem to be simpler cascade options using HQL delete
        this.sessionFactory
                .getCurrentSession()
                .createQuery(
                        "delete from TimePrimitive where id = (select tsEvent.timePrimitive.id from TsEvent tsEvent where id = ?)")
                .setLong(0, id).executeUpdate();
        this.sessionFactory
                .getCurrentSession()
                .createQuery(
                        "delete from TsGeometry where id = (select id from TsEvent where id = ?)")
                .setLong(0, id).executeUpdate();
        this.sessionFactory.getCurrentSession().createQuery(
                "delete TsEvent tsEvent where tsEvent.id = ?").setLong(0, id)
                .executeUpdate();
    }

    /**
     * Just for testing
     */
    public void deleteAll() {
        //TODO -  this is ugly, but it offers the best performance.  There don't
        //seem to be simpler cascade options using HQL delete
        this.sessionFactory.getCurrentSession().createQuery(
                "delete from TsGeometry where id in (select id from TsEvent)")
                .executeUpdate();
        this.sessionFactory
                .getCurrentSession()
                .createQuery(
                        "delete from TimePrimitive where id in (select tsevent.timePrimitive.id from TsEvent tsevent)")
                .executeUpdate();
        this.sessionFactory.getCurrentSession().createQuery(
                "delete from TsEvent").executeUpdate();

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.TsEventDao#findAll()
     */
    @SuppressWarnings("unchecked")
    public List<TsEvent> findAll() {
        return this.sessionFactory.getCurrentSession().createQuery(
                "select tsEvent from TsEvent tsEvent").list();
    }

    public TsEvent findById(Long id) {
        return (TsEvent) this.sessionFactory.getCurrentSession().get(
                TsEvent.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<TsEvent> findWithinGeometry(Geometry geometry) {
        String sql = "SELECT * FROM tsevent f, tsgeometry g "
                + "WHERE f.tsgeometry_id = g.id "
                + "AND MBRWithin(geometryCollection, Envelope(GeomFromText(:geom_text)))";
        List<TsEvent> tsEvents = this.sessionFactory.getCurrentSession()
                .createSQLQuery(sql).addEntity(TsEvent.class).setString(
                "geom_text", geometry.toText()).list();
        return tsEvents;
    }

}
