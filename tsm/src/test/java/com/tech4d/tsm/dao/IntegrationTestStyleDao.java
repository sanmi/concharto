package com.tech4d.tsm.dao;

import com.tech4d.tsm.model.geometry.Style;
import com.tech4d.tsm.util.ContextUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * User: frank
 * Date: Sep 15, 2007
 * Time: 4:45:54 PM
 */
public class IntegrationTestStyleDao {

    private static StyleDao styleDao;

    @Before
    public void setUp() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        styleDao = (StyleDao) appCtx.getBean("styleDao");

        //delete everything and save 1 style object
        StyleUtil.setupStyle();
    }

    @Test
    public void save() {
        //save a second style
        Style original = StyleUtil.getStyle();
        Style style = styleDao.findById(StyleUtil.getStyle().getId());
        assertEquals(original.getId(), style.getId());
        assertEquals(original.getLineStyle().getWidth(), style.getLineStyle().getWidth());
        assertEquals(original.getBaloonStyle().getBgColor(), style.getBaloonStyle().getBgColor());
    }

    @Test
    public void delete() {
        styleDao.delete(StyleUtil.getStyle());
        Style style = styleDao.findById(StyleUtil.getStyle().getId());
        assertNull(style);
    }

    @Test
    public void deleteById() {
        //test delete by
        styleDao.delete(StyleUtil.getStyle().getId());
        Style style = styleDao.findById(StyleUtil.getStyle().getId());
        assertNull(style);
    }

    @Test
    public void findAll() {
        //save a second style
        styleDao.save(new Style());
        List<Style> styles = styleDao.findAll();
        assertEquals(2, styles.size());
    }

}
