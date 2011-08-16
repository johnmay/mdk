/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.entities.reaction.participant;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.io.Mol2Reader;
import static org.junit.Assert.*;

/**
 *
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

        System.out.print( "[Test] Molecules from different databases should be equal..." );

        new Mol2Reader(getClass().getResourceAsStream("ChEBI_15422.mol") );


        System.out.println( "Passed" );

    }
}
