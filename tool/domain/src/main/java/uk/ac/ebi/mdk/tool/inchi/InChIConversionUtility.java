/*
 * Copyright (C) 2013 Pablo Moreno <pablacious at users.sf.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.mdk.tool.inchi;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.inchi.InChIToStructure;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 *
 * @author pmoreno
 */
public class InChIConversionUtility {

    private static InChIGeneratorFactory inchiGenFact;
    private static IChemObjectBuilder builder = SilentChemObjectBuilder.getInstance();

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
