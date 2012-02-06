
/**
 *
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
 *
 */
package uk.ac.ebi.chemet.entities.reaction.participant;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.interfaces.*;
import org.openscience.cdk.smsd.Isomorphism;
import org.openscience.cdk.smsd.interfaces.Algorithm;
import org.openscience.cdk.tools.manipulator.AtomContainerComparator;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.core.CompartmentImplementation;
import uk.ac.ebi.core.tools.hash.MolecularHashFactory;
import uk.ac.ebi.metabolomes.util.CDKUtils;


/**
 *
 * <h3>AtomContainerParticipant</h3>
 * <b>Created:</b> 2011.08.12 <br>
 *
 * A reaction participant that acts as a container for a CDK IAtomContainer object
 *
 * @version $Rev$ : Last Changed $Date$
 *
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *
 */
public class AtomContainerParticipant extends Participant<IAtomContainer , Double , CompartmentImplementation> {

    private static final Logger LOGGER = Logger.getLogger( AtomContainerParticipant.class );
    private static AtomContainerComparator comparator = new AtomContainerComparator();
    transient private IAtom[] atoms;
    transient private IBond[] bonds;
    transient private IAtomContainer skeleton;


    public AtomContainerParticipant( IAtomContainer molecule , Double coefficient ,
                                     CompartmentImplementation compartment ) {

        super( molecule , coefficient , compartment );
        skeleton = AtomContainerManipulator.removeHydrogens( molecule );
        setAtoms( molecule );

    }


    public AtomContainerParticipant( IAtomContainer molecule , Double coefficient ) {
        super( molecule , coefficient );
        skeleton = AtomContainerManipulator.removeHydrogens( molecule );
        setAtoms( molecule );

    }


    public AtomContainerParticipant( IAtomContainer molecule ) {
        super( molecule );
        skeleton = AtomContainerManipulator.removeHydrogens( molecule );
        setAtoms( molecule );

    }



    public AtomContainerParticipant() {
    }


    @Override
    public void setMolecule( IAtomContainer molecule ) {
        super.setMolecule( molecule );
        setAtoms( molecule );
        skeleton = AtomContainerManipulator.removeHydrogens( molecule );
    }


    private void setAtoms( IAtomContainer atomContainer ) {

        IAtomContainer trimmedMoleucle = AtomContainerManipulator.removeHydrogens( atomContainer );
        atoms =
        AtomContainerManipulator.getAtomArray( CDKUtils.setBondOrderSums( trimmedMoleucle ) );
        bonds = AtomContainerManipulator.getBondArray( trimmedMoleucle );

        Arrays.sort( atoms , new Comparator<IAtom>() {

            public int compare( IAtom o1 , IAtom o2 ) {
                // I wish symbol were an enumeration
                int val = o1.getSymbol().compareTo( o2.getSymbol() );
                if ( val != 0 ) {
                    return val;
                }
                Double bondOrder1 = o1.getBondOrderSum();
                Double bondOrder2 = o2.getBondOrderSum();
                return bondOrder1.compareTo( bondOrder2 );
            }


        } );
    }


    /**
     * Accessor for the skeleton molecule. This molecule is the input molecule with the hydrogens
     * removed using the CDK AtomContainerManipulator.removeHydrogens() method.
     */
    public IAtomContainer getSkelton() {
        return skeleton;
    }


    /**
     *
     * Calculate the hash value for a reactant participant. The method uses the default hashCode()
     * methods of {@see Double} and {@see Compartment}. The CDK {@see AtomContainer} object does not
     * override the hashCode method therfore this is calculated. The hash is calculated by using the
     * skeleton molecule (@see getSkeleton()) atom count and bond count. The atoms from the skeleton
     * are sorted and the symbol, atomic number, exact mass and bond order sum is used
     *
     * @return Non-unique integer for this participant
     *
     */
    @Override
    public int hashCode() {

        int hash = MolecularHashFactory.getInstance().getHash(skeleton).hash;

//        int hash = 7;
//
//        hash = 257 * hash + ( ( Double ) Math.pow( this.skeleton.getAtomCount() ,
//                                                   this.skeleton.getBondCount() ) ).hashCode();
//        for ( IAtom atom : atoms ) {
//            hash = 257 * hash + ( atom.getSymbol() != null ? atom.getSymbol().hashCode() : 0 );
//            hash = 257 * hash +
//                   ( atom.getAtomicNumber() != null ? atom.getAtomicNumber().hashCode() : 0 );
//            hash = 257 * hash + ( atom.getExactMass() != null ? atom.getExactMass().hashCode() : 0 );
//            hash = 257 * hash +
//                   ( ( Double ) AtomContainerManipulator.getBondOrderSum( this.skeleton , atom ) ).
//              hashCode(); // can't use this
//        }

        hash = 257 * hash + ( super.coefficient != null ? super.coefficient.hashCode() : 0 );
        hash = 257 * hash + ( super.compartment != null ? super.compartment.hashCode() : 0 );

        return hash;

    }


