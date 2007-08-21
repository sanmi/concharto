package com.tech4d.tsm.dao;

import java.util.Collection;

import org.springframework.context.ApplicationContext;


import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.util.ContextUtil;

import junit.framework.TestCase;


public class IntegrationTestEventDao extends TestCase {

    private EventDao eventDao;
    public void setUp() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        eventDao = (EventDao) appCtx.getBean("eventDao");
    }
    
    public void test() {
        Collection<Event> events = eventDao.loadAll();
        for (Event event : events) {
            System.out.println("event " + event.getId() + ", "  + event.getTitle());
        }
    }
}
