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
package org.tsm.concharto.util;

import info.bliki.wiki.model.WikiModel;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.tsm.concharto.model.Event;
import org.tsm.concharto.model.UserTag;
import org.tsm.concharto.model.time.TimeRange;
import org.tsm.concharto.model.time.VariablePrecisionDate;
import org.tsm.concharto.web.wiki.SubstitutionMacro;
import org.tsm.concharto.web.wiki.TsmWikiModel;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * For writing events in KML format
 *
 */
public class KmlFormat {
	private static final Log log = LogFactory.getLog(KmlFormat.class);
	private static final String TESSELLATE = "tessellate";
	private static final String POLY_OPACITY = "6f";
	private static final String POLY_COLOR = POLY_OPACITY + "0000FF";
    private static final String LINE_OPACITY = "9f";
	private static final String LINE_COLOR = LINE_OPACITY + "FF0000"; //slightly opaque
	private static final String LINE_WIDTH = "4";
	private static final String COORDINATES = "coordinates";
	private static final String DEFAULT_STYLES = "defaultStyles";
	private static final String STYLE = "Style";
	private static final String BASEPATH = "http://www.concharto.com";
    protected static final Log logger = LogFactory.getLog(KmlFormat.class);
	private static final String CATALOG_SUBST = "???";
    //TODO extract the constants from WikiModelFactory
    private static String basepath;
    private static String basepathWithoutCatalog;
	private static NumberFormat llFormat = NumberFormat.getInstance();
	private static NumberFormat rangeFormat = NumberFormat.getInstance();
	private static SimpleDateFormat sdf = new SimpleDateFormat();
	private static String[] precisionFormats = new String[VariablePrecisionDate.MAX_PRECISIONS];
	static {
		precisionFormats[VariablePrecisionDate.PRECISION_SECOND] = "yyyy-MM-dd'T'hh:mm:ss'Z'";
		precisionFormats[VariablePrecisionDate.PRECISION_MINUTE] = "yyyy-MM-dd'T'hh:mm:ss'Z'";
		precisionFormats[VariablePrecisionDate.PRECISION_HOUR] = "yyyy-MM-dd'T'hh:mm:ss'Z'";
		precisionFormats[VariablePrecisionDate.PRECISION_DAY] = "yyyy-MM-dd";
		precisionFormats[VariablePrecisionDate.PRECISION_MONTH] = "yyyy-MM";
		precisionFormats[VariablePrecisionDate.PRECISION_YEAR] = "yyyy";
		
    	llFormat.setMaximumFractionDigits(6);
    	llFormat.setGroupingUsed(false);
    	rangeFormat.setMaximumFractionDigits(0);
    	rangeFormat.setGroupingUsed(false);
	}
	
	/**
     * Serialize a list of events to KML
     * @param events
     * @throws ParserConfigurationException
     * @throws IOException
     */
	public static void toKML(List<Event> events, OutputStreamWriter out) {
		toKML(events, out, "Concharto events", null);
	}

	/**
	 * Serialize a list of events to KML
	 * @param events
	 * @param out
	 * @param docTitle
	 * @param snippet
	 */
	public static void toKML(List<Event> events, OutputStreamWriter out, String docTitle, String snippet) {
		toKML(events, out, docTitle, snippet, BASEPATH);
	}

