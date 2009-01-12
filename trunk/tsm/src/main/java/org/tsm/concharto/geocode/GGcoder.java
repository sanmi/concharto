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
package org.tsm.concharto.geocode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Geocoder class for google maps.  Based on some code I found on the web - can't you tell?
 * TODO build this up so that it can use geonames and MS virtual earth as a fallback   
 *
 */
public class GGcoder {
    public static final String URLstr = "http://maps.google.com/maps/geo?output=json";
    public static final int ACCURACY_UNKNOWN_LOCATION = 0;
    public static final int ACCURACY_COUNTRY = 1;
    public static final int ACCURACY_REGION = 2;
    public static final int ACCURACY_SUB_REGION = 3;
    public static final int ACCURACY_TOWN = 4;
    public static final int ACCURACY_POST_CODE = 5;
    public static final int ACCURACY_STREET = 6;
    public static final int ACCURACY_INTERSECTION = 7;
    public static final int ACCURACY_ADDRESS = 8;

    /**
     * Geocode an address
     * @param address
     * @param key
     * @return GAddress object
     * @throws IOException
     */
    public static GAddress geocode(String address, String key) throws IOException  {
        //TODO fix the throws exception
        URL url = new URL(URLstr + "&q=" + URLEncoder.encode(address, "UTF-8")
                + "&key=" + key);
        URLConnection conn = url.openConnection();
        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
        IOUtils.copy(conn.getInputStream(), output);
        output.close();

        GAddress gaddr = new GAddress();
        gaddr.setUserEnteredAddress(address);
        JSONObject json = JSONObject.fromObject(output.toString());

        // check for error
        Integer returnCode = Integer.valueOf(query(json, "Status.code").toString());
        if (HttpURLConnection.HTTP_OK == returnCode) {

            JSONObject placemark = (JSONObject) query(json, "Placemark[0]");

            final StringBuffer commonId = new StringBuffer("AddressDetails.Country.AdministrativeArea");

            StringBuffer addressId = commonId;
            //Sometimes google doesn't return SubAdministrativeArea (e.g. in the case of geocoding a zipcode, as of 11-12-07)
            if (!StringUtils.isEmpty(query(placemark, commonId + ".SubAdministrativeArea").toString())) {
            	addressId.append(".SubAdministrativeArea");
            } 
            addressId.append(".Locality");
            
            gaddr.setFullAddress(query(placemark, "address").toString());
            gaddr.setZipCode(query(
                    placemark,
                    addressId + ".PostalCode.PostalCodeNumber")
                    .toString());
            gaddr.setAddress(query(
                    placemark,
                    addressId + ".Thoroughfare.ThoroughfareName")
                    .toString());
            gaddr.setCity(query(
                    placemark,
                    addressId + ".LocalityName")
                    .toString());
            gaddr.setCounty(query(
                    placemark,
                    commonId+ ".SubAdministrativeArea.SubAdministrativeAreaName")
                    .toString());
            gaddr.setState(query(placemark, commonId + ".AdministrativeAreaName").toString());
            gaddr.setCountry(query(placemark,"AddressDetails.Country.CountryNameCode").toString());
            gaddr.setAccuracy(Integer.valueOf(query(placemark, "AddressDetails.Accuracy").toString()));
            gaddr.setPoint(
            		Double.parseDouble(query(placemark, "Point.coordinates[1]").toString()), 
            		Double.parseDouble(query(placemark, "Point.coordinates[0]").toString())
            		);
        }
        return gaddr;
    }

    /* allow query for json nested objects, ie. Placemark[0].address */
    private static Object query(JSONObject jo, String query) {
        try {
            String[] keys = query.split("\\.");
            Object r = queryHelper(jo, keys[0]);
            for (int i = 1; i < keys.length; i++) {
                r = queryHelper(JSONObject.fromObject(r), keys[i]);
            }
            return r;
        } catch (JSONException e) {
            return "";
        }
    }

    /* help in query array objects: Placemark[0] */
    private static Object queryHelper(JSONObject jo, String query) {
        int openIndex = query.indexOf('[');
        int endIndex = query.indexOf(']');
        if (openIndex > 0) {
            String key = query.substring(0, openIndex);
            int index = Integer.parseInt(query.substring(openIndex + 1,
                    endIndex));
            return jo.getJSONArray(key).get(index);
        }
        return jo.get(query);
    }

}
