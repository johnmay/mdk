/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.core.tools;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.chemet.entities.reaction.Reversibility;
import uk.ac.ebi.core.CompartmentImplementation;
import uk.ac.ebi.core.MetabolicReaction;
import uk.ac.ebi.core.MetaboliteImplementation;
import uk.ac.ebi.core.reaction.MetabolicParticipant;
import uk.ac.ebi.resource.chemical.BasicChemicalIdentifier;
import uk.ac.ebi.resource.reaction.BasicReactionIdentifier;
import uk.ac.ebi.resource.reaction.ReactionIdentifier;
import static org.junit.Assert.*;

/**
 *
 * @author johnmay
 */
public class TransportReactionUtilTest {

    public TransportReactionUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testGetClassification_SYMPORT() {

        System.out.print("testGetClassification_SYMPORT ");

        MetaboliteImplementation atp = new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "atp", "ATP");
        MetaboliteImplementation alanine = new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "dala", "D-Alanine");

        MetabolicReaction rxn = new MetabolicReaction(BasicReactionIdentifier.nextIdentifier(), "st", "symportTest");

        rxn.addReactant(new MetabolicParticipant(atp, CompartmentImplementation.CYTOPLASM));
        rxn.addReactant(new MetabolicParticipant(alanine, CompartmentImplementation.CYTOPLASM));

        rxn.addProduct(new MetabolicParticipant(atp, CompartmentImplementation.EXTRACELLULA));
        rxn.addProduct(new MetabolicParticipant(alanine, CompartmentImplementation.EXTRACELLULA));

        rxn.setReversibility(Reversibility.IRREVERSIBLE_LEFT_TO_RIGHT);

        System.out.println(rxn + " : " + TransportReactionUtil.getClassification(rxn));


        assertEquals(TransportReactionUtil.Classification.SYMPORTER, TransportReactionUtil.getClassification(rxn));

    }

    @Test
    public void testGetClassification_ANTIPORT() {
        System.out.print("testGetClassification_ANTIPORT ");


        MetaboliteImplementation atp = new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "atp", "ATP");
        MetaboliteImplementation alanine = new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "dala", "D-Alanine");

        MetabolicReaction rxn = new MetabolicReaction(BasicReactionIdentifier.nextIdentifier(), "st", "symportTest");

        rxn.addReactant(new MetabolicParticipant(atp, CompartmentImplementation.CYTOPLASM));
        rxn.addReactant(new MetabolicParticipant(alanine, CompartmentImplementation.EXTRACELLULA));

        rxn.addProduct(new MetabolicParticipant(atp, CompartmentImplementation.EXTRACELLULA));
        rxn.addProduct(new MetabolicParticipant(alanine, CompartmentImplementation.CYTOPLASM));

        rxn.setReversibility(Reversibility.IRREVERSIBLE_LEFT_TO_RIGHT);

        System.out.println(rxn + " : " + TransportReactionUtil.getClassification(rxn));

        assertEquals(TransportReactionUtil.Classification.ANTIPORTER, TransportReactionUtil.getClassification(rxn));

    }
    @Test
    public void testGetClassification_UNIPORT() {
        System.out.print("testGetClassification_UNIPORT ");


        MetaboliteImplementation atp = new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "atp", "ATP");
        MetaboliteImplementation alanine = new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "dala", "D-Alanine");

        MetabolicReaction rxn = new MetabolicReaction(BasicReactionIdentifier.nextIdentifier(), "st", "symportTest");

        rxn.addReactant(new MetabolicParticipant(atp, CompartmentImplementation.CYTOPLASM));
        rxn.addReactant(new MetabolicParticipant(alanine, CompartmentImplementation.EXTRACELLULA));

        rxn.addProduct(new MetabolicParticipant(atp, CompartmentImplementation.CYTOPLASM));
        rxn.addProduct(new MetabolicParticipant(alanine, CompartmentImplementation.CYTOPLASM));

        rxn.setReversibility(Reversibility.IRREVERSIBLE_LEFT_TO_RIGHT);

        System.out.println(rxn + " : " + TransportReactionUtil.getClassification(rxn));
        assertEquals(TransportReactionUtil.Classification.UNIPORTER, TransportReactionUtil.getClassification(rxn));

    }
}