	/**
	 * Serialize a list of events to KML
	 * @param events
	 * @param out
	 * @param docTitle
	 * @param snippet
	 * @param overrideBasepath
	 */
	public static void toKML(List<Event> events, OutputStreamWriter out, 
			String docTitle, String snippet, String overrideBasepath) {

		setupBasepath(overrideBasepath);
		//main kml document stuff
		Element kmlDocument = new Element("Document");
		kmlDocument.addContent(simpleElement("name", docTitle));
		Element snippetElement = simpleElement("Snippet",snippet);
		snippetElement.setAttribute("maxLines","2");
		kmlDocument.addContent(snippetElement);
		kmlDocument.addContent(simpleElement("open","1"));

		addDefaultStyles(kmlDocument);
		int i = 0;
		for (Event event : events) {
			log.debug(i++ + " " + event.getId() + ", " + event.getSummary());
			addPlacemark(kmlDocument, event);
		}

		Element root = new Element("kml", "http://earth.google.com/kml/2.2");
		root.addNamespaceDeclaration(Namespace.getNamespace("atom", "http://www.w3.org/2005/Atom"));
		root.addContent(kmlDocument);
		Document doc = new Document(root);

		XMLOutputter serializer = new XMLOutputter();
	    serializer.setFormat(Format.getPrettyFormat());
	    try {
	    	serializer.output(doc, out);       
	    }
	    catch (IOException e) {
	      logger.error(e);
	    }		
	}
	/**
	 * KML default style
	 * @param parent
	 */
	private static void addDefaultStyles(Element parent) {
		// TODO Auto-generated method stub
		Element style = new Element(STYLE);
		style.setAttribute("id",DEFAULT_STYLES);
		
		//icon
		Element iconStyle = new Element("IconStyle");
		iconStyle.addContent(simpleElement("scale", "1"));
		Element icon = new Element("Icon");
		icon.addContent(simpleElement("href", getBasepath() + "/images/icons/marker-clk.png"));
		iconStyle.addContent(icon);
		style.addContent(iconStyle);

		//line
		Element lineStyle = new Element("LineStyle");
		lineStyle.addContent(simpleElement("color", LINE_COLOR));
		lineStyle.addContent(simpleElement("width", LINE_WIDTH));
		lineStyle.addContent(simpleElement("colorMode", "random"));
		style.addContent(lineStyle);
		
		//poly
		Element polyStyle = new Element("PolyStyle");
		polyStyle.addContent(simpleElement("color", POLY_COLOR));
		polyStyle.addContent(simpleElement("colorMode", "normal"));
		style.addContent(polyStyle);
		
		parent.addContent(style);
	}

	/** 
	 * KML Placemark 
	 * @param parent
	 * @param event
	 */
	private static void addPlacemark(Element parent, Event event) {
		Element placemark = new Element("Placemark");
		placemark.setAttribute("id", "tsm:" + event.getId());
		
		Namespace atom = Namespace.getNamespace("atom", "http://www.w3.org/2005/Atom");
		placemark.addContent(simpleElement("author", null, atom));
		placemark.addContent(simpleElement("link", getLink(event), atom));
		addMetadata(placemark, event);
		placemark.addContent(simpleElement("name", event.getSummary() ));
		addSnippet(placemark, event);
		addDescription(placemark, event);
		placemark.addContent(simpleElement("visibility", "1"));
		placemark.addContent(simpleElement("styleUrl", "#" + DEFAULT_STYLES));
		addGeometry(placemark, event);
		addTimeSpan(placemark, event);
		parent.addContent(placemark);
	}

	/**
	 * Add description.  Formatted as KML HTML subset
	 * @param placemark
	 * @param event
	 */
	private static void addDescription(Element placemark, Event event) {
		Element description = new Element("description");
		StringBuffer sb = new StringBuffer()
			.append("<b>")
			.append(event.getWhen().getAsText())
			.append("</b><br/><em>")
			.append(event.getWhere())
			.append("</em>");
		sb.append("<br/><a href=\"")
		  .append(getLink(event)).append("\"><img src=\"")
		  .append(getBasepath()).append("/images/concharto-logo-xsm.png\"/></a>");
		sb.append("<br/><a href=\"")
		.append(getLink(event)).append("\">see more...</a>");
		String bpath = getBasepath(event);
		//NOTE: it is not the most efficient thing to create a new wikimodel for each event, but
		//until we are at 100,000 events it won't make a difference
    	WikiModel wikiModel = new TsmWikiModel(bpath, bpath + "images/${image}", bpath + "page.htm?page=${title}");
		sb = addNullableField(sb, renderWiki(wikiModel, event.getDescription()));
		sb.append("Source: ");
		sb = addNullableField(sb, renderWiki(wikiModel, event.getSource()));
		addTags(sb, event);

		description.addContent(new CDATA(sb.toString()));
		placemark.addContent(description);		
	}
	
	private static String renderWiki(WikiModel wikiModel, String rawWikiText) {
		String rendered = wikiModel.render(rawWikiText);
		return SubstitutionMacro.explicitStyles(rendered);
	}

	/**
	 * Add snippet
	 * @param placemark
	 * @param event
	 */
	private static void addSnippet(Element placemark, Event event) {
		Element snippet = new Element("Snippet");
		snippet.setAttribute("maxLines", "2");
		snippet.addContent(event.getWhen().getAsText() + ", " + event.getWhere() );
		placemark.addContent(snippet);
	}

