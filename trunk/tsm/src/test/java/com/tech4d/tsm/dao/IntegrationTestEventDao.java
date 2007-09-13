package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;

import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.User;
import com.tech4d.tsm.util.ContextUtil;

/**
 * Test all dao functions
 * NOTE: the order of tests is important in the integration test
 * @author frank
 *
 */
public class IntegrationTestEventDao extends TestCase {

    private EventDao eventDao;
    private static Event event = new Event();
    
    public void setUp() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        eventDao = (EventDao) appCtx.getBean("eventDao");
    }
    
    /**
     * Runs the first time
     */
    public void testInit() {
        eventDao.deleteAll();        
    }
    
    public void testSave() {
        event = createEvent("something is happening", new Date());
        Serializable id = eventDao.save(event);
        assertNotNull (id);
        assertEvents(1);
    }
    
    public void findAll() {
        Collection<Event> events = eventDao.findAll();
        for (Event event : events) {
//            System.out.println("event " + event.getId() + ", "  + event.getTitle());
        }
        assertEquals(1, events.size());
        for (Event event : events) {
            assertEquals(2, event.getContributors().size());
        }
    }
    
    public void testSaveOrUpdate() {
        String title = "a new title";
//        event.setTitle(title);
        eventDao.saveOrUpdate(event);
//        assertEquals(title, eventDao.findById(event.getId()).getTitle());
    }
    
    public void testFindById() {
//        assertEquals(event.getTitle(), eventDao.findById(event.getId()).getTitle());
    }
    
    
    public void testDelete() {
        eventDao.delete(event);
        assertEvents(0);
    }
    
    public void testDeleteById() {
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
    
    private Event createEvent(String title, Date date) {
        Event event = new Event();
//        event.setTitle(title);
        Set<User> people = new HashSet<User>();
        people.add(createPerson("Joe", 11));
        people.add(createPerson("mary", 10));
        event.setContributors(people);
        return event;
    }
    
    private User createPerson(String name, int age) {
        User user = new User();
//        user.setFirstname(name);
//        user.setAge(age);
        return user;
    }

}
