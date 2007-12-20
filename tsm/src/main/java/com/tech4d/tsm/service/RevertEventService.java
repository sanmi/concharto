package com.tech4d.tsm.service;

import java.util.Set;

import com.tech4d.tsm.model.Auditable;

public interface RevertEventService {
	Auditable revertToRevision(Class<?> clazz, Integer revision, Long eventId);
	public void setAuditFieldChangeFormatters(Set<String> auditFieldChangeFormatters) 
		throws InstantiationException, IllegalAccessException, ClassNotFoundException; 
}
