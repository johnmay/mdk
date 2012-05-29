/*
 * Copyright (C) 2012  John May and Pablo Moreno
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package uk.ac.ebi.mdk.tool.domain;

import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.templates.MoleculeFactory;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.mdk.domain.identifier.InChI;

import java.io.InputStream;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 * @name TestMoleculeFactory
 * @date 2011.08.12
 * @brief ...class description...
 */
public class TestMoleculeFactory {

    private static final Logger LOGGER  = Logger.getLogger(TestMoleculeFactory.class);
    private static final InChI  but1ene = new InChI("InChI=1S/C4H8/c1-3-4-2/h3H,1,4H2,2H3");
    private static final InChI  butane  = new InChI("InChI=1S/C4H10/c1-3-4-2/h3-4H2,1-2H3");


    public static IAtomContainer _123Triazole() {
        IAtomContainer ac = MoleculeFactory.make123Triazole();
        ac.setID("1,2,3-Triazole");
        return ac;
    }

    public static IAtomContainer _124Triazole() {
        IAtomContainer ac = MoleculeFactory.make124Triazole();
        ac.setID("1,2,4-Triazole");
        return ac;
    }

    public static IAtomContainer benzene() {
        IAtomContainer ac = MoleculeFactory.makeBenzene();
        ac.setID("Benzene");
        return ac;
    }

    public static IAtomContainer cyclohexane() {
        IAtomContainer ac = MoleculeFactory.makeCyclohexane();
        ac.setID("Cyclohexane");
        return ac;
    }

    public static IAtomContainer lAlanine() {
        return loadMol("ChEBI_15570.mol", "L-alanine", Boolean.FALSE);
    }

    public static IAtomContainer dAlanine() {
        return loadMol("ChEBI_16977.mol", "D-alanine", Boolean.FALSE);
    }

    public static IAtomContainer atp_minus_3() {
        return loadMol("ChEBI_57299.mol", "ATP (3-)", Boolean.FALSE);
    }

    public static IAtomContainer atp_minus_4() {
        return loadMol("ChEBI_30616.mol", "ATP (4-)", Boolean.FALSE);
    }

    public static IAtomContainer butan1ol() {
        return loadMol("ChEBI_28885.mol", "Butan-1-ol", Boolean.FALSE);
    }

    public static IAtomContainer butan2ol() {
        return loadMol("ChEBI_35687.mol", "Butan-2-ol", Boolean.FALSE);
    }

    public static IAtomContainer adenine() {
        IAtomContainer ac = MoleculeFactory.makeAdenine();
        ac.setID("Adenine");
        return ac;
    }

    public static IAtomContainer nad_plus() {
        return loadMol("ChEBI_57540.mol", "NAD+", false);
    }

    public static IAtomContainer primary_alcohol() {
        return loadMol("ChEBI_15734.mol", "primary alcohol", false);
    }

    public static IAtomContainer primary_alcohol_no_r() {
        return loadMol("ChEBI_15734_edit.mol", "primary alcohol", false);
    }

    public static IAtomContainer seconday_alcohol() {
        return loadMol("ChEBI_35681.mol", "secondary alcohol", true);
    }

    public static IAtomContainer hydron() {
        return loadMol("ChEBI_15378.mol", "H+", false);
    }

    public static IAtomContainer aldehyde_no_r() {
        return loadMol("ChEBI_17478_edit.mol", "aldehyde", false);
    }

    public static IAtomContainer aldehyde() {
        return loadMol("ChEBI_17478.mol", "aldehyde", true);
    }

    public static IAtomContainer ketone() {
        return loadMol("ChEBI_17087.mol", "ketone", false);
    }

    public static IAtomContainer nadh() {
        return loadMol("ChEBI_57945.mol", "NADH", false);
    }

    public static IAtomContainer _5bcholestane3a7a26triol() {
        return loadMol("ChEBI_28540.mol",
                       "5B-cholestane-3a,7a,26-triol", true);
    }

    public static IAtomContainer _3a7adihydroxy5Bcholestan26al() {
        return loadMol("ChEBI_27428.mol",
                       "3a,7a-dihydroxy-5B-cholestan-26-al",
                       true);

    }

    public static IAtomContainer formaldehyde() {
        return loadMol("ChEBI_16842.mol", "Formaldehyde", true);
    }

    public static IAtomContainer loadMol(Class root, String resource, String name, Boolean convertHydrogens) {
        return loadMol(root.getResourceAsStream(resource), name, convertHydrogens);
    }

    public static IAtomContainer loadMol(Class root, String resource, String name) {
        return loadMol(root.getResourceAsStream(resource), name, false);
    }

    public static IAtomContainer loadMol(InputStream stream, String name, Boolean convertHydrogens) {
        MDLV2000Reader mol2Reader = new MDLV2000Reader(stream);
        IMolecule molecule = DefaultChemObjectBuilder.getInstance().newInstance(IMolecule.class);
        try {
            mol2Reader.read(molecule);
            molecule.setID(name);
            CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(DefaultChemObjectBuilder.getInstance());
            AtomContainerManipulator.percieveAtomTypesAndConfigureUnsetProperties(molecule);

            if (convertHydrogens) {
                adder.addImplicitHydrogens(molecule);
                AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule);
            }
        } catch (CDKException ex) {
            // fail
            System.err.println("Fatal Error – Cannot load molecule");
            System.out.println(ex.getMessage());
        }
        return molecule;
    }

    public static IAtomContainer loadMol(String resource, String name, Boolean convertHydrogens) {
        return loadMol(TestMoleculeFactory.class.getResourceAsStream(resource), name, convertHydrogens);
    }

    public static IAtomContainer but1ene() {
        return loadMol("ChEBI_48362.mol",
                       "But-1-ene", false);
    }

    public static IAtomContainer butane() {
        return loadMol("ChEBI_37808.mol",
                       "Butane", false);
    }
}
