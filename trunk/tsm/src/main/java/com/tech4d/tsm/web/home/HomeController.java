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
package com.tech4d.tsm.web.home;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.model.Spotlight;
import com.tech4d.tsm.model.time.TimeRange;
import com.tech4d.tsm.service.SpotlightService;
import com.tech4d.tsm.service.TagAggregateService;
import com.tech4d.tsm.service.TagCloudEntry;
import com.tech4d.tsm.web.eventsearch.SearchHelper;
import com.tech4d.tsm.web.util.CatalogUtil;

public class HomeController extends SimpleFormController {


    private static final String PARAM_TAGINDEX_SELECTED = "1";

    private static final String PARAM_TAGINDEX = "tagindex";

    protected final Log log = LogFactory.getLog(getClass());

    private static final int MAX_RECENT_EVENTS = 6;
    public static final String MODEL_TOTAL_EVENTS = "totalEvents";
    public static final String MODEL_RECENT_EVENTS = "recentEvents";
    private static final Object MODEL_SPOTLIGHT_LABEL = "spotlightLabel";
    private static final Object MODEL_SPOTLIGHT_LINK = "spotlightLink";
    private static final Object MODEL_SPOTLIGHT_EMBED_LINK = "spotlightEmbedLink";
    private static final String MODEL_TAG_CLOUD = "tagCloud";
    private static final String MODEL_TAG_DAYS_BACK = "tagsDaysBack";
    private static final String MODEL_TAG_INDEX = "tagIndex";
    private static final String SESSION_SPOTLIGHT_INDEX = "spotlightIndex";
    private static final String COOKIE_SELECTED_TAB = "selectedTab";
    private static final Object COOKIE_SELECTED_TAB_INDEX = "index";


    private EventDao eventDao;
    private SpotlightService spotlightService;
    private TagAggregateService tagAggregateService;

    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public void setSpotlightService(SpotlightService spotlightService) {
        this.spotlightService = spotlightService;
    }

    public void setTagAggregateService(TagAggregateService tagAggregateService) {
        this.tagAggregateService = tagAggregateService;
    }

    /**
     * Some requests are submitted via POST to defeat the browser cache of
     * certain browsers (Safari 2, IE 6)
     */
    @Override
    protected boolean isFormSubmission(HttpServletRequest request) {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ModelAndView showForm(HttpServletRequest request,
            HttpServletResponse response, BindException errors, Map controlModel)
            throws Exception {
        Map model = errors.getModel();
        model.put(MODEL_RECENT_EVENTS, eventDao.findRecent(CatalogUtil
                .getCatalog(request), MAX_RECENT_EVENTS, 0));
        model.put(MODEL_TOTAL_EVENTS, eventDao.getTotalCount(CatalogUtil
                .getCatalog(request)));
        Cookie cookie = WebUtils.getCookie(request, COOKIE_SELECTED_TAB);
        //The index is so large that we don't normally want to send all of that data 
        //in the HTTP message, so we only setup the index if the user selected 'index' tab.
        //Also, when that happens, we don't want to increment the spotlight
        if (COOKIE_SELECTED_TAB_INDEX.equals(cookie.getValue())) {
            setupTagIndex(request, model);
        } 
        if (PARAM_TAGINDEX_SELECTED.equals(request.getParameter(PARAM_TAGINDEX))) {
            //clicked on the index tab, so we don't need to increment the spotlight
            setupSpotlight(request, model, false);
        } else {
            setupSpotlight(request, model, true);
        }
        setupTagCloud(request, model);
        // clear out the eventSearchForm session if there is one
        WebUtils.setSessionAttribute(request,
                SearchHelper.SESSION_EVENT_SEARCH_FORM, null);
        return new ModelAndView(getFormView(), model);
    }

    @SuppressWarnings("unchecked")
    private void setupTagIndex(HttpServletRequest request, Map model) {
        SortedMap<TimeRange, List<TagCloudEntry>> tagIndex = tagAggregateService.getTagIndex();
        model.put(MODEL_TAG_INDEX, tagIndex);
    }

    @SuppressWarnings("unchecked")
    private void setupTagCloud(HttpServletRequest request, Map model) {
        List<TagCloudEntry> tagCloud = tagAggregateService.getTagCloud();
        model.put(MODEL_TAG_CLOUD, tagCloud);
        model.put(MODEL_TAG_DAYS_BACK, tagAggregateService.getDefaultDaysBack());
    }

    /**
     * Find the next spotlight, add it to the model and save the index in the
     * session so we can show the next one next time.
     * 
     * @param request
     * @param model
     * @param increment true if the spotlight index should be incremented
     */
    @SuppressWarnings("unchecked")
    private void setupSpotlight(HttpServletRequest request, Map model, boolean increment) {
        Integer spotlightIndex = (Integer) WebUtils.getSessionAttribute(
                request, SESSION_SPOTLIGHT_INDEX);
        if (spotlightIndex == null) {
            // setup the new counter. Any old integer will do.
            spotlightIndex = Math.abs((new Random()).nextInt());
        }
        if (increment) {
            spotlightIndex++;
        }
        Spotlight spotlight = spotlightService.getSpotlight(spotlightIndex,
                CatalogUtil.getCatalog(request));
        WebUtils.setSessionAttribute(request, SESSION_SPOTLIGHT_INDEX,
                spotlightIndex);
        if (null == spotlight) {
            log.warn("no spotlight, using default");
            spotlight = new Spotlight();
            spotlight.setLink("search/eventsearch.htm?_what=");
            spotlight.setLabel("Items [[on the map so far]]");
        }
        model.put(MODEL_SPOTLIGHT_LABEL, formatLabel(spotlight));
        model.put(MODEL_SPOTLIGHT_LINK, spotlight.getLink());
        model.put(MODEL_SPOTLIGHT_EMBED_LINK, formatEmbedLabel(spotlight));
    }

    private String formatLabel(Spotlight spotlight) {
        String link = URLEncode(spotlight.getLink());
        String label = null;
        label = StringUtils.replace(spotlight.getLabel(), "[[", "<a href='"
                + link + "'>");
        label = StringUtils.replace(label, "]]", "</a>");
        return label;
    }

    private String formatEmbedLabel(Spotlight spotlight) {
        String label = StringUtils.replace(spotlight.getLink(),
                "search/eventsearch.htm", "search/embeddedsearch.htm");
        return URLEncode(label);
    }

    /**
     * A simple URLencoder that doesn't do the bad stuff that the URLEncoder
     * class does (e.g. replace : and / chars) NOTE: it doesn't do everything
     * according to the spec. because we don't need it at the moment.
     * 
     * @param str
     * @return encoded string
     */
    private String URLEncode(String str) {
        str = StringUtils.replace(str, "&", "&amp;");
        str = StringUtils.replace(str, "\"", "%22");
        str = StringUtils.replace(str, "\'", "%27");
        return str;
    }
}
