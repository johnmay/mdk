/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.entities.reaction.participant;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import uk.ac.ebi.chemet.TestMoleculeFactory;
import uk.ac.ebi.mdk.domain.entity.reaction.AtomContainerParticipant;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * @author johnmay
 */
public class AtomContainerParticipantTest {

    public AtomContainerParticipantTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testDiffDatabases() {

        System.out.printf("%-120s", "[TEST] ATP from ChEBI and ATP from KEGG should be matches");

        // raw downloads
        InputStream chebiATPStream = getClass().getResourceAsStream("ATP-chebi-15422.mol");
        InputStream keggATPStream = getClass().getResourceAsStream("ATP-kegg-C00002.mol");
        InputStream pubchemATP2DStream = getClass().getResourceAsStream("ATP-CID5957-2D.sdf");
        InputStream pubchemATP3DStream = getClass().getResourceAsStream("ATP-CID5957-3D.sdf");
        // InputStream keggATPStream = getClass().getResourceAsStream( "ATP_kegg-C00002.mol" );


        IAtomContainer chebiATP = TestMoleculeFactory.loadMol("ATP-chebi-15422.mol", "ATP ChEBI", false);
        IAtomContainer keggATP = TestMoleculeFactory.loadMol("TP-kegg-C00002.mol", "ATP KEGG", false);

        IteratingMDLReader mdlReader = new IteratingMDLReader(pubchemATP2DStream,
                                                              DefaultChemObjectBuilder.getInstance());
        IAtomContainer pchemATP = (IAtomContainer) mdlReader.next();
        mdlReader = new IteratingMDLReader(pubchemATP3DStream,
                                           DefaultChemObjectBuilder.getInstance());
        IAtomContainer pchem3DATP = (IAtomContainer) mdlReader.next();


        AtomContainerParticipant chebiParticipant = new AtomContainerParticipant(chebiATP);
        AtomContainerParticipant keggParticipant = new AtomContainerParticipant(keggATP);
        AtomContainerParticipant pchemParticipant = new AtomContainerParticipant(pchemATP);
        AtomContainerParticipant pchem3DParticipant = new AtomContainerParticipant(pchem3DATP);

        // assetions
        assertEquals(chebiParticipant.hashCode(), keggParticipant.hashCode());
        assertEquals(chebiParticipant.hashCode(), pchemParticipant.hashCode());
        assertEquals(chebiParticipant.hashCode(), pchem3DParticipant.hashCode());
        assertEquals(chebiParticipant, keggParticipant);
        assertEquals(chebiParticipant, pchemParticipant);
        assertEquals(chebiParticipant, pchem3DParticipant);


        //   assertEquals( pchemParticipant , keggParticipant );

        System.out.println("PASSED");

    }

    @Test
    public void testHashCode() {

        System.out.println(
                new AtomContainerParticipant(TestMoleculeFactory._3a7adihydroxy5Bcholestan26al()).hashCode());
        System.out.println(
                new AtomContainerParticipant(TestMoleculeFactory._5bcholestane3a7a26triol()).hashCode());

    }
}
