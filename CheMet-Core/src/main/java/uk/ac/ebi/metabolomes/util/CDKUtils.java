/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.PseudoAtom;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.*;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;


/**
 *
 * @author pmoreno
 */
public class CDKUtils {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(CDKUtils.class);


    /**
     * Visits all the atoms in an IAtomContainer and returns true if any of them
     * is an instance of the PseudoAtom class.
     *
     * @param mol to check whether it is generic or not.
     *
     * @return true is molecule has a PseudoAtom
     */
    public static boolean isMoleculeGeneric(IAtomContainer mol) {
        for (IAtom atom : mol.atoms()) {
            if (atom instanceof PseudoAtom) {
                return true;
            }
        }
        return false;
    }


    /**
     * Visits all the molecules in a reaction and returns true if any of them is
     * generic (has a PseudoAtom).
     *
     * @param rxn
     *
     * @return
     */
    public static boolean isRxnGeneric(IReaction rxn) {
        IMoleculeSet mols = rxn.getProducts();
        if (moleculeSetContainsGenericMol(mols)) {
            return true;
        }

        mols = rxn.getReactants();
        if (moleculeSetContainsGenericMol(mols)) {
            return true;
        }

        return false;
    }


    /**
     * Visits all the molecules in a MoleculeSet and returns true if any of them
     * is generic (has a PseudoAtom).
     *
     * @param mols
     *
     * @return
     */
    public static boolean moleculeSetContainsGenericMol(IMoleculeSet mols) {
        for (IAtomContainer mol : mols.molecules()) {
            if (isMoleculeGeneric(mol)) {
                return true;
            }
        }
        return false;
    }

    /*
     * Creates a shallow copy and removes pseudo atoms from the provided
     * molecule
     */

    public static IAtomContainer removePseudoAtoms(IAtomContainer molecule) {

        // first make a shallow copy
        IAtomContainer copiedAtomContainer = DefaultChemObjectBuilder.getInstance().newInstance(AtomContainer.class,
                                                                                                molecule);
        for (int i = 0; i < copiedAtomContainer.getAtomCount(); i++) {
            if (copiedAtomContainer.getAtom(i) instanceof PseudoAtom) {
                copiedAtomContainer.removeAtomAndConnectedElectronContainers(copiedAtomContainer.getAtom(i));
            }
        }
        return copiedAtomContainer;
    }


    public static IAtomContainer setBondOrderSums(IAtomContainer atomContainer) {
        for (IAtom atom : AtomContainerManipulator.getAtomArray(atomContainer)) {
            atom.setBondOrderSum(AtomContainerManipulator.getBondOrderSum(atomContainer, atom));
        }
        return atomContainer;
    }


    public static IAtomContainer mdlMolV2000Txt2CDKObject(String mol) throws CDKException, IOException {
        IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
        StringReader sr = new StringReader(mol);
        MDLV2000Reader reader = new MDLV2000Reader(sr);//(MDLV2000Reader) readerFactory.createReader(new FileReader(input));
        IAtomContainer auxMol = (IAtomContainer) reader.read(builder.newInstance(Molecule.class));
        reader.close();
        return auxMol;
    }

    private static final Map<String, Integer> symbolToOuterElectronCount = new HashMap();


    static {
        symbolToOuterElectronCount.put("H", 1);
        symbolToOuterElectronCount.put("C", 4);
        symbolToOuterElectronCount.put("N", 5);
        symbolToOuterElectronCount.put("O", 6);
        symbolToOuterElectronCount.put("P", 5);
        symbolToOuterElectronCount.put("S", 6);
    }


    /**
     * Sets the non-bonded valence electrons for each atom. The property
     * "nb-valence-electrons" is set for each atom
     *
     * @param molecule
     */
    public static void setNonBondedValenceElectronCount(IAtomContainer molecule) {

        for (IAtom atom : AtomContainerManipulator.getAtomArray(molecule)) {
            getNonBondedValenceElectronCount(molecule, atom);
        }

    }


    public static Integer getNonBondedValenceElectronCount(IAtomContainer molecule, IAtom atom) {

        if (atom.getProperties().containsKey("nb-valence-electrons")) {
            return (Integer) atom.getProperties().get("nb-valence-electrons");
        }

        Integer shellCount = symbolToOuterElectronCount.get(atom.getSymbol());

        if (shellCount != null) {
            int adjusted = shellCount - atom.getFormalCharge();
            int nonbondedvalence = adjusted - ((Double) AtomContainerManipulator.getBondOrderSum(molecule, atom)).intValue();
            atom.getProperties().put("nb-valence-electrons", nonbondedvalence);
            return nonbondedvalence;
        } else {
            LOGGER.error("Unhandled atom symbol: " + atom.getSymbol());
        }
        return 0;
    }

    private final static SmilesBugAmender smilesBugAmender = new SmilesBugAmender();


    /**
     * Looks for certain patterns of SMILES and uniforms them.
     *
     * @param smilesToAmend
     *
     * @return correctedSmiles
     */
    public static String amendCDKSmilesForPhosphateGroupLikeBug(String smilesToAmend) {
        return smilesBugAmender.amendSmiles(smilesToAmend);
    }


    /**
     * Visits all the molecules in a reaction and returns true if any of them is
     * empty (no atoms).
     *
     * @param rxn
     *
     * @return
     */
    public static boolean rxnHasParticipantWithNoAtoms(IReaction rxn) {
        IMoleculeSet mols = rxn.getProducts();
        if (moleculeSetContainsEmptyMol(mols)) {
            return true;
        }

        mols = rxn.getReactants();
        if (moleculeSetContainsEmptyMol(mols)) {
            return true;
        }

        return false;
    }


    /**
     * Visits all the molecules in a MoleculeSet and returns true if any of them
     * is empty (has no atoms).
     *
     * @param mols
     *
     * @return
     */
    public static boolean moleculeSetContainsEmptyMol(IMoleculeSet mols) {
        for (IAtomContainer mol : mols.molecules()) {
            if (isMoleculeEmpty(mol)) {
                return true;
            }
        }
        return false;
    }


    private static boolean isMoleculeEmpty(IAtomContainer mol) {
        return mol.getAtomCount() == 0;
    }


    /**
     * Similar to 
     * {@see IAtomContainer#removeAtomAndConnectedElectronContainers(IAtom)} but
     * returns a list of the atoms that the molecule was connected to
     * @param molecule
     * @param atom
     *
     * @return
     */
    public static List<IAtom> removeAtomAndGetConnected(IAtomContainer molecule, IAtom atom) {

        List<IAtom> atoms = molecule.getConnectedAtomsList(atom);       
      
        molecule.removeAtomAndConnectedElectronContainers(atom);

        return atoms;
    }


    /**
     * This method is similar to 
     * {@see IAtomContainer#removeAtomAndConnectedElectronContainers(IAtom)}
     * however the method returns a single atom. If the atom to remove is
     * connected to more then one a RuntimeException is thrown
     * 
     * 
     * @param molecule
     * @param atom
     *
     * @return
     */
    public static IAtom removeAtom(IAtomContainer molecule, IAtom atom) {

        List<IAtom> connected = removeAtomAndGetConnected(molecule, atom);
               
        if (connected.size() != 1) {
            throw new UnsupportedOperationException("There must be exactly 1 connected atom");
        }

        return connected.iterator().next();

    }
}
