package com.tech4d.tsm.audit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.dao.AuditEntryDao;
import com.tech4d.tsm.dao.StyleUtil;
import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.dao.EventTesterDao;
import com.tech4d.tsm.dao.EventUtil;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.audit.AuditEntry;
import com.tech4d.tsm.util.ContextUtil;
import com.vividsolutions.jts.io.ParseException;

@Transactional
public class IntegrationTestAuditEntry {

    private static final int MAX_RESULTS = 100;
    private static EventDao eventDao;
    private static AuditEntryDao auditEntryDao;

    private static EventUtil eventUtil;

    private Date begin;
    private Date end;

    @Before
    public void setUp() {
        Calendar cal = new GregorianCalendar(107 + 1900, 8, 22, 12, 22, 3);
        cal.set(Calendar.MILLISECOND, 750);
        begin = cal.getTime();
        cal.set(Calendar.SECOND, 35);
        end = cal.getTime();
    }

    @BeforeClass
    public static void setUpClass() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        eventDao = (EventDao) appCtx.getBean("eventDao");
        EventTesterDao eventTesterDao = (EventTesterDao) appCtx.getBean("eventTesterDao");
        auditEntryDao = (AuditEntryDao) appCtx.getBean("auditEntryDao");
        eventUtil = new EventUtil(eventTesterDao.getSessionFactory());
        eventTesterDao.deleteAll();
        StyleUtil.setupStyle();
    }

    /**
     * 
     * @throws ParseException e
     * @throws InterruptedException e
     */
    @Test
    public void testSaveAndResave() throws ParseException, InterruptedException {
        Event event = eventUtil.createEvent(begin, end);
        event.setDescription("This is some description.");
        Serializable id = eventDao.save(event);
        Event returned = eventDao.findById((Long) id);
        event.setDescription("sdfsdf");
        Thread.sleep(1000);
        eventDao.saveOrUpdate(event);
        //save, but don't make any changes.  Ensure no superfluous audit records
        eventDao.saveOrUpdate(event);
        eventDao.saveOrUpdate(event);
        Event returned2 = eventDao.findById((Long) id);
        assertEquals(EventUtil.filterMilliseconds(event.getCreated()), returned.getCreated());
        //make sure the last modified dates are different for the two instances we edited
        assertTrue(returned.getLastModified().compareTo(returned2.getLastModified()) != 0);
        
        //create another so that there are more than 2 total audit records
        eventDao.save(eventUtil.createEvent(begin, end));
        
        //now ensure two audit entries were created for this event
        List<AuditEntry> auditEntries = auditEntryDao.getAuditEntries(event, 0, MAX_RESULTS);
        assertEquals(2, auditEntries.size());
        //go down the list (entries are ordered by newest first)
        int version = auditEntries.size() - 1;
        for (AuditEntry auditEntry : auditEntries) {
            assertEquals((long) version--, auditEntry.getVersion());
        }

        //now test retrieval by fake object
        Event empty = new Event();
        empty.setId(event.getId());
        auditEntries = auditEntryDao.getAuditEntries(empty, 0, MAX_RESULTS);
        assertEquals(2, auditEntries.size());
        
        //now test getting the count
        Long count = auditEntryDao.getAuditEntriesCount(empty);
        assertEquals(2L, (long) count);

        //now test a bad ID
        empty.setId(4344L);
        auditEntries = auditEntryDao.getAuditEntries(empty, 0, MAX_RESULTS);
        assertEquals(0, auditEntries.size());
        
        //now test getting the count
        count = auditEntryDao.getAuditEntriesCount(empty);
        assertEquals(0L, (long) count);
        
    }
  
    

}
