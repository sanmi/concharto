package com.tech4d.tsm.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.util.GeometryType;
import com.tech4d.tsm.util.LapTimer;
import com.tech4d.tsm.util.LatLngBounds;
import com.tech4d.tsm.util.ProximityHelper;
import com.vividsolutions.jts.geom.Geometry;

@Transactional
public class EventSearchServiceHib implements EventSearchService {
	
    private static final String PARAM_GEOM_TEXT = "geom_text";
	private static final String PARAM_SEARCH_TEXT = "search_text";
	private static final String PARAM_LATEST = "latest";
	private static final String PARAM_EARLIEST = "earliest";
	private static final String PARAM_TAG = "tag";
	private SessionFactory sessionFactory;
    protected final Log log = LogFactory.getLog(getClass());

    private static final String SQL_PREFIX_GET_COUNT = "SELECT count(*) "; 
    private static final String SQL_PREFIX_SEARCH = "SELECT * "; 
    private static final String SQL_SELECT_STUB = " FROM Event ev ";
     
    private static final String SQL_GEO_JOIN ="INNER JOIN TsGeometry AS g ON ev.tsgeometry_id = g.id ";
    private static final String SQL_SEARCH_JOIN ="INNER JOIN EventSearchText AS es ON ev.eventsearchtext_id = es.id ";
    private static final String SQL_TIME_JOIN="INNER JOIN TimePrimitive AS t ON ev.when_id = t.id ";
    private static final String SQL_TAG_JOIN="INNER JOIN Event_UserTag AS ev_tag ON ev.id =ev_tag.Event_id " + 
    	" INNER JOIN UserTag AS tag ON tag.id = ev_tag.userTags_id"; 
    private static final String SQL_WHERE = " WHERE ";
    private static final String SQL_AND = " AND ";
    
    private static final String SQL_TIMERANGE_CLAUSE = 
        "((t.begin >= :earliest AND t.begin < :latest) OR " +  
        " (t.end > :earliest AND t.end <= :latest) OR " +
        " (t.begin < :earliest AND t.end > :latest)) ";
    private static String SQL_TIMERANGE_EXCLUDE_OVERLAPS_CLAUSE = 
        " (t.begin between :earliest AND :latest) AND " +  
        " (t.end between :earliest AND :latest)";

    private static final String SQL_VISIBLE_CLAUSE = " NOT(ev.visible  <=> false) ";
    private static final String SQL_HIDDEN_CLAUSE = " ev.visible  <=> false ";
    private static final String SQL_FLAGGED_CLAUSE = " ev.hasUnresolvedFlag = true ";
    //TODO I think this will be a performance problem when the DB gets large!!
    private static final String SQL_TAG_CLAUSE = " upper(tag.tag) = upper(:tag) ";
    	
    private static final String SQL_MBRWITHIN_CLAUSE = 
        " MBRIntersects(geometryCollection, Envelope(GeomFromText(:geom_text))) ";

    private static final String SQL_MATCH_CLAUSE = 
        " MATCH (es.summary, es._where, es.usertags, es.description, es.source) AGAINST (:search_text IN BOOLEAN MODE) ";

    private static final String SQL_ORDER_CLAUSE = " order by t.begin asc, ev.summary asc";

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
     * @see com.tech4d.tsm.service.EventSearchService#getTotalCount()
     */
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
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.service.EventSearchService#findById()
     */
    public Event findById(Long id) {
        return (Event) this.sessionFactory.getCurrentSession().get(
                Event.class, id);
    }

    /*
     * @see com.tech4d.tsm.service.EventSearchService#getCount
     */
	public Long getCount(LatLngBounds bounds, SearchParams params) {
        Long totalResults = 0L;
		if (bounds != null) {
	    	Set<Geometry> boxes =ProximityHelper.getBoundingBoxes(bounds.getSouthWest(), bounds.getNorthEast());
	        //There are 1 or 2 bounding boxes (see comment above)
	        for (Geometry boundingBox : boxes) {
	        	totalResults += getCountInternal(boundingBox, params);
	        }
		} else {
			totalResults = getCountInternal(null, params);
		}
		return totalResults;
	}

    /*
     * @see com.tech4d.tsm.service.EventSearchService#getCount
     */
    @SuppressWarnings("unchecked")
	private Long getCountInternal(Geometry boundingBox, SearchParams params) {
        LapTimer timer = new LapTimer(this.log);
        SQLQuery sqlQuery = createQuery(SQL_PREFIX_GET_COUNT, boundingBox, params);
        List result = sqlQuery.addScalar("count(*)", Hibernate.LONG).list();
        timer.timeIt("count").logInfoTime();
        return (Long) result.get(0);
    }

    /*
     * @see com.tech4d.tsm.service.EventSearchService#search
     */
    public List<Event> search(int maxResults, int firstResult, 
            LatLngBounds bounds, SearchParams params) {
        List<Event> events = new ArrayList<Event>();
    	if (bounds != null) {
        	Set<Geometry> boxes = ProximityHelper.getBoundingBoxes(bounds.getSouthWest(), bounds.getNorthEast());
            //There are 1 or 2 bounding boxes (see comment above)
            for (Geometry boundingBox : boxes) {
            	if (log.isDebugEnabled()) {
            		log.debug(boundingBox.toText());
            	}
                List<Event> results = searchInternal(maxResults, firstResult, 
                		boundingBox, params);

                //to fix mysql spatial search weaknesses
                removeNonIntersectingPolys(boundingBox, results);
               
				//add what is left
				events.addAll(results);
               
            }

            //if there were two boxes, one on either side of the international date line, we
            //may have twice as many records as we need so we have to sort and then strip off
            //the excess
            if (boxes.size() > 1) {
                sortByDate(events);
                removeExcess(events, maxResults);
            }
    		
    	} else {
    		events = searchInternal(maxResults, firstResult, null, params);
    	}
        return events;
    }

