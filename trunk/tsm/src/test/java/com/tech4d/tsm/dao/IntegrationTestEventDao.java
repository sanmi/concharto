package com.tech4d.tsm.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.User;
import com.tech4d.tsm.util.ContextUtil;
import com.vividsolutions.jts.io.ParseException;

/**
 * Test all dao functions
 * NOTE: the order of tests is important in the integration test
 * @author frank
 *
 */
public class IntegrationTestEventDao  {

    private static EventDao eventDao;
    private static Event event = new Event();
    private static boolean initialized;

    @BeforeClass
    public static void setUp() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        eventDao = (EventDao) appCtx.getBean("eventDao");
        if (!initialized) {
            initialized = true;
            eventDao.deleteAll();
            StyleUtil.setupStyle();
        }
    }
    
    @Test
    public void testSave() throws ParseException {
        event = createEvent("something is happening", new Date());
        Serializable id = eventDao.save(event);
        assertNotNull (id);
        assertEvents(1);
    }
    
    @Test
    public void testFindAll() {
        Collection<Event> events = eventDao.findAll();
        for (Event event : events) {
            System.out.println("event " + event.getId() + ", "  + event.getSourceUrl());
        }
        assertEquals(1, events.size());
        
        SessionFactory sessionFactory = eventDao.getSessionFactory();
//        Session session = sessionFactory.openSession();
//        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
//        session.beginTransaction();
//        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));

        //Just for testing we will use OpenSessionInViewInterceptor for the real stuff
        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
//        ThreadLocalSessionContext.bind(session);
//        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));

        for (Event event : events) {
            session.refresh(event);
            assertEquals(2, event.getContributors().size());
        }
        session.close();
    }
    
    @Test
    public void testSaveOrUpdate() {
        String title = "a new title";
//        event.setTitle(title);
        eventDao.saveOrUpdate(event);
//        assertEquals(title, eventDao.findById(event.getId()).getTitle());
    }
    
    @Test
    public void testFindById() {
//        assertEquals(event.getTitle(), eventDao.findById(event.getId()).getTitle());
    }
    
    
    @Test
    public void testDelete() {
        eventDao.delete(event);
        assertEvents(0);
    }
    
    @Test
    public void testDeleteById() throws ParseException {
        event = createEvent("something elese is happening", new Date());
        eventDao.save(event);
        assertEvents(1);
        eventDao.delete(event.getId());
        assertEvents(0);
    }

    private void assertEvents(int size) {
        Collection<Event> events = eventDao.findAll();
        assertEquals(size, events.size());
        
    }
    
    private Event createEvent(String title, Date date) throws ParseException {
        Event event = new Event();
        List<User> people = new ArrayList<User>();
        people.add(createUser("Joe", "1234"));
        people.add(createUser("mary", "5678"));
        event.setContributors(people);
        event.setFeature(FeatureUtil.createFeature());
        event.setSourceUrl("http://www.wikipedia.com");
        return event;
    }
    
    
    private User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

}
