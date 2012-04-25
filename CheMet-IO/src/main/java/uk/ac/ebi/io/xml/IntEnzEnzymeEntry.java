/*
 *     This file is part of Metabolic Network Builder
 *
 *     Metabolic Network Builder is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.io.xml;

import uk.ac.ebi.chemet.resource.classification.ECNumber;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.resource.DefaultIdentifierFactory;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * IntEnzEnzymeEntry.java
 * A class to represent the enzyme entry from the IntEnz XML file
 *
 * @author johnmay
 * @date May 6, 2011
 */
public class IntEnzEnzymeEntry {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( IntEnzEnzymeEntry.class );
    private ECNumber ec;
    private ECNumber transferedEC;
    private Boolean isPreliminaryEC;
    private String acceptedName;
    // store links as general database links
    // e.g. identifier and which db they are from
    private List<Identifier> links;
    // ELEMENT NAMES
    private static final String ENZYME_ELEMENT = "enzyme";
    private static final String EC_ELEMENT = "ec";
    private static final String ACCEPTED_NAME_ELEMENT = "accepted_name";
    private static final String LINKS_ELEMENT = "links";
    private static final String LINK_ELEMENT = "link";
    private static final String LINK_DB_ATTRIBUTE_NAME = "db";
    private static final String LINK_ACCESSION_ATTRIBUTE_NAME = "accession_number";
    // method names
    private static final String LINKS_PARSING_METHOD_NAME = "parseLinks";
    private static final String EC_PARSING_METHOD_NAME = "parseEC";
    private static final String ACCEPTED_NAME_METHOD_NAME = "parseAcceptedName";
    // method map for parsing
    private static final Map<String , Method> elementMethodMap = buildMethodMap();

    public IntEnzEnzymeEntry() {
    }

    /**
     * Create an entry given the StAX XMLStreamReader for the document
     * @param xmlr
     * @throws XMLStreamException
     */
    public IntEnzEnzymeEntry( XMLStreamReader xmlr ) throws XMLStreamException {

        links = new ArrayList<Identifier>();

        // check this is a valid start point
        if ( isEnzymeStartElement( xmlr ) ) {

            isPreliminaryEC = Boolean.parseBoolean( xmlr.getAttributeValue( 0 ) );

            while ( xmlr.hasNext() ) {
                int event = xmlr.next();
                if ( isEnzymeEndElement( event , xmlr ) ) {
                    break;
                }
                parse( event , xmlr );
            }
        } else {
            ec = null;
            transferedEC = null;
        }

    }

    /**
     * Get all links that where parsed irespective of their type
     * @return
     */
    public List<Identifier> getAllLinks() {
        return Collections.unmodifiableList( links );
    }

    /**
     * Get links of a certain class type e.g. UniProtIdentifier.class
     * @param <T>
     * @param idClassType
     * @return
     */
    public <T> List<T> getLinks( Class<T> idClassType ) {
        List<Identifier> sublist = new ArrayList<Identifier>();
        for ( Identifier link : links ) {
            if ( link.getClass() == idClassType ) {
                sublist.add( link );
            }
        }
        return ( List<T> ) sublist;
    }

    /************ ACCESSOR/MUTATOR METHODS *****************/
    /************ PARSING METHODS *****************/
    /**
     * The main parse method dispatches the method based on the element name. This uses
     * java reflect library for neatness
     * @param event
     * @param xmlr
     * @throws XMLStreamException
     */
    private void parse( int event , XMLStreamReader xmlr ) throws XMLStreamException {

        if ( event == XMLStreamConstants.START_ELEMENT ) {

            // is a start element so get the element name and see if there is a method for it
            String localName = xmlr.getLocalName();

            if ( elementMethodMap.containsKey( localName ) ) {
                try {
                    elementMethodMap.get( localName ).invoke( this , new Object[]{ xmlr } );
                } catch ( IllegalAccessException ex ) {
                    logger.error( "error invoking reflection method for element " + localName , ex );
                } catch ( IllegalArgumentException ex ) {
                    logger.error( "error invoking reflection method for element " + localName , ex );
                } catch ( InvocationTargetException ex ) {
                    logger.error( "error invoking reflection method for element " + localName , ex );
                }
            }
        }
    }

    /**
     * Parse the {@code <links>} element from the IntEnz entry
     * @param xmlr
     * @throws XMLStreamException
     */
    private void parseLinks( XMLStreamReader xmlr ) throws XMLStreamException {


        // iterate over the stream until we find the end element of the links
        while ( xmlr.hasNext() ) {
            int event = xmlr.next();
            // end element found so stop parsing the links
            if ( isLinksEndElement( event , xmlr ) ) {
                return;
            }
            // store the info in the link
            if ( isLinkStartElement( event , xmlr ) ) {
                parseLink( xmlr );
            }
        }
    }

