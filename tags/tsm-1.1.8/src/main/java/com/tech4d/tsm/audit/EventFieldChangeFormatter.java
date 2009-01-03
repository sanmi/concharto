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
package com.tech4d.tsm.audit;

import java.text.ParseException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tech4d.tsm.model.Auditable;
import com.tech4d.tsm.model.BaseEntity;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.PositionalAccuracy;
import com.tech4d.tsm.model.audit.AuditEntry;
import com.tech4d.tsm.model.geometry.TsGeometry;
import com.tech4d.tsm.util.TimeRangeFormat;
import com.vividsolutions.jts.io.WKTReader;

public class EventFieldChangeFormatter extends BaseAuditFieldChangeFormatter{
    public static final int SUMMARY = 0;
    public static final int DESCRIPTION = 1;
    public static final int WHERE = 2;
    public static final int USERTAGS = 3;
    public static final int SOURCE = 4;
    public static final int TSGEOMETRY = 5;
    public static final int WHEN = 6;
    public static final int MAPTYPE = 7;
    public static final int POSITIONAL_ACCURACY = 8;
    public static final int FLAGS = 9;
    public static final int DISCUSSION = 10;
    public static final int ZOOM_LEVEL = 11;
	public static final String ENTITY_ADDED = "ADDED";
    protected final Log log = LogFactory.getLog(getClass());

    @SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
        return clazz == Event.class;
    }

    /**
     * Get everything in memory so we don't get a lazy loading exception.  We will need it all 
     * later.
     */
    public void refresh(Auditable auditable) {
        //this is enough to fetch into memory
        ((Event)auditable).getUserTags().size();
        ((Event)auditable).getDiscussion();
    }
    
    /**
     * Record the following properties: 
     * <pre>
     *  Text: summary, description, source, _where
     *  Collections: tags
     *  Objects: when, tsGeometry
     * </pre>
     */
    public AuditEntry createUpdateAuditItems(Auditable currentA, Auditable previousA) {
        AuditEntry auditEntry = makeAuditEntry(currentA, AuditEntry.ACTION_UPDATE);
        Event current = (Event) currentA;
        Event previous = (Event) previousA;

        makeChange(SUMMARY,  current.getSummary(), previous.getSummary(), auditEntry);
        makeChange(DESCRIPTION, current.getDescription(), previous.getDescription(), auditEntry);
        makeChange(WHERE,  current.getWhere(), previous.getWhere(), auditEntry);
        makeChange(USERTAGS, current.getUserTagsAsString(), previous.getUserTagsAsString(), auditEntry);
        makeChange(SOURCE, current.getSource(), previous.getSource(), auditEntry);
        makeChange(TSGEOMETRY,
                current.getTsGeometry().getGeometry().toText(), 
                previous.getTsGeometry().getGeometry().toText(), auditEntry);
        makeChange(WHEN,
                TimeRangeFormat.format(current.getWhen()), 
                TimeRangeFormat.format(previous.getWhen()), auditEntry);
        makeChange(MAPTYPE, nullSafeToString(current.getMapType()), nullSafeToString(previous.getMapType()), auditEntry);
        makeChange(ZOOM_LEVEL, nullSafeToString(current.getZoomLevel()), nullSafeToString(previous.getZoomLevel()), auditEntry);
        makeChange(POSITIONAL_ACCURACY, nullSafeToString(current.getPositionalAccuracy()), nullSafeToString(previous.getPositionalAccuracy()), auditEntry);
        makeChange(DISCUSSION, checkExists(current.getDiscussion()), checkExists(previous.getDiscussion()), auditEntry);
        makeChange(FLAGS, nullSafeToString(current.getHasUnresolvedFlag()), nullSafeToString(previous.getHasUnresolvedFlag()), auditEntry);
        return auditEntry;
    }
    
    
   private String nullSafeToString(Boolean bool) {
		if (bool == null) {
			return "";
		} else {
			return bool.toString();
		}
	}

private String nullSafeToString(PositionalAccuracy positionalAccuracy) {
		if (positionalAccuracy == null) {
			return "";
		}
		return positionalAccuracy.getId().toString();
	}

   /**
    * null safe to string or integer
    * @param value an Integer
    * @return "" or string representation of the Integer
    */ 
   private String nullSafeToString(Integer value) {
		if (value == null) {
			return "";
		}
		return value.toString();
	}

/**
    * @param discussion
    * @return "" if value is null, "added" if there is a value
    */
	private String checkExists(BaseEntity object) {
		if (null == object) {
			return "";
		} else {
			return ENTITY_ADDED;
		}
	}

	public Auditable revertEntity(Auditable auditable, Map<Integer, String> changeList) {
		Event event = (Event) auditable;
		for (Integer field : changeList.keySet()) {
			String change = changeList.get(field);
			if (field == SUMMARY) {
				event.setSummary(change);
			} else if (field == DESCRIPTION) {
				event.setDescription(change);
			} else if (field == WHERE) {
				event.setWhere(change);
			} else if (field == USERTAGS) {
				event.setUserTagsAsString(change);
			} else if (field == SOURCE) {
				event.setSource(change);
			} else if (field == TSGEOMETRY) {
				try {
					event.setTsGeometry(new TsGeometry(new WKTReader().read(change)));
				} catch (com.vividsolutions.jts.io.ParseException e) {
					log.error("Error parsing geometry while performing a revert.  This should never happen.");
				}
			} else if (field == WHEN) {
				try {
					event.setWhen(TimeRangeFormat.parse(change));
				} catch (ParseException e) {
					log.error("Error parsing time while performing a revert.  This should never happen.");
				}
			} else if (field == MAPTYPE) {
				event.setMapType(new Integer(change));
			} else if (field == ZOOM_LEVEL) {
				event.setZoomLevel(new Integer(change));
			}  else if (field == POSITIONAL_ACCURACY) {
				PositionalAccuracy pa = new PositionalAccuracy();
				try {
					pa.setId(new Long(change));
					event.setPositionalAccuracy(pa);
				} catch (NumberFormatException e) {
					//nothing to do, just leave it null
				}
			} //NOTE you can't revert flags or discussions.
		}
		return event;
	}
    
    
}
