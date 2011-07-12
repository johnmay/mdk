package uk.ac.ebi.metabolomes.webservices;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BrendaQueryResults extends DefaultHandler {

	private ArrayList<String> compounds;
	private boolean withinLigand;
	private boolean withinName;
	
	private void init() {
		withinLigand = false;
		withinName = false;
		compounds = new ArrayList<String>();
	}

	private enum BrendaResFields {
		name, ligand
	}
	
	public BrendaQueryResults() {
		this.init();
	}
	
	public ArrayList<String> getCompoundNames() {
		return compounds;
	}
    /**
     * Overrides <code>startElement(...)</code> in
     * <code>org.xml.sax.helpers.DefaultHandler</code>,
     * which in turn implements <code>org.xml.sax.ContentHandler</code>.
     * Called for each start tag encountered.
     * 
     * @param namespaceURI Required if the namespaces property is true.
     * @param attributes The specified or defaulted attributes.
     * @param localName The local name (without prefix), or the empty
     *        string if Namespace processing is not being performed.
     * @param qualifiedName The qualified name (with prefix), or the
     *        empty string if qualified names are not available. 
     */
    public void startElement(String namespaceURI,
                             String localName,
                             String qualifiedName,
                             Attributes attributes)
                throws SAXException {
             if( getName(localName, qualifiedName).equalsIgnoreCase(BrendaResFields.ligand.name())) withinLigand = true;
             if( getName(localName, qualifiedName).equalsIgnoreCase(BrendaResFields.name.name())) withinName = true;
    }
    
    public void endElement(String uri,
            String localName,
            String qName)
     throws SAXException {
        if( getName(localName, qName).equalsIgnoreCase(BrendaResFields.ligand.name())) withinLigand = false;
        if( getName(localName, qName).equalsIgnoreCase(BrendaResFields.name.name())) withinName = false;
    }
	

	public void characters(char[] ch, int start,int length) throws SAXException {
		if(ch.length > start+length) {
			if(withinLigand && withinName) compounds.add(String.copyValueOf(ch, start, length));
		}
		else {
			SAXException e = new SAXException();
			e.initCause(new Throwable("Char array given is smaller than start+length combination given"));
			throw e;
		}		
	}
	
    private String getName(String s1, String s2) {
        if (s1 == null || "".equals(s1)) return s2;
        else return s1;
    }
}
