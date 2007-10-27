package com.tech4d.tsm.service;

import com.tech4d.tsm.model.Event;

public interface RevertEventService {
	Event revertToRevision(Integer revision, Long eventId);
}
