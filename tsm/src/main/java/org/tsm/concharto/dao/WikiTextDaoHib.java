/*******************************************************************************
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tsm.concharto.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;
import org.tsm.concharto.model.wiki.WikiText;


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
