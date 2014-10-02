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

package uk.ac.ebi.mdk.io.text.seed;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.ac.ebi.mdk.io.text.seed.ModelSeedReactionInput.fromStream;

/** @author John May */
public class ModelSeedReactionInputTest {

    @Test public void read() throws IOException {
        InputStream in = getClass().getResourceAsStream("reactions.tsv");
        List<ModelSeedReaction> rxns = fromStream(in);
        assertThat(rxns.get(0)
                       .id(), is("rxn00001"));
        assertThat(rxns.get(0)
                       .name(), is("Pyrophosphate phosphohydrolase"));
        assertThat(rxns.get(0)
                       .equation(), is("|H2O| + |PPi| => (2) |Phosphate| + (2) |H+|"));
        assertThat(rxns.get(0)
                       .enzyme(), is("3.6.1.1"));
        assertThat(rxns.get(0)
                       .roles(), hasItems("Inorganic pyrophospatase PpaX",
                                          "Manganese-dependent inorganic pyrophosphatase (EC 3.6.1.1)",
                                          "Inorganic pyrophosphatase (EC 3.6.1.1)"));
        assertThat(rxns.get(4)
                       .subsystems(), hasItems("Protection_from_Reactive_Oxygen_Species",
                                               "Photorespiration_(oxidative_C2_cycle)",
                                               "Oxidative_stress"));
    }
}
