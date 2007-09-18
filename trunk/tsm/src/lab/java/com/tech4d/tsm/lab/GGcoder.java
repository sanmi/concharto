package com.tech4d.tsm.lab;

import static org.junit.Assert.*;

import net.sf.json.JSONObject;
import net.sf.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class GGcoder {
    private static final String URLstr = "http://maps.google.com/maps/geo?output=json";

    private static final String DEFAULT_KEY = "ABQIAAAA1DZDDhaKApTfIDHGfvo13hQHaMf-gMmgKgj1cacwLLvRJWUPcRTWzCG3PTSVLKG0PgyzHQthDg5BUw";

    public static GAddress geocode(String address, String key) throws Exception {
        URL url = new URL(URLstr + "&q=" + URLEncoder.encode(address, "UTF-8")
                + "&key=" + key);
        // System.out.println(url);
        URLConnection conn = url.openConnection();
        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
        IOUtils.copy(conn.getInputStream(), output);
        output.close();

        GAddress gaddr = new GAddress();
        JSONObject json = JSONObject.fromObject(output.toString());

        // check for error
        JSONObject status = (JSONObject) json.get("Status");
        Integer returnCode = Integer.valueOf(status.get("code").toString());
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
                                    + ".SubAdministrativeArea.SubAdministrativeAreaName")
                            .toString());
            gaddr.setState(query(placemark,
                    commonId + ".AdministrativeAreaName").toString());
            gaddr.setLat(Double.parseDouble(query(placemark,
                    "Point.coordinates[1]").toString()));
            gaddr.setLng(Double.parseDouble(query(placemark,
                    "Point.coordinates[0]").toString()));
        }
        return gaddr;
    }

    public static GAddress geocode(String address) throws Exception {
        return geocode(address, DEFAULT_KEY);
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

    @Test
    public void testGeocode() throws Exception {
        assertPlace(GGcoder.geocode("Turners Creek, MD"));
        assertPlace(GGcoder.geocode("94103"));
        assertPlace(GGcoder.geocode("la la land, MD"));
        assertPlace(GGcoder.geocode("Turnsers Creek, MD"));
    }

    private void assertPlace(GAddress place) {
        assertNotNull(place.getLat());
        System.out.println(place.getFullAddress() + ", " + place.getCity()
                + ", " + place.getZipCode() + ", " + place.getLat() + ", "
                + place.getLng());
    }
}
