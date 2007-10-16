package com.tech4d.tsm.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.tech4d.tsm.model.Role;
import com.tech4d.tsm.model.User;
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
    public static void setupRoles() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        userDao = (UserDao) appCtx.getBean("userDao");
        eventTesterDao = (EventTesterDao) appCtx.getBean("eventTesterDao");
        eventTesterDao.deleteAll();
        eventTesterDao.save(new Role(ROLE_CAN_EDIT));
        eventTesterDao.save(new Role("canDelete"));
        eventTesterDao.save(new Role("canBan"));
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
        User user = new User("jonathan","diner","jon@tsm.com");
        List<Role> roles = userDao.getRoles();
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
        Role role = userDao.getRole(ROLE_CAN_EDIT);
        assertEquals(ROLE_CAN_EDIT, role.getName());
    }

    

}
