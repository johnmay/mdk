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
import java.io.Reader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/** @author John May */
public class MnxRefReactionInputTest {

    @Test(expected = NullPointerException.class)
    public void constructor_null_file() throws IOException {
        new MnxRefReactionInput(null, File.createTempFile("tmp", ""));
    }

    @Test(expected = NullPointerException.class)
    public void constructor_file_null() throws IOException {
        new MnxRefReactionInput(File.createTempFile("tmp", ""), null);
    }

    @Test
    public void entryById() throws IOException {
        MnxRefReactionInput in = new MnxRefReactionInput(reader("reac_prop.tsv"),
                                                         reader("reac_xref.tsv"));
        Optional<MnxRefReaction> o = in.entry("MNXR5");
        assertTrue(o.isPresent());
        MnxRefReaction c = o.get();

        assertThat(c.id(), is("MNXR5"));
        assertThat(c.equation(), is("1 MNXM1399 = 1 MNXM1399"));
        assertThat(c.source(), is("bigg:11DOCRTSLtm"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void entryById_invalid() throws IOException {
        MnxRefReactionInput in = new MnxRefReactionInput(reader("reac_prop.tsv"),
                                                         reader("reac_xref.tsv"));
        Optional<MnxRefReaction> o = in.entry("WAT!");
    }

    Reader reader(String path) {
        return new InputStreamReader(getClass().getResourceAsStream(path));
    }

}
