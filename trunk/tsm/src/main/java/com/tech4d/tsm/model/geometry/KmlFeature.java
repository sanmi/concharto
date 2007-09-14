package com.tech4d.tsm.model.geometry;

/**
 * Copyright 2007, Time Space Map
 *
 * Implements a subset of the KML Feature object for rendering
 * in a viewer such as google maps, google earth, etc
 */
public interface KmlFeature {

    /**
     * Street Address in single line form (e.g. street, city, state, zip)
     * @return String representing street address
     */
    String getStreetAddress();

    void setStreetAddress(String address);

    /**
     * Geometry - this can be a point, polygon, line, etc.
     * @return {@link TsGeometry} Geometry of this feature
     */
    TsGeometry getTsGeometry();

    void setTsGeometry(TsGeometry geometry);

    String getDescription();

    void setDescription(String description);

    String getSnippet();

    void setSnippet(String snippet);

    /**
     * This can be a single date or a date range
     * @return {@link TimePrimitive} when it happened
     */
    TimePrimitive getTimePrimitive();

    void setTimePrimitive(TimePrimitive timePrimative);

    /**
     * StyleSelector to be used in rendering this KML feature
     * @return {@link StyleSelector}  style to use to draw this feature
     */
    StyleSelector getStyleSelector();

    void setStyleSelector(StyleSelector styleSelector);

    String getSummary();

    void setSummary(String summary);
}
