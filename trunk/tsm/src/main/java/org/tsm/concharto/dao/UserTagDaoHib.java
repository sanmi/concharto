/*******************************************************************************
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tsm.concharto.dao;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;
import org.tsm.concharto.model.UserTag;
import org.tsm.concharto.model.time.TimeRange;
import org.tsm.concharto.service.tagCloud.TagCloudEntry;


/**
 * Implementation of UserTagDao
 */
@Transactional
public class UserTagDaoHib extends BaseDaoHib <UserTag> implements UserTagDao {

    private static final String SQL_TAG_COUNTS = 
        "Select t.tag as tag, count(*) as count, e.catalog as catalog FROM UserTag AS t " +
        "LEFT JOIN Event_UserTag as eut ON t.id = eut.userTags_id " +
        "LEFT JOIN Event as e ON e.id = eut.Event_id " +
        "WHERE (e.visible = true  OR e.visible is null) " +
        "AND ((t.created > :start) AND (t.created <= :end)) " +
        "GROUP BY t.tag, e.catalog " +
        "ORDER BY t.tag asc "; 

    private static final String SQL_TAG_COUNTS_BY_EVENT_DATE = 
        "Select t.tag as tag, count(*) as count, e.catalog as catalog FROM Event e " +
        "LEFT JOIN Event_UserTag as eut ON e.id = eut.Event_id " +
        "LEFT JOIN UserTag AS t ON eut.userTags_id = t.id " +
        "LEFT JOIN TimePrimitive as tp ON e.when_id = tp.id " +
        "WHERE (e.visible = true  OR e.visible is null) " +
        "AND ((tp.begin > :start) AND (tp.begin <= :end)) " +
        "GROUP BY t.tag, e.catalog " +
        "ORDER BY t.tag asc "; 

    public UserTagDaoHib() {
        super(UserTag.class);
    }

    public List<TagCloudEntry> getTagCounts(int daysBack) {
        //calculate the date of days back
        Calendar cal = Calendar.getInstance();
        Date end = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, -daysBack);
        Date start = cal.getTime();
        
        return getTagCounts(new TimeRange(start, end));
    }

    @SuppressWarnings("unchecked")
    public List<TagCloudEntry> getTagCounts(TimeRange timeRange) {
        Date start = timeRange.getBegin().getDate();
        Date end = timeRange.getEnd().getDate();
        List<TagCloudEntry> list = getSessionFactory().getCurrentSession().
        createSQLQuery(SQL_TAG_COUNTS).
        		setResultTransformer(Transformers.aliasToBean(TagCloudEntry.class)).
                setTimestamp("start", start).
                setTimestamp("end", end).
                list();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<TagCloudEntry> getTagCountsByEventBeginDate(TimeRange timeRange) {
        Date start = timeRange.getBegin().getDate();
        Date end = timeRange.getEnd().getDate();
        List<TagCloudEntry> list = getSessionFactory().getCurrentSession().
            createSQLQuery(SQL_TAG_COUNTS_BY_EVENT_DATE).
            		setResultTransformer(Transformers.aliasToBean(TagCloudEntry.class)).
                    setBigInteger("start", BigInteger.valueOf(start.getTime())).
                    setBigInteger("end", BigInteger.valueOf(end.getTime())).
                    list();
        return list;
    }

	@SuppressWarnings("unchecked")
	public List<String> getDistinctCatalogs() {
		return getSessionFactory().getCurrentSession().
			createSQLQuery("select distinct catalog from event").list();
	}

    
}
