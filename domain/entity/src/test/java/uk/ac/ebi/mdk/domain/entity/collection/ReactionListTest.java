/*
 * Copyright (c) 2012. John May <jwmay@users.sf.net>
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
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipantImplementation;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReactionImpl;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author John May
 */
public class ReactionListTest {

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

        Reactome reactome = new ReactionList();
        reactome.add(r1);
        reactome.add(r2);

        assertEquals(2, reactome.size());
        assertEquals(2, reactome.getReactions(a).size());
        assertEquals(1, reactome.getReactions(b).size());
        assertEquals(2, reactome.getReactions(c).size());

        reactome.remove(r1);

        assertEquals(1, reactome.size());
        assertEquals(1, reactome.getReactions(a).size());
        assertEquals(0, reactome.getReactions(b).size());
        assertEquals(1, reactome.getReactions(c).size());


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

        Reactome reactome = new ReactionList();

        assertEquals(0, reactome.size());
        reactome.add(r1);
        assertEquals(1, reactome.size());
        assertEquals(1, reactome.getReactions(a).size());
        assertEquals(1, reactome.getReactions(b).size());
        assertEquals(1, reactome.getReactions(c).size());

        // do not add again
        reactome.add(r1);
        assertEquals(1, reactome.size());
        assertEquals(1, reactome.getReactions(a).size());
        assertEquals(1, reactome.getReactions(b).size());
        assertEquals(1, reactome.getReactions(c).size());


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

        Reactome reactome = new ReactionList();
        reactome.add(r1);
        reactome.add(r2);

        assertEquals("incorrect number of reaction for 'a', ",
                     2, reactome.getReactions(a).size());
        assertEquals("incorrect number of reaction for 'b', ",
                     1, reactome.getReactions(b).size());
        assertEquals("incorrect number of reaction for 'c', ",
                     2, reactome.getReactions(c).size());
        assertEquals("incorrect number of reaction for 'd', ",
                     1, reactome.getReactions(d).size());


        reactome.removeKey(a, r1);

        assertEquals("incorrect number of reaction for 'a', ",
                     1, reactome.getReactions(a).size());
        assertEquals("incorrect number of reaction for 'b', ",
                     1, reactome.getReactions(b).size());
        assertEquals("incorrect number of reaction for 'c', ",
                     2, reactome.getReactions(c).size());
        assertEquals("incorrect number of reaction for 'd', ",
                     1, reactome.getReactions(d).size());

        reactome.removeKey(a, r2);

        assertEquals("incorrect number of reaction for 'a', ",
                     0, reactome.getReactions(a).size());
        assertEquals("incorrect number of reaction for 'b', ",
                     1, reactome.getReactions(b).size());
        assertEquals("incorrect number of reaction for 'c', ",
                     2, reactome.getReactions(c).size());
        assertEquals("incorrect number of reaction for 'd', ",
                     1, reactome.getReactions(d).size());

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

        Reactome reactome = new ReactionList();
        reactome.add(r1);
        reactome.add(r2);

        reactome.removeKey(a, r1);
        assertEquals("incorrect number of reaction for 'a', ",
                     1, reactome.getReactions(a).size());

        reactome.update(r1);  // update metabolite map
        assertEquals("incorrect number of reaction for 'a', ",
                     2, reactome.getReactions(a).size());


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

        Reactome reactome = new ReactionList();
        reactome.add(r1);
        reactome.add(r2);

        reactome.removeKey(a, r1);
        reactome.removeKey(a, r2);
        assertEquals("incorrect number of reaction for 'a', ",
                     0, reactome.getReactions(a).size());

        reactome.update(Arrays.asList(r1, r2));  // update metabolite map
        assertEquals("incorrect number of reaction for 'a', ",
                     2, reactome.getReactions(a).size());


    }

}
