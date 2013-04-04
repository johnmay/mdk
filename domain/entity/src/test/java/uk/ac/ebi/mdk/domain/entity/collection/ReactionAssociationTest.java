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

package uk.ac.ebi.mdk.domain.entity.collection;

import org.junit.Test;
import uk.ac.ebi.mdk.domain.entity.MetaboliteImpl;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.ReconstructionImpl;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipantImplementation;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReactionImpl;

import static org.junit.Assert.assertEquals;

/**
 * @author John May
 */
public class ReactionAssociationTest {

    @Test
    public void testRemove_Reaction() {

        MetaboliteImpl a = new MetaboliteImpl("a", "a");
        MetaboliteImpl b = new MetaboliteImpl("b", "b");
        MetaboliteImpl c = new MetaboliteImpl("c", "c");
        MetaboliteImpl d = new MetaboliteImpl("d", "d");

        MetabolicReaction r1 = new MetabolicReactionImpl();
        MetabolicReaction r2 = new MetabolicReactionImpl();

        r1.addReactant(new MetabolicParticipantImplementation(a));
        r1.addReactant(new MetabolicParticipantImplementation(b));
        r1.addProduct(new MetabolicParticipantImplementation(c));

        r2.addReactant(new MetabolicParticipantImplementation(a));
        r2.addReactant(new MetabolicParticipantImplementation(c));
        r2.addReactant(new MetabolicParticipantImplementation(d));

        Reconstruction recon = new ReconstructionImpl();
        Reactome reactome = new ReactomeImpl(recon);
        reactome.add(r1);
        reactome.add(r2);

        assertEquals(2, reactome.size());
        assertEquals(2, reactome.participatesIn(a).size());
        assertEquals(1, reactome.participatesIn(b).size());
        assertEquals(2, reactome.participatesIn(c).size());

        reactome.remove(r1);

        assertEquals(1, reactome.size());
        assertEquals(1, reactome.participatesIn(a).size());
        assertEquals(0, reactome.participatesIn(b).size());
        assertEquals(1, reactome.participatesIn(c).size());


    }

    @Test
    public void testAdd_Null(){
        new ReactionList().add(null); // should not fail
    }

    @Test
    public void testAdd() {
        MetaboliteImpl a = new MetaboliteImpl("a", "a");
        MetaboliteImpl b = new MetaboliteImpl("b", "b");
        MetaboliteImpl c = new MetaboliteImpl("c", "c");

        MetabolicReaction r1 = new MetabolicReactionImpl();

        r1.addReactant(new MetabolicParticipantImplementation(a));
        r1.addReactant(new MetabolicParticipantImplementation(b));
        r1.addProduct(new MetabolicParticipantImplementation(c));

        Reconstruction recon = new ReconstructionImpl();
        Reactome reactome = new ReactomeImpl(recon);

        assertEquals(0, reactome.size());
        reactome.add(r1);
        assertEquals(1, reactome.size());
        assertEquals(1, reactome.participatesIn(a).size());
        assertEquals(1, reactome.participatesIn(b).size());
        assertEquals(1, reactome.participatesIn(c).size());

        // do not add again
        reactome.add(r1);
        assertEquals(1, reactome.size());
        assertEquals(1, reactome.participatesIn(a).size());
        assertEquals(1, reactome.participatesIn(b).size());
        assertEquals(1, reactome.participatesIn(c).size());


    }

