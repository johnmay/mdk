/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.mdk.tool.inchi;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.inchi.InChIToStructure;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 *
 * @author pmoreno
 */
public class InChIConversionUtility {

    private static InChIGeneratorFactory inchiGenFact;
    private static IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();

    public static String[] convertInChI2StdInChI(String inchi) {
        String ans[] = null;
        try {
            // No problem here, the inchi is used as a reader.
            inchiGenFact = InChIGeneratorFactory.getInstance();
            InChIToStructure inchi2struc = inchiGenFact.getInChIToStructure(inchi, builder);
            ans = StdInChIGenerator.getInChIAndKeyFromCDKMol(inchi2struc.getAtomContainer());
        } catch (Exception ex) {
            Logger.getLogger(InChIConversionUtility.class.getName()).log(Level.FATAL, null, ex);
        }
        return ans;
    }

    public static String[] convertStdInChI2InChI(String inchi) {
        String ans[] = null;
        try {
            inchiGenFact = InChIGeneratorFactory.getInstance();
            IAtomContainer mol = StdInChIGenerator.getCDKMolFromInChI(inchi);
            AtomContainerManipulator.percieveAtomTypesAndConfigureUnsetProperties(mol);
            CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(mol.getBuilder());
            adder.addImplicitHydrogens(mol);
            InChIGenerator inchiGen = inchiGenFact.getInChIGenerator(mol);
            //net.sf.jniinchi.INCHI_OPTION.
            //inchiGenFact.getInChIGenerator(StdInChIGenerator.getCDKMolFromInChI(inchi), )
            ans = new String[] {inchiGen.getInchi(), inchiGen.getInchiKey()};
        } catch (Exception ex) {
            Logger.getLogger(InChIConversionUtility.class.getName()).log(Level.FATAL, null, ex);
        }

        return ans;
    }

}
