package com.tech4d.tsm.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.tech4d.tsm.model.user.Notification;
import com.tech4d.tsm.model.user.User;
import com.tech4d.tsm.model.user.Notification.NotificationType;
import com.tech4d.tsm.util.ContextUtil;

public class IntegrationTestNotificationDao {
    private static final String DESCRIPTION = "lllllls [dfsdf sdf] yo";
    private static final String TITLE = "User:jon";
    private static final String USER1= "jon";
    private static final String USER2= "marina";
	private static NotificationDao notificationDao;
    private static EventTesterDao eventTesterDao;
    private static UserDao userDao;
    private User user1;
    private User user2;

    @BeforeClass
    public static void setupClass() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        notificationDao = (NotificationDao) appCtx.getBean("notificationDao");
        eventTesterDao = (EventTesterDao) appCtx.getBean("eventTesterDao");
        userDao = (UserDao) appCtx.getBean("userDao");
    }
    
    @Before public void setUp() {
        eventTesterDao.deleteAll();
        user1 = makeUser(USER1);
        user2 = makeUser(USER2);
    }
    
    @Test public void saveGetDelete() {
    	//save two notifications
    	Notification notification = makeNotification();
    	Long id = (Long) notificationDao.save(notification);
    	id = (Long) notificationDao.save(notification);
    	assertNotNull(id);
    	notification = notificationDao.get(id);
    	assertEquals(DESCRIPTION, notification.getDescription());
    	
    	//no notifications for user1
    	assertEquals(0, notificationDao.find(user1).size());
    	//some notifications for user2
    	assertEquals(2, notificationDao.find(user2).size());
    	assertEquals(TITLE, notification.getTitle());
    	assertEquals(true, notificationDao.notificationsExist(user2.getUsername()));
    	assertEquals(false, notificationDao.notificationsExist(user1.getUsername()));
    	notificationDao.delete(id);
    	notification = notificationDao.get(id);
    	assertNull(notification);
    	assertEquals(false, notificationDao.notificationsExist(TITLE));
    	assertEquals(1, notificationDao.find(user2).size());

    	//now put anther back, then delete both using the other delete
    	notificationDao.save(makeNotification(user2));
    	notificationDao.save(makeNotification(user1));  
    	assertEquals(2, notificationDao.find(user2).size());
    	notificationDao.delete(user2.getUsername(), NotificationType.TALK);
    	assertEquals(0, notificationDao.find(user2).size());
    	assertEquals(1, notificationDao.find(user1).size());
    }
    
    
    private Notification makeNotification() {
    	return makeNotification(user2);
    }
    
    private Notification makeNotification(User toUser) {
    	Notification notification = new Notification();
    	notification.setDescription(DESCRIPTION);
    	notification.setTitle(TITLE);
    	notification.setFromUsername(user1.getUsername());
    	notification.setToUsername(toUser.getUsername());
    	notification.setType(NotificationType.TALK);
    	return notification;
    }
    private User makeUser(String username) {
    	User user = new User(username,username, "joe@yo.yo");
    	userDao.save(user);
    	return user;
    }
    
}
