package com.tech4d.tsm.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.tech4d.tsm.model.Role;
import com.tech4d.tsm.model.User;
import com.tech4d.tsm.model.user.UserNote;
import com.tech4d.tsm.util.ContextUtil;

/**
 * User: frank
 * Date: Sep 15, 2007
 * Time: 4:45:54 PM
 */
public class IntegrationTestUserDao {

    private static final String ROLE_CAN_EDIT = "canEdit";
    private static UserDao userDao;
    private static EventTesterDao eventTesterDao;

    @BeforeClass
    public static void setupClass() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        userDao = (UserDao) appCtx.getBean("userDao");
        eventTesterDao = (EventTesterDao) appCtx.getBean("eventTesterDao");
    }
    
    @Before public void setUp() {
        eventTesterDao.deleteAll();
    }
    
    @Test public void saveGet() {
        User user = new User("marina","restaurant","anonymous@tsm.com");
        Long id = (Long) userDao.save(user);
        User returned = userDao.find(id);
        assertEquals(user.getUsername(), returned.getUsername());
        returned = userDao.find(user.getUsername());
        assertEquals(user.getUsername(), returned.getUsername());
        userDao.delete(id);
        assertEquals(null, userDao.find(id));
        
    }
    
    @Test public void delete() {
        User user = new User("jonathan","diner","jon@tsm.com");
        Long id = (Long) userDao.save(user);
        assertEquals(id, user.getId());
        userDao.delete(user);
        assertEquals(null, userDao.find(id));
    }
    
    @Test public void roles() {
        setupRoles();
        User user = new User("jonathan","diner","jon@tsm.com");
        List<Role> roles = userDao.getRoles();
        assertEquals(3, roles.size());
        user.setRoles(roles);
        Long id = (Long) userDao.save(user);        
        User returned = userDao.find(id);
        
        //Need to refresh for unit tests.  In the web controller, we use an interceptor
        //to keep the session open
        SessionFactory sessionFactory = eventTesterDao.getSessionFactory();
        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
        session.refresh(returned);

        assertEquals(roles.size(), returned.getRoles().size());
        
        session.close();
    }
    
    @Test public void role() {
        setupRoles();
        Role role = userDao.getRole(ROLE_CAN_EDIT);
        assertEquals(ROLE_CAN_EDIT, role.getName());
    }

    @Test public void unique() {
        User user = new User("jonathan","diner","jon@tsm.com");
        userDao.save(user);
        try {
            userDao.save(user);
            fail("should have thrown an exception");
        } catch (ConstraintViolationException e) {
            //expected
        }
    }
    
    @Test public void userNote() {
    	String username = "marina";
    	String retrievalKey = "sdfsdfsdf";
    	User user = new User(username,"place","jon@tsm.com");
    	user.setUserNote(new UserNote(retrievalKey));
    	userDao.save(user);
    	User retreived = userDao.getUserFromPasswordRetrievalKey(retrievalKey);
    	assertEquals(username, retreived.getUsername());
    }
    
    private void setupRoles() {
        eventTesterDao.save(new Role(ROLE_CAN_EDIT));
        eventTesterDao.save(new Role("canDelete"));
        eventTesterDao.save(new Role("canBan"));
    }
}
