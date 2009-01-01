package com.tech4d.tsm.dao;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.ibm.icu.util.Calendar;
import com.tech4d.tsm.model.UserTag;
import com.tech4d.tsm.util.ContextUtil;
import com.tech4d.tsm.util.TimeRangeFormat;

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
        
        List<Object[]> tagCounts = userTagDao.getTagCounts(20);
        //results are in alphabetic order
        assertEquals(BigInteger.valueOf(2), tagCounts.get(0)[1]);
        assertEquals(BigInteger.valueOf(1), tagCounts.get(1)[1]);
        
        assertEquals(4, userTagDao.findAll(50).size());
        
        assertEquals(2, userTagDao.getTagCounts(TimeRangeFormat.parse("2000-2009")).size());
        //just trolling for exceptions here
        assertEquals(0, userTagDao.getTagCountsByEventBeginDate(TimeRangeFormat.parse("2000-2009")).size());
    }
    
}
