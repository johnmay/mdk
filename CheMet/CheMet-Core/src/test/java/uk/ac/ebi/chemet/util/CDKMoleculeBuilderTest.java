/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.util;

import uk.ac.ebi.core.util.CDKMoleculeBuilder;
import junit.framework.TestCase;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.templates.MoleculeFactory;
import org.openscience.cdk.tools.manipulator.AtomContainerComparator;
import uk.ac.ebi.metabolomes.identifier.InChI;

/**
 *
 * @author johnmay
 */
public class CDKMoleculeBuilderTest extends TestCase {

    public CDKMoleculeBuilderTest( String testName ) {
        super( testName );
    }

    /**
     * Test of build method, of class CDKMoleculeBuilder.
     */
    public void testBuild() {
    }

    /**
     * Test of buildFromInChI method, of class CDKMoleculeBuilder.
     */
    public void testBuildFromInChI() {
        String inchi = "InChI=1S/" +
                       "C6H6/" +
                       "c1-2-4-6-5-3-1/" +
                       "h1-6H";

        IMolecule benzene = MoleculeFactory.makeBenzene();

        InChI inchiObject = new InChI( inchi );

        IAtomContainer atomContainer = CDKMoleculeBuilder.getInstance().buildFromInChI( inchiObject );

        AtomContainerComparator comparator = new AtomContainerComparator();
        int comparison = comparator.compare( benzene , atomContainer );

        assertEquals( 0 , comparison );

    }

    /**
     * Test of buildFromMolFileV2000 method, of class CDKMoleculeBuilder.
     */
    public void testBuildFromMolFileV2000() {
    }

    /**
     * Test of buildFromMolFileV3000 method, of class CDKMoleculeBuilder.
     */
    public void testBuildFromMolFileV3000() {
    }
}
