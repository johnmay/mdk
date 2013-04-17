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

package uk.ac.ebi.mdk.io.text.fingerprint;

import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.BitSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * @author John May
 */
public class FingerprintReaderTest {

    @Test(expected = NullPointerException.class)
    public void nullClass() throws IOException {
        new FingerprintReader(mock(Reader.class), null);
    }

    @Test(expected = NullPointerException.class)
    public void nullReader() throws IOException {
        new FingerprintReader(null, getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongClass() throws IOException {
        Reader in = new StringReader("I'm not the class you're looking for");
        new FingerprintReader(in, getClass());
    }

    @Test
    public void testRead() throws Exception {
        String input = "FingerprintReaderTest\n20\n{1, 5, 8, 16}";
        FingerprintReader reader = new FingerprintReader(new StringReader(input), getClass());
        BitSet fp = reader.read();
        assertThat(fp.cardinality(), is(4));
        assertTrue(fp.get(1));
        assertTrue(fp.get(5));
        assertTrue(fp.get(8));
        assertTrue(fp.get(16));
        reader.close();
    }

    @Test
    public void testRead_Empty() throws Exception {
        String input = "FingerprintReaderTest\n20\n{}";
        FingerprintReader reader = new FingerprintReader(new StringReader(input), getClass());
        BitSet fp = reader.read();
        assertThat(fp.cardinality(), is(0));
        reader.close();
    }
}
