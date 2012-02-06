/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.io.sbml;

import uk.ac.ebi.io.xml.SBMLReactionReader;
import uk.ac.ebi.core.CompartmentImplementation;
import java.io.InputStream;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.chemet.entities.reaction.AtomContainerReaction;
import static org.junit.Assert.*;
import uk.ac.ebi.core.DefaultEntityFactory;


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
                    "streptomyces-coelicolor-6.2005.xml");
            SBMLReactionReader reactionReader = new SBMLReactionReader(sbmlStream, DefaultEntityFactory.getInstance());
            while (reactionReader.hasNext()) {
                AtomContainerReaction r = reactionReader.next();
                assertEquals(2, r.getAllReactionParticipants().size());
                assertEquals(CompartmentImplementation.EXTRACELLULA, r.getReactantCompartments().get(0));
                assertEquals(CompartmentImplementation.CYTOPLASM, r.getProductCompartments().get(0));
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}