    /**
     *                 
     * Mysql doesn't implement polygon/polyline searching to spec.  We only want to show 
     * objects that have at least one point in the bounding box, but mysql will return 
     * a result if the bounding box of the line intersects with the bounding box of the 
     * search, event though the points on the line are not within the bounding box of 
     * the line.  See issue TSM-323. 
	 *
     * @param bounds
     * @param events
     */
	private void removeNonIntersectingPolys(Geometry boundingBox, List<Event> events) {

		Iterator<Event> it = events.iterator(); 
		while (it.hasNext()) {
			Event event = it.next();
			Geometry geom = event.getTsGeometry().getGeometry();
			if (GeometryType.getGeometryType(geom) != GeometryType.POINT) {
				if (!(boundingBox.intersects(geom))) {
					it.remove();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void sortByDate(List<Event> events) {
		Collections.sort(events, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				Event event0 = (Event) arg0;
				Event event1 = (Event) arg1;
				return(event0.getWhen().getBegin().getDate().compareTo(event1.getWhen().getBegin().getDate()));
			}
		  });
	}

	private void removeExcess(List<Event> events, int maxRecords) {
		int originalSize = events.size();
		for (int i=0; i<originalSize; i++) {
			if (i >= maxRecords) {
				events.remove(events.size()-1);
			}
		}
		
	}


    @SuppressWarnings("unchecked")
    private List<Event> searchInternal(int maxResults, int firstResult, 
            Geometry boundingBox, SearchParams params) {
        LapTimer timer = new LapTimer(this.log);
        SQLQuery sqlQuery = createQuery(SQL_PREFIX_SEARCH, boundingBox, params);
               
        List<Event> events = sqlQuery
            .addEntity(Event.class)
            .setMaxResults(maxResults)
            .setFirstResult(firstResult)
            .list(); 
        timer.timeIt("search").logInfoTime();
        return events;
    }

    private SQLQuery createQuery(String prefix, Geometry boundingBox, SearchParams params) {
        StringBuffer select = new StringBuffer(prefix).append(SQL_SELECT_STUB);
    	select.append(SQL_TIME_JOIN); //always join on time, so we can order by time
        StringBuffer clause = new StringBuffer();
        boolean hasConjuncted = false;
        if (params.getVisibility() == Visibility.NORMAL) {
            hasConjuncted = addClause(hasConjuncted, clause, SQL_VISIBLE_CLAUSE);
        } else if (params.getVisibility() == Visibility.HIDDEN) {
        	hasConjuncted = addClause(hasConjuncted, clause, SQL_HIDDEN_CLAUSE);
        } else if (params.getVisibility() == Visibility.FLAGGED) {
        	hasConjuncted = addClause(hasConjuncted, clause, SQL_FLAGGED_CLAUSE);
        } 
        if (!StringUtils.isEmpty(params.getTextFilter())) {
        	select.append(SQL_SEARCH_JOIN);
        	hasConjuncted = addClause(hasConjuncted, clause, SQL_MATCH_CLAUSE);
        }
        if (boundingBox != null) {
        	select.append(SQL_GEO_JOIN);
        	hasConjuncted = addClause(hasConjuncted, clause, SQL_MBRWITHIN_CLAUSE);
        }
        if (params.getTimeRange() != null) {
        	if (params.isIncludeTimeRangeOverlaps()) {
            	addClause(hasConjuncted, clause, SQL_TIMERANGE_CLAUSE);
        	} else {
            	addClause(hasConjuncted, clause, SQL_TIMERANGE_EXCLUDE_OVERLAPS_CLAUSE);
        	}
        }
        if (!StringUtils.isEmpty(params.getUserTag())) {
        	select.append(SQL_TAG_JOIN);
        	hasConjuncted = addClause(hasConjuncted, clause, SQL_TAG_CLAUSE);
        }
        clause.append(SQL_ORDER_CLAUSE);
        select.append(clause);

        // Note: Hibernate always uses prepared statements
        SQLQuery sqlQuery = this.sessionFactory.getCurrentSession()
                .createSQLQuery(select.toString());
        
        if (boundingBox != null) {
            sqlQuery.setString(PARAM_GEOM_TEXT, boundingBox.toText());
        }
        if (!StringUtils.isEmpty(params.getTextFilter())) {
            sqlQuery.setString(PARAM_SEARCH_TEXT, params.getTextFilter());
        }
        if (!StringUtils.isEmpty(params.getUserTag())) {
        	sqlQuery.setString(PARAM_TAG, StringUtils.trim(params.getUserTag()));
        }
        if (params.getTimeRange() != null) {
            sqlQuery.setBigInteger(PARAM_EARLIEST, BigInteger.valueOf(params.getTimeRange().getBegin().getDate().getTime()));
            sqlQuery.setBigInteger(PARAM_LATEST, BigInteger.valueOf(params.getTimeRange().getEnd().getDate().getTime()));
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
