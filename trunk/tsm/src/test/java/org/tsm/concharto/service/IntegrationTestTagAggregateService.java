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
package org.tsm.concharto.service;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.tsm.concharto.OpenSessionInViewIntegrationTest;
import org.tsm.concharto.dao.EventDao;
import org.tsm.concharto.dao.EventTesterDao;
import org.tsm.concharto.dao.EventUtil;
import org.tsm.concharto.dao.StyleUtil;
import org.tsm.concharto.model.Event;
import org.tsm.concharto.model.time.TimeRange;
import org.tsm.concharto.service.tagCloud.TagAggregateService;
import org.tsm.concharto.service.tagCloud.TagCloudEntry;
import org.tsm.concharto.util.ContextUtil;
import org.tsm.concharto.util.TimeRangeFormat;
import org.tsm.concharto.web.util.CatalogUtil;


public class IntegrationTestTagAggregateService extends OpenSessionInViewIntegrationTest {
    private static final String CATALOG_BOATING = "boating";
	private static final String TAG_D = "tagD";
	private static final String TAG_C = "tagC";
	private static final String TAG_B = "tagB";
	private static final String TAG_A = "tagA";
	private static final int MAX_FONT_FOR_TEST = 30;
    private static EventDao eventDao;
    private static EventTesterDao eventTesterDao;
    private static TagAggregateService tagAggregateService;
    private static EventUtil eventUtil;
    private String lastDecade; 

    @BeforeClass
    public static void setupClass() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        eventTesterDao = (EventTesterDao) appCtx.getBean("eventTesterDao");
        eventUtil = new EventUtil(eventTesterDao.getSessionFactory());
        tagAggregateService = (TagAggregateService) appCtx.getBean("tagAggregateService");
        eventDao = (EventDao) appCtx.getBean("eventDao");
        eventTesterDao.deleteAll();
        StyleUtil.setupStyle();
    }
    
    @Before public void setUp() {
        eventTesterDao.deleteAll();
        Calendar cal = Calendar.getInstance();
        lastDecade = "2000 - " + cal.get(Calendar.YEAR);
    }

    @Test public void simple() throws ParseException {
        createEvents(5, TAG_A, "2009");
        createEvents(20, TAG_B, "2009");
        createEvents(1, TAG_C, "2009");
        createEvents(40, TAG_D, "2009");
        tagAggregateService.setMaxFont(MAX_FONT_FOR_TEST);
        tagAggregateService.refresh();
        List<TagCloudEntry> tagCloud = tagAggregateService.getRecent(CatalogUtil.CATALOG_WWW);
        assertEquals(4, tagCloud.size());
        assertEquals((Integer)12, tagCloud.get(0).getFontSize());
        assertEquals((Integer)20, tagCloud.get(1).getFontSize());
        assertEquals((Integer)10, tagCloud.get(2).getFontSize());
        assertEquals((Integer)30, tagCloud.get(3).getFontSize());
    }

    @Test public void small() throws ParseException {
        createEvents(1, TAG_A, "2009");
        createEvents(1, TAG_B, "2009");
        tagAggregateService.setMaxFont(MAX_FONT_FOR_TEST);
        tagAggregateService.refresh();
        List<TagCloudEntry> tagCloud = tagAggregateService.getRecent(CatalogUtil.CATALOG_WWW);
        assertEquals(2, tagCloud.size());
        assertEquals((Integer)MAX_FONT_FOR_TEST, tagCloud.get(0).getFontSize());
        assertEquals((Integer)MAX_FONT_FOR_TEST, tagCloud.get(1).getFontSize());
    }

    @Test public void tagIndex() throws ParseException {
        createEvents(10, TAG_A, "2001");
        createEvents(10, TAG_A, "1920-2000");
        createEvents(2, TAG_A, "900 AD");
        createEvents(2, TAG_A, "20000 BC");
        createEvents(5, TAG_B, "2001");
        createEvents(5, TAG_B, "901");
        createEvents(5, TAG_C, "1999");
        createEvents(5, TAG_D, "1920 - 2001");
        
        tagAggregateService.refresh();
        SortedMap<TimeRange, List<TagCloudEntry>> tagIndex = tagAggregateService.getTagIndex(CatalogUtil.CATALOG_WWW);
        
        assertEquals(2, tagIndex.get(TimeRangeFormat.parse(lastDecade)).size() );
        assertEquals(3, tagIndex.get(TimeRangeFormat.parse("1900 - 2000")).size() );
        assertEquals(1, tagIndex.get(TimeRangeFormat.parse("800 - 900")).size() );
        assertEquals(1, tagIndex.get(TimeRangeFormat.parse("1000000 BC - 1 AD")).size() );
    }
    
    @Test public void multipleCatalogs() throws ParseException {
    	//create 2 tags for two catalogs, one overlap
        createEvents(10, TAG_A, "2001", CATALOG_BOATING);
        createEvents(5,  TAG_B, "2001", CATALOG_BOATING);
        createEvents(5,  TAG_C, "2001", CATALOG_BOATING);
        createEvents(10, TAG_A, "2001", CatalogUtil.CATALOG_WWW);
        createEvents(10, TAG_D, "2001", CatalogUtil.CATALOG_WWW);

        tagAggregateService.refresh();
        
        //verify getRecent
        List<TagCloudEntry> tagCloud = tagAggregateService.getRecent(CatalogUtil.CATALOG_WWW);
        assertEquals(2, tagCloud.size());
        tagCloud = tagAggregateService.getRecent(CatalogUtil.CATALOG_WWW);
        assertEquals(2, tagCloud.size());

        //verify getTagIndex
        SortedMap<TimeRange, List<TagCloudEntry>> tagIndex = tagAggregateService.getTagIndex(CatalogUtil.CATALOG_WWW);
        assertEquals(2, tagIndex.get(TimeRangeFormat.parse(lastDecade)).size() );
        tagIndex = tagAggregateService.getTagIndex(CATALOG_BOATING);
        assertEquals(3, tagIndex.get(TimeRangeFormat.parse(lastDecade)).size() );
        
    }

    private void createEvents(int count, String tag, String timeRange) throws ParseException {
    	createEvents(count, tag, timeRange, CatalogUtil.CATALOG_WWW); 
    }
    
    private void createEvents(int count, String tag, String timeRange, String catalog ) throws ParseException {
        TimeRange tr;
        tr = TimeRangeFormat.parse(timeRange);
        for (int i = 0; i < count; i++) {
            Event event = eventUtil.createEvent(tr);
            event.setCatalog(catalog);
            event.setUserTagsAsString(tag);
            eventDao.saveOrUpdate(event);
        }
    }
}
