package com.tech4d.tsm.service;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.List;
import java.util.SortedMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.ibm.icu.util.Calendar;
import com.tech4d.tsm.OpenSessionInViewIntegrationTest;
import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.dao.EventTesterDao;
import com.tech4d.tsm.dao.EventUtil;
import com.tech4d.tsm.dao.StyleUtil;
import com.tech4d.tsm.dao.UserTagDao;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.UserTag;
import com.tech4d.tsm.model.time.TimeRange;
import com.tech4d.tsm.util.ContextUtil;
import com.tech4d.tsm.util.TimeRangeFormat;

public class IntegrationTestTagAggregateService extends OpenSessionInViewIntegrationTest {
    private static final int MAX_FONT_FOR_TEST = 30;
    private static UserTagDao userTagDao;
    private static EventDao eventDao;
    private static EventTesterDao eventTesterDao;
    private static TagAggregateService tagAggregateService;
    private static EventUtil eventUtil;

    @BeforeClass
    public static void setupClass() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        userTagDao = (UserTagDao) appCtx.getBean("userTagDao");
        eventTesterDao = (EventTesterDao) appCtx.getBean("eventTesterDao");
        eventUtil = new EventUtil(eventTesterDao.getSessionFactory());
        tagAggregateService = (TagAggregateService) appCtx.getBean("tagAggregateService");
        eventDao = (EventDao) appCtx.getBean("eventDao");
        eventTesterDao.deleteAll();
        StyleUtil.setupStyle();
    }
    
    @Before public void setUp() {
        eventTesterDao.deleteAll();
    }

    @Test public void simple() {
        createTags(5, "tagA");
        createTags(20, "tagB");
        createTags(1, "tagC");
        createTags(40, "tagD");
        tagAggregateService.setMaxFont(MAX_FONT_FOR_TEST);
        tagAggregateService.refreshRecent();
        List<TagCloudEntry> tagCloud = tagAggregateService.getTagCloud();
        assertEquals(4, tagCloud.size());
        assertEquals(12, tagCloud.get(0).getFontSize());
        assertEquals(20, tagCloud.get(1).getFontSize());
        assertEquals(10, tagCloud.get(2).getFontSize());
        assertEquals(30, tagCloud.get(3).getFontSize());
    }

    private void createTags(int count, String tagName) {
        for (int i=0; i<count; i++) {
            userTagDao.save(new UserTag(tagName));
        }
    }

    @Test public void tagIndex() throws ParseException {
        createEvents(10, "tagA", "2001");
        createEvents(10, "tagA", "1920-2000");
        createEvents(2, "tagA", "900 AD");
        createEvents(2, "tagA", "20000 BC");
        createEvents(5, "tagB", "2001");
        createEvents(5, "tagB", "901");
        createEvents(5, "tagC", "1999");
        createEvents(5, "tagD", "1920 - 2001");
        
        tagAggregateService.refreshIndex();
        SortedMap<TimeRange, List<TagCloudEntry>> tagIndex = tagAggregateService.getTagIndex();
        
        Calendar cal = Calendar.getInstance();
        assertEquals(2, tagIndex.get(TimeRangeFormat.parse("2000 - " + cal.get(Calendar.YEAR))).size() );
        assertEquals(3, tagIndex.get(TimeRangeFormat.parse("1900 - 2000")).size() );
        assertEquals(1, tagIndex.get(TimeRangeFormat.parse("800 - 900")).size() );
        assertEquals(1, tagIndex.get(TimeRangeFormat.parse("1000000 BC - 1 AD")).size() );
    }

    private void createEvents(int count, String tag, String timeRange) throws ParseException {
        TimeRange tr;
        tr = TimeRangeFormat.parse(timeRange);
        for (int i = 0; i < count; i++) {
            Event event = eventUtil.createEvent(tr);
            event.setUserTagsAsString(tag);
            eventDao.saveOrUpdate(event);
        }
    }
}
