/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version 
 * 1.1 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 * 
 * The Original Code is Concharto.
 * 
 * The Initial Developer of the Original Code is
 * Time Space Map, LLC
 * Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 * the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s):
 * Time Space Map, LLC
 * 
 * ***** END LICENSE BLOCK *****
 ******************************************************************************/
package org.tsm.concharto.dao;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.tsm.concharto.OpenSessionInViewIntegrationTest;
import org.tsm.concharto.dao.EventDao;
import org.tsm.concharto.dao.FlagDao;
import org.tsm.concharto.model.Event;
import org.tsm.concharto.model.Flag;
import org.tsm.concharto.util.ContextUtil;

import com.vividsolutions.jts.io.ParseException;

public class IntegrationTestFlagDao extends OpenSessionInViewIntegrationTest {

    private static EventDao eventDao;
    private static FlagDao flagDao;
    private static EventTesterDao eventTesterDao;
    private static EventUtil eventUtil;

    @BeforeClass
    public static void setUpClass() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        eventDao = (EventDao) appCtx.getBean("eventDao");
        flagDao = (FlagDao) appCtx.getBean("flagDao");
        eventTesterDao = (EventTesterDao) appCtx.getBean("eventTesterDao");
        eventUtil = new EventUtil(eventTesterDao.getSessionFactory());
        eventTesterDao.deleteAll();
        StyleUtil.setupStyle();
    }

    @Test
    public void testFlag() throws ParseException {
    	Event event = eventUtil.createEvent();
        Serializable id = eventDao.save(event);
        
        Flag flag = new Flag("it is bad!", "isBad", "joe", event);
        //Save it the hard way
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(flag);
        event.setFlags(flags);
        eventDao.saveOrUpdate(event);
        Event returned = eventDao.findById((Long)id);
        eventUtil.assertEquivalent(event, returned);
    	
        //Now try it the easy way
        flag = new Flag("it is really bad!", "isBad", "joe", event);
        flagDao.save(flag);
        
        //in order to check, we will add the flag to our event
        event.getFlags().add(flag);
        returned = eventDao.findById((Long)id);
        eventUtil.assertEquivalent(event, returned);
        getSessionFactory().getCurrentSession().evict(event);
        
        //now delete one
        flagDao.delete(returned.getFlags().get(0).getId());
        //in order to check, we will add the flag to our event
        event.getFlags().remove(0);
        returned = eventDao.findById((Long)id);
        eventUtil.assertEquivalent(event, returned);
    }

    @Test
    public void disposition() {
        Flag flag = new Flag("it is really bad!", "isBad", "joe", null);
        Long id = (Long) flagDao.save(flag);
        flagDao.setFlagDisposition(id, Flag.DISPOSITION_CODES[0]);
    	Flag returned = flagDao.find(id);
    	assertEquals(Flag.DISPOSITION_CODES[0], returned.getDisposition());
    }
}