    @Test
    public void testRemoveKey() {

        MetaboliteImpl a = new MetaboliteImpl("a", "a");
        MetaboliteImpl b = new MetaboliteImpl("b", "b");
        MetaboliteImpl c = new MetaboliteImpl("c", "c");
        MetaboliteImpl d = new MetaboliteImpl("d", "d");

        MetabolicReaction r1 = new MetabolicReactionImpl();
        MetabolicReaction r2 = new MetabolicReactionImpl();

        r1.addReactant(new MetabolicParticipantImplementation(a));
        r1.addReactant(new MetabolicParticipantImplementation(b));
        r1.addProduct(new MetabolicParticipantImplementation(c));

        r2.addReactant(new MetabolicParticipantImplementation(a));
        r2.addReactant(new MetabolicParticipantImplementation(c));
        r2.addReactant(new MetabolicParticipantImplementation(d));

        Reconstruction recon = new ReconstructionImpl();
        Reactome reactome = new ReactomeImpl(recon);
        reactome.add(r1);
        reactome.add(r2);

        assertEquals("incorrect number of reaction for 'a', ",
                     2, reactome.participatesIn(a).size());
        assertEquals("incorrect number of reaction for 'b', ",
                     1, reactome.participatesIn(b).size());
        assertEquals("incorrect number of reaction for 'c', ",
                     2, reactome.participatesIn(c).size());
        assertEquals("incorrect number of reaction for 'd', ",
                     1, reactome.participatesIn(d).size());


        recon.dissociate(a, r1);

        assertEquals("incorrect number of reaction for 'a', ",
                     1, reactome.participatesIn(a).size());
        assertEquals("incorrect number of reaction for 'b', ",
                     1, reactome.participatesIn(b).size());
        assertEquals("incorrect number of reaction for 'c', ",
                     2, reactome.participatesIn(c).size());
        assertEquals("incorrect number of reaction for 'd', ",
                     1, reactome.participatesIn(d).size());

        recon.dissociate(a, r2);

        assertEquals("incorrect number of reaction for 'a', ",
                     0, reactome.participatesIn(a).size());
        assertEquals("incorrect number of reaction for 'b', ",
                     1, reactome.participatesIn(b).size());
        assertEquals("incorrect number of reaction for 'c', ",
                     2, reactome.participatesIn(c).size());
        assertEquals("incorrect number of reaction for 'd', ",
                     1, reactome.participatesIn(d).size());

    }

    @Test
    public void testUpdate_Reaction() {

        MetaboliteImpl a = new MetaboliteImpl("a", "a");
        MetaboliteImpl b = new MetaboliteImpl("b", "b");
        MetaboliteImpl c = new MetaboliteImpl("c", "c");
        MetaboliteImpl d = new MetaboliteImpl("d", "d");

        MetabolicReaction r1 = new MetabolicReactionImpl();
        MetabolicReaction r2 = new MetabolicReactionImpl();

        r1.addReactant(new MetabolicParticipantImplementation(a));
        r1.addReactant(new MetabolicParticipantImplementation(b));
        r1.addProduct(new MetabolicParticipantImplementation(c));

        r2.addReactant(new MetabolicParticipantImplementation(a));
        r2.addReactant(new MetabolicParticipantImplementation(c));
        r2.addReactant(new MetabolicParticipantImplementation(d));

        Reconstruction recon = new ReconstructionImpl();
        Reactome reactome = new ReactomeImpl(recon);
        reactome.add(r1);
        reactome.add(r2);

        recon.dissociate(a, r1);
        assertEquals("incorrect number of reaction for 'a', ",
                     1, reactome.participatesIn(a).size());

        recon.associate(a, r1);  // update metabolite map
        assertEquals("incorrect number of reaction for 'a', ",
                     2, reactome.participatesIn(a).size());


    }

    @Test
    public void testUpdate_Reactions() {

        MetaboliteImpl a = new MetaboliteImpl("a", "a");
        MetaboliteImpl b = new MetaboliteImpl("b", "b");
        MetaboliteImpl c = new MetaboliteImpl("c", "c");
        MetaboliteImpl d = new MetaboliteImpl("d", "d");

        MetabolicReaction r1 = new MetabolicReactionImpl();
        MetabolicReaction r2 = new MetabolicReactionImpl();

        r1.addReactant(new MetabolicParticipantImplementation(a));
        r1.addReactant(new MetabolicParticipantImplementation(b));
        r1.addProduct(new MetabolicParticipantImplementation(c));

        r2.addReactant(new MetabolicParticipantImplementation(a));
        r2.addReactant(new MetabolicParticipantImplementation(c));
        r2.addReactant(new MetabolicParticipantImplementation(d));

        Reconstruction recon = new ReconstructionImpl();
        Reactome reactome = new ReactomeImpl(recon);
        reactome.add(r1);
        reactome.add(r2);

        recon.dissociate(a, r1);
        recon.dissociate(a, r2);
        assertEquals("incorrect number of reaction for 'a', ",
                     0, reactome.participatesIn(a).size());

        recon.associate(a, r1);  // update metabolite map
        recon.associate(a, r2);  // update metabolite map
        assertEquals("incorrect number of reaction for 'a', ",
                     2, reactome.participatesIn(a).size());


    }

}
