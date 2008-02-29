package com.tech4d.tsm.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.tech4d.tsm.model.wiki.WikiText;
import com.tech4d.tsm.util.ContextUtil;

public class IntegrationTestWikiPageDao {
    private static final String WIKI_PAGE = "lllllls [dfsdf sdf] yo";
    private static final String WIKI_PAGE_TITLE = "User:jon";
	private static WikiTextDao wikiTextDao;
    private static EventTesterDao eventTesterDao;

    @BeforeClass
    public static void setupClass() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        wikiTextDao = (WikiTextDao) appCtx.getBean("wikiTextDao");
        eventTesterDao = (EventTesterDao) appCtx.getBean("eventTesterDao");
    }
    
    @Before public void setUp() {
        eventTesterDao.deleteAll();
    }
    
    @Test public void saveGetDelete() {
    	WikiText wikiPage = new WikiText();
    	wikiPage.setText(WIKI_PAGE);
    	wikiPage.setTitle(WIKI_PAGE_TITLE);
    	Long id = (Long) wikiTextDao.save(wikiPage);
    	assertNotNull(id);
    	wikiPage = wikiTextDao.get(id);
    	assertEquals(WIKI_PAGE, wikiPage.getText());
    	
    	wikiPage = wikiTextDao.find(WIKI_PAGE_TITLE);
    	assertEquals(WIKI_PAGE_TITLE, wikiPage.getTitle());
    	assertEquals(true, wikiTextDao.exists(WIKI_PAGE_TITLE));
    	wikiTextDao.delete(id);
    	wikiPage = wikiTextDao.get(id);
    	assertNull(wikiPage);
    	assertEquals(false, wikiTextDao.exists(WIKI_PAGE_TITLE));
    }
    
}
