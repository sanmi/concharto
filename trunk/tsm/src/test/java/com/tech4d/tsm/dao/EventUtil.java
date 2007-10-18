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

import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.User;
import com.tech4d.tsm.model.UserTag;
import com.tech4d.tsm.model.kml.Style;
import com.tech4d.tsm.model.geometry.TsGeometry;
import com.tech4d.tsm.model.time.TimeRange;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class EventUtil {
    public SessionFactory sessionFactory;
    private Date begin;
    private Date end;
    List<User> users = new ArrayList<User>();

    /**
     * Instantiate this utility with a hibernate session factory so 
     * that we can refresh objects
     * @param sessionFactory  hibernate session factory
     */
    public EventUtil(SessionFactory sessionFactory) {
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

    public Event createEvent() throws ParseException {
        return createEvent(new Date(), new Date());
    }

    public Event createEvent(Date begin, Date end) throws ParseException {
        return createEvent(new WKTReader().read("POINT (20 20)"), new TimeRange(begin, end));

    }

    public Event createEvent(Geometry geometry, TimeRange timeRange) {
        return createEvent(geometry, timeRange, StyleUtil.getStyle(), null, null);
    }

    public Event createEvent(Geometry geometry, TimeRange timeRange,
            String description) {
        return createEvent(geometry, timeRange, StyleUtil.getStyle(), null, description);
    }

    public Event createEvent(Geometry geometry, TimeRange timeRange,
            String summary, String description) {
        return createEvent(geometry, timeRange, StyleUtil.getStyle(), summary, description);
    }

    public Event createEvent(Geometry geometry, TimeRange timeRange,
            com.tech4d.tsm.model.kml.Style style, String summary, String description) {

//        List<User> people = new ArrayList<User>();
//        people.add(new User("Joe", "1234", "f@joe.com"));
//        people.add(new User("Mary", "1234", "m@mary.com"));

        List<UserTag> tags = new ArrayList<UserTag>();
        tags.add(new UserTag("tag a"));
        tags.add(new UserTag("tag b"));
        tags.add(new UserTag("tag b"));
        return createEvent(tags, geometry, timeRange, style, summary, description) ;
    }
    
    public Event createEvent( List<UserTag> usertags, Geometry geometry, TimeRange timeRange,
            Style style, String summary, String description) {
        Event event = new Event();
        event.setWhere("17 Mockinbird Ln, Nameless, TN, 60606");
        event.setSnippet("This is like some sort of small description yo");
        event.setSummary(summary);
        event.setDescription(description);

        TsGeometry tsPoint = new TsGeometry(geometry);
        event.setTsGeometry(tsPoint);
        event.setWhen(timeRange);
        event.setStyleSelector(style);
        event.setSource("http://www.wikipedia.com");
        event.setUserTags(usertags);
        
        return event;
    }
    
    public void assertEquivalent(Event expected, Event actual) {
        SessionFactory sessionFactory = this.sessionFactory;
        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
        session.refresh(actual);

        assertEquals(expected.getWhen().getBegin(), actual.getWhen().getBegin());
        com.tech4d.tsm.model.kml.Style expectedStyle = (Style) expected.getStyleSelector();
        com.tech4d.tsm.model.kml.Style actualStyle = (Style) actual.getStyleSelector();
        assertEquals(
                expectedStyle.getBaloonStyle().getBgColor(), 
                actualStyle.getBaloonStyle().getBgColor());
        //assertEquals(expected.getContributors().size(), actual.getContributors().size());
        assertEquals(expected.getUserTags().size(), actual.getUserTags().size());

        session.close();
    }
    
    public static Date filterMilliseconds(Date date) {
        // NOTE: MySQL doesn't store dates with millisecond precision, so we
        // need to strip out
        // the msec in order to compare
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static void printTimeRange(TimeRange tr) {
        System.out.println("begin: " + tr.getBegin().getTime() 
            + ", end: " + tr.getEnd().getTime());
    }


}
