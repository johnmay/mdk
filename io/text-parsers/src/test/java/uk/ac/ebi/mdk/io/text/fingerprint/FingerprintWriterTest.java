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
import java.io.Writer;
import java.util.BitSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author John May
 */
public class FingerprintWriterTest {

    @Test(expected = NullPointerException.class)
    public void nullWriter() throws IOException {
        new FingerprintWriter(null, getClass(), 1);
    }

    @Test(expected = NullPointerException.class)
    public void nullClass() throws IOException {
        new FingerprintWriter(mock(Writer.class), null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroLength() throws IOException {
        new FingerprintWriter(mock(Writer.class), getClass(), 0);
    }

    @Test public void testConstruction() throws IOException {
        Writer mock = mock(Writer.class);
        FingerprintWriter writer = new FingerprintWriter(mock, getClass(), 5);
        writer.close();

        char[] buffer = new char[8192];
        char[] data = new char[]{'F', 'i', 'n', 'g', 'e', 'r', 'p', 'r', 'i', 'n',
                                 't', 'W', 'r', 'i', 't', 'e', 'r', 'T', 'e',
                                 's', 't', '\n', '5', '\n'};
        System.arraycopy(data, 0, buffer, 0, data.length);


        verify(mock, times(1))
                .write(buffer,
                       0,
                       24);
        verify(mock, times(1)).close();
    }

    @Test public void testWriteOnce() throws IOException {
        Writer mock = mock(Writer.class);
        FingerprintWriter writer = new FingerprintWriter(mock, getClass(), 5);
        BitSet fp = new BitSet();
        fp.set(1);
        fp.set(5);
        fp.set(27);
        writer.write(fp);
        writer.close();

        char[] buffer = new char[8192];
        char[] data = new char[]{'F', 'i', 'n', 'g', 'e', 'r', 'p', 'r', 'i', 'n',
                                 't', 'W', 'r', 'i', 't', 'e', 'r', 'T', 'e',
                                 's', 't', '\n', '5', '\n', '{', '1', ',', ' ',
                                 '5', ',', ' ', '2', '7', '}', '\n'};
        System.arraycopy(data, 0, buffer, 0, data.length);
        verify(mock, times(1))
                .write(buffer,
                       0,
                       35);
        verify(mock, times(1)).close();
    }

    @Test public void testWriteTwice() throws IOException {
        Writer mock = mock(Writer.class);
        FingerprintWriter writer = new FingerprintWriter(mock, getClass(), 5);
        BitSet fp = new BitSet();
        fp.set(1);
        fp.set(5);
        fp.set(27);
        writer.write(fp);
        writer.write(fp);
        writer.close();

        char[] buffer = new char[8192];
        char[] data = new char[]{'F', 'i', 'n', 'g', 'e', 'r', 'p', 'r', 'i', 'n',
                                 't', 'W', 'r', 'i', 't', 'e', 'r', 'T', 'e',
                                 's', 't', '\n', '5', '\n', '{', '1', ',', ' ',
                                 '5', ',', ' ', '2', '7', '}', '\n', '{', '1',
                                 ',', ' ', '5', ',', ' ', '2', '7', '}', '\n'};
        System.arraycopy(data, 0, buffer, 0, data.length);
        verify(mock, times(1))
                .write(buffer,
                       0,
                       46);
        verify(mock, times(1)).close();
    }
}
