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

import org.springframework.transaction.annotation.Transactional;
import org.tsm.concharto.model.Flag;


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
