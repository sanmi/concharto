package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import com.tech4d.tsm.model.Spotlight;

public interface SpotlightDao {
	public Serializable save(Spotlight spotlight);
	public void delete(Spotlight spotlight);
	public void delete(Long id);
	public Spotlight find(Long id);
	public List<Spotlight> find(int maxResults, int firstResult);
	public List<Spotlight> findAll();
	public Spotlight getVisible(int position);
	public Integer getTotalVisible();
	public List<Spotlight> findVisible();
}
