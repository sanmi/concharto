package com.tech4d.tsm.dao;

import java.io.Serializable;

import com.tech4d.tsm.model.Flag;

public interface FlagDao {
    public Serializable save(Flag flag);

	public abstract void deleteFlag(Long id);

	public abstract Flag setFlagDisposition(Long flagId, String disposition);

	public Flag find(Long id);

}
