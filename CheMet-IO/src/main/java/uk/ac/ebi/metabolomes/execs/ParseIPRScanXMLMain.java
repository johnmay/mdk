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
package uk.ac.ebi.metabolomes.execs;

import au.com.bytecode.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.cli.Option;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;
import uk.ac.ebi.mdk.deprecated.XMLHelper;
import uk.ac.ebi.mdk.db.DatabaseProperties;

/**
 * ParseIPRScanXMLMain.java – MetabolicDevelopmentKit – Jun 3, 2011
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public class ParseIPRScanXMLMain
        extends CommandLineMain {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( ParseIPRScanXMLMain.class );

    public ParseIPRScanXMLMain( String[] args ) {
        super( args );
    }

    @Override
    public void setupOptions() {
        super.add( new Option( "x" , "xml" , true , "IPRScan XML output" ) );
    }

    @Override
    public void process() {
        if ( !getCmd().hasOption( "x" ) ) {
            System.out.println( "no xml option provided" );
            return;
        }

        File xmlFile = new File( getCmd().getOptionValue( "x" ) );
        if ( !xmlFile.exists() ) {
            System.out.println( "provided xml file does not exists: " + xmlFile );
        }

        Document doc = XMLHelper.buildDocument(xmlFile);
        Map<String , ECNumber> ip2ecMap = makeInterProtoECMap();

        NodeList proteinNodes = doc.getElementsByTagName( "protein" );
        for ( int i = 0; i < proteinNodes.getLength(); i++ ) {
            Node proteinNode = proteinNodes.item( i );
            NamedNodeMap attributeMap = proteinNode.getAttributes();
            Node queryId = attributeMap.getNamedItem( "id" );
            Node child = proteinNode.getFirstChild();

            List<Node> hits = new ArrayList<Node>();
            while ( child != null ) {
                if ( child.getNodeName().equals( "interpro" ) ) {
                    NamedNodeMap interproAttributeMap = child.getAttributes();
                    hits.add( interproAttributeMap.getNamedItem( "id" ) );
                }
                child = child.getNextSibling();
            }
            if ( !hits.isEmpty() ) {
                HashSet<ECNumber> eccodes = new HashSet<ECNumber>();
                for ( Node node : hits ) {
                    if ( ip2ecMap.containsKey( node.getTextContent() ) ) {
                        eccodes.add( ip2ecMap.get( node.getTextContent() ) );
                    }
                }
                if ( eccodes.isEmpty() ) {
                    System.out.println( queryId.getTextContent() + "\t" + "NA" );
                } else {
                    System.out.println( queryId.getTextContent() + "\t" + StringUtils.join( new ArrayList( eccodes ) , ';' ) );
                }
            }

        }
    }

    public Map makeInterProtoECMap() {
        try {
            Map<String , ECNumber> ipToECMap = new HashMap<String , ECNumber>( 200 );
            File ecCodes = DatabaseProperties.getInstance().getFile( "interpro.ec.codes" );
            CSVReader reader = new CSVReader( new FileReader( ecCodes ) , '\t' );
            String[] row;
            while ( ( row = reader.readNext() ) != null ) {
                ipToECMap.put( row[0] , new ECNumber( row[2] ) );
            }
            reader.close();
            return ipToECMap;
        } catch ( IOException ex ) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void main( String[] args ) {
        ParseIPRScanXMLMain parser = new ParseIPRScanXMLMain( args );
        parser.process();
    }
}
