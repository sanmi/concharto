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
		current = (++current % spotlights.size());
		return spotlights.get(current);
	}
	
}
