/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.io.sbml;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.entity.reaction.AtomContainerReaction;
import uk.ac.ebi.core.CompartmentImplementation;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.io.xml.SBMLReactionReader;
import uk.ac.ebi.mdk.domain.tool.AutomaticCompartmentResolver;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;


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
            SBMLReactionReader reactionReader = new SBMLReactionReader(sbmlStream, DefaultEntityFactory.getInstance(), new AutomaticCompartmentResolver());
            while (reactionReader.hasNext()) {
                AtomContainerReaction r = reactionReader.next();
                assertEquals(2, r.getAllReactionParticipants().size());
                assertEquals(CompartmentImplementation.EXTRACELLULA, r.getReactantParticipants().get(0).getCompartment());
                assertEquals(CompartmentImplementation.CYTOPLASM, r.getReactantParticipants().get(0).getCompartment());
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}
