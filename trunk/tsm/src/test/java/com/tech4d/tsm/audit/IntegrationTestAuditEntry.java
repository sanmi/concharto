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
import com.tech4d.tsm.dao.TsEventDao;
import com.tech4d.tsm.dao.TsEventTesterDao;
import com.tech4d.tsm.dao.TsEventUtil;
import com.tech4d.tsm.model.TsEvent;
import com.tech4d.tsm.model.audit.AuditEntry;
import com.tech4d.tsm.util.ContextUtil;
import com.vividsolutions.jts.io.ParseException;

@Transactional
public class IntegrationTestAuditEntry {

    private static TsEventDao tsEventDao;
    private static TsEventTesterDao tsEventTesterDao;
    private static AuditEntryDao auditEntryDao;

    private static TsEventUtil tsEventUtil;

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
        tsEventDao = (TsEventDao) appCtx.getBean("tsEventDao");
        tsEventTesterDao = (TsEventTesterDao) appCtx.getBean("tsEventTesterDao");
        auditEntryDao = (AuditEntryDao) appCtx.getBean("auditEntryDao");
        tsEventUtil = new TsEventUtil(tsEventTesterDao.getSessionFactory());
        tsEventTesterDao.deleteAll();
        StyleUtil.setupStyle();
    }
    
    /**
     * Tests Auditing
     * @throws ParseException
     * @throws InterruptedException
     */
    @Test
    public void testSaveAndResave() throws ParseException, InterruptedException {
        TsEvent event = tsEventUtil.createTsEvent(begin, end);
        event.setDescription("This is some description.");
        Serializable id = tsEventDao.save(event);
        TsEvent returned = tsEventDao.findById((Long) id);
        event.setDescription("sdfsdf");
        Thread.sleep(1000);
        tsEventDao.saveOrUpdate(event);
        //save, but don't make any changes.  Ensure no superfluous audit records
        tsEventDao.saveOrUpdate(event);
        tsEventDao.saveOrUpdate(event);
        TsEvent returned2 = tsEventDao.findById((Long) id);
        assertEquals(TsEventUtil.filterMilliseconds(event.getCreated()), returned.getCreated());
        //make sure the last modified dates are different for the two instances we edited
        assertTrue(returned.getLastModified().compareTo(returned2.getLastModified()) != 0);
        
        //create another so that there are more than 2 total audit records
        tsEventDao.save(tsEventUtil.createTsEvent(begin, end));
        
        //now ensure two audit entries were created for this event
        List<AuditEntry> auditEntries = auditEntryDao.getAuditEntries(event, 0, 100);
        assertEquals(2, auditEntries.size());
        for (int i=0; i<auditEntries.size(); i++) {
            AuditEntry auditEntry = auditEntries.get(i);
            assertEquals(new Long(i), auditEntry.getVersion());
        }
    }
  

}
