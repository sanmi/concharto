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
package org.tsm.concharto.web.changehistory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.tsm.concharto.dao.AuditEntryDao;
import org.tsm.concharto.dao.AuditUserChange;
import org.tsm.concharto.dao.EventDao;
import org.tsm.concharto.dao.WikiTextDao;
import org.tsm.concharto.model.Auditable;
import org.tsm.concharto.model.Event;
import org.tsm.concharto.model.audit.AuditEntry;
import org.tsm.concharto.model.wiki.WikiText;
import org.tsm.concharto.web.util.CatalogUtil;
import org.tsm.concharto.web.util.DisplayTagHelper;
import org.tsm.concharto.web.wiki.WikiConstants;


/**
 * Helper class for displaying change lists based on GET string parameters.  Used by
 * web controllers wishing to display a list of changes using displaytag.
 * 
 */
public class ChangeHistoryControllerHelper {
    private static final String MODEL_ID = "id";
    private static final String MODEL_USER = "user";
    private static final String MODEL_AUDIT_ENTRIES = "auditEntries";
    private static final String MODEL_EVENT_CHANGES = "userChanges";
    private static final String MODEL_WIKI_CHANGES = "wikiChanges";
    private static final String DISPLAYTAG_TABLE_ID = "simpleTable";
	private static final String MODEL_EVENT_ID = "eventId";
	private static final String MODEL_EVENT = "event";
	private static final Object MODEL_USER_PAGES = "userPages";
    private AuditEntryDao auditEntryDao;
    private EventDao eventDao;
    private WikiTextDao wikiTextDao;
    
    public void setAuditEntryDao(AuditEntryDao auditEntryDao) {
		this.auditEntryDao = auditEntryDao;
	}
	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}
	public void setWikiTextDao(WikiTextDao wikiTextDao) {
		this.wikiTextDao = wikiTextDao;
	}
	
	@SuppressWarnings("unchecked")
    public Map doProcess(Class<?> clazz, String formView, HttpServletRequest request, Map model, int pageSize) throws Exception {
        Integer firstRecord = DisplayTagHelper.getFirstRecord(request, DISPLAYTAG_TABLE_ID, pageSize);
        Long id = null;
        boolean hasEmptyId = false;
        try {
        	id = ServletRequestUtils.getLongParameter(request, MODEL_ID);
        } catch (ServletRequestBindingException e) {
        	//this can happen when a discussion has not yet been created for 
        	//an event.  The URL will say ?id=&eventId=1234
        	hasEmptyId = true;
        }
       
        String user = ServletRequestUtils.getStringParameter(request, MODEL_USER);
        String catalog = CatalogUtil.getCatalog(request);
        Long totalResults = 0L;
        
        //TODO - split this into two controllers one for user contribs, one for event changes
        if ((id != null) ||(hasEmptyId)) {
        	//we are doing history for an Auditable
            Long eventId = ServletRequestUtils.getLongParameter(request, MODEL_EVENT_ID);
            Event event;
            //TODO this is a UI hack : auditables are event or event.discussion
            if (null == eventId) {
            	event = eventDao.findById(id);
            } else {
            	event = eventDao.findById(eventId);
            }
            model.put(MODEL_EVENT_ID, eventId);
            model.put(MODEL_EVENT, event);
            if (!hasEmptyId) {
	            Auditable auditable = (Auditable) clazz.newInstance();
	            auditable.setId(id);
	            List<AuditEntry> auditEntries = auditEntryDao.getAuditEntries(auditable, firstRecord, pageSize);
	            totalResults = auditEntryDao.getAuditEntriesCount(auditable);
	            model.put(MODEL_AUDIT_ENTRIES, auditEntries);
	    		Set<String> titles = new HashSet<String>();
	    		for (AuditEntry auditEntry : auditEntries) {
	    			addTitle(titles, auditEntry.getUser());
	    		}
	    		updateModelUserPages(model, titles);
            }
        } else if (!StringUtils.isEmpty(user)){
        	//we are doing history for a user
        	List<AuditUserChange> userChanges = auditEntryDao.getAuditEntries(catalog, user, clazz, firstRecord, pageSize);
            totalResults = auditEntryDao.getAuditEntriesCount(catalog, user, clazz);
            model.put(MODEL_EVENT_CHANGES, userChanges);
    		Set<String> titles = new HashSet<String>();
    		addTitle(titles, user);
    		updateModelUserPages(model, titles);
        } else {
        	//just get everything
        	List<AuditUserChange> eventChanges = 
        		auditEntryDao.getLatestEventEntries(catalog, firstRecord, pageSize);
            List<AuditUserChange> wikiChanges = 
                auditEntryDao.getLatestAuditEntries(catalog, WikiText.class, firstRecord, pageSize);
            totalResults = auditEntryDao.getAuditEntriesCount(catalog, Event.class);
            model.put(MODEL_EVENT_CHANGES, eventChanges);
            model.put(MODEL_WIKI_CHANGES, wikiChanges);
    		Set<String> titles = new HashSet<String>();
    		for (AuditUserChange auditUserChange : eventChanges) {
    			addTitle(titles, auditUserChange.getAuditEntry().getUser());
    		}
    		updateModelUserPages(model, titles);
        }
        
        model.put(DisplayTagHelper.MODEL_PAGESIZE, pageSize);
        model.put(DisplayTagHelper.MODEL_REQUEST_URI, request.getRequestURI());
        model.put(DisplayTagHelper.MODEL_TOTAL_RESULTS, Math.round(totalResults));
        model.put(MODEL_ID, id);

        return model;
    }

	/**
	 * Add user page titles to the given set.
	 * @param titles
	 * @param username
	 */
	private void addTitle(Set<String> titles, String username) {
		//wiki convention replaces ' ' with '_'
		username = StringUtils.replace(username, " ", "_");
		titles.add(WikiConstants.PREFIX_USER + username);
		titles.add(WikiConstants.PREFIX_USER_TALK + username);
	}
	
	/**
	 * Update the model with a set of user page titles.  This allows the view to give hints to 
	 * the user about which user pages have been created and which haven't (e.g. red links indicate
	 * the page hasn't been created yet)
	 * @param model
	 * @param titles
	 */
	@SuppressWarnings("unchecked")
	private void updateModelUserPages(Map model, Set<String> titles) {
		Map<String,Long> pages;
		if (titles.size() != 0 ) {
			pages = wikiTextDao.exists((String[])(titles.toArray(new String[titles.size()])));
		} else {
			pages = new HashMap<String,Long>();
		}
		model.put(MODEL_USER_PAGES, pages);
	}
	
}
