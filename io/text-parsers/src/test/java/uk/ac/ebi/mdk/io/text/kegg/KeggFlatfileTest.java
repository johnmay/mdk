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
import uk.ac.ebi.mdk.io.text.attribute.AttributedEntry;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static uk.ac.ebi.mdk.io.text.kegg.KEGGReactionField.COMMENT;
import static uk.ac.ebi.mdk.io.text.kegg.KEGGReactionField.DEFINITION;
import static uk.ac.ebi.mdk.io.text.kegg.KEGGReactionField.ENTRY;
import static uk.ac.ebi.mdk.io.text.kegg.KEGGReactionField.ENZYME;
import static uk.ac.ebi.mdk.io.text.kegg.KEGGReactionField.EQUATION;
import static uk.ac.ebi.mdk.io.text.kegg.KEGGReactionField.NAME;
import static uk.ac.ebi.mdk.io.text.kegg.KEGGReactionField.RPAIR;

/** @author John May */
public class KeggFlatfileTest {

    static Reader reader(String path) {
        return new InputStreamReader(KeggFlatfileTest.class
                                             .getResourceAsStream(path));
    }

    @Test public void testReaction() throws Exception {
        Iterable<ReactionEntry> entries =
                KeggFlatfile.reactions(reader("reaction"));
        Iterator<ReactionEntry> it = entries
                .iterator();
        ReactionEntry e = it.next();
        assertThat(e.accession(), is("R00001"));
        assertThat(e.name(), is("Polyphosphate polyphosphohydrolase"));

        e = it.next();
        assertThat(e.accession(), is("R00002"));
        assertThat(e.name(), is("Reduced ferredoxin:dinitrogen oxidoreductase (ATP-hydrolysing)"));
        assertThat(e.comment(), is("a part of multi-step reaction (see R05185,R00002+R00067+R00153+R02802+R04782)"));
        assertThat(e.rpairs(), hasItems("RP00003  C00002_C00008 main",
                                          "RP00010  C00002_C00009 leave",
                                          "RP05676  C00001_C00009 leave"));
        assertThat(e.enzymes(), hasItem("1.18.6.1"));
    }

    @Test public void testKey() throws Exception {
        KeggFlatfile<KEGGReactionField> parser = new KeggFlatfile<KEGGReactionField>(mock(Reader.class),
                                                                                     Arrays.asList(KEGGReactionField
                                                                                                           .values()));
        assertThat(parser.field("            "), is(nullValue()));
        assertThat(parser.field("ENTRY       "), is(ENTRY));
        assertThat(parser.field("            "), is(ENTRY));
        assertThat(parser.field("ENZYME      "), is(KEGGReactionField.ENZYME));
        assertThat(parser.field("            "), is(KEGGReactionField.ENZYME));
    }

    @Test public void testClose() throws Exception {
        Reader reader = mock(Reader.class);
        KeggFlatfile<KEGGReactionField> parser = new KeggFlatfile<KEGGReactionField>(reader,
                                                                                     Arrays.asList(KEGGReactionField
                                                                                                           .values()));
        parser.close();
        verify(reader).close();
    }

    @Test
    public void testPad() throws Exception {
        assertThat(KeggFlatfile.pad("ENTRY").length(), is(12));
        assertThat(KeggFlatfile.pad("ENTRY"), is("ENTRY       "));
        assertThat(KeggFlatfile.pad("ENTRY:"), is("ENTRY:      "));
    }
}
