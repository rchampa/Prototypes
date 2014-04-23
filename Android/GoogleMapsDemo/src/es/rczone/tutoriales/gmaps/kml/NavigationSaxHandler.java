package es.rczone.tutoriales.gmaps.kml;

import java.util.Currency;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NavigationSaxHandler extends DefaultHandler {

	// ===========================================================
	// Fields
	// ===========================================================

	private int state = 0;
	private final int state_kml = 1;
	private final int state_placemark = 2;
	private final int state_name = 3;
	private final int state_description = 4;
	private final int state_geometrycollection = 5;
	private final int state_linestring = 6;
	private final int state_point = 7;
	private final int state_coordinates = 8;

	private String buffer;
	private String coors;

	private NavigationDataSet navigationDataSet = new NavigationDataSet();

	public NavigationDataSet getParsedData() {
		//navigationDataSet.getCurrentPlacemark().setCoordinates(buffer.toString().trim());
		return this.navigationDataSet;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	@Override
	public void startDocument() throws SAXException {
		this.navigationDataSet = new NavigationDataSet();
	}

	@Override
	public void endDocument() throws SAXException {
		// Nothing to do
	}

	/**
	 * Gets be called on opening tags like: <tag> Can provide attribute(s), when
	 * xml was like: <tag attribute="attributeValue">
	 */
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		if (localName.equals("kml")) {
			this.state = state_kml;
		} else if (localName.equals("Placemark")) {
			this.state = state_placemark;
			navigationDataSet.setCurrentPlacemark(new Placemark());
		} else if (localName.equals("name")) {
			this.state = state_name;
		} else if (localName.equals("description")) {
			this.state = state_description;
		} else if (localName.equals("GeometryCollection")) {
			this.state = state_geometrycollection;
		} else if (localName.equals("LineString")) {
			this.state = state_linestring;
		} else if (localName.equals("point")) {
			this.state = state_point;
		} else if (localName.equals("coordinates")) {
			buffer = "";
			this.coors = "";
			this.state = state_coordinates;
		}

	}

	/**
	 * Gets be called on closing tags like: </tag>
	 */
	@Override
	public void endElement(String namespaceURI, String localName, String qName)	throws SAXException {

		if (localName.equals("Placemark")) {
			if ("Route".equals(navigationDataSet.getCurrentPlacemark().getTitle()))
				navigationDataSet.setRoutePlacemark(navigationDataSet.getCurrentPlacemark());
			else
				navigationDataSet.addCurrentPlacemark();

		}
		else if(localName.equals("coordinates")){
			Placemark placemark = navigationDataSet.getCurrentPlacemark();
			placemark.addCoordinates(coors);
		}
		
	}

	/**
	 * Gets be called on the following structure: <tag>characters</tag>
	 */
	@Override
	public void characters(char ch[], int start, int length) {
		if (this.state == state_name) {
			if (navigationDataSet.getCurrentPlacemark() == null)
				navigationDataSet.setCurrentPlacemark(new Placemark());
			navigationDataSet.getCurrentPlacemark().setTitle(
					new String(ch, start, length));
		} else if (this.state == state_description) {
			if (navigationDataSet.getCurrentPlacemark() == null)
				navigationDataSet.setCurrentPlacemark(new Placemark());
			navigationDataSet.getCurrentPlacemark().setDescription(
					new String(ch, start, length));
		} else if (this.state == state_coordinates) { 
			if (navigationDataSet.getCurrentPlacemark() == null)
				navigationDataSet.setCurrentPlacemark(new Placemark());
			// navigationDataSet.getCurrentPlacemark().setCoordinates(new
			// String(ch, start, length));
			coors = coors + (new String(ch, start, length));
			
		}
	}
}