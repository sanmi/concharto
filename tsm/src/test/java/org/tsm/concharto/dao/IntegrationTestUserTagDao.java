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

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.tsm.concharto.dao.UserTagDao;
import org.tsm.concharto.model.UserTag;
import org.tsm.concharto.service.tagCloud.TagCloudEntry;
import org.tsm.concharto.util.ContextUtil;
import org.tsm.concharto.util.TimeRangeFormat;

import com.ibm.icu.util.Calendar;

/**
 * Test UserTagDao
 */
public class IntegrationTestUserTagDao {
    private static UserTagDao userTagDao;
    private static EventTesterDao eventTesterDao;

    @BeforeClass
    public static void setupClass() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        userTagDao = (UserTagDao) appCtx.getBean("userTagDao");
        eventTesterDao = (EventTesterDao) appCtx.getBean("eventTesterDao");
    }
    
    @Before public void setUp() {
        eventTesterDao.deleteAll();
    }
    
    @Test public void tagCounts() throws ParseException {
        String tag1 = "tag1";
        String tag2 = "tag2";
        userTagDao.save(new UserTag(tag1));
        userTagDao.save(new UserTag(tag1));
        userTagDao.save(new UserTag(tag2));

        UserTag userTag = new UserTag(tag2);
        userTagDao.save(userTag);
        //now modify the create date to be older than 20 days ago
        //it should not show up in the results
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -21);
        userTag.setCreated(cal.getTime());
        userTagDao.saveOrUpdate(userTag);
        
        List<TagCloudEntry> tagCloudEntries = userTagDao.getTagCounts(20);
        //results are in alphabetic order
        assertEquals(BigInteger.valueOf(2), tagCloudEntries.get(0).getCount());
        assertEquals(BigInteger.valueOf(1), tagCloudEntries.get(1).getCount());
        
        assertEquals(4, userTagDao.findAll(50).size());
        
        assertEquals(2, userTagDao.getTagCounts(TimeRangeFormat.parse("2000-2009")).size());
        //just trolling for exceptions here
        assertEquals(0, userTagDao.getTagCountsByEventBeginDate(TimeRangeFormat.parse("2000-2009")).size());
    }
    
}
