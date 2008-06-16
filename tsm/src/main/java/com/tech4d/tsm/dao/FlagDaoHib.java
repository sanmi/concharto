package com.tech4d.tsm.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.Flag;

@Transactional
public class FlagDaoHib implements FlagDao{
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Serializable save(Flag flag) {
    	return this.sessionFactory.getCurrentSession().save(flag);
    }

	public void deleteFlag(Long id) {
		Flag flag = new Flag();
		flag.setId(id);
		this.sessionFactory.getCurrentSession().delete(flag);
	}

	public void delete(Flag flag) {
		this.sessionFactory.getCurrentSession().delete(flag);
	}

	public Flag setFlagDisposition(Long flagId, String disposition) {
		Flag flag = (Flag) this.sessionFactory.getCurrentSession().load(Flag.class, flagId);
		flag.setDisposition(disposition);
		this.sessionFactory.getCurrentSession().flush();
		return flag;
	}

	public Flag find(Long id) {
		return (Flag) this.sessionFactory.getCurrentSession().get(Flag.class, id);
	}
}
