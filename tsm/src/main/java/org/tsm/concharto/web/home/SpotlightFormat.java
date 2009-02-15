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
package org.tsm.concharto.web.home;

import org.apache.commons.lang.StringUtils;
import org.tsm.concharto.model.Spotlight;

public class SpotlightFormat {

    public static String formatLabel(Spotlight spotlight) {
        String link = URLEncode(spotlight.getLink());
        String label = null;
        label = StringUtils.replace(spotlight.getLabel(), "[[", "<a href='"
                + link + "'>");
        label = StringUtils.replace(label, "]]", "</a>");
        return label;
    }

    public static String formatEmbedLabel(Spotlight spotlight) {
        String label = StringUtils.replace(URLEncode(spotlight.getLink()),
                "search/eventsearch.htm", "search/embeddedsearch.htm");
        return URLEncode(label);
    }

    /**
     * A simple URLencoder that doesn't do the bad stuff that the URLEncoder
     * class does (e.g. replace : and / chars) NOTE: it doesn't do everything
     * according to the spec. because we don't need it at the moment.
     * 
     * @param str
     * @return encoded string
     */
    public static String URLEncode(String str) {
        str = StringUtils.replace(str, "&", "&amp;");
        str = StringUtils.replace(str, "\"", "%22");
        str = StringUtils.replace(str, "\'", "%27");
        return str;
    }

}
