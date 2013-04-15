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

package uk.ac.ebi.mdk.io.text.img;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static uk.ac.ebi.mdk.io.text.img.ImgFunctionReader.ImgFunction;

/** @author John May */
public class ImgFunctionReaderTest {

    @Test
    public void testIsHeader() throws Exception {
        assertFalse(ImgFunctionReader.isHeader(new String[0]));
        assertFalse(ImgFunctionReader.isHeader(new String[4]));
        assertFalse(ImgFunctionReader.isHeader(new String[4]));
        assertFalse(ImgFunctionReader.isHeader(new String[]{"", "", "", ""}));
        assertTrue(ImgFunctionReader.isHeader(new String[]{"IMG Class",
                                                           "Gene Object ID",
                                                           "Gene Product Name",
                                                           "IMG EC"}));
    }

    @Test public void testParse() throws IOException {
        ImgFunction func = ImgFunctionReader.parse(new String[]{
                "wfws", "637365049",
                "DNA polymerase III, beta subunit (EC 2.7.7.7) (IMGterm)  ",
                "EC:2.7.7.7"
        });
        assertThat(func.id(), is(637365049));
        assertThat(func.product(), is("DNA polymerase III, beta subunit (EC 2.7.7.7) (IMGterm)"));
        assertThat(func.ec(), hasItem("EC:2.7.7.7"));
    }

    @Test public void testParse_NoEc() throws IOException {
        ImgFunction func = ImgFunctionReader.parse(new String[]{
                "wfws", "637365108",
                "cassette chromosome recombinase A",
                ""
        });
        assertThat(func.id(), is(637365108));
        assertThat(func.product(), is("cassette chromosome recombinase A"));
        assertTrue(func.ec().isEmpty());
    }

    @Test public void testParse_multipleEC() throws IOException {
        ImgFunction func = ImgFunctionReader.parse(new String[]{
                "wfws", "637365111",
                "copper-transporting ATPase copA ( EC:3.6.3.3,EC:3.6.3.5 ) ",
                "EC:3.6.3.3, EC:3.6.3.5"
        });
        assertThat(func.id(), is(637365111));
        assertThat(func.product(), is("copper-transporting ATPase copA ( EC:3.6.3.3,EC:3.6.3.5 )"));
        assertThat(func.ec(), hasItem("EC:3.6.3.3"));
        assertThat(func.ec(), hasItem("EC:3.6.3.5"));
    }

    @Test public void test() throws IOException {
        InputStream in = getClass().getResourceAsStream("img-sample.tsv");
        Collection<ImgFunction> funcs = ImgFunctionReader.fromInputStream(in);
        assertThat(funcs.size(), is(26));
    }
}
