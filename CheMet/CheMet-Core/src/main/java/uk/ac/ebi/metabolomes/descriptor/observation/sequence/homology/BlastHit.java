
package uk.ac.ebi.metabolomes.descriptor.observation.sequence.homology;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * BlastHit.java
 * An observation of a blast high-scoring segment pair (Hit)
 *
 * @author johnmay
 * @date Mar 24, 2011
 */
public class BlastHit extends LocalAlignment
  implements Externalizable {

    private static final org.apache.log4j.Logger logger =
                                                 org.apache.log4j.Logger.getLogger(BlastHit.class);
    // for parsing the node
    private static final String HIT_DEFINITION_NODE_NAME = "Hit_def";
    private static final String HIT_IDENTIFIER_NODE_NAME = "Hit_id";
    private static final String HIT_LENGTH_NODE_NAME = "Hit_len";
    private static final String HIT_HIGHSCORING_SEQUENCE_PAIR_NODE_NAME = "Hit_hsps";
    private static final long serialVersionUID = 4492752062401736830L;
    private List<BlastHSP> hsps = new ArrayList<BlastHSP>();


    public BlastHit() {
    }


    /**
     * Construct the hit from the hit element in the Blast XML (option -m 7)
     * @param n
     */
    public BlastHit(Node node) {

        while( node != null ) {
            String nodeName = node.getNodeName();
            if( nodeName.equals(HIT_IDENTIFIER_NODE_NAME) ) {
                setHitIdString(node.getTextContent());
            } else if( nodeName.equals(HIT_DEFINITION_NODE_NAME) ) {
                setDefinition(node.getTextContent());
            } else if( nodeName.equals(HIT_LENGTH_NODE_NAME) ) {
                setHitLength(Integer.parseInt(node.getTextContent()));
            } else if( nodeName.equals(HIT_HIGHSCORING_SEQUENCE_PAIR_NODE_NAME) ) {
                hsps.add(new BlastHSP(node, this, false));
            }
            node = node.getNextSibling();
        }

    }


    /**
     * Returns the High-scoring Sequence Pairs for this hit
     * @return ArrayList of BlastHSP objects
     */
    public List<BlastHSP> getHsps() {
        return hsps;
    }


    /**
     * Return the bit score from a BlastHit is meaningless, in reality you
     * want the average or best bit score from one or more HSPs {@see getBestBitScore()}
     *
     * @return
     */
    @Override
    public Double getBitScore() {
        return getBestBitScore();
    }


    /**
     * Returns the best bit score from the hits High-scoring Sequence Pairs
     * @return
     */
    public Double getBestBitScore() {
        if( hsps.size() == 1 ) {
            return hsps.get(0).getBitScore();
        } else {
            throw new UnsupportedOperationException("Not yet implemented for mulitple hits");
        }
    }


    @Override
    public Double getExpectedValue() {
        return getBestExpectedValue();
    }


    @Override
    public Integer[] getHitRange() {
        if( hsps.size() == 1 ) {
            return hsps.get(0).getHitRange();
        } else {
            throw new UnsupportedOperationException("Not yet implemented for mulitple hits");
        }
    }


    @Override
    public Integer[] getQueryRange() {
        if( hsps.size() == 1 ) {
            return hsps.get(0).getQueryRange();
        } else {
            throw new UnsupportedOperationException("Not yet implemented for mulitple hits");
        }
    }


    @Override
    public Integer getAlignmentLength() {
        if( hsps.size() == 1 ) {
            return hsps.get(0).getAlignmentLength();
        } else {
            throw new UnsupportedOperationException("Not yet implemented for mulitple hits");
        }
    }


    @Override
    public Integer getIdentity() {
        if( hsps.size() == 1 ) {
            return hsps.get(0).getIdentity();
        } else {
            throw new UnsupportedOperationException("Not yet implemented for mulitple hits");
        }
    }


    @Override
    public Integer getPositive() {
        if( hsps.size() == 1 ) {
            return hsps.get(0).getPositive();
        } else {
            throw new UnsupportedOperationException("Not yet implemented for mulitple hits");
        }
    }


    public double getBestExpectedValue() {
        if( hsps.size() == 1 ) {
            return hsps.get(0).getExpectedValue();
        } else {
            throw new UnsupportedOperationException("Not yet implemented for mulitple hits");
        }
    }


    /**
     * List the blast hit values
     * @param out The print stream to list too (e.g. System.out)
     */
    public void list(PrintStream out) {
        out.println("  |- Target: " + getIdString() + "\n" +
                    "    |- Evalue: " + getExpectedValue() + "\n" +
                    "    \\- BitScore: " + getBestBitScore());
    }


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        hsps = (List<BlastHSP>) in.readObject();
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(hsps);
    }


}

