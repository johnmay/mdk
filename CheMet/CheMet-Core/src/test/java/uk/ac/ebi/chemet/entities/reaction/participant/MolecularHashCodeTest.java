/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.entities.reaction.participant;

import java.io.InputStream;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import uk.ac.ebi.core.util.CDKMoleculeBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.templates.MoleculeFactory;
import uk.ac.ebi.chemet.TestMoleculeFactory;
import uk.ac.ebi.core.tools.MolecularHashCode;
import static org.junit.Assert.*;

/**
 *
 * @author johnmay
 */
public class MolecularHashCodeTest {

    public MolecularHashCodeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testElectronMatrix() {
        InputStream chebiATPStream = getClass().getResourceAsStream("ATP-chebi-15422.mol");
        CDKMoleculeBuilder moleculeBuilder = CDKMoleculeBuilder.getInstance();

        IAtomContainer chebiATP = moleculeBuilder.buildFromMolFileV2000(new MDLV2000Reader(chebiATPStream));

        byte[][] bem = MolecularHashCode.getConnectionMatrix(MoleculeFactory.makeBenzene());
        for (byte[] bs : bem) {
            for (byte b : bs) {
                System.out.printf(" %s ", b);
            }
            System.out.println("");
        }

    }

    @Test
    public void timeTaken() {
        InputStream chebiATPStream = getClass().getResourceAsStream("ATP-chebi-15422.mol");
        CDKMoleculeBuilder moleculeBuilder = CDKMoleculeBuilder.getInstance();

        IAtomContainer chebiATP = moleculeBuilder.buildFromMolFileV2000(new MDLV2000Reader(chebiATPStream));

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            MolecularHashCode.hashCode(chebiATP);
        }
        long end = System.currentTimeMillis();
        System.out.println("time taken: " + (end - start));

    }

    @Test
    public void testHashCode() {


        InputStream chebiATPStream = getClass().getResourceAsStream("ATP-chebi-15422.mol");
        InputStream keggATPStream = getClass().getResourceAsStream("ATP-kegg-C00002.mol");


        CDKMoleculeBuilder moleculeBuilder = CDKMoleculeBuilder.getInstance();

        IAtomContainer chebiATP = moleculeBuilder.buildFromMolFileV2000(new MDLV2000Reader(chebiATPStream));
        IAtomContainer keggATP = moleculeBuilder.buildFromMolFileV2000(new MDLV2000Reader(keggATPStream));



        System.out.println("Hash code:" + MolecularHashCode.hashCode(chebiATP));
        System.out.println("Hash code:" + MolecularHashCode.hashCode(keggATP));

    }
}