	/**
	 * KML TimeSpan
	 * @param parent
	 * @param event
	 */
	private static void addTimeSpan(Element parent, Event event) {
		TimeRange tr = event.getWhen();
		if (tr.getBegin().getPrecision() == null) {
			//this is legacy data from before precisions.  We fix it by parsing it then formatting it
			//which is how it would work in the UI
			String trText = TimeRangeFormat.format(tr);
			try {
				tr = TimeRangeFormat.parse(trText);
			} catch (ParseException e) {
				log.error("Error re-constituting time for event id " + event.getId());
				throw new RuntimeException(e);
			}
			
		}
		Element timePrimitive = new Element("TimeSpan");
		timePrimitive.addContent(simpleElement("begin", getTimeStamp(tr.getBegin())));
		timePrimitive.addContent(simpleElement("end", getTimeStamp(tr.getEnd())));
		parent.addContent(timePrimitive);
	}

	private static String getTimeStamp(VariablePrecisionDate date) {
		sdf.applyPattern(precisionFormats[date.getPrecision()]);
		return sdf.format(date.getDate());
	}

	/**
	 * KML geometry - Polygon, LineString or Point
	 * @param parent
	 * @param event
	 */
	private static void addGeometry(Element parent, Event event) {
		Geometry geometry = event.getTsGeometry().getGeometry();
		addLookAt(parent, event);
		if (geometry instanceof Point) {
			addPoint(parent, (Point)geometry);
		} else if (geometry instanceof LineString) {
			addLine(parent, (LineString)geometry);
		}else if (geometry instanceof Polygon) {
			addPoly(parent, (Polygon)geometry);
		}
	}

	/**
	 * KML polygon
	 * @param parent
	 * @param poly
	 */
	private static void addPoly(Element parent, Polygon poly) {
		Element polygon = new Element("Polygon");
        addTesselate(polygon);
        Element outerBoundaryIs = new Element("outerBoundaryIs");
        Element linearRing = new Element("LinearRing");
        addCoordinates(linearRing, poly.getExteriorRing());
        outerBoundaryIs.addContent(linearRing);
        polygon.addContent(outerBoundaryIs);
        parent.addContent(polygon);
	}
	
	/**
	 * output coordinates in kml comma delimited format
	 * @param parent
	 * @param line
	 */
	private static void addCoordinates(Element parent, LineString line) {
    	StringBuffer sb = new StringBuffer();
        for (int i=0; i<line.getNumPoints(); i++) {
        	sb.append(getCoordinates(line.getPointN(i)))
        		.append(' ');
        }
        parent.addContent(simpleElement(COORDINATES, sb.toString()));
	}

	/**
	 * KML LineString
	 * @param parent
	 * @param line
	 */
	private static void addLine(Element parent, LineString line) {
		Element lineString = new Element("LineString");
        addTesselate(lineString);
        addCoordinates(lineString, line);
        parent.addContent(lineString);
	}

	/** */
	private static void addTesselate(Element parent) {
		//follow terrain.  Need to do this for very long lines, otherwise
        //they sink under the earth
        parent.addContent(simpleElement(TESSELLATE, "1"));
	}

	/**
	 * KML point
	 * @param parent
	 * @param point
	 */
	private static void addPoint(Element parent, Point point) {

		Element pointEmt = new Element("Point");
		pointEmt.addContent(simpleElement(COORDINATES, getCoordinates(point)));
		parent.addContent(pointEmt);
	}
	
	/**
	 * Format KML coordinate string 
	 * @param point
	 * @return
	 */
	private static String getCoordinates(Point point) {
		return 	llFormat.format(point.getX()) + 
		"," + 
		llFormat.format(point.getY());
	}
	
	/**
	 * LookAt KML element
	 * @param parent
	 * @param event
	 */
	private static void addLookAt(Element parent, Event event) {
		Element lookAt = new Element("LookAt");
		Geometry geometry = event.getTsGeometry().getGeometry();
		
		lookAt.addContent(simpleElement("longitude", 
				llFormat.format(getLookatPoint(geometry).getX())));
		lookAt.addContent(simpleElement("latitude", 
				llFormat.format(getLookatPoint(geometry).getY())));

		//TODO map this to zooms
		lookAt.addContent(simpleElement("tilt","0.0"));
		lookAt.addContent(simpleElement("heading","0.0"));
		lookAt.addContent(simpleElement("range",rangeFormat.format(zoomToRange(event.getZoomLevel()))));
		parent.addContent(lookAt);
	}

	/**
	 * Finds the place to which to attach the balloon text and look at the polygon
	 * (NOTE: this deosn't seem to work for lines in google earth, but we will leave it
	 * here for now)
	 * @param geometry
	 * @return
	 */
	private static Point getLookatPoint(Geometry geometry) {
		if (geometry instanceof Point) {
			return (Point) geometry;
		} else if (geometry instanceof LineString) {
			return findClosestToCentroid((LineString) geometry);
		}else if (geometry instanceof Polygon) {
			return findClosestToCentroid(((Polygon)geometry).getExteriorRing());
		}
		return null;
	}

