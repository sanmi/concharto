package com.tech4d.tsm.service;

import java.util.List;

import com.tech4d.tsm.dao.SpotlightDao;
import com.tech4d.tsm.model.Spotlight;

/**
 * Service for a round robin queue of spotlights for rendering of the home page
 * @author frank
 *
 */
public class SpotlightService {
	SpotlightDao spotlightDao;
	List<Spotlight> spotlights;
	int current = 0;

	public void setSpotlightDao(SpotlightDao spotlightDao) {
		this.spotlightDao = spotlightDao;
		refresh();
	}

	public void refresh() {
		spotlights = spotlightDao.findVisible();
	}
	
	/**
	 * Round robin iterator.  When it gets to the end it starts
	 * all over again.
	 * @return the next spotlight
	 */
	public Spotlight getNext() {
		if (spotlights.size() > 0) {
			current = (++current % spotlights.size());
			return spotlights.get(current);
		} else {
			return null;
		}
	}
	
	/**
	 * Round robin iterator.  Returns the Spotlight at position
	 * curr modulo the number of spotlight events.
	 * @param curr any integer
	 * @return the spotlight event
	 */
	public Spotlight getSpotlight(int curr) {
		if (spotlights.size() > 0) {
			int index = (curr %  spotlights.size());
			return spotlights.get(index);
		} else {
			return null;
		}
	}
	
}
