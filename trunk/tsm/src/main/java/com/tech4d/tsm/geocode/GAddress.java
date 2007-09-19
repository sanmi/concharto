package com.tech4d.tsm.geocode;

import org.apache.commons.lang.builder.ToStringBuilder;

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
  private double lat;
  private double lng;

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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
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
        append("lat",lat).
        append("lng",lng).
        toString();
    }


 }