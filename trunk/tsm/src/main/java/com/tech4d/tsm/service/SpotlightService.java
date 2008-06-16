package com.tech4d.tsm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tech4d.tsm.dao.SpotlightDao;
import com.tech4d.tsm.model.Spotlight;

/**
 * Service for a round robin queue of spotlights for rendering of the home page
 * @author frank
 *
 */
public class SpotlightService {
	SpotlightDao spotlightDao;
	Map<String, List<Spotlight>> spotlightMap;
	int current = 0;

	public void setSpotlightDao(SpotlightDao spotlightDao) {
		this.spotlightDao = spotlightDao;
		refresh();
	}

	public void refresh() {
		spotlightMap = new HashMap<String, List<Spotlight>>();
		List<Spotlight> findVisible = spotlightDao.findVisible();
		for (Spotlight spotlight : findVisible) {
			List<Spotlight> catalogSet = spotlightMap.get(spotlight.getCatalog());
			if (null == catalogSet) {
				//doesn't exist so create a new one and put it in the map
				catalogSet = new ArrayList<Spotlight>();
				spotlightMap.put(spotlight.getCatalog(), catalogSet);
			}
			catalogSet.add(spotlight);
		}
	}
	
	/**
	 * Round robin iterator.  When it gets to the end it starts
	 * all over again.
	 * @return the next spotlight
	 */
	public Spotlight getNext(String catalog) {
		if (spotlightMap.size() > 0) {
			current = (++current % spotlightMap.size());
			return spotlightMap.get(catalog).get(current);
		} else {
			return null;
		}
	}
	
	/**
	 * Round robin iterator.  Returns the Spotlight at position
	 * curr modulo the number of spotlight events.
	 * @param curr any positive integer
	 * @return the spotlight event
	 */
	public Spotlight getSpotlight(int curr, String catalog) {
		if ((spotlightMap.get(catalog) != null) && ((spotlightMap.get(catalog)).size() > 0)) {
			int index = curr % spotlightMap.get(catalog).size();
			return spotlightMap.get(catalog).get(index);
		} else {
			return null;
		}
	}
	
}
