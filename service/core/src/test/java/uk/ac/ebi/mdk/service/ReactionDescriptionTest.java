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

package uk.ac.ebi.mdk.service;

import org.junit.Test;
import uk.ac.ebi.mdk.domain.entity.reaction.Direction;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/** @author John May */
public class ReactionDescriptionTest {

    @Test
    public void testAddReactant() throws Exception {
        ReactionDescription r = new ReactionDescription(Direction.UNKNOWN);
        assertThat(r.nReactants(), is(0));
        assertThat(r.nProducts(), is(0));
        r.addReactant("a", "cytoplasm", 1.0);
        assertThat(r.nReactants(), is(1));
        assertThat(r.nProducts(), is(0));
        assertThat(r.reactant(0).compound(), is("a"));
    }

    @Test
    public void testAddProduct() throws Exception {
        ReactionDescription r = new ReactionDescription(Direction.UNKNOWN);
        assertThat(r.nReactants(), is(0));
        assertThat(r.nProducts(), is(0));
        r.addProduct("b", "cytoplasm", 1.0);
        assertThat(r.nReactants(), is(0));
        assertThat(r.nProducts(), is(1));
        assertThat(r.product(0).compound(), is("b"));
    }
}
