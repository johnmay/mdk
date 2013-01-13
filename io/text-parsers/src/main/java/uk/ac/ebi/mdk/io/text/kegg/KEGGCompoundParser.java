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

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

/**
 * A parser for KEGG Compound flat files (v57). This parser could be more
 * abstract but serves it's purpose for now.
 */
public final class KEGGCompoundParser implements Iterable<KEGGCompoundEntry>,
                                                 Closeable {

    private BufferedReader reader;
    private EnumSet<KEGGCompoundField> fields = EnumSet.noneOf(KEGGCompoundField.class);

    /**
     * Create a new parser specifying which fields you which to read. Only
     * fields provided will be stored in the constructed entry.
     *
     * @param reader input reader
     * @param fields the fields to be persisted
     */
    public KEGGCompoundParser(Reader reader, KEGGCompoundField... fields) {
        if (reader == null)
            throw new IllegalArgumentException("null reader not allowed");
        this.reader = new BufferedReader(reader);
        for (KEGGCompoundField f : fields) {
            this.fields.add(f);
        }
    }


    /**
     * Read the next entry from in the input. If the input has reached the end a
     * null entry is returned.
     *
     * @return the next entry.
     * @throws IOException low level io-error
     */
    public KEGGCompoundEntry readNext() throws IOException {

        KEGGCompoundEntry entry = new KEGGCompoundEntry();

        String line;
        KEGGCompoundField field = null;

        // read until null or end of entry
        while ((line = reader.readLine()) != null && !line.equals("///")) {

            // get the field
            String key = line.substring(0, Math.min(line.length(), 12)).trim();
            field = key.isEmpty() ? field : KEGGCompoundField.valueOf(key);

            // is this field wanted?
            if (fields.contains(field) && line.length() > 12) {
                // add the line suffix to the entry for that field
                entry.put(field, line.substring(12));
            }

        }

        return line == null ? null : entry;
    }


    /**
     * Utility that wraps the IO operations in an iterator. Note the checked
     * exception handling is sacrificed. Any {@link IOException} is logged to
     * the Log4J appender as an error.
     *
     * <blockquote><pre>
     * KEGGCompoundParser parser = new KEGGCompoundParser(in,
     *
     * KEGGCompoundField.ENTRY);
     *
     * for (KEGGCompoundEntry entry : parser) {
     *     // handle entry
     * }
     *
     * parser.close(); // close the resource
     *
     * </pre>
     * </blockquote>
     *
     * @return an iterator for kegg entries
     */
    @Override public Iterator<KEGGCompoundEntry> iterator() {
        final KEGGCompoundParser self = this;
        try {
            return new Iterator<KEGGCompoundEntry>() {

                KEGGCompoundEntry next = self.readNext();

                @Override public boolean hasNext() {
                    if (next == null) {
                        try {
                            next = self.readNext();
                        } catch (IOException e) {
                            Logger.getLogger(getClass()).error("unable to reader entry from KEGG Compound");
                        }
                    }
                    return next != null;
                }

                @Override public KEGGCompoundEntry next() {
                    KEGGCompoundEntry tmp = next;
                    next = null;
                    return tmp;
                }

                @Override public void remove() {
                    throw new UnsupportedOperationException("removal of entries not allowed");
                }
            };
        } catch (IOException e) {
            Logger.getLogger(getClass()).error("unable to create iterator for kegg compound parser");
        }

        // return an empty iterator
        List<KEGGCompoundEntry> empty = Collections.emptyList();

        return empty.iterator();
    }

    /**
     * Convenience method for parsing a single entry from an input stream. This
     * method will load all values in {@link KEGGCompoundField}. If an exception
     * occurred an empty entry is returned.
     *
     * @param in input data
     * @return entry from the input
     */
    public static KEGGCompoundEntry load(InputStream in) {
        return load(in, KEGGCompoundField.values());
    }

    /**
     * Convenience method for parsing a single entry from an input stream. This
     * method will load only the specified fields. If an exception occurred an
     * empty entry is returned.
     *
     * @param in input data
     * @return entry from the input
     */
    public static KEGGCompoundEntry load(InputStream in, KEGGCompoundField... fields) {

        KEGGCompoundParser parser = new KEGGCompoundParser(new InputStreamReader(in),
                                                           fields);
        try {
            return parser.readNext();
        } catch (IOException e) {
            Logger.getLogger(KEGGCompoundParser.class).error(e.getMessage());
        } finally {
            try {
                parser.close();
            } catch (IOException e) {
                // can't do anything
            }
        }

        // empty entry return
        return new KEGGCompoundEntry();
    }

    /**
     * @inheritDoc
     */
    @Override public void close() throws IOException {
        if (reader != null)
            reader.close();
    }
}

