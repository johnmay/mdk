/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.tool.inchi;

import java.util.List;
import net.sf.jniinchi.INCHI_OPTION;
import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 *
 * @author pmoreno
 */
public abstract class InChIProducer {

    private static final Logger LOGGER = Logger.getLogger(InChIProducer.class.getName());

    protected InChIResult result;
    private Boolean generic;
    private Boolean bondNull;
    private Boolean atomNull;

    public abstract InChIResult calculateInChI(IAtomContainer mol);

    public abstract void setInChIOptions(List<INCHI_OPTION> inchiConfigOptions);

    public static void typeMoleculeForInChI(IAtomContainer mol) throws CDKException {
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
    }

    public static void addHydrogensToMolecule(IAtomContainer mol) throws CDKException {
        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(mol.getBuilder());
        adder.addImplicitHydrogens(mol);
        AtomContainerManipulator.convertImplicitToExplicitHydrogens(mol);
    }

    /**
     * Checks whether the molecule is healthy for an InChI calculation.
     * 
     * @deprecated use {@link InChIMoleculeChecker} instead.
     * 
     * @param mol
     * @return true is inchi can be calculated, false if the molecule is not fit for inchi. 
     */
    @Deprecated
    public static boolean checkMoleculeForInChI(IAtomContainer mol) {
        return InChIMoleculeChecker.getInstance().checkMoleculeForInChI(mol);
    }
    
}
