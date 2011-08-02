/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.util.inchi;

import java.util.List;
import net.sf.jniinchi.INCHI_OPTION;
import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 *
 * @author pmoreno
 */
public abstract class InChIProducer {

    protected InChIResult result;
    private static Logger logger = Logger.getLogger(InChIProducer.class.getName());

    public abstract InChIResult calculateInChI(IAtomContainer mol);

    public abstract void setInChIOptions(List<INCHI_OPTION> inchiConfigOptions);

    public static void typeMoleculeForInChI(IAtomContainer mol) throws CDKException {
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
    }

    public static void addHydrogensToMolecule(IAtomContainer mol) throws CDKException{
        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(mol.getBuilder());
        adder.addImplicitHydrogens(mol);
        AtomContainerManipulator.convertImplicitToExplicitHydrogens(mol);
    }

    public static boolean checkMoleculeForInChI(IAtomContainer mol) {
		// Has pseudoatoms?
		if (mol.getAtomCount() == 0)
			return false;
		boolean[] flags = mol.getFlags();
		for (IBond b : mol.bonds()) {
			if (b == null) {
				logger.info("Null bonds for: " + mol.getID());
				return false;
			}
			if (b.getAtomCount() < 2) {
				logger.info("bond with less than two atoms: "
						+ mol.getID());
				return false;
			}
			if (b.getAtom(0) == null || b.getAtom(1) == null) {
				logger.info("Bond with one or two atoms null: "
						+ mol.getID());
				return false;
			}
		}
		for (IAtom a : mol.atoms()) {
                        if(a == null)
                            return false;

			String className = a.getClass().getName();
			if (className.equalsIgnoreCase("org.openscience.cdk.PseudoAtom"))
				return false;
		}
		return true;
	}

}
