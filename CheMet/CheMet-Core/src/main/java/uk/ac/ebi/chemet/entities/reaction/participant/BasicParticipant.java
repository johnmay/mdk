package uk.ac.ebi.chemet.entities.reaction.participant;

import org.apache.log4j.Logger;

import uk.ac.ebi.core.AbstractReconstructionEntity;
import uk.ac.ebi.interfaces.reaction.Participant;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


/**
 * BasicParticipant - 04.03.12
 * <p/>
 * Represents a participant in a chemical reaction, pairing a coefficient with
 * a molecule. The class is generic allow storage of different molecule and
 * coefficient types. The coefficient should extend a number (i.e. {@see Double}
 * or {@see Integer}.
 */
public class BasicParticipant<M, S extends Number>
        extends AbstractReconstructionEntity
        implements Participant<M, S> {

    private static final Logger LOGGER = Logger.getLogger(BasicParticipant.class);

    protected M molecule;

    protected S coefficient;


    /**
     * Default constructor creates an empty participant without
     * any default instantiation of the molecule or coefficient
     */
    public BasicParticipant() {
    }


    /**
     * Instantiate the participant with a molecule and no
     * coefficient
     *
     * @param molecule the molecule of the participant
     */
    public BasicParticipant(M molecule) {
        this.molecule = molecule;
    }


    /**
     * Instantiate the participant, providing both the molecule
     * and the coefficient.
     *
     * @param molecule    the molecule of the participant
     * @param coefficient the coefficient
     */
    public BasicParticipant(M molecule, S coefficient) {
        this.molecule = molecule;
        this.coefficient = coefficient;
    }


    /**
     * Coefficient accessor
     *
     * @return
     */
    public S getCoefficient() {
        return coefficient;
    }


    /**
     * Coefficient mutator
     *
     * @param coefficient
     */
    public void setCoefficient(S coefficient) {
        this.coefficient = coefficient;
    }


    /**
     * Molecule accessor
     *
     * @return
     */
    public M getMolecule() {
        return molecule;
    }


    /**
     * Molecule mutator
     *
     * @param molecule
     */
    public void setMolecule(M molecule) {
        this.molecule = molecule;
    }

    /**
     * @deprecated converting IO of entities to versioned marshalling
     */
    @Deprecated
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.molecule);
        out.writeObject(this.coefficient);
    }

    /**
     * @deprecated converting IO of entities to versioned marshalling
     */
    @Deprecated
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.molecule = (M) in.readObject();
        this.coefficient = (S) in.readObject();
    }


    /**
     * Provides a string representation of the reaction
     * participant. This will display the string representation
     * of the molecule prefixed with the coefficient.
     *
     * @inheritDoc
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder(20);

        if (this.coefficient != null) {
            sb.append(this.coefficient.toString()).append(' ');
        }

        sb.append(this.molecule);

        return sb.toString();

    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            LOGGER.debug("Object null");
            return false;
        }
        if (!obj.getClass().isInstance(this)) {
            LOGGER.debug("Classes not equal");
            return false;
        }
        final BasicParticipant<M, S> other =
                (BasicParticipant<M, S>) obj;

        return equals(other);
    }


    /**
     * Calculates the hash code for a molecule. By default this molecule calls the provided
     * molecules {@code hashCode()} method. This method is intended to be overridden by sub
     * classes that use an 'M' object that do not override the default {@code hashCode}
     * (e.g. CDK AtomContainer).
     *
     * @return hash code for the provided molecule
     */
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + molecule.hashCode();
        hash = 67 * hash + (coefficient != null ? coefficient.hashCode() : 0);
        return hash;
    }


    public boolean equals(Participant<M, S> other) {


        if (this.molecule != other.getMolecule() && (this.molecule == null || !this.molecule.equals(
                other.getMolecule()))) {
            return false;
        }
        if (this.coefficient != other.getCoefficient()
                && (this.coefficient == null || !this.coefficient.equals(other.getCoefficient()))) {
            return false;
        }
        return true;
    }


    public int compareTo(Participant<M, S> o) {

        if (this.coefficient != null && o.getCoefficient() != null && this.coefficient instanceof Comparable) {
            int coefComparison = ((Comparable) this.coefficient).compareTo(o.getCoefficient());
            if (coefComparison != 0) {
                return coefComparison;
            }
        }

        if (this.molecule != null && o.getMolecule() != null && this.molecule instanceof Comparable) {
            return ((Comparable) this.molecule).compareTo(o.getMolecule());
        }

        return 0;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Participant newInstance() {
        return new BasicParticipant();
    }
}
