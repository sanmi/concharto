/*******************************************************************************
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tsm.concharto.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.tsm.concharto.dao.StyleDao;
import org.tsm.concharto.model.kml.Style;
import org.tsm.concharto.util.ContextUtil;


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
        EventTesterDao eventTesterDao = (EventTesterDao) appCtx.getBean("eventTesterDao");
        styleDao = (StyleDao) appCtx.getBean("styleDao");

        //delete everything and save 1 style object
        eventTesterDao.deleteAll();
        StyleUtil.setupStyle();
    }

    @Test
    public void save() {
        //save a second style
        Style original = StyleUtil.getStyle();
        org.tsm.concharto.model.kml.Style style = styleDao.find(StyleUtil.getStyle().getId());
        assertEquals(original.getId(), style.getId());
        assertEquals(original.getLineStyle().getWidth(), style.getLineStyle().getWidth());
        assertEquals(original.getBaloonStyle().getBgColor(), style.getBaloonStyle().getBgColor());
    }

    @Test
    public void delete() {
        styleDao.delete(StyleUtil.getStyle());
        org.tsm.concharto.model.kml.Style style = styleDao.find(StyleUtil.getStyle().getId());
        assertNull(style);
    }

    @Test
    public void deleteById() {
        //test delete by
        styleDao.delete(StyleUtil.getStyle().getId());
        Style style = styleDao.find(StyleUtil.getStyle().getId());
        assertNull(style);
    }

    @Test
    public void findAll() {
        //save a second style
        styleDao.save(new org.tsm.concharto.model.kml.Style());
        List<org.tsm.concharto.model.kml.Style> styles = styleDao.findAll();
        assertEquals(2, styles.size());
    }

}
