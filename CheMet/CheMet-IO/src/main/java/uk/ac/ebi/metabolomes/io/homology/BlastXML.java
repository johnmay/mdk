/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package uk.ac.ebi.metabolomes.io.homology;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.ac.ebi.metabolomes.core.gene.GeneProductCollection;
import uk.ac.ebi.metabolomes.core.gene.GeneProteinProduct;
import uk.ac.ebi.metabolomes.identifier.GenericIdentifier;
import uk.ac.ebi.metabolomes.descriptor.observation.JobParameters;
import uk.ac.ebi.metabolomes.descriptor.observation.sequence.homology.BlastHit;
import uk.ac.ebi.metabolomes.utilities.XMLHelper;

/**
 * BlastXML.java
 * A class for loading the Blast XML output (blastall -m 7) into the workable object
 *
 * @author johnmay
 * @date Apr 7, 2011
 */
public class BlastXML extends XMLHelper {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( BlastXML.class );
    private static int MAX_LOAD = 10000;
    private File xmlFile;

    public BlastXML( File xmlFile ) {
        this.xmlFile = xmlFile;
    }

    /**
     *
     * @param xmlFile
     * @param maxLoad The maximum number of hits to load
     */
    public BlastXML( File xmlFile , int maxLoad ) {
        this( xmlFile );
        MAX_LOAD = maxLoad;
    }

    public GeneProductCollection loadProteinHomologyObservations( GeneProductCollection products , JobParameters params ) {
        return loadProteinHomologyObservations( products , 10D, params );
    }

    public GeneProductCollection loadProteinHomologyObservations( GeneProductCollection products ,
                                                                  double expectValueThreshold ,
                                                                  JobParameters params ) {

        Boolean emptyProductSet = products.numberOfProducts() == 0 ? Boolean.TRUE : Boolean.FALSE;

        logger.debug("empty product collection provided, will not fetch new products from the collection");


        Document doc = buildDocument( this.xmlFile );
        NodeList iterators = doc.getElementsByTagName( "Iteration" );


        for ( int i = 0; i < iterators.getLength(); i++ ) {

            Node node = iterators.item( i ).getFirstChild();

            GeneProteinProduct currentProduct = null;

            while ( node != null ) {
                String nodeName = node.getNodeName();
                // if queryName != null then the second part isn't checked
                if ( nodeName.equals( QUERY_NODE_NAME ) ) {


                    if ( emptyProductSet ) {
                        // if the collection is empty we want to create one seperately and then try adding
                        currentProduct = new GeneProteinProduct( new GenericIdentifier( node.getTextContent() ) );
                        Boolean added = products.addProduct( currentProduct );
                        if (added == Boolean.FALSE){
                            logger.error("Gene Product was not added to collection as the intial collection provided was empty, this is likely a duplicate set of results");
                        }
                    }
                    else {
                        // get the product from the collection (a new one is created if a mathcing )
                        currentProduct = products.getProteinProduct( new GenericIdentifier( node.getTextContent() ) );
                    }

                } // add the hits as new observations of this gene product
                else if ( nodeName.equals( HITS_NODE_NAME ) ) {
                    Node hitsNode = node.getFirstChild();
                    while ( hitsNode != null ) {
                        Node hitsChild = hitsNode.getFirstChild();
                        if ( hitsChild != null ) {
                            BlastHit blasthit = new BlastHit( hitsChild );
                            blasthit.setParameters( params );
                            if ( blasthit.getBestExpectedValue() < expectValueThreshold ) {
                                currentProduct.addObservation( blasthit );
                            }
                        }
                        hitsNode = hitsNode.getNextSibling();
                    }
                } else if (nodeName.equals(QUERY_LENGTH)){
                    currentProduct.setSequenceLength(Integer.parseInt( node.getTextContent() ));
                }
                node = node.getNextSibling();
            }
        }
        return products;
    }
    private static final String QUERY_NODE_NAME = "Iteration_query-def";
    private static final String HITS_NODE_NAME = "Iteration_hits";
    private static final String QUERY_LENGTH = "Iteration_query-len";

    public static void main( String[] args ) {
//        File f = new File( "/Users/johnmay/EBI/Project/runspace/S.epidermidis/split/637000281.genes.1.fa-hits.xml" );
//        BlastXML blastXML = new BlastXML( f );
//        GeneProteinProduct[] products = blastXML.loadProteinHomologyObservations();
//
//        for ( GeneProteinProduct geneProteinProduct : products ) {
//            ArrayList<BlastHit> hits = geneProteinProduct.getObservations( BlastHit.class );
//            for ( BlastHit blastHit : hits ) {
//                System.out.println( blastHit.getTarget() );
//                System.out.println( blastHit.getBestExpectedValue() );
//            }
//        }
    }
}