    /**
     * Parses a link element and adds a new identifier to the links
     * list
     * @param xmlr the active stream reader (at a {@code <link>} node)
     */
    private void parseLink( XMLStreamReader xmlr ) {
        String db_name = null;
        String accession = null;

        int attributeCount = xmlr.getAttributeCount();
        if ( attributeCount != 0 ) {
            for ( int i = 0; i < attributeCount; i++ ) {
                String attributeName = xmlr.getAttributeLocalName( i );
                // get the database name
                if ( attributeName.equals( LINK_DB_ATTRIBUTE_NAME ) ) {
                    db_name = xmlr.getAttributeValue( i );
                } // get the value which will be the db accession
                else if ( attributeName.equals( LINK_ACCESSION_ATTRIBUTE_NAME ) ) {
                    accession = xmlr.getAttributeValue( i );
                }
                if ( db_name != null && accession != null ) {
                    if(DefaultIdentifierFactory.getInstance().hasSynonym(db_name)) {
                        links.add( DefaultIdentifierFactory.getInstance().ofSynonym( db_name , accession ) );
                    }
                    return;
                }
            }
        }


    }

    /**
     * Parse the {@code <ec>} from the IntEnz Entry
     * @param xmlr
     * @throws XMLStreamException
     */
    private void parseEC( XMLStreamReader xmlr ) throws XMLStreamException {
        xmlr.next();
        ec = new ECNumber( xmlr.getText() );
        ec.setPreliminary( isPreliminaryEC );
    }

    private void parseAcceptedName( XMLStreamReader xmlr ) throws XMLStreamException {
        xmlr.next();
        acceptedName = xmlr.getText();
    }

    public ECNumber getEc() {
        return ec;
    }

    public String getAcceptedName() {
        return acceptedName;
    }

    public static Boolean isEnzymeStartElement( XMLStreamReader xmlr ) {
        if ( xmlr.getEventType() == XMLStreamConstants.START_ELEMENT
             && xmlr.getLocalName().equals( ENZYME_ELEMENT ) ) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;

    }

    private static Boolean isEnzymeEndElement( int event , XMLStreamReader xmlr ) {
        if ( event == XMLStreamConstants.END_ELEMENT
             && xmlr.getLocalName().equals( ENZYME_ELEMENT ) ) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private static Boolean isECStartElement( int event , XMLStreamReader xmlr ) {
        return startElementTest( event , xmlr , EC_ELEMENT );
    }

    private static Boolean isLinkStartElement( int event , XMLStreamReader xmlr ) {
        return startElementTest( event , xmlr , LINK_ELEMENT );
    }

    private static Boolean isLinkEndElement( int event , XMLStreamReader xmlr ) {
        return startElementTest( event , xmlr , LINK_ELEMENT );
    }

    private static Boolean isLinksStartElement( int event , XMLStreamReader xmlr ) {
        return startElementTest( event , xmlr , LINKS_ELEMENT );
    }

    private static boolean isLinksEndElement( int event , XMLStreamReader xmlr ) {
        return endElementTest( event , xmlr , LINKS_ELEMENT );
    }

    private static Boolean startElementTest( int event , XMLStreamReader xmlr , String requiredName ) {
        if ( event == XMLStreamConstants.START_ELEMENT && xmlr.getLocalName().equals( requiredName ) ) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private static Boolean endElementTest( int event , XMLStreamReader xmlr , String requiredName ) {
        if ( event == XMLStreamConstants.END_ELEMENT && xmlr.getLocalName().equals( requiredName ) ) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * Builds a map of element names to the method names that parse them
     * @return
     */
    private static Map<String , Method> buildMethodMap() {

        Map<String , Method> methodMap = new HashMap<String , Method>();


        try {
            // map the start element name to the appropaite method

            methodMap.put( EC_ELEMENT , IntEnzEnzymeEntry.class.getDeclaredMethod( EC_PARSING_METHOD_NAME , XMLStreamReader.class ) );
            methodMap.put( LINKS_ELEMENT , IntEnzEnzymeEntry.class.getDeclaredMethod( LINKS_PARSING_METHOD_NAME , XMLStreamReader.class ) );
            methodMap.put( ACCEPTED_NAME_ELEMENT , IntEnzEnzymeEntry.class.getDeclaredMethod( ACCEPTED_NAME_METHOD_NAME , XMLStreamReader.class ) );

        } catch ( NoSuchMethodException ex ) {
            ex.printStackTrace();
        } catch ( SecurityException ex ) {
            ex.printStackTrace();
        }

        return methodMap;
    }
}
