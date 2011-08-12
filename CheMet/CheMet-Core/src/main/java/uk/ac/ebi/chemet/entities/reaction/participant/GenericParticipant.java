/**
 * AtomContainerParticipant.java
 *
 * 2011.08.12
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

import org.apache.log4j.Logger;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smsd.Isomorphism;
import org.openscience.cdk.smsd.interfaces.Algorithm;
import org.openscience.cdk.tools.manipulator.AtomContainerComparator;
import uk.ac.ebi.chemet.entities.Compartment;

/**
 * @name    AtomContainerParticipant
 * @date    2011.08.12
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class GenericParticipant extends AtomContainerParticipant {

    private static final Logger LOGGER = Logger.getLogger( GenericParticipant.class );
    private static AtomContainerComparator comparator = new AtomContainerComparator();

    public GenericParticipant( IAtomContainer molecule , Double coefficient , Compartment compartment ) {
        super( molecule , coefficient , compartment );
    }

    public GenericParticipant( IAtomContainer molecule , Double coefficient ) {
        super( molecule , coefficient );
    }

    public GenericParticipant( IAtomContainer molecule ) {
        super( molecule );
    }

    public GenericParticipant( Participant<IAtomContainer , Double , Compartment> participant ) {
        super( participant );
    }

    public GenericParticipant() {
    }

    @Override
    public int hashCode() {
        int hash = 7;

        hash = 67 * hash + super.molecule.getBondCount();
        hash = 67 * hash + super.molecule.getAtomCount();
        // hash atoms
        for ( int i = 0; i < super.molecule.getAtomCount(); i++ ) {
            IAtom atom = super.molecule.getAtom( i );
            hash = 67 * hash + ( atom.getCharge() != null ? atom.getCharge().hashCode() : 0 );
            hash = 67 * hash + ( atom.getAtomicNumber() != null ? atom.getAtomicNumber().hashCode() : 0 );
            hash = 67 * hash + ( atom.getMassNumber() != null ? atom.getMassNumber().hashCode() : 0 );
            hash = 67 * hash + ( atom.getExactMass() != null ? atom.getExactMass().hashCode() : 0 );
        }
        // don't check the bonds this is simply for a quicker hashCode

        hash = 67 * hash + ( super.coefficient != null ? super.coefficient.hashCode() : 0 );
        hash = 67 * hash + ( super.compartment != null ? super.compartment.hashCode() : 0 );
        return hash;

    }

    @Override
    public boolean equals( Participant<IAtomContainer , Double , Compartment> other ) {

        // other is also of Generic.. so we check raw
        // similarity instead of checking substructure
        if ( other instanceof GenericParticipant ) {
            return super.equals( other );
        }
        if ( this.coefficient != other.coefficient &&
             ( this.coefficient == null || !this.coefficient.equals( other.coefficient ) ) ) {
            return false;
        }
        if ( this.compartment != other.compartment &&
             ( this.compartment == null || !this.compartment.equals( other.coefficient ) ) ) {
            return false;
        }
        try {
            if ( this.molecule != other.molecule ) {
                Isomorphism comparison = new Isomorphism( Algorithm.DEFAULT , true );
                comparison.init( this.molecule , other.molecule , false , true );
                comparison.setChemFilters( false , true , false );
                return comparison.isSubgraph();
            }
        } catch ( Exception ex ) {
            LOGGER.error( "Unable to compare generic reaction participants: " + ex.getMessage() );
            return false;
        }
        return true;
    }

    @Override
    public int compareTo( Participant<IAtomContainer , Double , Compartment> o ) {
        if ( this.coefficient != null && o.coefficient != null ) {
            int coefComparison = this.coefficient.compareTo( o.coefficient );
            if ( coefComparison != 0 ) {
                return coefComparison;
            }
        }
        if ( this.compartment != null && o.compartment != null ) {
            int compComparison = this.compartment.compareTo( o.compartment );
            if ( compComparison != 0 ) {
                return compComparison;
            }
        }
        if ( this.molecule != null && o.molecule != null ) {
            return comparator.compare( this.molecule , o.molecule );
        }

        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if ( this.coefficient != null ) {
            sb.append( coefficient ).append( " " );
        }
        sb.append( molecule.getID() );
        if ( this.compartment != null ) {
            sb.append( " [" ).append( compartment ).append( "]" );
        }
        return sb.toString();
    }
}
