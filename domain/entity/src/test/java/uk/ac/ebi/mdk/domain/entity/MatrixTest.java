/*
 * Copyright (c) 2014. EMBL, European Bioinformatics Institute
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

package uk.ac.ebi.mdk.domain.entity;

import org.junit.Test;
import uk.ac.ebi.mdk.domain.matrix.BasicStoichiometricMatrix;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** @author John May */
public class MatrixTest {

    @Test public void sameEntity() {
        BasicStoichiometricMatrix m = BasicStoichiometricMatrix.create(5, 5);
        m.addReaction("3 H + A -> B + 4 H");

        assertThat(m.getValuesForMolecule(m.getIndex("H")), hasItem(1d));
    }

    @Test public void duplication() {
        BasicStoichiometricMatrix m = BasicStoichiometricMatrix.create(5, 5);
        m.addReaction("A + B -> C");
        m.addReaction(new String[]{"C"}, new String[]{"A", "B"});
        assertThat(m.getReactionCount(), is(1));
    }

    @Test public void makeIrreversible() {
        BasicStoichiometricMatrix m = BasicStoichiometricMatrix.create(5, 5);
        m.addReaction(new String[]{"C"}, new String[]{"A", "B"}, true);
        assertTrue(m.isReversible(0));
        m.addReaction(new String[]{"C"}, new String[]{"A", "B"}, false);
        assertFalse(m.isReversible(0));
        assertThat(m.getReactionCount(), is(1));
        assertThat(m.get(m.getIndex("A"), 0), is(1d));
        assertThat(m.get(m.getIndex("B"), 0), is(1d));
        assertThat(m.get(m.getIndex("C"), 0), is(-1d));
    }

    @Test public void makeIrreversible_andInvert() {
        BasicStoichiometricMatrix m = BasicStoichiometricMatrix.create(5, 5);
        m.addReaction(new String[]{"C"}, new String[]{"A", "B"}, true);
        assertTrue(m.isReversible(0));
        m.addReaction(new String[]{"A", "B"}, new String[]{"C"}, false);
        assertFalse(m.isReversible(0));
        assertThat(m.getReactionCount(), is(1));
        assertThat(m.get(m.getIndex("A"), 0), is(-1d));
        assertThat(m.get(m.getIndex("B"), 0), is(-1d));
        assertThat(m.get(m.getIndex("C"), 0), is(1d));
    }

    @Test public void doNotReplace() {
        BasicStoichiometricMatrix m = BasicStoichiometricMatrix.create(5, 5);
        m.addReaction(new String[]{"C"}, new String[]{"A", "B"}, false);
        m.addReaction(new String[]{"A", "B"}, new String[]{"C"}, false);
        assertThat(m.getReactionCount(), is(2));
    }

    @Test public void makeReversible() {
        BasicStoichiometricMatrix m = BasicStoichiometricMatrix.create(5, 5);
        m.addReaction(new String[]{"C"}, new String[]{"A", "B"}, false);
        m.addReaction(new String[]{"A", "B"}, new String[]{"C"}, true);
        assertTrue(m.isReversible(0));
        assertThat(m.getReactionCount(), is(1));
    }

}
