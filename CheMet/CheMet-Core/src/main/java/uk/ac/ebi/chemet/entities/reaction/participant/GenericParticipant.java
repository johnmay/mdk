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
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.PseudoAtom;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smsd.Isomorphism;
import org.openscience.cdk.smsd.interfaces.Algorithm;
import org.openscience.cdk.tools.manipulator.AtomContainerComparator;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.chemet.entities.Compartment;
import uk.ac.ebi.metabolomes.util.CDKUtils;

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
    protected IAtomContainer trimmedMolecule;

    public GenericParticipant( IAtomContainer molecule , Double coefficient , Compartment compartment ) {
        super( molecule , coefficient , compartment );
        trimmedMolecule = CDKUtils.removePseudoAtoms( molecule );
    }

    public GenericParticipant( IAtomContainer molecule , Double coefficient ) {
        super( molecule , coefficient );
        trimmedMolecule = CDKUtils.removePseudoAtoms( molecule );
    }

    public GenericParticipant( IAtomContainer molecule ) {
        super( molecule );
        trimmedMolecule = CDKUtils.removePseudoAtoms( molecule );
    }

    public GenericParticipant( GenericParticipant participant ) {
        super( participant );
        this.trimmedMolecule = participant.trimmedMolecule;
    }

    public GenericParticipant() {
    }

    @Override
    public void setMolecule( IAtomContainer molecule ) {
        super.setMolecule( molecule );
        trimmedMolecule = CDKUtils.removePseudoAtoms( molecule );
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
            // the trimmed molecule should be a fragment
            if (this.trimmedMolecule.getAtomCount() >= other.molecule.getAtomCount() ) {
                return false;
            }

            if ( this.trimmedMolecule != other.molecule ) {
                Isomorphism comparison = new Isomorphism( Algorithm.DEFAULT , true );
                comparison.init( this.trimmedMolecule , other.molecule , false , true );
                comparison.setChemFilters( false , true , false );
                return comparison.isSubgraph();
            }
        } catch ( Exception ex ) {
            LOGGER.error( "Unable to compare generic reaction participants: " + ex.getMessage() );
            return false;
        }
        return true;
    }
}
