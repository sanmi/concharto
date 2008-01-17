package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.Spotlight;

@Transactional
public class SpotlightDaoHib implements SpotlightDao {
    private static final String SQL_SELECT_ALL = 
    	"select spotlight from Spotlight spotlight order by id asc";
    private static final String SQL_SELECT_VISIBLE = 
    	"select spotlight from Spotlight spotlight where spotlight.visible = true order by id asc";
	private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
	public void delete(Spotlight spotlight) {
		this.sessionFactory.getCurrentSession().delete(spotlight);
	}

	public void delete(Long id) {
		Spotlight spotlight = new Spotlight();
		spotlight.setId(id);
		delete(spotlight);
	}

	public Spotlight find(Long id) {
		return (Spotlight) this.sessionFactory.getCurrentSession().get(
				Spotlight.class, id);
	}

	public List<Spotlight> find(int maxResults, int firstResult) {
		return find(SQL_SELECT_ALL, maxResults, firstResult);
	}

	@SuppressWarnings("unchecked")
	private List<Spotlight> find(String sql, int maxResults, int firstResult) {
		return this.sessionFactory.getCurrentSession().createQuery(
				sql).setMaxResults(maxResults)
				.setFirstResult(firstResult).list();
	}

	@SuppressWarnings("unchecked")
	public List<Spotlight> findAll() {
		return this.sessionFactory.getCurrentSession().createQuery(
        SQL_SELECT_ALL).list();
	}

	public Serializable save(Spotlight spotlight) {
		 return this.sessionFactory.getCurrentSession().save(spotlight);
	}

	public Spotlight getVisible(int position) {
		List<Spotlight> spotlights = find(SQL_SELECT_VISIBLE, 1, position);
		return (Spotlight) DaoHelper.getOnlyFirst(spotlights);
	}

	@SuppressWarnings("unchecked")
	public Integer getTotalVisible() {
		List results = this.sessionFactory.getCurrentSession()
    		.createQuery("select count(spotlight) from Spotlight spotlight where spotlight.visible = true")
    		.list();
    	Long count = (Long) results.get(0);
    	//cast to Integer.  It aint never going to be bigger!
    	return Math.round(count);	
    	}

	@SuppressWarnings("unchecked")
	public List<Spotlight> findVisible() {
		return this.sessionFactory.getCurrentSession().createQuery(
		        SQL_SELECT_VISIBLE).list();
	}
	

}
