package com.tech4d.tsm.dao;

import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.Flag;

@Transactional
public class FlagDaoHib extends BaseDaoHib<Flag> implements FlagDao {

    public FlagDaoHib() {
        super(Flag.class);
    }

    public Flag setFlagDisposition(Long flagId, String disposition) {
		Flag flag = (Flag) getSessionFactory().getCurrentSession().load(Flag.class, flagId);
		flag.setDisposition(disposition);
		getSessionFactory().getCurrentSession().flush();
		return flag;
	}

	public Flag find(Long id) {
		return (Flag) getSessionFactory().getCurrentSession().get(Flag.class, id);
	}
}
