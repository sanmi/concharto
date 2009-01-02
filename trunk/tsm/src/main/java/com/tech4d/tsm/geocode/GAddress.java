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
package com.tech4d.tsm.geocode;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 *  Class to hold a geocode result
 *  TODO this is specific to the EN_US locale.    
 */
public class GAddress {
  private String userEnteredAddress;
  private String address;
  private String fullAddress;
  private String city;
  private String county;
  private String state;
  private String zipCode;
  private String country;
  private int accuracy;
  private Point point;

  /* getters and setters */

  public String getUserEnteredAddress() {
      return userEnteredAddress;
  }

  public void setUserEnteredAddress(String freeFormAddress) {
      this.userEnteredAddress = freeFormAddress;
  }

  public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String city) {
        this.county = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public void setPoint(Double lat, Double lng) {
		GeometryFactory gf = new GeometryFactory();
		this.point = gf.createPoint(new Coordinate(lng, lat));
	}

	public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public String toString() {
        return new ToStringBuilder(this).
        append("userEnteredAddress", userEnteredAddress).
        append("address", address).
        append("fullAddress", fullAddress).
        append("city",city).
        append("county",county).
        append("state",state).
        append("zipCode", zipCode).
        append("country",country).
        append("accuracy",accuracy).
        append("lat",point.getY()).
        append("lng",point.getX()).
        toString();
    }


 }
