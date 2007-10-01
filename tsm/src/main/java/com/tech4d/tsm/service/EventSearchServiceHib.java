package com.tech4d.tsm.service;

import com.tech4d.tsm.model.TsEvent;
import com.tech4d.tsm.model.geometry.TimeRange;
import com.tech4d.tsm.util.LapTimer;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class EventSearchServiceHib implements EventSearchService {
    private SessionFactory sessionFactory;
    /** Logger that is available to subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    private static String SQL_SELECT_STUB = " FROM tsevent f, tsgeometry g, eventsearchtext es, timeprimitive t "
            + "WHERE f.tsgeometry_id = g.id "
            + "AND f.eventsearchtext_id = es.id "
            + "AND f.when_id = t.id ";

    private static String SQL_TIMERANGE_CLAUSE = 
        "AND (t.begin >= :earliest AND t.end <= :latest ) "
        + "OR (t.time >= :earliest AND t.time <= :latest ) ";
    
    private static String SQL_MBRWITHIN_CLAUSE = 
        "AND MBRWithin(geometryCollection, Envelope(GeomFromText(:geom_text))) ";

    private static String SQL_MATCH_CLAUSE = 
        "AND MATCH (es.summary, es._where, es.usertags, es.description, es.source) AGAINST (:search_text) ";

    private static String SQL_PREFIX_GET_COUNT = "SELECT count(*) "; 

    private static String SQL_PREFIX_SEARCH = "SELECT * "; 

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.service.EventSearchService#setSessionFactory(org.hibernate.SessionFactory)
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.service.EventSearchService#getSessionFactory()
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.service.EventSearchService#findWithinGeometry(com.vividsolutions.jts.geom.Geometry)
     */
    @SuppressWarnings("unchecked")
    public List<TsEvent> findWithinGeometry(Geometry geometry) {
        // TODO change this to prepared statements
        String sql = "SELECT * FROM tsevent f, tsgeometry g " + "WHERE f.tsgeometry_id = g.id "
                + "AND MBRWithin(geometryCollection, Envelope(GeomFromText(:geom_text)))";
        List<TsEvent> tsEvents = this.sessionFactory.getCurrentSession()
                .createSQLQuery(sql)
                .addEntity(TsEvent.class)
                .setString("geom_text", geometry.toText())
                .list();
        return tsEvents;
    }

    public Long getCount(String textFilter, TimeRange timeRange, Geometry boundingBox) {
        String sql = createQuery(SQL_PREFIX_GET_COUNT, textFilter, timeRange,boundingBox);
        List result = this.sessionFactory.getCurrentSession()
                .createSQLQuery(sql)
                .addScalar("count(*)", Hibernate.LONG)
                .setString("geom_text", boundingBox.toText())
                .setString("search_text", textFilter)
                .setDate("earliest", timeRange.getBegin())
                .setDate("latest", timeRange.getEnd())
                .list();
        return (Long) result.get(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.service.EventSearchService#search(java.lang.String,
     *      com.tech4d.tsm.model.geometry.TimeRange,
     *      com.vividsolutions.jts.geom.Geometry)
     */
    @SuppressWarnings("unchecked")
    public List<TsEvent> search(int maxResults, String textFilter, TimeRange timeRange,
            Geometry boundingBox) {
        LapTimer timer = new LapTimer(this.logger);
        String sql = createQuery(SQL_PREFIX_SEARCH, textFilter, timeRange,boundingBox);
        // Note: Hibernate always uses prepared statements
        SQLQuery sqlQuery = this.sessionFactory.getCurrentSession()
                .createSQLQuery(sql)
                .addEntity(TsEvent.class);
        if (boundingBox != null) {
            sqlQuery.setString("geom_text", boundingBox.toText());
        }
        if (!StringUtils.isEmpty(textFilter)) {
            sqlQuery.setString("search_text", textFilter);
        }
        if (timeRange != null) {
            sqlQuery.setDate("earliest", timeRange.getBegin());
            sqlQuery.setDate("latest", timeRange.getEnd());
        }
                
        List<TsEvent> tsEvents = sqlQuery.setMaxResults(maxResults).list(); 
        timer.timeIt("search").logDebugTime();
        return tsEvents;
    }

    private String createQuery(String prefix, String textFilter, TimeRange timeRange, Geometry boundingBox) {
        StringBuffer query = new StringBuffer(prefix).append(SQL_SELECT_STUB);
        if (!StringUtils.isEmpty(textFilter)) {
            query.append(SQL_MATCH_CLAUSE);
        }
        if (boundingBox != null) {
            query.append(SQL_MBRWITHIN_CLAUSE);
        }
        if (timeRange != null) {
            query.append(SQL_TIMERANGE_CLAUSE);
        }
        return query.toString();
    }

}
