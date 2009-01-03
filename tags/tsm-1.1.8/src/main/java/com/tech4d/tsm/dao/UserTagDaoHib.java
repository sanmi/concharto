/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version 
 * 1.1 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 * 
 * The Original Code is Concharto.
 * 
 * The Initial Developer of the Original Code is
 * Time Space Map, LLC
 * Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 * the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s):
 * Time Space Map, LLC
 * 
 * ***** END LICENSE BLOCK *****
 ******************************************************************************/
package com.tech4d.tsm.dao;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.UserTag;
import com.tech4d.tsm.model.time.TimeRange;

/**
 * Implementation of UserTagDao
 */
@Transactional
public class UserTagDaoHib extends BaseDaoHib <UserTag> implements UserTagDao {

    private static final String SQL_TAG_COUNTS = 
        "Select t.tag, count(*) FROM UserTag AS t " +
        "LEFT JOIN Event_UserTag as eut ON t.id = eut.userTags_id " +
        "LEFT JOIN Event as e ON e.id = eut.Event_id " +
        "WHERE (e.visible = true  OR e.visible is null) " +
        "AND ((t.created > :start) AND (t.created <= :end)) " +
        "GROUP BY t.tag " +
        "ORDER BY t.tag asc "; 

    private static final String SQL_TAG_COUNTS_BY_EVENT_DATE = 
        "Select t.tag, count(*) FROM Event e " +
        "LEFT JOIN Event_UserTag as eut ON e.id = eut.Event_id " +
        "LEFT JOIN UserTag AS t ON eut.userTags_id = t.id " +
        "LEFT JOIN TimePrimitive as tp ON e.when_id = tp.id " +
        "WHERE (e.visible = true  OR e.visible is null) " +
        "AND ((tp.begin > :start) AND (tp.begin <= :end)) " +
        "GROUP BY t.tag " +
        "ORDER BY t.tag asc "; 

    public UserTagDaoHib() {
        super(UserTag.class);
    }

    public List<Object[]> getTagCounts(int daysBack) {
        //calculate the date of days back
        Calendar cal = Calendar.getInstance();
        Date end = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, -daysBack);
        Date start = cal.getTime();
        
        return getTagCounts(new TimeRange(start, end));
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> getTagCounts(TimeRange timeRange) {
        Date start = timeRange.getBegin().getDate();
        Date end = timeRange.getEnd().getDate();
        List list = getSessionFactory().getCurrentSession().
            createSQLQuery(SQL_TAG_COUNTS).
                    setTimestamp("start", start).
                    setTimestamp("end", end).
                    list();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> getTagCountsByEventBeginDate(TimeRange timeRange) {
        Date start = timeRange.getBegin().getDate();
        Date end = timeRange.getEnd().getDate();
        List list = getSessionFactory().getCurrentSession().
            createSQLQuery(SQL_TAG_COUNTS_BY_EVENT_DATE).
                    setBigInteger("start", BigInteger.valueOf(start.getTime())).
                    setBigInteger("end", BigInteger.valueOf(end.getTime())).
                    list();
        return list;
    }
    

}
