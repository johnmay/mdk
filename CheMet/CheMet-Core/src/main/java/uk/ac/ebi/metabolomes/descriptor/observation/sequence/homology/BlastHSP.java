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
package uk.ac.ebi.metabolomes.descriptor.observation.sequence.homology;

import java.awt.Dimension;
import org.w3c.dom.Node;

/**
 * BlastHSP.java
 * Blast High-scoring Sequence Pair
 *
 * @author johnmay
 * @date Apr 7, 2011
 */
public class BlastHSP extends LocalAlignment {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( BlastHSP.class );
    private static final String HSP_E_NODE_NAME = "Hsp_evalue";
    private static final String HSP_BIT_NODE_NAME = "Hsp_bit-score";
    private static final String HSP_NODE_NAME = "Hsp";
    private static final String HSP_IDENTITY = "Hsp_identity";
    private static final String HSP_POSITIVE = "Hsp_positive";
    private static final String HSP_ALIGN_LENGTH = "Hsp_align-len";
    private static final String HSP_QUERY_FROM = "Hsp_query-from";
    private static final String HSP_QUERY_TO = "Hsp_query-to";
    private static final String HSP_HIT_FROM = "Hsp_hit-from";
    private static final String HSP_HIT_TO = "Hsp_hit-to";
    private static final long serialVersionUID = -5692094407895765912L;
    private boolean parseHSPSequence;
    private BlastHit hit;

//    private String query;
//    private String alignment;
//    private String target;
    /**
     * Create a new Blast High-scoring Sequence Pair (Local alignment)
     * @param n
     * @param parent
     * @param parseSequence
     */
    public BlastHSP( Node n , BlastHit parent , boolean parseSequence ) {

        parseHSPSequence = parseSequence;

        // parse the values from the node

        // Structure: <Hit_hsps><Hsp><Hsps_evalue> etc.. so need to drill down one more teir
        Node node = getHspNode( n );

        Integer[] queryRange = new Integer[ 2 ];
        Integer[] hitRange = new Integer[ 2 ];

        while ( node != null ) {
            String nodeName = node.getNodeName();

            if ( nodeName.equals( HSP_BIT_NODE_NAME ) ) {
                setBitScore( Double.parseDouble( node.getTextContent() ) );
            } else if ( nodeName.equals( HSP_E_NODE_NAME ) ) {
                setExpectedValue( Double.parseDouble( node.getTextContent() ) );
            } else if ( nodeName.equals( HSP_QUERY_FROM ) ) {
                queryRange[0] = Integer.parseInt( node.getTextContent() );
            } else if ( nodeName.equals( HSP_QUERY_TO ) ) {
                queryRange[1] = Integer.parseInt( node.getTextContent() );
            } else if ( nodeName.equals( HSP_HIT_FROM ) ) {
                hitRange[0] = Integer.parseInt( node.getTextContent() );
            } else if ( nodeName.equals( HSP_HIT_TO ) ) {
                hitRange[1] = Integer.parseInt( node.getTextContent() );
            } else if ( nodeName.equals( HSP_POSITIVE ) ) {
                setPositive( Integer.parseInt( node.getTextContent() ) );
            } else if ( nodeName.equals( HSP_IDENTITY ) ) {
                setIdentity( Integer.parseInt( node.getTextContent() ) );
            } else if ( nodeName.equals( HSP_ALIGN_LENGTH ) ) {
                setAlignmentLength( Integer.parseInt( node.getTextContent() ) );
            } else if ( parseHSPSequence ) {
                throw new UnsupportedOperationException( "Parsing of blast HSP sequences is not yet supported" );
            }
            node = node.getNextSibling();
        }
        setQueryRange( queryRange );
        setHitRange( hitRange );
    }

    private Node getHspNode( Node n ) {
        Node node = n.getFirstChild();

        // find hsp node and return it's first child
        while ( node != null ) {
            if ( node.getNodeName().equals( HSP_NODE_NAME ) ) {
                return node.getFirstChild();
            }
            node = node.getNextSibling();
        }

        // returning a null node just means if won't be parsed in setScoring
        return node;
    }

    /**
     * Returns the hit (parent)
     * @return
     */
    public BlastHit getHit() {
        return hit;
    }
}
