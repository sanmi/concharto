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
package org.tsm.concharto.model.time;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * Used to store a date as a BIGINT in the database.  This is useful for
 * when the database doesn't support the full calendar.  For instance,
 * MySQL doesn't support dates earlier than 1000 AD.
 */
public class UtcDateTimeType implements UserType {
    
    /**
     * SQL type.
     */
    private static final int[] SQL_TYPES = { Types.BIGINT };
    
    /** 
     * Make a copy of the date.
     * @see UserType#deepCopy(java.lang.Object)
     */
    public Object deepCopy(Object obj) throws HibernateException {
        return (obj == null) ? null : new Date(((Date)obj).getTime());
    }
    
    /**
     * Compare via {@link Object#equals(java.lang.Object)}.
     * @see UserType#equals(java.lang.Object, java.lang.Object)
     */
    public boolean equals(Object x, Object y) {
        return (x == null) ? (y == null) : x.equals(y);
    }
    
    /**
     * Dates are mutable.
     * @see org.hibernate.usertype.UserType#isMutable()
     */
    public boolean isMutable() {
        return true;
    }
    
    /**
     * Return an instance of the date or null if no value is specified.
     * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
     */
    public Object nullSafeGet(ResultSet rs, String[] columns, Object owner)
            throws HibernateException, SQLException {

        long value = rs.getLong(columns[0]);
        Date date;
        if(rs.wasNull()) {
            date = null;
        } else {
            date = new Date(value);
        }
        return date; 
        
    }

    /**
     * Set an instance of the date into the database field.
     * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
     */
    public void nullSafeSet(PreparedStatement statement, Object value, int index)
            throws HibernateException, SQLException {
        
        if(value == null) {
            statement.setNull(index, Types.BIGINT);
        } else {
            Date date = (Date)value;
            statement.setLong(index, date.getTime());
        }
    }
    
    /**
     * Return the {@link Date} class.
     * @see org.hibernate.usertype.UserType#returnedClass()
     */
    public Class returnedClass() {
        return Date.class;
    }
    
    /**
     * Return the supported SQL types.
     * @see org.hibernate.usertype.UserType#sqlTypes()
     */
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) deepCopy(value);
    }

    public int hashCode(Object x) throws HibernateException {
        // TODO Auto-generated method stub
        return 0;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        // TODO Auto-generated method stub
        return null;
    }
}
