package uk.ac.ebi.chemet;

/**
 * TestMoleculeFactory.java
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
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.templates.MoleculeFactory;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.chemet.resource.chemical.InChI;
import uk.ac.ebi.core.util.CDKMoleculeBuilder;
import uk.ac.ebi.metabolomes.util.CDKAtomTyper;

/**
 * @name    TestMoleculeFactory
 * @date    2011.08.12
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class TestMoleculeFactory {

    private static final Logger LOGGER = Logger.getLogger(TestMoleculeFactory.class);
    private static final InChI but1ene = new InChI("InChI=1S/C4H8/c1-3-4-2/h3H,1,4H2,2H3");
    private static final InChI butane = new InChI("InChI=1S/C4H10/c1-3-4-2/h3-4H2,1-2H3");

    public static IAtomContainer butane() {
        IMolecule mol = CDKMoleculeBuilder.getInstance().buildFromInChI(butane);
        mol.setID("Butane");
        CDKAtomTyper.typeAtoms(mol);
        return mol;
    }

    public static IAtomContainer but1ene() {
        IMolecule mol = CDKMoleculeBuilder.getInstance().buildFromInChI(but1ene);
        mol.setID("But-1-ene");
        CDKAtomTyper.typeAtoms(mol);

        return mol;
    }

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
        return loadMol("ChEBI_15570.mol", "L-alanine", Boolean.TRUE);
    }

    public static IAtomContainer dAlanine() {
        return loadMol("ChEBI_16977.mol", "D-alanine", Boolean.TRUE);
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

    public static IAtomContainer loadMol(String resource, String name, Boolean addH) {

        InputStream stream = TestMoleculeFactory.class.getResourceAsStream(resource);
        MDLV2000Reader mol2Reader = new MDLV2000Reader(stream);
        IMolecule molecule = DefaultChemObjectBuilder.getInstance().newInstance(IMolecule.class);
        try {
            mol2Reader.read(molecule);
            molecule.setID(name);
            CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(DefaultChemObjectBuilder.getInstance());
            AtomContainerManipulator.percieveAtomTypesAndConfigureUnsetProperties(molecule);

            if (addH) {
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
}
