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
package uk.ac.ebi.metabolomes.io.xml;

import uk.ac.ebi.metabolomes.identifier.MIRIAMEntry;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import uk.ac.ebi.metabolomes.resource.DatabaseProperties;
import uk.ac.ebi.metabolomes.utilities.XMLHelper;

/**
 * MIRIAMResourceLoader.java – MetabolicDevelopmentKit – Jun 25, 2011
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public class MIRIAMResourceLoader {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( MIRIAMResourceLoader.class );
    private final File file = DatabaseProperties.getInstance().getDatabasePath( "miriam.xml" );
    private Map<String , MIRIAMEntry> nameToEntryMap = new HashMap<String , MIRIAMEntry>( 50 );

    /**
     * Singleton Accessor
     */
    public static MIRIAMResourceLoader getInstance() {
        return MIRIAMResourcesHolder.INSTANCE;
    }

    private static class MIRIAMResourcesHolder {

        private static final MIRIAMResourceLoader INSTANCE = new MIRIAMResourceLoader();
    }

    private MIRIAMResourceLoader() {
        if ( file != null ) {
            loadEntries();
        }
    }

    private void loadEntries() {

        Document xmlDocument = XMLHelper.buildDocument( file );

        if ( xmlDocument == null ) {
            return;
        }

        Node datatypeNode = xmlDocument.getLastChild().getFirstChild();

        while ( datatypeNode != null ) {
            if ( datatypeNode.getNodeName().equals( "datatype" ) ) {
                Node datatypeChild = datatypeNode.getFirstChild();

                String name = null,
                        urn = null,
                        definition = null;

                String id = datatypeNode.getAttributes().getNamedItem( "id" ).getNodeValue();
                String pattern = datatypeNode.getAttributes().getNamedItem( "pattern" ).getNodeValue();

                while ( datatypeChild != null ) {
                    if ( datatypeChild.getNodeName().equals( "name" ) ) {
                        name = datatypeChild.getTextContent();
                    } else if ( datatypeChild.getNodeName().equals( "definition" ) ) {
                        definition = datatypeChild.getTextContent();
                    } else if ( datatypeChild.getNodeName().equals( "uris" ) ) {
                        urn = datatypeChild.getChildNodes().item( 1 ).getTextContent();
                    }
                    datatypeChild = datatypeChild.getNextSibling();
                }
                // add to the map
                nameToEntryMap.put( name.toLowerCase() ,
                                    new MIRIAMEntry( id , pattern , name , definition , urn ) );
            }
            datatypeNode = datatypeNode.getNextSibling();
        }
    }

    /**
     * Access a MIRIAM resource entry by it's name, such as, 'chebi'.
     * @param name A Lower case name of resource with any space characters included, 'kegg compound
     * @return The MIRIAM entry associated with that name
     */
    public MIRIAMEntry getEntry( String name ) {
        if ( nameToEntryMap.containsKey( name ) ) {
            return nameToEntryMap.get( name );
        }
        logger.error("No MIRIAM entry found for name '" + name +"'");
        return null;
    }

}
