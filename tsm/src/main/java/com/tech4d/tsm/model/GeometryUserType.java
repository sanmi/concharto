package com.tech4d.tsm.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.UnhandledException;
import org.hibernate.HibernateException;
import org.hibernate.usertype.EnhancedUserType;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ByteOrderValues;
import com.vividsolutions.jts.io.InputStreamInStream;
import com.vividsolutions.jts.io.OutputStreamOutStream;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKBWriter;

public class GeometryUserType implements EnhancedUserType {

  /**
   * Return the SQL type codes for the columns mapped by this type. The codes
   * are defined on <tt>java.sql.Types</tt>.
   * 
   * @see java.sql.Types
   * @return int[] the typecodes
   */
  public int[] sqlTypes() {
    return new int[] { Types.BLOB };
  }

  /**
   * The class returned by <tt>nullSafeGet()</tt>.
   * 
   * @return Class
   */
  public Class returnedClass() {
    return Geometry.class;
  }

  /**
   * Compare two instances of the class mapped by this type for persistence
   * "equality". Equality of the persistent state.
   * 
   * @param x
   * @param y
   * @return boolean
   */
  public boolean equals(Object x, Object y) throws HibernateException {
    return x == y;
  }

  /**
   * Get a hashcode for the instance, consistent with persistence "equality"
   */
  public int hashCode(Object x) throws HibernateException {
    return x.hashCode();
  }

  /**
   * Retrieve an instance of the mapped class from a JDBC resultset.
   * Implementors should handle possibility of null values.
   * 
   * @param rs a JDBC result set
   * @param names the column names
   * @param owner the containing entity
   * @return Object
   * @throws HibernateException
   * @throws SQLException
   */
  @SuppressWarnings("deprecation")
  public Object nullSafeGet(ResultSet rs, String[] names, Object owner) 
    throws HibernateException, SQLException 
  {
    try {
      InputStream is = rs.getBinaryStream(names[0]);
      
      if(is == null)
        return null;
      
      byte [] buf = new byte[4];
      is.read(buf);
      
      int SRID = ByteOrderValues.getInt(buf, ByteOrderValues.LITTLE_ENDIAN);
      
      WKBReader reader = new WKBReader();

      Geometry ret = reader.read(new InputStreamInStream(is));
      ret.setSRID(SRID);
      
      return ret;
    }
    catch (ParseException e) {
      throw new IllegalStateException(e);
    }
    catch (IOException e) {
      throw new UnhandledException(e);
    }
  }

  /**
   * Write an instance of the mapped class to a prepared statement. Implementors
   * should handle possibility of null values. A multi-column type should be
   * written to parameters starting from <tt>index</tt>.
   * 
   * @param st a JDBC prepared statement
   * @param value the object to write
   * @param index statement parameter index
   * @throws HibernateException
   * @throws SQLException
   */
  @SuppressWarnings("deprecation")
  public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException,
      SQLException {
    if (value == null) {
      st.setNull(index, Types.BLOB);
    }
    else {
      Geometry geom = (Geometry)value;
      
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      int SRID = geom.getSRID();
      
      try {
        byte [] buf = new byte[4];
        ByteOrderValues.putInt(SRID, buf, ByteOrderValues.LITTLE_ENDIAN);
        bos.write(buf);
        
        WKBWriter writer = new WKBWriter(2, ByteOrderValues.LITTLE_ENDIAN);
        writer.write(geom, new OutputStreamOutStream(bos));
      }
      catch (IOException e) {
        // should be impossible
        throw new UnhandledException(e);
      }
      
      st.setBytes(index, bos.toByteArray());
    }
  }

  /**
   * Return a deep copy of the persistent state, stopping at entities and at
   * collections. It is not necessary to copy immutable objects, or null values,
   * in which case it is safe to simply return the argument.
   * 
   * @param value the object to be cloned, which may be null
   * @return Object a copy
   */
  public Object deepCopy(Object value) throws HibernateException {
    return value;
  }

  /**
   * Are objects of this type mutable?
   * 
   * @return boolean
   */
  public boolean isMutable() {
    return false;
  }

  /**
   * Transform the object into its cacheable representation. At the very least
   * this method should perform a deep copy if the type is mutable. That may not
   * be enough for some implementations, however; for example, associations must
   * be cached as identifier values. (optional operation)
   * 
   * @param value the object to be cached
   * @return a cachable representation of the object
   * @throws HibernateException
   */
  public Serializable disassemble(Object value) throws HibernateException {
    return (Geometry)value;
  }

  /**
   * Reconstruct an object from the cacheable representation. At the very least
   * this method should perform a deep copy if the type is mutable. (optional
   * operation)
   * 
   * @param cached the object to be cached
   * @param owner the owner of the cached object
   * @return a reconstructed object from the cachable representation
   * @throws HibernateException
   */
  public Object assemble(Serializable cached, Object owner) throws HibernateException {
    return (Geometry)cached;
  }

  /**
   * During merge, replace the existing (target) value in the entity we are
   * merging to with a new (original) value from the detached entity we are
   * merging. For immutable objects, or null values, it is safe to simply return
   * the first parameter. For mutable objects, it is safe to return a copy of
   * the first parameter. For objects with component values, it might make sense
   * to recursively replace component values.
   * 
   * @param original the value from the detached entity being merged
   * @param target the value in the managed entity
   * @return the value to be merged
   */
  public Object replace(Object original, Object target, Object owner) throws HibernateException {
    return original;
  }

  // enhanced methods

  @SuppressWarnings("deprecation")
  public String objectToSQLString(Object value) {
    Geometry geom = (Geometry)value;
    return "GeomFromText(" + geom.toText() + ", " + geom.getSRID() + ")";
  }

  public String toXMLString(Object value) {
    return objectToSQLString(value);
  }

  public Object fromXMLString(String xmlValue) {
    throw new NotImplementedException("Currently not implemented");
  }

}