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


import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.BitSet;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Read chemical fingerprints (represented as BitSets) from a provided input.
 *
 * <blockquote><pre>
 * Reader            reader   = ...;
 * Class<?>          c        = HybridizationFingerprinter.class
 * FingerprintReader fpReader = new FingerPrintReader(reader, c);
 *
 * BitSet fp = fpReader.read();
 *
 * </pre></blockquote>
 *
 * @author Pablo Moreno
 * @author John May
 * @see FingerprintWriter
 */
public final class FingerprintReader implements Closeable {

    private final BufferedReader reader;

    /* length of the fingerprints */
    private final int            length;

    /**
     * Create a new fingerprint reader for the given class. The class is simply
     * used as a check that the user knows which fingerprint they are reading.
     *
     * @param reader input reader
     * @param c      IFingerprint class
     * @throws IOException              low-level io exception
     * @throws IllegalArgumentException if the name of provided class does not
     *                                  match that which was used to write the
     *                                  bit sets
     * @throws NullPointerException     reader or class was null
     */
    public FingerprintReader(Reader reader, Class<?> c) throws IOException {

        // verify non-null arguments
        checkNotNull(reader, "reader was null");
        checkNotNull(c,      "class was null");

        this.reader = new BufferedReader(reader);
        checkArgument(c.getSimpleName().equals(this.reader.readLine()));
        this.length = Integer.parseInt(this.reader.readLine());
    }

    /**
     * Read the text bit set from the input reader.
     *
     * @return read bit set
     * @throws IOException low-level io exception
     */
    public BitSet read() throws IOException {

        BitSet fp = new BitSet(length);
        String line = reader.readLine();

        if(line.length() < 3)
            return fp;

        for (String bit : line.substring(1, line.length() - 1).split(", ")) {
            fp.set(Integer.parseInt(bit));
        }
        return fp;
    }

    /**
     * @inheritDoc
     */
    @Override public void close() throws IOException {
        this.reader.close();
    }
}
