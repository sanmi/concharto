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
package org.tsm.concharto.model;

import java.util.Date;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseAuditableEntity extends BaseEntity implements Auditable {
    private Date lastModified;
    private Date created;
    private Long version;

    /* (non-Javadoc)
     * @see org.tsm.concharto.model.Auditable#getCreated()
     */
    public Date getCreated() {
        return created;
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.model.Auditable#setCreated(java.util.Date)
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.model.Auditable#getLastModified()
     */
    public Date getLastModified() {
        return lastModified;
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.model.Auditable#setLastModified(java.util.Date)
     */
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
    
}
