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
package com.tech4d.tsm.audit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.tech4d.tsm.OpenSessionInViewIntegrationTest;
import com.tech4d.tsm.auth.ThreadLocalUserContext;
import com.tech4d.tsm.auth.UserContext;
import com.tech4d.tsm.dao.AuditEntryDao;
import com.tech4d.tsm.dao.AuditUserChange;
import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.dao.EventTesterDao;
import com.tech4d.tsm.dao.EventUtil;
import com.tech4d.tsm.dao.FlagDao;
import com.tech4d.tsm.dao.StyleUtil;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.Flag;
import com.tech4d.tsm.model.audit.AuditEntry;
import com.tech4d.tsm.model.audit.AuditFieldChange;
import com.tech4d.tsm.model.geometry.TsGeometry;
import com.tech4d.tsm.model.wiki.WikiText;
import com.tech4d.tsm.service.RevertEventService;
import com.tech4d.tsm.util.ContextUtil;
import com.tech4d.tsm.web.util.CatalogUtil;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class IntegrationTestAuditEntry extends OpenSessionInViewIntegrationTest {

    private static final String USERNAME = "bob";
    private static final String USERNAME2 = "joe";
    private static final int MAX_RESULTS = 100;
    private static EventDao eventDao;
    private static AuditEntryDao auditEntryDao;
    private static EventTesterDao eventTesterDao;
    private static RevertEventService revertEventService;
    private static EventUtil eventUtil;
	private static FlagDao flagDao;
   

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
        flagDao = (FlagDao) appCtx.getBean("flagDao");
        eventTesterDao = (EventTesterDao) appCtx.getBean("eventTesterDao");
        auditEntryDao = (AuditEntryDao) appCtx.getBean("auditEntryDao");
        revertEventService =  (RevertEventService) appCtx.getBean("revertEventService");
        eventUtil = new EventUtil(eventTesterDao.getSessionFactory());
        eventTesterDao.deleteAll();
        StyleUtil.setupStyle();
    }

    @Before public void setupUserContext() {
    	setupUserContext(USERNAME);
        eventTesterDao.deleteAll();
    }
    
    private void setupUserContext(String username) {
        UserContext userContext = new UserContext();
        userContext.setUsername(username);
        ThreadLocalUserContext.setUserContext(userContext);
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
        getSessionFactory().getCurrentSession().evict(event); //guarantees the object gets written to the DB 
        
        Thread.sleep(1000);
        Event returned = eventDao.findById((Long) id);
        returned.setDescription("sdfsdf");
        eventDao.saveOrUpdate(returned);
        getSessionFactory().getCurrentSession().evict(returned);
        //save, but don't make any changes.  
        eventDao.saveOrUpdate(returned);
        getSessionFactory().getCurrentSession().evict(returned);
        eventDao.saveOrUpdate(returned);
        getSessionFactory().getCurrentSession().evict(returned);
        Event returned2 = eventDao.findById((Long) id);
        
        assertEquals(EventUtil.filterMilliseconds(event.getCreated()), returned.getCreated());
        //make sure the last modified dates are different for the two instances we edited
        assertTrue(returned.getLastModified().compareTo(returned2.getLastModified()) != 0);
        
        //create another so that there are more than 2 total audit records
        eventDao.save(eventUtil.createEvent(begin, end));
        
        //now ensure correct number audit entries were created for this event
        List<AuditEntry> auditEntries = auditEntryDao.getAuditEntries(event, 0, MAX_RESULTS);
        assertEquals(4, auditEntries.size());
        //go down the list (entries are ordered by newest first)
        //verify username is in the field
        int version = auditEntries.size() - 1;
        for (AuditEntry auditEntry : auditEntries) {
            assertEquals((long) version--, auditEntry.getVersion());
            assertEquals(USERNAME, auditEntry.getUser());
        }

        //now test retrieval by fake object
        Event empty = new Event();
        empty.setId(event.getId());
        auditEntries = auditEntryDao.getAuditEntries(empty, 0, MAX_RESULTS);
        assertEquals(4, auditEntries.size());
        
        //now test getting the count
        Long count = auditEntryDao.getAuditEntriesCount(empty);
        assertEquals(4L, (long) count);
        
        //now test getting one of the entries
        //Session session = eventTesterDao.getSessionFactory().openSession();
        //session.refresh(auditEntries.get(0));
        Collection<AuditFieldChange> changes = auditEntries.get(0).getAuditEntryFieldChange();
        for (AuditFieldChange auditFieldChange : changes) {
            AuditFieldChange newChange = auditEntryDao.getAuditFieldChange(auditFieldChange.getId());
            assertEquals(newChange.getNewValue(), auditFieldChange.getNewValue());
		}
        //session.close();

        //now test a bad ID
        empty.setId(4344L);
        auditEntries = auditEntryDao.getAuditEntries(empty, 0, MAX_RESULTS);
        assertEquals(0, auditEntries.size());
        
        //now test getting the count
        count = auditEntryDao.getAuditEntriesCount(empty);
        assertEquals(0L, (long) count);
    }
  
    @Test public void testRevert() throws ParseException {
    	//create an event with five changes and revert each one
        Event rev0 = eventUtil.createEvent(begin, end);
        rev0.setDescription("r0 description");
        rev0.setSummary("r0 summary");
        rev0.setSource(null);
        rev0.setUserTagsAsString("r0 tag a, tag b");
        Serializable id = eventDao.save(rev0);
        freeFromSession(rev0);
        
        Event rev1 = eventDao.findById((Long) id);
        rev1.setSummary("r1 summary");
        rev1.setSource("r1 source");
        eventDao.saveOrUpdate(rev1);
        freeFromSession(rev1);
        
        Event rev2 = eventDao.findById((Long) id);
        rev2.setDescription("r2 description");
        rev2.setSummary("r2 summary");
        rev2.setTsGeometry(new TsGeometry(new WKTReader().read("POINT (3300 3530)")));
        eventDao.saveOrUpdate(rev2);
        freeFromSession(rev2);

        Event rev3 = eventDao.findById((Long) id);
        rev3.setDescription("r3 description");
        rev3.setSummary("r3 summary");
        rev3.setTsGeometry(new TsGeometry(new WKTReader().read("POINT (530 530)")));
        eventDao.saveOrUpdate(rev3);
        rev3.getFlags().size();
        freeFromSession(rev3);

        Event rev4 = eventDao.findById((Long) id);
        rev4.setUserTagsAsString("r4tags, tag b");
        rev4.setDescription("r4 description");
        eventDao.saveOrUpdate(rev4);
        freeFromSession(rev4);
        
        revertAndAssert(rev4, 4);
        getSessionFactory().getCurrentSession().flush();
        revertAndAssert(rev3, 3);
        revertAndAssert(rev2, 2);
        revertAndAssert(rev1, 1);
        revertAndAssert(rev0, 0);
    }
    
    /** 
     * Test getting auditable events by username
     * @throws ParseException
     */
    @Test public void byUsername() throws ParseException {
    	makeEvents(3);
    	setupUserContext(USERNAME2);
    	makeEvents(5);
    	assertEquals(3, auditEntryDao.getAuditEntries(USERNAME, Event.class, 0, 20).size());
    	assertEquals(3L, (long)auditEntryDao.getAuditEntriesCount(USERNAME, Event.class));
    	assertEquals(5, auditEntryDao.getAuditEntries(USERNAME2, Event.class, 0, 20).size());
    	assertEquals(5L, (long)auditEntryDao.getAuditEntriesCount(USERNAME2, Event.class));
    	//limit size of results
    	List<AuditUserChange> entries = auditEntryDao.getAuditEntries(USERNAME2, Event.class, 0, 3);
    	AuditUserChange first = entries.get(0);
    	assertEquals(3, entries.size());
    	//limit size of results, first record is different
    	entries = auditEntryDao.getAuditEntries(USERNAME2, Event.class, 1, 3);
    	AuditUserChange second = entries.get(0);
    	assertEquals(3, entries.size());
    	//check the order
    	assertTrue(first.getAuditEntry().getEntityId() < second.getAuditEntry().getEntityId());
    	//check the join.  The first event summary should be different than the second
    	assertTrue(!((Event)first.getAuditable()).getSummary().equals(((Event)second.getAuditable()).getSummary()));
    }
    
    @Test public void wikiText() throws ParseException {
    	Event event = eventUtil.createEvent();
    	WikiText wikiText = new WikiText();
    	wikiText.setText("==header== some stuff here");
    	event.setDiscussion(wikiText);
    	eventDao.saveOrUpdate(event);
    	wikiText.setText("==header== some new stuff here");
    	eventDao.saveOrUpdate(event);
    	
    	List<AuditEntry> auditEntries = auditEntryDao.getAuditEntries(wikiText,  0, 20); 
    	assertEquals(2, auditEntries.size());
    	Collection<AuditFieldChange> auditEntryFieldChanges = auditEntries.get(0).getAuditEntryFieldChange();
    	assertEquals(1, auditEntryFieldChanges.size());
    	
    }

    @Test public void testRevertWikiText() throws ParseException {
    	//create an event with five changes and revert each one
    	WikiText rev0 = new WikiText();
    	rev0.setText("==header== some stuff here");
        Serializable id = eventDao.saveAuditable(rev0);

        WikiText rev1 = (WikiText) eventDao.findById(WikiText.class, (Long) id);
        rev1.setText("==header== some extra stuff here");
        eventDao.saveOrUpdateAuditable(rev1);

        WikiText rev2 = (WikiText) eventDao.findById(WikiText.class, (Long) id);
        rev2.setText("==header== some extra stuff here and here too!");
        eventDao.saveOrUpdateAuditable(rev2);
        
        
        revertAndAssertWikiText(rev2, 2);
        revertAndAssertWikiText(rev1, 1);
        revertAndAssertWikiText(rev0, 0);
    }

    @Test public void testGetRecent() throws ParseException {
    	makeEvents(3);
    	assertEquals(3, auditEntryDao.getLatestAuditEntries(Event.class, 0, 10).size());
    	Event event = eventUtil.createEvent(begin, end);
    	eventDao.save(event);
    	event.setDescription("some new text here");
    	eventDao.saveOrUpdate(event);
    	assertEquals(5, auditEntryDao.getLatestAuditEntries(Event.class, 0, 10).size());
    	assertEquals(5L, (long)auditEntryDao.getAuditEntriesCount(Event.class));
    	
    	
    	//now add some wikitext
    	WikiText text = new WikiText();
    	text.setText("==header== some stuff here");
        eventDao.saveAuditable(text);
        text.setText("==header== some extra stuff here");
        eventDao.saveOrUpdateAuditable(text);
    	assertEquals(2, auditEntryDao.getLatestAuditEntries(WikiText.class, 0, 10).size());
    	assertEquals(2L, (long)auditEntryDao.getAuditEntriesCount(WikiText.class));
    	
    }
    
    @Test public void testFlag() throws ParseException {
    	//make an event
    	Event event = eventUtil.createEvent();
    	eventDao.save(event);
    	//add a flag
    	Flag flag = new Flag();
    	flag.setComment("sdf");
    	flag.setEvent(event);
    	List<Flag> flags = new ArrayList<Flag>();
    	flags.add(flag);
    	event.setFlags(flags);
    	event.setHasUnresolvedFlag(true);
    	eventDao.saveOrUpdate(event);
    	
        //now ensure correct number audit entries were created for this event
        List<AuditEntry> auditEntries = auditEntryDao.getAuditEntries(event, 0, MAX_RESULTS);
        assertEquals(2, auditEntries.size());
    	
    }

    @Test public void testCatalog() throws ParseException {
    	//create three events, and another from another event.
    	makeEvents(3);
    	Event event = eventUtil.createEvent();
    	String newCatalog = "sdfsdf";
    	event.setCatalog(newCatalog);
    	eventDao.save(event);

    	//change them all
    	List<Event> events = eventDao.findRecent(4, 0);
    	for (Event e : events) {
    		e.setDescription("ee");
    		eventDao.saveOrUpdate(e);
    	}
    	
    	//get all of the changes for the default catalog.  There should be 
    	//two for each event (create and change)
    	assertEquals(6, auditEntryDao.getLatestEventEntries(CatalogUtil.CATALOG_WWW, 0, 10).size());
    	//get all of the changes for the new catalog  
    	assertEquals(2, auditEntryDao.getLatestEventEntries(newCatalog, 0, 10).size());
    }
    
    private void makeEvents(int numEvents) throws ParseException {
    	for (int i=0; i<numEvents; i++) {
        	eventDao.save(eventUtil.createEvent("summary " + i));
    	}
    }
    private void revertAndAssertWikiText(WikiText expected, int rev) {
        //List<AuditEntry> auditEntries = auditEntryDao.getAuditEntries(expected, 0, MAX_RESULTS);
        WikiText reverted = (WikiText) revertEventService.revertToRevision(expected.getClass(), rev, expected.getId());
        assertEquals(expected.getText(), reverted.getText());
    }
    
    private void revertAndAssert(Event expected, int rev) {
        //List<AuditEntry> auditEntries = auditEntryDao.getAuditEntries(expected, 0, MAX_RESULTS);
        Event reverted = (Event) revertEventService.revertToRevision(Event.class, rev, expected.getId());
        eventUtil.assertEquivalent(expected, reverted);
        reverted = eventDao.findById(expected.getId());
        eventUtil.assertEquivalent(expected, reverted);
    }

    /**
     * This is to handle a problem that only occurs during integration testing, where
     * we need to evict the object so that we can have multiple different copies of it that reflect
     * it's previous states!  When we do that, the collections won't get loaded in (lazy loading)
     * so we do it by hand by accessing one of the objects.
     * 
     * @param event
     */
    private void freeFromSession(Event event) {
        if (event.getFlags() != null) {
        	event.getFlags().size();
        }
        event.getUserTags().size();
        getSessionFactory().getCurrentSession().evict(event);
    }
    

}
