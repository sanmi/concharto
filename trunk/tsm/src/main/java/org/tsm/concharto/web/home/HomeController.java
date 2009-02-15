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
package org.tsm.concharto.web.home;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.util.WebUtils;
import org.tsm.concharto.dao.EventDao;
import org.tsm.concharto.model.Spotlight;
import org.tsm.concharto.model.time.TimeRange;
import org.tsm.concharto.service.SpotlightService;
import org.tsm.concharto.service.TagAggregateService;
import org.tsm.concharto.service.TagCloudEntry;
import org.tsm.concharto.web.eventsearch.SearchHelper;
import org.tsm.concharto.web.util.CatalogUtil;


public class HomeController extends SimpleFormController {


    protected final Log log = LogFactory.getLog(getClass());

    private static final String INDEX_URI = "index.htm";
    private static final String PARAM_IS_INDEX = "isIndex";
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
        //The index is so large that we don't normally want to send all of that data 
        //in the HTTP message, so we only setup the index if the user selected 'index' tab.
        //So only show the index when the URL is index.htm 
        //Also, when that happens, we don't want to increment the spotlight
        if (StringUtils.contains(request.getRequestURI(), INDEX_URI)) {
            setupTagIndex(request, model);
            
            //tell the jsp view that this is the index page.  Apparently, you can't count on the jsp
            //knowing the real URL because it things the url is the name of the jsp page, not the 
            //spring binding.  E.g. Even if we got to the jsp page via index.htm, the jsp 
            //${pageContext.request.requestURI} will say "/home/home.jsp" instead of "index.htm"
            //since we need to notify the jsp view to display things for index instead of home
            request.setAttribute(PARAM_IS_INDEX, true);

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
        model.put(MODEL_SPOTLIGHT_LABEL, SpotlightFormat.formatLabel(spotlight));
        model.put(MODEL_SPOTLIGHT_LINK, SpotlightFormat.URLEncode(spotlight.getLink()));
        model.put(MODEL_SPOTLIGHT_EMBED_LINK, SpotlightFormat.formatEmbedLabel(spotlight));
    }

}
