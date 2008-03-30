package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.Map;

import com.tech4d.tsm.model.wiki.WikiText;

public interface WikiTextDao {
	public WikiText get(Long id);
	public WikiText find(String title);
	public Boolean exists(String title);
	public Map<String,Long> exists(String[] titles);
	public Serializable save(WikiText wikiText);
	public void saveOrUpdate(WikiText wikiText);
	public void delete(Long id);
}