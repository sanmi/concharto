package com.tech4d.tsm.lab.kml;

import info.bliki.wiki.model.WikiModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.tech4d.tsm.OpenSessionInViewIntegrationTest;
import com.tech4d.tsm.dao.EventTesterDao;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.time.TimeRange;
import com.tech4d.tsm.model.time.VariablePrecisionDate;
import com.tech4d.tsm.util.ContextUtil;
import com.tech4d.tsm.web.wiki.TsmWikiModel;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class Serialize extends OpenSessionInViewIntegrationTest {
	private static final String TESSELLATE = "tessellate";
	private static final String POLY_OPACITY = "6f";
	private static final String POLY_COLOR = POLY_OPACITY + "0000FF";
    private static final String LINE_OPACITY = "6f";
	private static final String LINE_COLOR = LINE_OPACITY + "0000FF"; //slightly opaque
	private static final String LINE_WIDTH = "4";
	private static final String COORDINATES = "coordinates";
	private static final String DEFAULT_STYLES = "defaultStyles";
	private static final String STYLE = "Style";
	private static final String BR = "<br/>";
    private static final String BASEPATH = "http://www.timespacemap.com";
	private static EventTesterDao eventTesterDao;
    protected static final Log logger = LogFactory.getLog(Serialize.class);
    //TODO extract the constants from WikiModelFactory
	private WikiModel wikiModel = new TsmWikiModel(BASEPATH, BASEPATH + "images/${image}", BASEPATH + "page.htm?page=${title}");
	private static NumberFormat numberFormat = NumberFormat.getInstance();
	private SimpleDateFormat sdf = new SimpleDateFormat();
	private static String[] precisionFormats = new String[VariablePrecisionDate.MAX_PRECISIONS];
	static {
		precisionFormats[VariablePrecisionDate.PRECISION_SECOND] = "yyyy-MM-DD'T'hh:mm:ss'Z'";
		precisionFormats[VariablePrecisionDate.PRECISION_MINUTE] = "yyyy-MM-DD'T'hh:mm:ss'Z'";
		precisionFormats[VariablePrecisionDate.PRECISION_HOUR] = "yyyy-MM-DD'T'hh:mm:ss'Z'";
		precisionFormats[VariablePrecisionDate.PRECISION_DAY] = "yyyy-MM-DD";
		precisionFormats[VariablePrecisionDate.PRECISION_MONTH] = "yyyy-MM";
		precisionFormats[VariablePrecisionDate.PRECISION_YEAR] = "yyyy";
	}

    @BeforeClass
    public static void setUp() {
    	numberFormat.setMaximumFractionDigits(6);

        ApplicationContext appCtx = ContextUtil.getCtx();       
        SessionFactory sessionFactory = (SessionFactory) appCtx.getBean("myTestDbSessionFactory");
        //different session factory - the test context!!
        setSessionFactory(sessionFactory);
        
        eventTesterDao = (EventTesterDao) appCtx.getBean("eventTesterDao");
        //replace the default with the test db
        eventTesterDao.setSessionFactory(sessionFactory);
    }
  
    @Test
    public void doSerialize() throws IOException, ParserConfigurationException {

    	List<Event> events = eventTesterDao.findRecent(20,0);
    	System.out.println(events.size() + "======================");
		serialize(events);
    }


	private void serialize(List<Event> events) throws ParserConfigurationException, IOException {
		
		//main kml document stuff
		Element kmlDocument = new Element("Document");
		kmlDocument.addContent(simpleElement("name","time space map events"));
		Element snippet = simpleElement("Snippet",null);
		snippet.setAttribute("maxLines","1");
		kmlDocument.addContent(snippet);
		kmlDocument.addContent(simpleElement("open","1"));

		addDefaultStyles(kmlDocument);
		for (Event event : events) {
			addPlacemark(kmlDocument, event);
		}

		Element root = new Element("kml", "http://earth.google.com/kml/2.0");
		root.addNamespaceDeclaration(Namespace.getNamespace("atom", "http://www.w3.org/2005/Atom"));
		root.addContent(kmlDocument);
		Document doc = new Document(root);

		FileOutputStream fileOutputStream = new FileOutputStream("tmp3.kml");
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8"); // I've also tried with "UTF8", no luck.

		XMLOutputter serializer = new XMLOutputter();
	    serializer.setFormat(Format.getPrettyFormat());
	    try {
	    	serializer.output(doc, outputStreamWriter);       
	    }
	    catch (IOException e) {
	      System.err.println(e);
	    }		
	}


	private void addDefaultStyles(Element parent) {
		// TODO Auto-generated method stub
		Element style = new Element(STYLE);
		style.setAttribute("id",DEFAULT_STYLES);
		
		//icon
		Element iconStyle = new Element("IconStyle");
		iconStyle.addContent(simpleElement("scale", "1"));
		Element icon = new Element("Icon");
		icon.addContent(simpleElement("href", "http://www.timespacemap.com/images/icons/markerA.png"));
		iconStyle.addContent(icon);
		style.addContent(iconStyle);

		//line
		Element lineStyle = new Element("LineStyle");
		lineStyle.addContent(simpleElement("color", LINE_COLOR));
		lineStyle.addContent(simpleElement("width", LINE_WIDTH));
		style.addContent(lineStyle);
		
		//poly
		Element polyStyle = new Element("PolyStyle");
		polyStyle.addContent(simpleElement("color", POLY_COLOR));
		polyStyle.addContent(simpleElement("colorMode", "random"));
		style.addContent(polyStyle);
		
		parent.addContent(style);
	}

	/** 
	 * Add a placemark to the kml document
	 * @param parent
	 * @param event
	 */
	private void addPlacemark(Element parent, Event event) {
		Element placemark = new Element("Placemark");
		placemark.setAttribute("id", "tsm:" + event.getId());
		
		Namespace atom = Namespace.getNamespace("atom", "http://www.w3.org/2005/Atom");
		placemark.addContent(simpleElement("author", null, atom));
		placemark.addContent(simpleElement("link", getLink(event), atom));
		addMetadata(placemark, event);
		placemark.addContent(simpleElement("name", event.getSummary()));
		Element description = new Element("description");
		description.addContent(new CDATA(printEvent(event)));
		placemark.addContent(description);
		placemark.addContent(simpleElement("visibility", "1"));
		placemark.addContent(simpleElement("styleUrl", "#" + DEFAULT_STYLES));
		addGeometry(placemark, event);
		addTimePrimitive(placemark, event);
		parent.addContent(placemark);
}

	private void addTimePrimitive(Element parent, Event event) {
		TimeRange tr = event.getWhen();
		Element timePrimitive = new Element("TimePrimitive");
		timePrimitive.addContent(simpleElement("begin", getTimeStamp(tr.getBegin())));
		timePrimitive.addContent(simpleElement("end", getTimeStamp(tr.getEnd())));
		parent.addContent(timePrimitive);
	}

	private String getTimeStamp(VariablePrecisionDate date) {
		sdf.applyPattern(precisionFormats[date.getPrecision()]);
		return sdf.format(date.getDate());
	}

	private void addGeometry(Element parent, Event event) {
		Geometry geometry = event.getTsGeometry().getGeometry();
		if (geometry instanceof Point) {
			addPoint(parent, (Point)geometry);
			addLookAt(parent, event);
		} else if (geometry instanceof LineString) {
			addLine(parent, (LineString)geometry);
		}else if (geometry instanceof Polygon) {
			addPoly(parent, (Polygon)geometry);
		}
	}

	private void addPoly(Element parent, Polygon poly) {
		Element polygon = new Element("Polygon");
        addTesselate(polygon);
        Element outerBoundaryIs = new Element("outerBoundaryIs");
        Element linearRing = new Element("LinearRing");
        addCoordinates(linearRing, poly.getExteriorRing());
        outerBoundaryIs.addContent(linearRing);
        polygon.addContent(outerBoundaryIs);
        parent.addContent(polygon);
	}
	
	private void addCoordinates(Element parent, LineString line) {
    	StringBuffer sb = new StringBuffer();
        for (int i=0; i<line.getNumPoints(); i++) {
        	sb.append(getCoordinates(line.getPointN(i)))
        		.append(' ');
        }
        parent.addContent(simpleElement(COORDINATES, sb.toString()));
	}

	private void addLine(Element parent, LineString line) {
		Element lineString = new Element("LineString");
        addTesselate(lineString);
        addCoordinates(lineString, line);
        parent.addContent(lineString);
	}

	private void addTesselate(Element parent) {
		//follow terrain.  Need to do this for very long lines, otherwise
        //they sink under the earth
        parent.addContent(simpleElement(TESSELLATE, "1"));
	}

	private void addPoint(Element parent, Point point) {

		Element pointEmt = new Element("Point");
		pointEmt.addContent(simpleElement(COORDINATES, getCoordinates(point)));
		parent.addContent(pointEmt);
	}
	
	private String getCoordinates(Point point) {
		return 	numberFormat.format(point.getX()) + 
		"," + 
		numberFormat.format(point.getY());
	}
	
	private void addLookAt(Element parent, Event event) {
		Element lookAt = new Element("LookAt");
		lookAt.addContent(simpleElement("longitude", 
				numberFormat.format((event.getTsGeometry().getGeometry().getCentroid().getX()) )));
		lookAt.addContent(simpleElement("latitude", 
				numberFormat.format((event.getTsGeometry().getGeometry().getCentroid().getY()) )));
		//TODO map this to zooms
		lookAt.addContent(simpleElement("range","2500.0"));
		lookAt.addContent(simpleElement("tilt","0.0"));
		lookAt.addContent(simpleElement("heading","0.0"));
		parent.addContent(lookAt);
	}

	/**
	 * Print the event to an HTML formatted string
	 * @param event
	 * @return
	 */
	private String printEvent(Event event) {
		return new StringBuffer()
			.append(event.getSummary()).append(BR)
			.append(event.getWhen().getAsText()).append(BR)
			.append(event.getWhere()).append(BR)
			.append(wikiModel.render(event.getDescription())).append(BR)
			.append("Source: ").append(wikiModel.render(event.getSource())).append(BR)
			.append("Tags: ").append(event.getUserTagsAsString()).append(BR)
			.toString();
	}

	/**
	 * Construct a permalink to the event
	 * @param event
	 * @return
	 */
	private String getLink(Event event) {
		return "http://www.timespacemap.com/list/event.htm?_id=" + event.getId();
	}


	/**
	 * Add the Metadata object
	 * @param parent
	 * @param event
	 */
	private void addMetadata(Element parent, Event event) {
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
	private Element simpleElement(String elementName, String text) {
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
	private Element simpleElement(String elementName, String text, Namespace namespace) {
		Element element = simpleElement(elementName, text);
		element.setNamespace(namespace);
		return element;
	}

}
