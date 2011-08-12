///**
// * GenericReaction.java
// *
// * 2011.08.11
// *
// * This file is part of the CheMet library
// *
// * The CheMet library is free software: you can redistribute it and/or modify
// * it under the terms of the GNU Lesser General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * CheMet is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU Lesser General Public License
// * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
// */
//package uk.ac.ebi.chemet.entities.reaction.generic;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import org.apache.log4j.Logger;
//import org.openscience.cdk.Molecule;
//import org.openscience.cdk.Reaction;
//import org.openscience.cdk.interfaces.IMolecule;
//import org.openscience.cdk.smsd.Isomorphism;
//import org.openscience.cdk.smsd.interfaces.Algorithm;
//import uk.ac.ebi.chemet.entities.reaction.AtomContainerReaction;
//import uk.ac.ebi.metabolomes.util.CDKUtils;
//
///**
// * @name    GenericReaction
// * @date    2011.08.11
// * @version $Rev$ : Last Changed $Date$
// * @author  johnmay
// * @author  $Author$ (this version)
// * @brief   GenericReaction is a reaction which allows comparison with -R group molecules. The class adds functionality
// *          to the AtomContainerReaction with a {@see equalsGeneric()} method
// *
// */
//public class GenericReaction
//        extends AtomContainerReaction {
//
//    private static final Logger LOGGER = Logger.getLogger( GenericReaction.class );
//
//
//    public boolean equalsGeneric( AtomContainerReaction other ) {
//
//        /* this more intensive then the simple equals() method as sorting R group may sort higher or
//        lower thenthe compared molecule. We therefore need to test every molecule.
//        To make this slightly easier we can first remove all 100 % matching molecules */
//
//        Map<Integer , Integer> hashCodeMap = new HashMap<Integer , Integer>();
//
//        // First get the hash codes for all molecules
//        for ( IMolecule mol : this.getAllReactionParticipants() ) {
//            Integer hash = moleculeHashCode( mol );
//            hashCodeMap.put( hash , hashCodeMap.containsKey( hash ) ? hashCodeMap.get( hash ) + 1 : 1 );
//            System.out.printf( "%15s %s\n" , hash , mol.getProperty( "name" ) );
//        }
//        for ( IMolecule mol : other.getAllReactionParticipants() ) {
//            Integer hash = moleculeHashCode( mol );
//            hashCodeMap.put( hash , hashCodeMap.containsKey( hash ) ? hashCodeMap.get( hash ) + 1 : 1 );
//            System.out.printf( "%15s %s\n" , hash , mol.getProperty( "name" ) );
//
//        }
//
//        System.out.println( hashCodeMap );
//
//        LinkedList<IMolecule> queryReac = new LinkedList( this.getReactantMolecules() );
//        LinkedList<IMolecule> queryProd = new LinkedList( this.getProductMolecules() );
//        LinkedList<IMolecule> otherReac = new LinkedList( other.getReactantMolecules() );
//        LinkedList<IMolecule> otherProd = new LinkedList( other.getProductMolecules() );
//
//        AtomContainerReaction queryMatchReaction = new AtomContainerReaction();
//        AtomContainerReaction otherMatchReaction = new AtomContainerReaction();
//
//        List<IMolecule> queryCompounds = new ArrayList<IMolecule>();
//        List<IMolecule> otherCompounds = new ArrayList<IMolecule>();
//
//        // get the differences differences
//        for ( IMolecule m : queryReac ) {
//            if ( hashCodeMap.get( moleculeHashCode( m ) ) > 1 ) {
//                queryMatchReaction.addReactant( m );
//            } else {
//                queryCompounds.add( new Molecule( m ) );
//            }
//        }
//        for ( IMolecule m : queryProd ) {
//            if ( hashCodeMap.get( moleculeHashCode( m ) ) > 1 ) {
//                queryMatchReaction.addProduct( m );
//            } else {
//                queryCompounds.add( new Molecule( m ) );
//            }
//        }
//        for ( IMolecule m : otherReac ) {
//            if ( hashCodeMap.get( moleculeHashCode( m ) ) > 1 ) {
//                otherMatchReaction.addReactant( m );
//            } else {
//                otherCompounds.add( new Molecule( m ) );
//            }
//        }
//        for ( IMolecule m : otherProd ) {
//            if ( hashCodeMap.get( moleculeHashCode( m ) ) > 1 ) {
//                otherMatchReaction.addProduct( m );
//            } else {
//                otherCompounds.add( new Molecule( m ) );
//            }
//        }
//
//
////        // other parts don't match so don't check the not matching parts
////        if ( queryMatchReaction.equals( otherMatchReaction ) == false ) {
////            return false;
////        }
//
//        if ( queryMatchReaction.hashCode() != otherMatchReaction.hashCode() ||
//             queryMatchReaction.equals( otherMatchReaction ) == false ) {
//            return false;
//        }
//        Map<IMolecule , IMolecule> genericMap = new HashMap<IMolecule , IMolecule>();
//        try {
//            Boolean[][] results = new Boolean[ 2 ][ 2 ];
//
//            for ( int i = 0; i < queryCompounds.size(); i++ ) {
//                IMolecule reference = queryCompounds.get( i );
//
////                for ( int k = 0; k < reference.getAtomCount(); k++ ) {
////                    if ( reference.getAtom( k ) instanceof PseudoAtom ) {
////                        reference.removeAtom( k );
////                        reference.removeElectronContainer( k );
////                    }
////                }
//
//                genericMap.put( reference , null );
//
//                for ( int j = 0; j < otherCompounds.size(); j++ ) {
//                    IMolecule query = otherCompounds.get( j );
//
//                    Isomorphism comparison = new Isomorphism( Algorithm.DEFAULT , true );
//                    comparison.init( reference , query , false , true );
//                    comparison.setChemFilters( false , true , false );
//
//                    if ( comparison.isSubgraph() ) {
//                        if ( genericMap.get( reference ) != null ) {
//                            throw new UnsupportedOperationException(
//                                    "Multiple matches found for generic reaction .equals() – unable to resolve this" );
//                        } else {
//                            genericMap.put( reference , query );
//                        }
//                    }
//
//                }
//
//                if ( genericMap.get( reference ) == null ) {
//                    return false;
//                }
//            }
//
//
//
//        } catch ( Exception ex ) {
//            System.out.println( ex.getClass() );
//        }
//
//
//
//        // check similarities
//        return true;
//    }
//
//    @Override
//    public boolean moleculeEqual( IMolecule m1 , IMolecule m2 ) throws Exception {
//        if ( super.moleculeEqual( m1 , m2 ) ) {
//            return true;
//        }
//        // check if both are generic... don't want to check this
//        if ( CDKUtils.isMoleculeGeneric( m1 ) & CDKUtils.isMoleculeGeneric( m2 ) ) {
//            return false;
//        }
//
//        Isomorphism comparison = new Isomorphism( Algorithm.DEFAULT , true );
//        if ( CDKUtils.isMoleculeGeneric( m1 ) ) {
//            comparison.init( m1 , m2 , false , true );
//        } else if ( CDKUtils.isMoleculeGeneric( m2 ) ) {
//            comparison.init( m2 , m1 , false , true );
//        }
//
//        comparison.setChemFilters( false , true , false );
//
//        return comparison.isSubgraph();
//    }
//}
