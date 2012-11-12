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

package uk.ac.ebi.mdk.domain.entity.reaction;

import org.junit.Test;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.MetaboliteImpl;
import uk.ac.ebi.mdk.domain.entity.reaction.compartment.Organelle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author John May
 */
public class MetabolicReactionImplTest {

    @Test
    public void testRemove_Null() {
        assertFalse(new MetabolicReactionImpl().remove(null));
    }

    @Test
    public void testRemove() {

        MetabolicReaction rxn = new MetabolicReactionImpl();

        Metabolite h = new MetaboliteImpl("H");
        Metabolite o = new MetaboliteImpl("O");

        rxn.addReactant(new MetabolicParticipantImplementation(h, Organelle.CYTOPLASM));
        rxn.addReactant(new MetabolicParticipantImplementation(o, Organelle.CYTOPLASM));
        rxn.addProduct(new MetabolicParticipantImplementation(h, Organelle.EXTRACELLULAR));

        assertEquals(2, rxn.getReactantCount());
        assertEquals(1, rxn.getProductCount());

        rxn.remove(h);

        assertEquals(1, rxn.getReactantCount());
        assertEquals(0, rxn.getProductCount());

    }

}
