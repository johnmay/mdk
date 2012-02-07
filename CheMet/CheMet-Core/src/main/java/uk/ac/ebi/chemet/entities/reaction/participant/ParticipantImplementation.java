/**
 * Participant.java
 *
 * 2011.08.11
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.chemet.entities.reaction.participant;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.reaction.CompartmentalisedParticipant;
import uk.ac.ebi.interfaces.reaction.Participant;


/**
 * @name    Participant
 * @date    2011.08.11
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   A container for a reaction participant
 * @param <M>
 * @param <S>
 * @param <C>
 */
public class ParticipantImplementation<M, S extends Number, C>
        implements CompartmentalisedParticipant<M, S, C> {

    private static final Logger LOGGER = Logger.getLogger(ParticipantImplementation.class);

    protected M molecule;

    protected S coefficient;

    protected C compartment;


    public ParticipantImplementation() {
    }


    public ParticipantImplementation(M molecule) {
        this.molecule = molecule;
    }


    public ParticipantImplementation(M molecule, S coefficient) {
        this.molecule = molecule;
        this.coefficient = coefficient;
    }


    public ParticipantImplementation(M molecule, S coefficient, C compartment) {
        this.molecule = molecule;
        this.coefficient = coefficient;
        this.compartment = compartment;
    }


    public S getCoefficient() {
        return coefficient;
    }


    public void setCoefficient(S coefficient) {
        this.coefficient = coefficient;
    }


    public C getCompartment() {
        return compartment;
    }


    public void setCompartment(C compartment) {
        this.compartment = compartment;
    }


    public M getMolecule() {
        return molecule;
    }


    public void setMolecule(M molecule) {
        this.molecule = molecule;
    }


    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.molecule);
        out.writeObject(this.coefficient);
        out.writeObject(this.compartment);
    }


    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.molecule = (M) in.readObject();
        this.coefficient = (S) in.readObject();
        this.compartment = (C) in.readObject();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(4);
        if (this.coefficient != null) {
            sb.append(this.coefficient.toString()).append(' ');
        }
        sb.append(this.molecule);
        if (this.compartment != null) {
            sb.append(' ').append(this.compartment.toString());
        }
        return sb.toString();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParticipantImplementation<M, S, C> other =
                                                 (ParticipantImplementation<M, S, C>) obj;

        return equals(other);
    }


    /**
     * Calculates the hash code for a molecule. By default this molecule calls the provided
     * molecules {@code hashCode()} method. This method is intended to be overridden by sub
     * classes that use an 'M' object that do not override the default {@code hashCode}
     * (e.g. CDK AtomContainer).
     * @param  molecule The molecule to calculate the hashCode for
     * @return hash code for the provided molecule
     */
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + molecule.hashCode();
        hash = 67 * hash + (coefficient != null ? coefficient.hashCode() : 0);
        hash = 67 * hash + (compartment != null ? compartment.hashCode() : 0);
        return hash;
    }


    public boolean equals(ParticipantImplementation<M, S, C> other) {
        if (this.molecule != other.molecule && (this.molecule == null || !this.molecule.equals(
                                                other.molecule))) {
            return false;
        }
        if (this.coefficient != other.coefficient
            && (this.coefficient == null || !this.coefficient.equals(other.coefficient))) {
            return false;
        }
        if (this.compartment != other.compartment
            && (this.compartment == null || !this.compartment.equals(other.compartment))) {
            return false;
        }
        return true;
    }


    public int compareTo(Participant<M,S> o) {



        if (this.coefficient != null && o.getCoefficient() != null && this.coefficient instanceof Comparable) {
            int coefComparison = ((Comparable) this.coefficient).compareTo(o.getCoefficient());
            if (coefComparison != 0) {
                return coefComparison;
            }
        }

        if (o instanceof CompartmentalisedParticipant) {
            CompartmentalisedParticipant co = (CompartmentalisedParticipant) o;
            if (this.compartment != null && co.getCompartment() != null && this.compartment instanceof Comparable) {
                int compComparison = ((Comparable) this.compartment).compareTo(co.getCompartment());
                if (compComparison != 0) {
                    return compComparison;
                }
            }
        }

        if (this.molecule != null && o.getMolecule() != null && this.molecule instanceof Comparable) {
            return ((Comparable) this.molecule).compareTo(o.getMolecule());
        }

        return 0;
    }



}
