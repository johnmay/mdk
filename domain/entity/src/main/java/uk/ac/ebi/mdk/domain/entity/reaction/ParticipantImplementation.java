/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.domain.entity.reaction;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.UUID;


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
public class ParticipantImplementation<M, S extends Number, C> extends BasicParticipant<M, S>
        implements CompartmentalisedParticipant<M, S, C> {

    private static final Logger LOGGER = Logger.getLogger(ParticipantImplementation.class);

    protected C compartment;


    public ParticipantImplementation(UUID uuid) {
        super(uuid);
    }


    public ParticipantImplementation(M molecule) {
        this(UUID.randomUUID());
        this.molecule = molecule;
    }


    public ParticipantImplementation(M molecule, S coefficient) {
        this(UUID.randomUUID());
        this.molecule = molecule;
        this.coefficient = coefficient;
    }


    public ParticipantImplementation(M molecule, S coefficient, C compartment) {
        super(molecule, coefficient);
        this.compartment = compartment;
    }

    public C getCompartment() {
        return compartment;
    }


    public void setCompartment(C compartment) {
        this.compartment = compartment;
    }

    @Deprecated
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.molecule);
        out.writeObject(this.coefficient);
        out.writeObject(this.compartment);
    }

    @Deprecated
    @SuppressWarnings("unchecked") public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
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
            sb.append(" [").append(this.compartment.toString()).append("]");
        }
        return sb.toString();
    }


    @Override
    @SuppressWarnings("unchecked") public boolean equals(Object obj) {
        if (obj == null) {
            LOGGER.debug("Object null");
            return false;
        }
        if (!obj.getClass().isInstance(this)) {
            LOGGER.debug("Classes not equal");
            return false;
        }
        final CompartmentalisedParticipant<M, S, C> other =
                                                    (CompartmentalisedParticipant<M, S, C>) obj;

        return equals(other);
    }


    /**
     * Calculates the hash code for a molecule. By default this molecule calls the provided
     * molecules {@code hashCode()} method. This method is intended to be overridden by sub
     * classes that use an 'M' object that do not override the default {@code hashCode}
     * (e.g. CDK AtomContainer).
     * @return hash code for the provided molecule
     */
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + molecule.hashCode();
        hash = 67 * hash + (coefficient != null ? coefficient.hashCode() : 0);
        hash = 67 * hash + (compartment != null ? compartment.hashCode() : 0);
        return hash;
    }


    public boolean equals(CompartmentalisedParticipant<M, S, C> other) {


        if (this.molecule != other.getMolecule() && (this.molecule == null || !this.molecule.equals(
                                                     other.getMolecule()))) {
            return false;
        }
        if (this.coefficient != other.getCoefficient()
            && (this.coefficient == null || !this.coefficient.equals(other.getCoefficient()))) {
            return false;
        }
        if (this.compartment != other.getCompartment()
            && (this.compartment == null || !this.compartment.equals(other.getCompartment()))) {
            return false;
        }
        return true;
    }


    @SuppressWarnings("unchecked") public int compareTo(Participant<M, S> o) {



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

    @Override
    public CompartmentalisedParticipant newInstance() {
        return new ParticipantImplementation(UUID.randomUUID());
    }
}
