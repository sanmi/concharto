package com.tech4d.tsm.dao;

import com.tech4d.tsm.model.Flag;

/**
 * For accessing Flag objects
 *
 */
public interface FlagDao extends BaseDao <Flag> {

    /**
     * Set the disposition of a flag
     * @param flagId
     * @param disposition
     * @return
     */
	public abstract Flag setFlagDisposition(Long flagId, String disposition);

}
