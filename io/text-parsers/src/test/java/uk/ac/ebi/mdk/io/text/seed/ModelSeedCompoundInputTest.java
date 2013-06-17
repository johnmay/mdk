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

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.ac.ebi.mdk.io.text.seed.ModelSeedCompoundInput.fromStream;

/** @author John May */
public class ModelSeedCompoundInputTest {

    @Test public void read() throws IOException {
        InputStream in = getClass().getResourceAsStream("compounds.tsv");
        List<ModelSeedCompound> cpds = fromStream(in);
        assertThat(cpds.get(0)
                       .id(), is("cpd00001"));
        assertThat(cpds.get(0)
                       .name(), is("OH-"));
        assertThat(cpds.get(0)
                       .formula(), is("H2O"));
        assertThat(cpds.get(0)
                       .mass(), is(18d));
        assertThat(cpds.get(0)
                       .keggIds(), hasItems("C00001",
                                            "C01328"));
    }
}
