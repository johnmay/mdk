/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.chemet.io.sbml;

import uk.ac.ebi.core.Compartment;
import java.io.InputStream;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.chemet.entities.reaction.AtomContainerReaction;
import static org.junit.Assert.*;

/**
 *
 * @author johnmay
 */
public class ReactionLoaderTest {


public ReactionLoaderTest() {
}


@BeforeClass
public static void setUpClass() throws Exception {
}


@AfterClass
public static void tearDownClass() throws Exception {
}


@Test
public void testSBMLLoader() {
    try {
        InputStream sbmlStream = getClass().getResourceAsStream(
            "streptomyces-coelicolor-6.2005.xml" );
        SBMLReactionReader reactionReader = new SBMLReactionReader( sbmlStream );
        while ( reactionReader.hasNext() ) {
            AtomContainerReaction r = reactionReader.next();
            assertEquals( 2 , r.getAllReactionParticipants().size() );
            assertEquals( Compartment.EXTRACELLULA , r.getReactantCompartments().get( 0 ) );
            assertEquals( Compartment.CYTOPLASM , r.getProductCompartments().get( 0 ) );
        }
    } catch ( Exception ex ) {
        System.err.println( ex.getMessage() );
    }
}

}
