/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version 
 * 1.1 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 * 
 * The Original Code is Concharto.
 * 
 * The Initial Developer of the Original Code is
 * Time Space Map, LLC
 * Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 * the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s):
 * Time Space Map, LLC
 * 
 * ***** END LICENSE BLOCK *****
 ******************************************************************************/
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
