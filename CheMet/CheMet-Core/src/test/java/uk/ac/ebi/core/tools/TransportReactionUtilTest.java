/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.core.tools;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.chemet.entities.reaction.DirectionImplementation;
import uk.ac.ebi.core.CompartmentImplementation;
import uk.ac.ebi.core.MetabolicReactionImplementation;
import uk.ac.ebi.core.MetaboliteImplementation;
import uk.ac.ebi.core.reaction.MetabolicParticipantImplementation;
import uk.ac.ebi.resource.chemical.BasicChemicalIdentifier;
import uk.ac.ebi.resource.reaction.BasicReactionIdentifier;
import static org.junit.Assert.*;
import uk.ac.ebi.interfaces.entities.MetabolicReaction;
import uk.ac.ebi.interfaces.entities.Metabolite;


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

        Metabolite atp = new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "atp", "ATP");
        Metabolite alanine = new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "dala", "D-Alanine");

        MetabolicReactionImplementation rxn = new MetabolicReactionImplementation(BasicReactionIdentifier.nextIdentifier(), "st", "symportTest");

        rxn.addReactant(new MetabolicParticipantImplementation(atp, CompartmentImplementation.CYTOPLASM));
        rxn.addReactant(new MetabolicParticipantImplementation(alanine, CompartmentImplementation.CYTOPLASM));

        rxn.addProduct(new MetabolicParticipantImplementation(atp, CompartmentImplementation.EXTRACELLULA));
        rxn.addProduct(new MetabolicParticipantImplementation(alanine, CompartmentImplementation.EXTRACELLULA));

        rxn.setDirection(DirectionImplementation.FORWARD);

        System.out.println(rxn + " : " + TransportReactionUtil.getClassification(rxn));


        assertEquals(TransportReactionUtil.Classification.SYMPORTER, TransportReactionUtil.getClassification(rxn));

    }


    @Test
    public void testGetClassification_ANTIPORT() {
        System.out.print("testGetClassification_ANTIPORT ");


        MetaboliteImplementation atp = new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "atp", "ATP");
        MetaboliteImplementation alanine = new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "dala", "D-Alanine");

        MetabolicReactionImplementation rxn = new MetabolicReactionImplementation(BasicReactionIdentifier.nextIdentifier(), "st", "symportTest");

        rxn.addReactant(new MetabolicParticipantImplementation(atp, CompartmentImplementation.CYTOPLASM));
        rxn.addReactant(new MetabolicParticipantImplementation(alanine, CompartmentImplementation.EXTRACELLULA));

        rxn.addProduct(new MetabolicParticipantImplementation(atp, CompartmentImplementation.EXTRACELLULA));
        rxn.addProduct(new MetabolicParticipantImplementation(alanine, CompartmentImplementation.CYTOPLASM));

        rxn.setDirection(DirectionImplementation.IRREVERSIBLE_LEFT_TO_RIGHT);

        System.out.println(rxn + " : " + TransportReactionUtil.getClassification(rxn));

        assertEquals(TransportReactionUtil.Classification.ANTIPORTER, TransportReactionUtil.getClassification(rxn));

    }


    @Test
    public void testGetClassification_UNIPORT() {
        System.out.print("testGetClassification_UNIPORT ");


        MetaboliteImplementation atp = new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "atp", "ATP");
        MetaboliteImplementation alanine = new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "dala", "D-Alanine");

        MetabolicReactionImplementation rxn = new MetabolicReactionImplementation(BasicReactionIdentifier.nextIdentifier(), "st", "symportTest");

        rxn.addReactant(new MetabolicParticipantImplementation(atp, CompartmentImplementation.CYTOPLASM));
        rxn.addReactant(new MetabolicParticipantImplementation(alanine, CompartmentImplementation.EXTRACELLULA));

        rxn.addProduct(new MetabolicParticipantImplementation(atp, CompartmentImplementation.CYTOPLASM));
        rxn.addProduct(new MetabolicParticipantImplementation(alanine, CompartmentImplementation.CYTOPLASM));

        rxn.setDirection(DirectionImplementation.IRREVERSIBLE_LEFT_TO_RIGHT);

        System.out.println(rxn + " : " + TransportReactionUtil.getClassification(rxn));
        assertEquals(TransportReactionUtil.Classification.UNIPORTER, TransportReactionUtil.getClassification(rxn));

    }
}
