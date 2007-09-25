package com.tech4d.tsm.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.tech4d.tsm.model.TsEvent;
import com.tech4d.tsm.model.User;
import com.tech4d.tsm.model.UserTag;
import com.tech4d.tsm.model.geometry.Style;
import com.tech4d.tsm.model.geometry.TimePrimitive;
import com.tech4d.tsm.model.geometry.TimeRange;
import com.tech4d.tsm.model.geometry.TsGeometry;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class TsEventUtil {
    public SessionFactory sessionFactory;
    private Date begin;
    private Date end;

    /**
     * Instantiate this utility with a hibernate session factory so 
     * that we can refresh objects
     * @param sessionFactory
     */
    public TsEventUtil(SessionFactory sessionFactory) {
        super();
        this.sessionFactory = sessionFactory;
        Calendar cal = new GregorianCalendar(107 + 1900, 8, 22, 12, 22, 3);
        cal.set(Calendar.MILLISECOND, 750);
        begin = cal.getTime();
        cal.set(Calendar.SECOND, 35);
        end = cal.getTime();
    }

    public Date getBegin() {
        return begin;
    }

    public Date getEnd() {
        return end;
    }

    public TsEvent createTsEvent() throws ParseException {
        return createTsEvent(new Date(), new Date());
    }

    public TsEvent createTsEvent(Date begin, Date end) throws ParseException {
        return createTsEvent(new WKTReader().read("POINT (20 20)"), new TimeRange(begin, end));

    }

    public TsEvent createTsEvent(Geometry geometry, TimeRange timeRange) {
        return createTsEvent(geometry, timeRange, StyleUtil.getStyle(), null);
    }

    public TsEvent createTsEvent(Geometry geometry, TimePrimitive timePrimitive,
            String description) {
        return createTsEvent(geometry, timePrimitive, StyleUtil.getStyle(), description);
    }

    public TsEvent createTsEvent(Geometry geometry, TimePrimitive timePrimitive,
            Style style, String description) {
        TsEvent tsEvent = new TsEvent();
        tsEvent.setStreetAddress("17 Mockinbird Ln, Nameless, TN, 60606");
        tsEvent.setSnippet("This is like some sort of small description yo");
        tsEvent.setDescription(description);

        TsGeometry tsPoint = new TsGeometry(geometry);
        tsEvent.setTsGeometry(tsPoint);
        tsEvent.setTimePrimitive(timePrimitive);
        tsEvent.setStyleSelector(style);

        List<User> people = new ArrayList<User>();
        people.add(new User("Joe", "1234", "f@joe.com"));
        people.add(new User("Mary", "1234", "m@mary.com"));
        tsEvent.setContributors(people);
        tsEvent.setSourceUrl("http://www.wikipedia.com");

        List<UserTag> tags = new ArrayList<UserTag>();
        tags.add(new UserTag("tag a"));
        tags.add(new UserTag("tag b"));
        tags.add(new UserTag("tag b"));
        tsEvent.setUserTags(tags);
        return tsEvent;
    }
    
    public void assertEquivalent(TsEvent expected, TsEvent actual) {
        SessionFactory sessionFactory = this.sessionFactory;
        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
        session.refresh(actual);

        Date correctedDate = filterMilliseconds(((TimeRange) (expected.getTimePrimitive()))
                .getBegin());
        assertEquals(correctedDate, ((TimeRange) (actual.getTimePrimitive())).getBegin());
        Style expectedStyle = (Style) expected.getStyleSelector();
        Style actualStyle = (Style) actual.getStyleSelector();
        assertEquals(
                expectedStyle.getBaloonStyle().getBgColor(), 
                actualStyle.getBaloonStyle().getBgColor());
        assertEquals(expected.getContributors().size(), actual.getContributors().size());
        assertEquals(expected.getUserTags().size(), actual.getUserTags().size());

        session.close();
    }

    public Date filterMilliseconds(Date date) {
        // NOTE: MySQL doesn't store dates with millisecond precision, so we
        // need to strip out
        // the msec in order to compare
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MILLISECOND, 0);

        Date correctedBegin = cal.getTime();
        return correctedBegin;
    }

}
