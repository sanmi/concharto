package com.tech4d.tsm.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.TsEvent;
import com.tech4d.tsm.model.geometry.TimeRange;
import com.vividsolutions.jts.geom.Geometry;

@Transactional
public class EventSearchServiceHib implements EventSearchService {
    private SessionFactory sessionFactory;
    private static String SEARCH_QUERY_STUB = " FROM tsevent f, tsgeometry g, eventsearchtext es, timeprimitive t "
        + "WHERE f.tsgeometry_id = g.id "
        + "AND f.eventsearchtext_id = es.id "
        + "AND f.timePrimitive_id = t.id "
        + "AND MBRWithin(geometryCollection, Envelope(GeomFromText(:geom_text))) "
        + "AND MATCH (es.summary, es.description, es.usertags, es.source) AGAINST (:search_text)"
        + "AND (t.begin > :earliest AND t.end < :latest ) " 
        + "OR (t.time > :earliest AND t.time < :latest )";
    private static String SQL_GET_COUNT = "SELECT count(*) " + SEARCH_QUERY_STUB;  
    private static String SQL_SEARCH = "SELECT * " + SEARCH_QUERY_STUB;
    

    /* (non-Javadoc)
     * @see com.tech4d.tsm.service.EventSearchService#setSessionFactory(org.hibernate.SessionFactory)
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.service.EventSearchService#getSessionFactory()
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    /* (non-Javadoc)
     * @see com.tech4d.tsm.service.EventSearchService#findWithinGeometry(com.vividsolutions.jts.geom.Geometry)
     */
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
    
    /* (non-Javadoc)
     * @see com.tech4d.tsm.service.EventSearchService#search(java.lang.String, com.tech4d.tsm.model.geometry.TimeRange, com.vividsolutions.jts.geom.Geometry)
     */
    @SuppressWarnings("unchecked")
    public List<TsEvent> search(
            int maxResults,
            String textFilter, 
            TimeRange timeRange, 
            Geometry boundingBox) {
        //Note: Hibernate always uses prepared statements
        List<TsEvent> tsEvents = this.sessionFactory.getCurrentSession()
            .createSQLQuery(SQL_SEARCH)
            .addEntity(TsEvent.class)
            .setString("geom_text", boundingBox.toText())
            .setString("search_text", textFilter)
            .setDate("earliest", timeRange.getBegin())
            .setDate("latest", timeRange.getEnd())
            .setMaxResults(maxResults)
            .list();
    return tsEvents;
    }

}
