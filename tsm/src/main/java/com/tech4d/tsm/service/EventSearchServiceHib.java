package com.tech4d.tsm.service;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.time.TimeRange;
import com.tech4d.tsm.util.LapTimer;
import com.vividsolutions.jts.geom.Geometry;

@Transactional
public class EventSearchServiceHib implements EventSearchService {
	
    private SessionFactory sessionFactory;
    /** Logger that is available to subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    private static final String SQL_PREFIX_GET_COUNT = "SELECT count(*) "; 
    private static final String SQL_PREFIX_SEARCH = "SELECT * "; 
    private static final String SQL_SELECT_STUB = " FROM Event ev ";
     
    private static final String SQL_GEO_JOIN ="INNER JOIN TsGeometry AS g ON ev.tsgeometry_id = g.id ";
    private static final String SQL_SEARCH_JOIN ="INNER JOIN EventSearchText AS es ON ev.eventsearchtext_id = es.id ";
    private static final String SQL_TIME_JOIN="INNER JOIN TimePrimitive AS t ON ev.when_id = t.id ";
    private static final String SQL_WHERE = " WHERE ";
    private static final String SQL_AND = " AND ";
    
    private static final String SQL_TIMERANGE_CLAUSE = 
        "((t.begin >= :earliest AND t.begin < :latest) OR " +  
        " (t.end > :earliest AND t.end <= :latest) OR " +
        " (t.begin < :earliest AND t.end > :latest)) ";

    private static final String SQL_VISIBLE_CLAUSE = " NOT(ev.visible  <=> false) ";
    private static final String SQL_HIDDEN_CLAUSE = " ev.visible  <=> false ";
    private static final String SQL_FLAGGED_CLAUSE = " ev.hasUnresolvedFlag = true ";
    	
    private static final String SQL_MBRWITHIN_CLAUSE = 
        " MBRIntersects(geometryCollection, Envelope(GeomFromText(:geom_text))) ";

    private static final String SQL_MATCH_CLAUSE = 
        " MATCH (es.summary, es._where, es.usertags, es.description, es.source) AGAINST (:search_text) ";

    
    private static final String SQL_ORDER_CLAUSE = " order by t.begin asc";

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

    @SuppressWarnings("unchecked")
	public Integer getTotalCount() {
    	List results = this.sessionFactory.getCurrentSession()
    	.createQuery("select count(event) from Event event")
    	.list();
    	Long count = (Long) results.get(0);
    	//cast to Integer.  It aint never going to be bigger!
    	return Math.round(count);
    }


    /*
     * @see com.tech4d.tsm.service.EventSearchService#getCount
     */
    @SuppressWarnings("unchecked")
	public Long getCount(String textFilter, TimeRange timeRange, Geometry boundingBox, Visibility visibility) {
        LapTimer timer = new LapTimer(this.logger);
        SQLQuery sqlQuery = createQuery(SQL_PREFIX_GET_COUNT, textFilter, timeRange, boundingBox, visibility);
        List result = sqlQuery.addScalar("count(*)", Hibernate.LONG).list();
        timer.timeIt("count").logDebugTime();
        return (Long) result.get(0);
    }

    /*
     * @see com.tech4d.tsm.service.EventSearchService#search
     */
    @SuppressWarnings("unchecked")
    public List<Event> search(int maxResults, int firstResult, String textFilter, TimeRange timeRange,
            Geometry boundingBox, Visibility visibility) {
        LapTimer timer = new LapTimer(this.logger);
        SQLQuery sqlQuery = createQuery(SQL_PREFIX_SEARCH, textFilter, timeRange, boundingBox, visibility);
               
        List<Event> events = sqlQuery
            .addEntity(Event.class)
            .setMaxResults(maxResults)
            .setFirstResult(firstResult)
            .list(); 
        timer.timeIt("search").logDebugTime();
        return events;
    }

    private SQLQuery createQuery(String prefix, String textFilter, TimeRange timeRange, 
    		Geometry boundingBox, Visibility visbility) {
        StringBuffer select = new StringBuffer(prefix).append(SQL_SELECT_STUB);
    	select.append(SQL_TIME_JOIN); //always join on time, so we can order by time
        StringBuffer clause = new StringBuffer();
        boolean hasConjuncted = false;
        if (visbility == Visibility.NORMAL) {
            hasConjuncted = addClause(hasConjuncted, clause, SQL_VISIBLE_CLAUSE);
        } else if (visbility == Visibility.HIDDEN) {
        	hasConjuncted = addClause(hasConjuncted, clause, SQL_HIDDEN_CLAUSE);
        } else if (visbility == Visibility.FLAGGED) {
        	hasConjuncted = addClause(hasConjuncted, clause, SQL_FLAGGED_CLAUSE);
        } 
        if (!StringUtils.isEmpty(textFilter)) {
        	select.append(SQL_SEARCH_JOIN);
        	hasConjuncted = addClause(hasConjuncted, clause, SQL_MATCH_CLAUSE);
        }
        if (boundingBox != null) {
        	select.append(SQL_GEO_JOIN);
        	hasConjuncted = addClause(hasConjuncted, clause, SQL_MBRWITHIN_CLAUSE);
        }
        if (timeRange != null) {
        	addClause(hasConjuncted, clause, SQL_TIMERANGE_CLAUSE);
        }
        clause.append(SQL_ORDER_CLAUSE);
        select.append(clause);

        // Note: Hibernate always uses prepared statements
        SQLQuery sqlQuery = this.sessionFactory.getCurrentSession()
                .createSQLQuery(select.toString());
        
        if (boundingBox != null) {
            sqlQuery.setString("geom_text", boundingBox.toText());
        }
        if (!StringUtils.isEmpty(textFilter)) {
            sqlQuery.setString("search_text", textFilter);
        }
        if (timeRange != null) {
            sqlQuery.setBigInteger("earliest", BigInteger.valueOf(timeRange.getBegin().getTime()));
            sqlQuery.setBigInteger("latest", BigInteger.valueOf(timeRange.getEnd().getTime()));
        }
        return sqlQuery;
    }
    
    private boolean addClause(boolean hasConjuncted, StringBuffer clause, String sql) {
    	if (!hasConjuncted) {
    		hasConjuncted = true;
    		clause.append(SQL_WHERE);
    	} else {
    		clause.append(SQL_AND);
    	}
    	clause.append(sql);
    	return hasConjuncted;
    }

}
