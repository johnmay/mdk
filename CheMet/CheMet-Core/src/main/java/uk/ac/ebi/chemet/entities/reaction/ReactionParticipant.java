/**
 * ReactionParticipant.java
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
package uk.ac.ebi.chemet.entities.reaction;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.Class;
import org.apache.log4j.Logger;

/**
 * @name    ReactionParticipant
 * @date    2011.08.11
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   A container for a reaction participant
 * @param <M>
 * @param <S>
 * @param <C>
 */
public class ReactionParticipant<M , S , C>
        implements Externalizable {

    private static final Logger LOGGER = Logger.getLogger( ReactionParticipant.class );
    private M molecule;
    private S coefficient;
    private C compartment;

    public ReactionParticipant() {
    }

    public ReactionParticipant( M molecule ) {
        this.molecule = molecule;
    }

    public ReactionParticipant( M molecule , S coefficient ) {
        this.molecule = molecule;
        this.coefficient = coefficient;
    }

    public ReactionParticipant( M molecule , S coefficient , C compartment ) {
        this.molecule = molecule;
        this.coefficient = coefficient;
        this.compartment = compartment;
    }

    public ReactionParticipant( ReactionParticipant<M , S , C> participant ) {
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
            sb.append( this.coefficient.toString() );
        }
        sb.append( this.molecule );
        if ( this.compartment != null ) {
            sb.append( ' ' ).append( this.compartment.toString() );
        }
        return sb.toString();
    }
}
