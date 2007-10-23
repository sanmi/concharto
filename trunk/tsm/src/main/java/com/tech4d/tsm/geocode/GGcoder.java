package com.tech4d.tsm.geocode;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;

/**
 * Geocoder class for google maps.  
 * TODO consider using a DynaBean instead of GAddress object.  Move strings to another global config object
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


    public static GAddress geocode(String address, String key) throws Exception {
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

            final String commonId = "AddressDetails.Country.AdministrativeArea";

            gaddr.setFullAddress(query(placemark, "address").toString());
            gaddr
                    .setZipCode(query(
                            placemark,
                            commonId
                                    + ".SubAdministrativeArea.Locality.PostalCode.PostalCodeNumber")
                            .toString());
            gaddr
                    .setAddress(query(
                            placemark,
                            commonId
                                    + ".SubAdministrativeArea.Locality.Thoroughfare.ThoroughfareName")
                            .toString());
            gaddr
            .setCity(query(
                    placemark,
                    commonId
                            + ".SubAdministrativeArea.Locality.LocalityName")
                    .toString());
            gaddr
                    .setCounty(query(
                            placemark,
                            commonId
                                    + ".SubAdministrativeArea.SubAdministrativeAreaName")
                            .toString());
            gaddr.setState(query(placemark,
                    commonId + ".AdministrativeAreaName").toString());
            gaddr.setCountry(query(placemark,
                    "AddressDetails.Country.CountryNameCode").toString());
            gaddr.setAccuracy(Integer.valueOf(query(placemark,
                    "AddressDetails.Accuracy").toString()));
            gaddr.setLat(Double.parseDouble(query(placemark,
                    "Point.coordinates[1]").toString()));
            gaddr.setLng(Double.parseDouble(query(placemark,
                    "Point.coordinates[0]").toString()));
        }
        return gaddr;
    }

    public static GAddress geocode(String address) throws Exception {
        return geocode(address, "enter key here");
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
