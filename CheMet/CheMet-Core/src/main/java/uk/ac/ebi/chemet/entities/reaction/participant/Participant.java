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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.Class;
import java.util.List;
import org.apache.log4j.Logger;

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
public class Participant<M , S , C>
        implements Externalizable , Comparable<Participant<M , S , C>> {

    private static final Logger LOGGER = Logger.getLogger( Participant.class );
    protected M molecule;
    protected S coefficient;
    protected C compartment;

    public Participant() {
    }

    public Participant( M molecule ) {
        this.molecule = molecule;
    }

    public Participant( M molecule , S coefficient ) {
        this.molecule = molecule;
        this.coefficient = coefficient;
    }

    public Participant( M molecule , S coefficient , C compartment ) {
        this.molecule = molecule;
        this.coefficient = coefficient;
        this.compartment = compartment;
    }

    public Participant( Participant<M , S , C> participant ) {
        this.molecule = participant.molecule;
        this.coefficient = participant.coefficient;
        this.compartment = participant.compartment;
    }

    public S getCoefficient() {
        return coefficient;
    }

    public void setCoefficient( S coefficient ) {
        this.coefficient = coefficient;
    }

    public C getCompartment() {
        return compartment;
    }

    public void setCompartment( C compartment ) {
        this.compartment = compartment;
    }

    public M getMolecule() {
        return molecule;
    }

    public void setMolecule( M molecule ) {
        this.molecule = molecule;
    }

    public void writeExternal( ObjectOutput out ) throws IOException {
        out.writeObject( this.molecule );
        out.writeObject( this.coefficient );
        out.writeObject( this.compartment );
    }

    public void readExternal( ObjectInput in ) throws IOException , ClassNotFoundException {
        this.molecule = ( M ) in.readObject();
        this.coefficient = ( S ) in.readObject();
        this.compartment = ( C ) in.readObject();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder( 4 );
        if ( this.coefficient != null ) {
            sb.append( this.coefficient.toString() ).append( ' ' );
        }
        sb.append( this.molecule );
        if ( this.compartment != null ) {
            sb.append( ' ' ).append( this.compartment.toString() );
        }
        return sb.toString();
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Participant<M , S , C> other =
                                     ( Participant<M , S , C> ) obj;

        return equals( other );
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
        hash = 67 * hash + ( coefficient != null ? coefficient.hashCode() : 0 );
        hash = 67 * hash + ( compartment != null ? compartment.hashCode() : 0 );
        return hash;
    }

    public boolean equals( Participant<M , S , C> other ) {
        if ( this.molecule != other.molecule && ( this.molecule == null || !this.molecule.equals( other.molecule ) ) ) {
            return false;
        }
        if ( this.coefficient != other.coefficient &&
             ( this.coefficient == null || !this.coefficient.equals( other.coefficient ) ) ) {
            return false;
        }
        if ( this.compartment != other.compartment &&
             ( this.compartment == null || !this.compartment.equals( other.compartment ) ) ) {
            return false;
        }
        return true;
    }

    public Boolean containedIn( List<Participant<M , S , C>> list ) {
        for ( Participant<M , S , C> participant : list ) {
            if ( participant.equals( this ) ) {
                return true;
            }
        }
        // return list.contains(this); // this doens't work for some reason
        return false;
    }

    public int compareTo( Participant<M , S , C> o ) {

        if ( this.coefficient != null && o.coefficient != null && this.coefficient instanceof Comparable ) {
            int coefComparison = ( ( Comparable ) this.coefficient ).compareTo( o.coefficient );
            if ( coefComparison != 0 ) {
                return coefComparison;
            }
        }
        if ( this.compartment != null && o.compartment != null && this.compartment instanceof Comparable ) {
            int compComparison = ( ( Comparable ) this.compartment ).compareTo( o.compartment );
            if ( compComparison != 0 ) {
                return compComparison;
            }
        }
        if ( this.molecule != null && o.molecule != null && this.molecule instanceof Comparable ) {
            return ( ( Comparable ) this.molecule ).compareTo( o.molecule );
        }

        return 0;
    }
}
