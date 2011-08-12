/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.util;

import java.io.IOException;
import java.io.StringReader;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.PseudoAtom;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.io.MDLV2000Reader;

/**
 *
 * @author pmoreno
 */
public class CDKUtils {

    /**
     * Visits all the atoms in an IAtomContainer and returns true if any
     * of them is an instance of the PseudoAtom class.
     *
     * @param mol to check whether it is generic or not.
     * @return true is molecule has a PseudoAtom
     */
    public static boolean isMoleculeGeneric( IAtomContainer mol ) {
        for ( IAtom atom : mol.atoms() ) {
            if ( atom instanceof PseudoAtom ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Visits all the molecules in a reaction and returns true if any of them
     * is generic (has a PseudoAtom).
     * @param rxn
     * @return
     */
    public static boolean isRxnGeneric( IReaction rxn ) {
        IMoleculeSet mols = rxn.getProducts();
        if ( moleculeSetContainsGenericMol( mols ) ) {
            return true;
        }

        mols = rxn.getReactants();
        if ( moleculeSetContainsGenericMol( mols ) ) {
            return true;
        }

        return false;
    }

    /**
     * Visits all the molecules in a MoleculeSet and returns true if any of them
     * is generic (has a PseudoAtom).
     * @param mols
     * @return
     */
    public static boolean moleculeSetContainsGenericMol( IMoleculeSet mols ) {
        for ( IAtomContainer mol : mols.molecules() ) {
            if ( isMoleculeGeneric( mol ) ) {
                return true;
            }
        }
        return false;
    }

    /*
     * Creates a shallow copy and removes pseudo atoms from the provided molecule
     */
    public static IAtomContainer removePseudoAtoms( IAtomContainer molecule ) {

        // first make a shallow copy
        IAtomContainer copiedAtomContainer = DefaultChemObjectBuilder.getInstance().newInstance( AtomContainer.class ,
                                                                                             molecule );
        for ( int i = 0; i < copiedAtomContainer.getAtomCount(); i++ ) {
            if ( copiedAtomContainer.getAtom( i ) instanceof PseudoAtom ) {
                copiedAtomContainer.removeAtomAndConnectedElectronContainers( copiedAtomContainer.getAtom( i ) );
            }
        }
        return copiedAtomContainer;
    }

    public static IAtomContainer mdlMolV2000Txt2CDKObject( String mol ) throws CDKException , IOException {
        IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
        StringReader sr = new StringReader( mol );
        MDLV2000Reader reader = new MDLV2000Reader( sr );//(MDLV2000Reader) readerFactory.createReader(new FileReader(input));
        IAtomContainer auxMol = ( IAtomContainer ) reader.read( builder.newInstance( Molecule.class ) );
        reader.close();
        return auxMol;
    }
}
