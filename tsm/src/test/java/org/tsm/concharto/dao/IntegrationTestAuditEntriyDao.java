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
package org.tsm.concharto.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.tsm.concharto.model.Event;
import org.tsm.concharto.util.ContextUtil;

import com.vividsolutions.jts.io.ParseException;

/**
 * Verify AuditEntryDao
 *
 */
public class IntegrationTestAuditEntriyDao extends BaseEventIntegrationTest {

    private static AuditEntryDao auditEntryDao;
    private static final int MAX_RESULTS = 10;
    private static final int FIRST_RESULT = 0;
    private static final String USERNAME1 = "bob";
    private static final String USERNAME2 = "joe";
    private static final String CATALOG1 = "www";
    private static final String CATALOG2 = "boats";
    
    

    @BeforeClass
    public static void setUpClass() {
        baseSetUpClass();
        ApplicationContext appCtx = ContextUtil.getCtx();
        auditEntryDao = (AuditEntryDao) appCtx.getBean("auditEntryDao");
    }

    @Before public void setUp() {
        setupUserContext(USERNAME1);
        getEventTesterDao().deleteAll();
    }

    @Test
    public void testDao() throws ParseException {
        Event visibleEvent = makeEvent(true); //1
        visibleEvent.setDescription("sdfsdf");
        getEventDao().saveOrUpdate(visibleEvent); //2
        makeEvent(null); //3
        Event hiddenEvent = makeEvent(false);  //shouldn't count
        setupUserContext(USERNAME2); 
        makeEvent(false); // different user, visible shouldn't count
        makeEvent(true); //4, different user
        makeEvent(true, CATALOG2); //5, different catalog
        

        assertEquals(4, auditEntryDao.getLatestAuditEntries(CATALOG1, Event.class, FIRST_RESULT, MAX_RESULTS).size());
        assertEquals(1, auditEntryDao.getLatestAuditEntries(CATALOG2, Event.class, FIRST_RESULT, MAX_RESULTS).size());
        assertEquals(2, auditEntryDao.getAuditEntries(visibleEvent, FIRST_RESULT, MAX_RESULTS).size());
        assertEquals(3, auditEntryDao.getAuditEntries(CATALOG1, USERNAME1, Event.class, FIRST_RESULT, MAX_RESULTS).size());
        assertEquals(1, auditEntryDao.getAuditEntries(CATALOG1, USERNAME2, Event.class, FIRST_RESULT, MAX_RESULTS).size());
        assertEquals(new Long(2), auditEntryDao.getAuditEntriesCount(visibleEvent));
        assertEquals(new Long(1), auditEntryDao.getAuditEntriesCount(hiddenEvent));
        assertEquals(1, auditEntryDao.getAuditEntries(hiddenEvent, FIRST_RESULT, MAX_RESULTS).size());

        assertEquals(new Long(4), auditEntryDao.getAuditEntriesCount(CATALOG1, Event.class));
        assertEquals(new Long(3), auditEntryDao.getAuditEntriesCount(CATALOG1, USERNAME1, Event.class));
    }

    private Event makeEvent(Boolean visible) throws ParseException {
        return makeEvent(visible, CATALOG1);
    }
    private Event makeEvent(Boolean visible, String catalog) throws ParseException {
        Event event = getEventUtil().createEvent();
        event.setVisible(visible);
        event.setCatalog(catalog);
        getEventDao().save(event);
        return event;
    }
}
