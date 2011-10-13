/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.io.flatfile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.xml.stream.XMLStreamException;
import uk.ac.ebi.resource.classification.ECNumber;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import uk.ac.ebi.resource.protein.UniProtIdentifier;
import uk.ac.ebi.metabolomes.resource.IntEnzDatabaseProperties;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import uk.ac.ebi.resource.protein.SwissProtIdentifier;

/**
 * IntEnzXML.java
 * Class for reading the IntEnz XML data and creating a data model
 *
 * @author johnmay
 * @date Mar 21, 2011
 */
public class IntEnzXML {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( IntEnzXML.class );
    private static final IntEnzDatabaseProperties intEnzDatabaseProperties = IntEnzDatabaseProperties.getInstance();
    private HashMap<ECNumber , IntEnzEnzymeEntry> enzymeSequenceMap;
    private HashMap<UniProtIdentifier , List<ECNumber>> sequenceEnzymeMap;
    private HashMap<String , IntEnzEnzymeEntry> entryMap;
    private List<IntEnzEnzymeEntry> entries;
    private Boolean loaded;

    public static IntEnzXML getLoadedInstance() {
        if ( IntEnzXMLHolder.INSTANCE.isLoaded() ) {
            return IntEnzXMLHolder.INSTANCE;
        }
        IntEnzXMLHolder.INSTANCE.loadEntries();
        return getInstance();
    }

    public static IntEnzXML getInstance() {
        return IntEnzXMLHolder.INSTANCE;
    }

    /**
     * Access a specific IntEnz Entry given the accession
     * @param accessionNumber
     * @return
     */
    public IntEnzEnzymeEntry getEntry( String accessionNumber ) {
        if ( entryMap.containsKey( accessionNumber ) ) {
            return entryMap.get( accessionNumber );
        }
        return null;
    }

    private static class IntEnzXMLHolder {

        public static IntEnzXML INSTANCE = new IntEnzXML();
    }

    /*
     * Constructor does not load the data
     */
    private IntEnzXML() {
        entries = new ArrayList<IntEnzEnzymeEntry>();
        sequenceEnzymeMap = new HashMap<UniProtIdentifier , List<ECNumber>>();
        enzymeSequenceMap = new HashMap<ECNumber , IntEnzEnzymeEntry>();
        loaded = false;
    }

    public List<ECNumber> getECNumbers( UniProtIdentifier identifier ) {
        return sequenceEnzymeMap.containsKey( identifier ) ? sequenceEnzymeMap.get( identifier ) : new ArrayList();
    }

    public List<IntEnzEnzymeEntry> getUniprotIdentifiers( ECNumber ec ) {
        return enzymeSequenceMap.containsKey( ec ) ? enzymeSequenceMap.get( ec ).getLinks( UniProtIdentifier.class ) : new ArrayList();
    }

    public String getAcceptedName( ECNumber ec ) {
        return enzymeSequenceMap.get( ec ).getAcceptedName();
    }

    /**
     * Loads the entries from the XML file
     */
    private void loadEntries() {

        // only load if i
        if ( loaded ) {
            return;
        }

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader xmlr = factory.createXMLStreamReader( new FileInputStream( intEnzDatabaseProperties.getIntEnzASCIIXML() ) );

            ArrayList<String> list = new ArrayList<String>();

            // when XMLStreamReader is created, it is positioned
            // at START_DOCUMENT event.
            int eventType = xmlr.getEventType();

            // check if there are more events in the input stream
            while ( xmlr.hasNext() ) {
                int event = xmlr.next();
                if ( event == XMLStreamConstants.END_DOCUMENT ) {
                    xmlr.close();
                    break;
                }
                if ( IntEnzEnzymeEntry.isEnzymeStartElement( xmlr ) ) {
                    IntEnzEnzymeEntry intEnzEnzymeEntry = new IntEnzEnzymeEntry( xmlr );
                    entries.add( intEnzEnzymeEntry );

                    enzymeSequenceMap.put( intEnzEnzymeEntry.getEc() , intEnzEnzymeEntry );

                    List<UniProtIdentifier> uniProtIdentifiers = intEnzEnzymeEntry.getLinks( UniProtIdentifier.class );
                    for ( UniProtIdentifier uniProtIdentifier : uniProtIdentifiers ) {
                        if ( !sequenceEnzymeMap.containsKey( uniProtIdentifier ) ) {
                            sequenceEnzymeMap.put( uniProtIdentifier , new ArrayList<ECNumber>() );
                        }
                        sequenceEnzymeMap.get( uniProtIdentifier ).add( intEnzEnzymeEntry.getEc() );
                    }
                }
            }
        } catch ( XMLStreamException ex ) {
            logger.error( "IntEnzXML encountered an error when loading the data" , ex );
        } catch ( FileNotFoundException ex ) {
            logger.error( "Could not load IntEnz XML as the file was not found: " + intEnzDatabaseProperties.getIntEnzASCIIXML() );
        }

        loaded = true;

    }

    public HashMap<ECNumber , IntEnzEnzymeEntry> getEnzymeSequenceMap() {
        return enzymeSequenceMap;
    }

    /*
     * Returns whether the data is loaded or not
     *
     */
    private Boolean isLoaded() {
        return loaded;
    }

    public static void main( String[] args ) {

        // could put into a test class
        IntEnzXML iex = IntEnzXML.getLoadedInstance();
        List<ECNumber> ecs = iex.getECNumbers( new SwissProtIdentifier( "P41747" ) );
        System.out.println( ecs );

        // query
    }
}
