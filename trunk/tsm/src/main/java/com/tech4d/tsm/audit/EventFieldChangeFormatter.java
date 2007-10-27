package com.tech4d.tsm.audit;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tech4d.tsm.model.Auditable;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.audit.AuditEntry;
import com.tech4d.tsm.model.audit.AuditFieldChange;
import com.tech4d.tsm.model.geometry.TsGeometry;
import com.tech4d.tsm.util.ClassName;
import com.tech4d.tsm.util.TimeRangeFormat;
import com.vividsolutions.jts.io.WKTReader;

public class EventFieldChangeFormatter implements AuditFieldChangeFormatter{
    public static final int SUMMARY = 0;
    public static final int DESCRIPTION = 1;
    public static final int WHERE = 2;
    public static final int USERTAGS = 3;
    public static final int SOURCE = 4;
    public static final int TSGEOMETRY = 5;
    public static final int WHEN = 6;
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
    }
    
    /**
     * Just record that the object was created and who did it
     * @param auditable Auditable
     */
    public AuditEntry createInsertAuditItems(Auditable auditable) {
        return makeAuditEntry(auditable, AuditEntry.ACTION_INSERT);
    }
    
    private AuditEntry makeAuditEntry(Auditable auditable, Integer action) {
        AuditEntry auditEntry = new AuditEntry();
        auditEntry.setAuditEntryFieldChange(new ArrayList<AuditFieldChange>());
        auditEntry.setAction(action);
        auditEntry.setEntityClass(ClassName.getClassName(auditable));
        auditEntry.setEntityId(auditable.getId());
        Long currVersion = auditable.getVersion();
        if (null == currVersion) {
            currVersion = 0L;
        }
        auditEntry.setVersion(currVersion);
        return auditEntry;
    }

    /**
     * We don't need to record what got deleted - its gone and ain't coming back
     * @param auditable Auditable
     */
    public AuditEntry createDeleteAuditItems(Auditable auditable) {
        return makeAuditEntry(auditable, AuditEntry.ACTION_DELETE);
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
        return auditEntry;
    }
    
    private void makeChange(Integer propertyName, String current, String previous, AuditEntry auditEntry ) {
        if (!StringUtils.equals(current, previous)) {
            //concatenate in case it is larger than the space alloted
            current = StringUtils.abbreviate(current, AuditFieldChange.SZ_VALUE);
            previous = StringUtils.abbreviate(previous, AuditFieldChange.SZ_VALUE);

            // create the audit entry for this property
            AuditFieldChange auditFieldChange = new AuditFieldChange();
            auditFieldChange.setPropertyName(propertyName);
            auditFieldChange.setNewValue(current == null ? null : current);
            auditFieldChange.setOldValue(previous == null ? null : previous);
            // hook up relation
            auditFieldChange.setAuditEntry(auditEntry);
            auditEntry.getAuditEntryFieldChange().add(auditFieldChange);
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
			}
		}
		return event;
	}
    
    
}
