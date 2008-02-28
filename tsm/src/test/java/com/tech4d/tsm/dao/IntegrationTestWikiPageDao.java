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
	private static WikiTextDao wikiPageDao;
    private static EventTesterDao eventTesterDao;

    @BeforeClass
    public static void setupClass() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        wikiPageDao = (WikiTextDao) appCtx.getBean("wikiPageDao");
        eventTesterDao = (EventTesterDao) appCtx.getBean("eventTesterDao");
    }
    
    @Before public void setUp() {
        eventTesterDao.deleteAll();
    }
    
    @Test public void saveGetDelete() {
    	WikiText wikiPage = new WikiText();
    	wikiPage.setText(WIKI_PAGE);
    	wikiPage.setTitle(WIKI_PAGE_TITLE);
    	Long id = (Long) wikiPageDao.save(wikiPage);
    	assertNotNull(id);
    	wikiPage = wikiPageDao.get(id);
    	assertEquals(WIKI_PAGE, wikiPage.getText());
    	
    	wikiPage = wikiPageDao.find(WIKI_PAGE_TITLE);
    	assertEquals(WIKI_PAGE_TITLE, wikiPage.getTitle());
    	wikiPageDao.delete(id);
    	wikiPage = wikiPageDao.get(id);
    	assertNull(wikiPage);
    }
    
}
