package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Geometry;
import com.tech4d.tsm.model.EventSearchText;
import com.tech4d.tsm.model.TsEvent;
import com.tech4d.tsm.model.geometry.TimeRange;

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
        //TODO First save a copy of the text to the MyISAM table.  Note MyISAM doesn't support 
        //transactions, so if this fails and the second succeeds, we may have a duplicate 
        //record later on 
        tsEvent.setEventSearchText(new EventSearchText(tsEvent));
        //now we can save
        return this.sessionFactory.getCurrentSession().save(tsEvent);
    }

    public void saveOrUpdate(TsEvent tsEvent) {
        //TODO First update the search text object (see note above)
        if (tsEvent.getEventSearchText() != null) {
            tsEvent.getEventSearchText().copyFrom(tsEvent);
        } else {
            tsEvent.setEventSearchText(new EventSearchText(tsEvent));
        }
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
        //This may look goofy, but it is really ugly if you try to do it from an hql query 
        //because of all of the foreign keys.  This way, hibernate handles it for us.
        TsEvent event = new TsEvent();
        event.setId(id);
        this.sessionFactory.getCurrentSession().delete(event);
    }

    /**
     * Just for testing.  Do not use this in production
     * TODO figure out how to remove this from the "production" dao interface
     */
    public void deleteAll() {
        //TODO -  this is ugly, but it just for testing.  There don't
        //seem to be simpler cascade options using HQL delete
        List<TsEvent> tsEvents = this.findAll();
        for (TsEvent event : tsEvents) {
            this.delete(event);
        }

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
        //TODO change this to prepared statements
        String sql = "SELECT * FROM tsevent f, tsgeometry g "
                + "WHERE f.tsgeometry_id = g.id "
                + "AND MBRWithin(geometryCollection, Envelope(GeomFromText(:geom_text)))";
        List<TsEvent> tsEvents = this.sessionFactory.getCurrentSession()
                .createSQLQuery(sql).addEntity(TsEvent.class).setString(
                "geom_text", geometry.toText()).list();
        return tsEvents;
    }
    
    @SuppressWarnings("unchecked")
    public List<TsEvent> search(String textFilter, TimeRange timeRange, Geometry boundingBox) {
        //TODO change this to prepared statements
        String sql = "SELECT * FROM tsevent f, tsgeometry g, eventsearchtext es, timeprimitive t "
            + "WHERE f.tsgeometry_id = g.id "
            + "AND f.eventsearchtext_id = es.id "
            + "AND f.timePrimitive_id = t.id "
            + "AND MBRWithin(geometryCollection, Envelope(GeomFromText(:geom_text))) "
            + "AND MATCH (es.summary, es.description, es.usertags, es.source) AGAINST (:search_text)"
            + "AND (t.begin > :earliest AND t.end < :latest ) " 
            + "OR (t.time > :earliest AND t.time < :latest )";
        List<TsEvent> tsEvents = this.sessionFactory.getCurrentSession()
            .createSQLQuery(sql)
            .addEntity(TsEvent.class)
            .setString("geom_text", boundingBox.toText())
            .setString("search_text", textFilter)
            .setDate("earliest", timeRange.getBegin())
            .setDate("latest", timeRange.getEnd())
            .list();
    return tsEvents;
    }
}