    /**
     *
     * Determines whether this participant is equal to the other. TODO(write doc)
     *
     *
     * @param other
     * @return
     *
     */
    public boolean equals( Participant<IAtomContainer , Double , CompartmentImplementation> other ) {

        if ( other instanceof AtomContainerParticipant == false ) {
            return false;
        }

        // if the other participant is Generic (has R-group) call their equals instead
        // we also need to check that participant isn't generic otherwise we'd get into
        // and infinate loop of parsing to different equals methods
        if ( other instanceof GenericParticipant && this.getClass() ==
                                                    AtomContainerParticipant.class ) {
            LOGGER.debug( "Using Generic Comparisson" );
            return other.equals( this );
        }

        if ( this.hashCode() != other.hashCode() ) {
            LOGGER.debug( "Hash codes are not equal" );
            return false;
        }


        if ( this.coefficient != other.coefficient &&
             ( this.coefficient == null || !this.coefficient.equals( other.coefficient ) ) ) {
            LOGGER.debug( "Coefficients are not equal" );

            return false;
        }
        if ( this.compartment != other.compartment &&
             ( this.compartment == null || !this.compartment.equals( other.compartment ) ) ) {
            LOGGER.debug( "Compartments are not equal" );
            return false;
        }
        if ( this.molecule == other.molecule ) {
            return true;
        }

        try {

            if ( this.skeleton.getAtomCount() != ( ( AtomContainerParticipant ) other ).skeleton.
              getAtomCount() ) {
                LOGGER.debug( "Atom counts are not equal" );
                return false;
            }
            if ( this.skeleton.getBondCount() != ( ( AtomContainerParticipant ) other ).skeleton.
              getBondCount() ) {
                LOGGER.debug( "Bond counts are not equal" );
                return false;
            }

            // for single atom cases
            if ( this.molecule.getAtomCount() == 1 ) {
                IAtom queryAtom = this.molecule.getAtom( 0 );
                IAtom otherAtom = other.molecule.getAtom( 0 );

                if ( queryAtom.getSymbol() != otherAtom.getSymbol() && ( queryAtom.getSymbol() !=
                                                                         null ||
                                                                         !queryAtom.getSymbol().
                                                                        equals(
                                                                        otherAtom.getSymbol() ) ) ) {
                    return false;
                }
                if ( queryAtom.getCharge() != otherAtom.getCharge() && ( queryAtom.getCharge() !=
                                                                         null ||
                                                                         !queryAtom.getCharge().
                                                                        equals(
                                                                        otherAtom.getCharge() ) ) ) {
                    return false;
                }
                return true;
            }


            Isomorphism isoChecker = new Isomorphism( Algorithm.DEFAULT , true );
            isoChecker.init( this.molecule ,
                             other.molecule ,
                             true ,
                             true );
            isoChecker.setChemFilters( false , false , false );

            return isoChecker.getTanimotoSimilarity() == 1;

        } catch ( Exception ex ) {
            System.out.println( this.molecule.getID() );
            System.out.println( other.molecule.getID() );
            LOGGER.error( "Could not compare molecule: " + ex.getMessage() );
            ex.printStackTrace();
        }

        return false;

    }


    /**
     *
     * Overridden compare to method used to sort the participants. This method first uses the default comparators
     * of Compartment and Double and then the AtomContainerComparator of CDK to sort based on the molecule. If all
     * these methods are 0 then
     *
     * @param  o The other participant to compare to
     *
     * @return -1, 1 (Note: 0 will throw an exception at the moment as we want to use a TreeSet
     *                      to store the participants)
     *
     * @throws UnsupportedOperationException The method will throw and UnsupportedOperationException if the molecules
     *         are equal. This is unlikely to happen but is there as a precaution to warn the user.
     *
     */
    @Override
    public int compareTo( Participant<IAtomContainer , Double , CompartmentImplementation> o ) {

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

        throw new UnsupportedOperationException(
          "Possible addition of duplicate molecules to a reaction" );

    }


    /**
     *
     * Displays the participant as a string. The participant name tries the getID() method
     * from AtomContainer if the getID() is null it then tries uses the properties labeled
     * "Name" or "name" if they do not exists the AtomContainer toString() method is used.
     * The molecule name is prefixed with the stoichiometric coefficient and postfixed with
     * the compartment in square brackets (e.g. "2 ATP [e]")
     *
     * @return String-ified representation of the participant
     *
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder( 10 );

        if ( this.coefficient != null ) {
            sb.append( coefficient ).append( " " );
        }

        Map properites = molecule.getProperties();
        String name = molecule.getID() != null ? molecule.getID() :
                      properites.containsKey( "Name" ) ? properites.get( "Name" ).toString() :
                      properites.containsKey( "name" ) ? properites.get( "name" ).toString() :
                      molecule.toString();

        sb.append( name );

        if ( this.compartment != null ) {
            sb.append( compartment );
        }

        return sb.toString();

    }


}

