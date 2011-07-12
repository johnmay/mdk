/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.core.gene;

import java.io.Serializable;
import java.util.Collections;
import org.openscience.cdk.Reaction;
import uk.ac.ebi.metabolomes.core.ObjectDescriptor;
import uk.ac.ebi.metabolomes.core.reaction.ReactionCollection;

/**
 * GeneProduct.java
 *
 *
 * @author johnmay
 * @date Apr 4, 2011
 */
public abstract class GeneProduct
        extends ObjectDescriptor
        implements Serializable {

    private transient static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( GeneProduct.class );
    private ProductType type;
    private ReactionCollection reactions;
    private String sequence; // have to store as simple string as BioJava3 ProteinSequence is not serializable and we want to store the object it to disk :-)
    private Integer sequenceLength;

    public ProductType getType() {
        return type;
    }

    public void setType( ProductType type ) {
        this.type = type;
    }

    /**
     * Returns the identifier or calls the generic toString()
     * method of object
     * @return Identifiable name of product
     */
    @Override
    public String toString() {
        return getIdentifier() == null ? super.toString() : getIdentifier().toString();
    }

    /**
     * Accessor for the BioJava3 ProteinSequence stored in the GeneProteinProduct
     * @return ProteinSequence for this gene product
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * Mutator for the BioJava3 ProteinSequence stored in the GeneProteinProduct
     * @param sequence The new protein sequence
     */
    public void setSequence( String sequence ) {
        this.sequence = sequence;
    }

    public Integer getSequenceLength() {
        return sequenceLength;
    }

    public void setSequenceLength( Integer sequenceLength ) {
        this.sequenceLength = sequenceLength;
    }

    // reaction
    public boolean addReaction( Reaction reaction ) {
        return reactions.add( reaction );
    }

    public boolean removeReaction( Reaction reaction ) {
        return reactions.remove( reaction );
    }

    /**
     * Returns reactions associated with this gene product
     * @return
     */
    public ReactionCollection getReactions() {
        return ( ReactionCollection ) Collections.unmodifiableList( reactions );
    }
    // metabolies?
    // parent gene
}
