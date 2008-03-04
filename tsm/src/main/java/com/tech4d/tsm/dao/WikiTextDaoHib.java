package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.wiki.WikiText;

@Transactional
public class WikiTextDaoHib implements WikiTextDao {
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

	public WikiText get(Long id) {
		return (WikiText) this.sessionFactory.getCurrentSession().get(WikiText.class, id);
	}

	public WikiText find(String title) {
        return (WikiText) DaoHelper.getOnlyFirst(
        		this.sessionFactory.getCurrentSession().createQuery(
                "select wikiText from WikiText wikiText where wikiText.title = ?")
                .setParameter(0, title)
                .list()
        );
	}

	@SuppressWarnings("unchecked")
	public Boolean exists(String title) {
		List ids = this.sessionFactory.getCurrentSession().createQuery(
				"select wikiText.id from WikiText wikiText where wikiText.title = ?")
                .setParameter(0, title)
                .list();
		if (ids.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Long> exists(String[] titles) {
		List ids = this.sessionFactory.getCurrentSession().createQuery(
		"select title, id from WikiText wikiText where wikiText.title in (:titles)")
		.setParameterList("titles", titles)
		.list();
		Map<String,Long> results = new HashMap<String,Long>();
		for (Object object : ids) {
			Object[] result = (Object[]) object;
			results.put((String)result[0], (Long)result[1]);
		}
		return results;
	}

	public Serializable save(WikiText wikiText) {
		return this.sessionFactory.getCurrentSession().save(wikiText);
	}
	
	public void saveOrUpdate(WikiText wikiText) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(wikiText);
	}

	public void delete(Long id) {
		WikiText wikiText = new WikiText();
		wikiText.setId(id);
		this.sessionFactory.getCurrentSession().delete(wikiText);
	}

}
