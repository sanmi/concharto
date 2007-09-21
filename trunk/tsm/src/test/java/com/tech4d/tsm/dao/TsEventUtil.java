package com.tech4d.tsm.dao;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TsEventUtil {
    
    public static TsEvent createTsEvent() throws ParseException {
        return createTsEvent(new Date(), new Date());
    }

    public static TsEvent createTsEvent(Date begin, Date end) throws ParseException {
        return createTsEvent( new WKTReader()
        .read("POINT (20 20)"), new TimeRange(begin, end));
        
    }
    
    public static TsEvent createTsEvent(Geometry geometry, TimeRange timeRange) {
        return CreateTsEvent(geometry, timeRange, StyleUtil.getStyle());
    }

    public static TsEvent CreateTsEvent(Geometry geometry, TimePrimitive timePrimitive, Style style) {
        TsEvent tsEvent = new TsEvent();
        tsEvent.setStreetAddress("17 Mockinbird Ln, Nameless, TN, 60606");
        tsEvent.setSnippet("This is like some sort of small description yo");
        tsEvent
                .setDescription("This is like the full sort of small description yo with a bunch of stuff");

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

}
