/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static uk.ac.ebi.mdk.io.text.kegg.KEGGCompoundField.ENTRY;
import static uk.ac.ebi.mdk.io.text.kegg.KEGGCompoundField.NAME;

/**
 * @author John May
 */
public class KEGGCompoundParserTest {

    @Test public void testRead_Name() throws IOException {

        KEGGCompoundParser parser = new KEGGCompoundParser(new InputStreamReader(getClass().getResourceAsStream("C00009.txt")),
                                                           KEGGCompoundField.NAME);

        KEGGCompoundEntry entry = parser.readNext();

        String[] expected = new String[]{
                "Orthophosphate;",
                "Phosphate;",
                "Phosphoric acid;",
                "Orthophosphoric acid"
        };
        String[] actual = entry.get(NAME).toArray(new String[0]);

        Assert.assertArrayEquals(expected, actual);

    }

    @Test public void testRead_DBLINKS() throws IOException {

        KEGGCompoundParser parser = new KEGGCompoundParser(new InputStreamReader(getClass().getResourceAsStream("C00009.txt")),
                                                           KEGGCompoundField.DBLINKS);

        KEGGCompoundEntry entry = parser.readNext();

        String[] expected = new String[]{
                "CAS: 7664-38-2",
                "PubChem: 3311",
                "ChEBI: 18367 26078",
                "KNApSAcK: C00007408",
                "PDB-CCD: 2HP PI PO4",
                "3DMET: B00002",
                "NIKKAJI: J3.746J"
        };
        String[] actual = entry.get(KEGGCompoundField.DBLINKS).toArray(new String[0]);

        Assert.assertArrayEquals(expected, actual);

    }

    @Test public void testRead_FORMULA() throws IOException {

        KEGGCompoundParser parser = new KEGGCompoundParser(new InputStreamReader(getClass().getResourceAsStream("C00009.txt")),
                                                           KEGGCompoundField.FORMULA);

        KEGGCompoundEntry entry = parser.readNext();

        String[] expected = new String[]{
                "H3PO4"
        };
        String[] actual = entry.get(KEGGCompoundField.FORMULA).toArray(new String[0]);

        Assert.assertArrayEquals(expected, actual);

    }

    @Test public void testRead_ENTRY() throws IOException {

        InputStreamReader in = new InputStreamReader(getClass().getResourceAsStream("C00009.txt"));
        KEGGCompoundParser parser = new KEGGCompoundParser(in, KEGGCompoundField.ENTRY);

        KEGGCompoundEntry entry = parser.readNext();

        String[] expected = new String[]{
                "C00009                      Compound"
        };
        String[] actual = entry.get(KEGGCompoundField.ENTRY).toArray(new String[0]);

        Assert.assertArrayEquals(expected, actual);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_Null() {
        KEGGCompoundParser parser = new KEGGCompoundParser(null);
    }

    @Test public void testRead_Iterable() throws IOException {

        InputStreamReader in = new InputStreamReader(getClass().getResourceAsStream("C00009-C00011.txt"));
        KEGGCompoundParser parser = new KEGGCompoundParser(in, KEGGCompoundField.ENTRY);

        List<KEGGCompoundEntry> entries = new ArrayList<KEGGCompoundEntry>(5);
        for (KEGGCompoundEntry entry : parser) {
            entries.add(entry);
        }

        Assert.assertEquals(3, entries.size());
        Assert.assertEquals("C00009                      Compound",
                            entries.get(0).getFirst(ENTRY));
        Assert.assertEquals("C00010                      Compound",
                            entries.get(1).getFirst(ENTRY));
        Assert.assertEquals("C00011                      Compound",
                            entries.get(2).getFirst(ENTRY));

        parser.close();

    }

    /**
     * ensure empty iterator on stream exception
     *
     * @throws IOException
     */
    @Test public void testRead_Iterable_Empty() throws IOException {

        InputStreamReader in = new InputStreamReader(new InputStream() {
            @Override public int read() throws IOException {
                throw new IOException();
            }
        });
        KEGGCompoundParser parser = new KEGGCompoundParser(in, KEGGCompoundField.ENTRY);

        Assert.assertEquals(false, parser.iterator().hasNext());

        parser.close();

    }

}