	/**
	 * Find the point on the line that is is in closest proximity to the centroid 
	 * of the line.   
	 * @param line
	 * @return
	 */
	private static Point findClosestToCentroid(LineString line) {
		
		double minDist = 1000000000;
		Coordinate centroid = line.getCentroid().getCoordinate();
		Coordinate[] linecoords = line.getCoordinates();
		Coordinate closest = centroid;
		double distance;
		for (Coordinate coord : linecoords) {
			distance = centroid.distance(coord);
			if ( distance < minDist) {
				minDist = distance;
				closest = coord;
			}
		}
		GeometryFactory gf = new GeometryFactory(); 
		return gf.createPoint(closest);
	}

	/** Add linked tags
	 * 
	 * @param sb
	 * @param userTags
	 */
	private static void addTags(StringBuffer sb, Event event) {
		int i=0;
		for (UserTag tag : event.getUserTags()) {
			if (i == 0) {
				sb.append("Tags: ");
			}
			sb.append("<a href=\"")
			  .append(getBasepath(event)).append("/search/eventsearch.htm?_tag=");
			
			try {
				sb.append(URLEncoder.encode(tag.getTag(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				log.error("Error encoding tag " + tag);
				throw new RuntimeException(e);
			}
			sb.append("&_maptype=3")
			  .append("\">")
			  .append(tag.getTag())
			  .append("</a>");
			if (++i < event.getUserTags().size()) {
				sb.append(", ");
			}
		}
		
	}
	/**
	 * Add a field or <br/> if the field is null
	 * @param sb
	 * @param field
	 * @return
	 */
	private static StringBuffer addNullableField(StringBuffer sb, String field) {
		if (StringUtils.isEmpty(field)) {
			sb.append("<br/>");
		} else {
			sb.append(field);
		}
		return sb;
	}
	
	/**
	 * Construct a permalink to the event
	 * @param event
	 * @return
	 */
	private static String getLink(Event event) {
		return getBasepath(event) + "/list/event.htm?_id=" + event.getId();
	}


	/**
	 * Add the Metadata object
	 * @param parent
	 * @param event
	 */
	private static void addMetadata(Element parent, Event event) {
		Element metadata = new Element("Metadata");
		Element permalink = new Element("permalink");
		permalink.addContent(simpleElement("href", getLink(event)));
		metadata.addContent(permalink);
		parent.addContent(metadata);
	}

	/**
	 * Create a simple element of the form
	 * <pre>
	 * &LT;elementName&GTtext&LT/elementName&GT
	 * </pre>
	 * @param elementName
	 * @param text
	 * @return Element
	 */
	private static Element simpleElement(String elementName, String text) {
		Element element = new Element(elementName);
		element.setText(text);
		return element;
	}
	
	/**
	 * add a simple element, using the given namespace
	 * <pre>
	 * &LT;namespace:elementName&GTtext&LT/namespace:elementName&GT
	 * </pre>
	 * @param elementName
	 * @param text
	 * @param namespace
	 * @return
	 */
	private static Element simpleElement(String elementName, String text, Namespace namespace) {
		Element element = simpleElement(elementName, text);
		element.setNamespace(namespace);
		return element;
	}


	private static final double ZOOM_TO_RANGE[] = 
	{10619000,8331000,7253000,6314000,4622000,2311000,1116000,578000,300000,160270,77400,32540,16850,8430,4220,1440,780,425,170,115,50};
	/**
	 * map google map zoom levels to KML altitudes.  
	 * TODO refactor this - it is really an exponential function
	 * @param zoom
	 * @return
	 */
	protected static double zoomToRange(int zoom) {
		return ZOOM_TO_RANGE[zoom];
	}

	/**
	 * Setup for processing more than catalog (e.g. history, fishing, ...)
	 * @param initialBasepath
	 */
	private static void setupBasepath(String initialBasepath) {
		basepath = initialBasepath;
		basepathWithoutCatalog = StringUtils.substringAfter(initialBasepath, ".");
		basepathWithoutCatalog = "http://" + CATALOG_SUBST + "." + basepathWithoutCatalog; 
	}
	
	private static String getBasepath() {
		return basepath;
	}
	private static String getBasepath(Event event) {
		return StringUtils.replace(basepathWithoutCatalog, CATALOG_SUBST, event.getCatalog());
	}
}
