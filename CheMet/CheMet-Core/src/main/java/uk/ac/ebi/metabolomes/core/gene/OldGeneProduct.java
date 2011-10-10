/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.core.gene;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;
import uk.ac.ebi.chemet.entities.reaction.Reaction;
import uk.ac.ebi.core.AbstractAnnotatedEntity;


/**
 * GeneProduct.java
 *
 *
 * @author johnmay
 * @date Apr 4, 2011
 */
public abstract class OldGeneProduct
  extends AbstractAnnotatedEntity
  implements Externalizable {

    private transient static final org.apache.log4j.Logger logger =
                                                           org.apache.log4j.Logger.getLogger(
      OldGeneProduct.class);
    private ProductType type;
    private List<Reaction> reactions = new ArrayList();
    private String sequence; // have to store as simple string as BioJava3 ProteinSequence is not serializable and we want to store the object it to disk :-)
    private Integer sequenceLength;
    public static final String BASE_TYPE = "Product";


    public ProductType getType() {
        return type;
    }


    public void setType(ProductType type) {
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
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }


    public Integer getSequenceLength() {
        return sequenceLength;
    }


    public void setSequenceLength(Integer sequenceLength) {
        this.sequenceLength = sequenceLength;
    }

    // reaction

    public boolean addReaction(Reaction reaction) {
        return reactions.add(reaction);
    }


    public boolean removeReaction(Reaction reaction) {
        return reactions.remove(reaction);
    }


    /**
     * Returns reactions associated with this gene product
     * @return
     */
    public List<Reaction> getReactions() {
        return reactions;
    }
    // metabolies?
    // parent gene


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        sequence = in.readUTF();
        sequenceLength = in.read();
//        type = (ProductType) in.readObject();
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeUTF(sequence);
        out.write(sequenceLength);
//        out.writeObject(type);
    }


    @Override
    public String getBaseType() {
        return BASE_TYPE;
    }


}

