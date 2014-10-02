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

package uk.ac.ebi.mdk.io.text.kegg;

import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/** @author John May */
public class ReactionEntryTest {

    @Test public void singleEnzyme() throws IOException {
        ReactionEntry e = KeggFlatfile.reaction(getClass().getResourceAsStream("reaction"));
        Set<String> enzymes = e.enzymes();
        assertThat(enzymes, hasItem("3.6.1.10"));
    }

    @Test public void multiEnzyme() throws IOException {
        ReactionEntry e = KeggFlatfile.reaction(getClass().getResourceAsStream("R00203"));
        Set<String> enzymes = e.enzymes();
        assertThat(enzymes, hasItems("1.2.1.23", "1.2.1.22"));
    }

    @Test public void reactionFromEntry() throws IOException {
        ReactionEntry e = KeggFlatfile.reaction(getClass().getResourceAsStream("R00203"));
        assertThat(e.reactantCount(), is(3));
        assertThat(e.productCount(), is(3));
        // C00546 + C00003 + C00001 <=> C00022 + C00004 + C00080
        assertThat(e.reactant(0), is("C00546"));
        assertThat(e.reactant(1), is("C00003"));
        assertThat(e.reactant(2), is("C00001"));
        assertThat(e.product(0), is("C00022"));
        assertThat(e.product(1), is("C00004"));
        assertThat(e.product(2), is("C00080"));
    }

    @Test
    public void testNormaliseCoefficient() throws Exception {
        assertThat(ReactionEntry.normaliseCoefficient(""), is("1"));
        assertThat(ReactionEntry.normaliseCoefficient("2"), is("2"));
        assertThat(ReactionEntry.normaliseCoefficient("8"), is("8"));
        assertThat(ReactionEntry.normaliseCoefficient("8"), is("8"));
        assertThat(ReactionEntry.normaliseCoefficient("2.1"), is("2.1"));
        assertThat(ReactionEntry.normaliseCoefficient("n+1"), is("3"));
        assertThat(ReactionEntry.normaliseCoefficient("n-1"), is("1"));
        assertThat(ReactionEntry.normaliseCoefficient("3n"), is("6"));
        assertThat(ReactionEntry.normaliseCoefficient("2n"), is("4"));
    }

    @Test public void testParticipants() throws Exception {
        String[][] parsed = ReactionEntry.particpants("C00002 + C00005");
        assertThat(parsed, is(new String[][]{
                {"1", "C00002"},
                {"1", "C00005"}
        }));
    }

    @Test public void testParticipants_2() throws Exception {
        String[][] parsed = ReactionEntry.particpants("C00002 + (3) C00005");
        assertThat(parsed, is(new String[][]{
                {"1", "C00002"},
                {"3", "C00005"}
        }));
    }
}
