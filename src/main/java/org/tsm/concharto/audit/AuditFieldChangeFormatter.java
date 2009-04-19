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
package org.tsm.concharto.audit;

import java.util.Map;

import org.tsm.concharto.model.Auditable;
import org.tsm.concharto.model.audit.AuditEntry;


public interface AuditFieldChangeFormatter {
    @SuppressWarnings("unchecked")
	public boolean supports(Class clazz);
    public void refresh(Auditable auditable);
    public AuditEntry createInsertAuditItems(Auditable auditable);
    public AuditEntry createUpdateAuditItems(Auditable currentInstance, Auditable previousInstance);
    public AuditEntry createDeleteAuditItems(Auditable auditable);
    public Auditable revertEntity(Auditable auditable, Map<Integer, String> changeList);

}
