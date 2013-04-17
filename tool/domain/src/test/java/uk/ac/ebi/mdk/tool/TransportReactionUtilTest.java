/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

        rxn.addProduct(new MetabolicParticipantImplementation(atp, Organelle.EXTRACELLULAR));
        rxn.addProduct(new MetabolicParticipantImplementation(alanine, Organelle.EXTRACELLULAR));

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
        rxn.addReactant(new MetabolicParticipantImplementation(alanine, Organelle.EXTRACELLULAR));

        rxn.addProduct(new MetabolicParticipantImplementation(atp, Organelle.EXTRACELLULAR));
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
        rxn.addReactant(new MetabolicParticipantImplementation(alanine, Organelle.EXTRACELLULAR));

        rxn.addProduct(new MetabolicParticipantImplementation(atp, Organelle.CYTOPLASM));
        rxn.addProduct(new MetabolicParticipantImplementation(alanine, Organelle.CYTOPLASM));

        rxn.setDirection(Direction.BACKWARD);

        System.out.println(rxn + " : " + TransportReactionUtil.getClassification(rxn));
        assertEquals(TransportReactionUtil.Classification.UNIPORTER, TransportReactionUtil.getClassification(rxn));

    }
}
