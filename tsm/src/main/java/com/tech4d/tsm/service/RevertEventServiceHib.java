package com.tech4d.tsm.service;

import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.audit.AuditChangeUtil;
import com.tech4d.tsm.audit.EventFieldChangeFormatter;
import com.tech4d.tsm.dao.AuditEntryDao;
import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.audit.AuditEntry;

@Transactional
public class RevertEventServiceHib implements RevertEventService {
    private SessionFactory sessionFactory;
    private AuditEntryDao auditEntryDao;
    private EventDao eventDao;

    public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setAuditEntryDao(AuditEntryDao auditEntryDao) {
		this.auditEntryDao = auditEntryDao;
	}

	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}


	public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

	public Event revertToRevision(Integer revision, Long eventId) {
		Event event = eventDao.findById(eventId);
		Long count = auditEntryDao.getAuditEntriesCount(event);
		//only get the audit records necessary for reverting
		List<AuditEntry> auditEntries = 
			auditEntryDao.getAuditEntries(event, 0, (int) (count-revision-1));
		return revertToRevision(event, auditEntries);
	}

	private Event revertToRevision(Event event, List<AuditEntry> auditEntries) {

		//1. get the diff (change list)
		Map<Integer, String> changes = AuditChangeUtil.getChanges(auditEntries);
		
		if (changes.size() != 0 ) {
			//2. get the original event.  TODO assumes all auditEntries are for the same entity.  
			//could be a problem in the future.
			EventFieldChangeFormatter eventFieldChangeFormatter = new EventFieldChangeFormatter();
			event = (Event) eventFieldChangeFormatter.revertEntity(event, changes);
			
			//3. save the object
			eventDao.save(event);
			//flush, otherwise the audit record won't get written to the database.  
			// TODO Perhaps we can find it somewhere in the session instead of retrieving it in step 4?
			this.sessionFactory.getCurrentSession().flush();
			
			//4. TODO KLUDGE - I don't know how to modify the AuditInterceptor to realize that 
			// this particular save is actually a "revert", but we want the audit record to reflect that....
			// so let's change the last audit record from "ACTION_UPDATE" to "ACTION_REVERT"
			List<AuditEntry> updated = auditEntryDao.getAuditEntries(event, 0, 1);
			AuditEntry mostRecent = updated.get(0);
			mostRecent.setAction(AuditEntry.ACTION_REVERT);
			auditEntryDao.update(mostRecent);
		}
	
		return event;
	}

}
