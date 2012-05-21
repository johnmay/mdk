/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.tool;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicReactionIdentifier;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.MetaboliteImpl;
import uk.ac.ebi.mdk.domain.entity.reaction.Direction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipantImplementation;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReactionImpl;
import uk.ac.ebi.mdk.domain.entity.reaction.compartment.Organelle;
import uk.ac.ebi.mdk.tool.domain.TransportReactionUtil;

import static org.junit.Assert.assertEquals;


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

        Metabolite atp = new MetaboliteImpl(BasicChemicalIdentifier.nextIdentifier(), "atp", "ATP");
        Metabolite alanine = new MetaboliteImpl(BasicChemicalIdentifier.nextIdentifier(), "dala", "D-Alanine");

        MetabolicReactionImpl rxn = new MetabolicReactionImpl(BasicReactionIdentifier.nextIdentifier(), "st", "symportTest");

        rxn.addReactant(new MetabolicParticipantImplementation(atp, Organelle.CYTOPLASM));
        rxn.addReactant(new MetabolicParticipantImplementation(alanine, Organelle.CYTOPLASM));

        rxn.addProduct(new MetabolicParticipantImplementation(atp, Organelle.EXTRACELLULA));
        rxn.addProduct(new MetabolicParticipantImplementation(alanine, Organelle.EXTRACELLULA));

        rxn.setDirection(Direction.BACKWARD.FORWARD);

        System.out.println(rxn + " : " + TransportReactionUtil.getClassification(rxn));


        assertEquals(TransportReactionUtil.Classification.SYMPORTER, TransportReactionUtil.getClassification(rxn));

    }


    @Test
    public void testGetClassification_ANTIPORT() {
        System.out.print("testGetClassification_ANTIPORT ");


        MetaboliteImpl atp = new MetaboliteImpl(BasicChemicalIdentifier.nextIdentifier(), "atp", "ATP");
        MetaboliteImpl alanine = new MetaboliteImpl(BasicChemicalIdentifier.nextIdentifier(), "dala", "D-Alanine");

        MetabolicReactionImpl rxn = new MetabolicReactionImpl(BasicReactionIdentifier.nextIdentifier(), "st", "symportTest");

        rxn.addReactant(new MetabolicParticipantImplementation(atp, Organelle.CYTOPLASM));
        rxn.addReactant(new MetabolicParticipantImplementation(alanine, Organelle.EXTRACELLULA));

        rxn.addProduct(new MetabolicParticipantImplementation(atp, Organelle.EXTRACELLULA));
        rxn.addProduct(new MetabolicParticipantImplementation(alanine, Organelle.CYTOPLASM));

        rxn.setDirection(Direction.FORWARD);

        System.out.println(rxn + " : " + TransportReactionUtil.getClassification(rxn));

        assertEquals(TransportReactionUtil.Classification.ANTIPORTER, TransportReactionUtil.getClassification(rxn));

    }


    @Test
    public void testGetClassification_UNIPORT() {
        System.out.print("testGetClassification_UNIPORT ");


        MetaboliteImpl atp = new MetaboliteImpl(BasicChemicalIdentifier.nextIdentifier(), "atp", "ATP");
        MetaboliteImpl alanine = new MetaboliteImpl(BasicChemicalIdentifier.nextIdentifier(), "dala", "D-Alanine");

        MetabolicReactionImpl rxn = new MetabolicReactionImpl(BasicReactionIdentifier.nextIdentifier(), "st", "symportTest");

        rxn.addReactant(new MetabolicParticipantImplementation(atp, Organelle.CYTOPLASM));
        rxn.addReactant(new MetabolicParticipantImplementation(alanine, Organelle.EXTRACELLULA));

        rxn.addProduct(new MetabolicParticipantImplementation(atp, Organelle.CYTOPLASM));
        rxn.addProduct(new MetabolicParticipantImplementation(alanine, Organelle.CYTOPLASM));

        rxn.setDirection(Direction.BACKWARD);

        System.out.println(rxn + " : " + TransportReactionUtil.getClassification(rxn));
        assertEquals(TransportReactionUtil.Classification.UNIPORTER, TransportReactionUtil.getClassification(rxn));

    }
}
