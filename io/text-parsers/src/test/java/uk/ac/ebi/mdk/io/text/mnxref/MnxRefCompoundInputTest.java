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

package uk.ac.ebi.mdk.io.text.mnxref;

import com.google.common.base.Optional;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/** @author John May */
public class MnxRefCompoundInputTest {

    @Test(expected = NullPointerException.class)
    public void constructor_null_file() throws IOException {
        new MnxRefCompoundInput(null, File.createTempFile("tmp", ""));
    }

    @Test(expected = NullPointerException.class)
    public void constructor_file_null() throws IOException {
        new MnxRefCompoundInput(File.createTempFile("tmp", ""), null);
    }

    @Test
    public void xrefs() throws IOException {
        InputStreamReader prop = new InputStreamReader(getClass()
                                                               .getResourceAsStream("chem_prop.tsv"));
        InputStreamReader xref = new InputStreamReader(getClass()
                                                               .getResourceAsStream("chem_xref.tsv"));
        MnxRefCompoundInput in = new MnxRefCompoundInput(prop, xref);
        Optional<MnxRefCompound> o = in.entry("MNXM22074");
        assertTrue(o.isPresent());
        MnxRefCompound c = o.get();


        assertThat(c.id(), is("MNXM22074"));
        assertThat(c.name(), is("thymidine-dimer-containing DNA"));
        assertThat(c.source(), is("brenda:BG95541"));
    }

}
