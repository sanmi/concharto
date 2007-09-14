/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tech4d.tsm.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;

import com.tech4d.tsm.dao.TsEventDao;
import com.tech4d.tsm.dao.TsEventDaoHib;

/**
 * The central switchboard
 * {@link org.springframework.web.servlet.mvc.Controller} implementation for the
 * application.
 *
 * @author Rick Evans
 */
public class SwitchBoardController extends MultiActionController {

    private TsEventDao tsEventDao;

    /**
     * Sets the {@link TsEventDaoHib} that to which this presentation component delegates
     * in order to perform dao operations.
     *
     * @param tsEventDao the {@link TsEventDao} to which this presentation
     *                      component delegates in order to perform dao operations
     */
    public void setTsEventDao(TsEventDao tsEventDao) {
        this.tsEventDao = tsEventDao;
    }

    public ModelAndView listEvents(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView().addObject(this.tsEventDao.findAll());
    }
    
    public ModelAndView deleteEvent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        Long id = ServletRequestUtils.getLongParameter(request, "listid");
        if (id != null) {
            tsEventDao.delete(id);
        }
        //redirect back to the list
        return new ModelAndView(new RedirectView("/switchboard/listEvents.htm", true));
    }

    public ModelAndView page0(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView();
    }
    
    public ModelAndView page1(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView();
    }

    public ModelAndView page2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView();
    }
    

}
