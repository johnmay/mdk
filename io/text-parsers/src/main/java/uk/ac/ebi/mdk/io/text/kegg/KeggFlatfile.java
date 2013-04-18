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

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.io.text.attribute.AttributedEntry;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.primitives.Ints.min;

/**
 * A simple parser for KEGG flatfiles. The parser simple loads the key/value
 * pairs into an attributed entry.
 *
 * @param <E> kegg field type
 * @author John May
 */
public class KeggFlatfile<E extends Enum & KEGGField>
        implements Iterable<AttributedEntry<E, String>>,
                   Closeable {

    private static final Collection<KEGGCompoundField> COMPOUND_FIELDS = Arrays
            .asList(KEGGCompoundField.values());
    private static final Collection<KEGGReactionField> REACTION_FIELDS = Arrays
            .asList(KEGGReactionField.values());


    /** separates the records. */
    private static final String RECORD_SEPARATOR = "///";

    /** field padding 12 spaces. */
    private static final String EMPTY_FIELD = "            ";

    /** the length of the padding. */
    private static final int KEY_LENGTH = EMPTY_FIELD.length();

    /** input. */
    private final BufferedReader reader;

    /** lookup of field. */
    private final Map<String, E> fields = new HashMap<String, E>(31);

    /**
     * Create a new parser for the specified COMPOUND_FIELDS.
     *
     * @param r  a reader
     * @param fs collection of all possible COMPOUND_FIELDS.
     */
    KeggFlatfile(final Reader r, final Collection<E> fs) {
        checkNotNull(r, "no reader proivded");
        checkNotNull(fs, "no fields proivded");
        checkArgument(!fs.isEmpty(), "no fields provided");
        this.reader = new BufferedReader(r);
        for (E field : fs) {
            for (String key : field.names()) {
                this.fields.put(pad(key), field);
            }
        }
    }

    /**
     * Get the field for the provided line. If the line is blank the previous
     * field is used (index in the map).
     *
     * @param line line from the file
     * @return the field
     */
    E field(final String line) {
        String key = line.substring(0, min(line.length(), KEY_LENGTH));
        E newField = fields.get(key);
        fields.put(EMPTY_FIELD, newField);
        return newField;
    }

    /**
     * Read the next entry.
     *
     * @return the next attributed entry
     * @throws IOException low-level io exception
     */
    AttributedEntry<E, String> read() throws IOException {
        String line;
        AttributedEntry<E, String> entry = new AttributedEntry<E, String>();
        while ((line = reader.readLine()) != null
                && !line.equals(RECORD_SEPARATOR)) {
            E field = field(line);
            if (field != null && line.length() > KEY_LENGTH) {
                entry.put(field, line.substring(KEY_LENGTH));
            }
        }
        return entry;
    }

    /**
     * @throws IOException low-level io exception
     * @inheritDoc
     */
    @Override public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }

    @Override public Iterator<AttributedEntry<E, String>> iterator() {
        final KeggFlatfile<E> self = this;
        try {
            return new Iterator<AttributedEntry<E, String>>() {

                AttributedEntry<E, String> next = self.read();

                @Override public boolean hasNext() {
                    if (next == null) {
                        try {
                            next = self.read();
                            // last entry.. close the reaer
                            if (next.isEmpty()) {
                                try {
                                    self.close();
                                } catch (IOException e) {
                                    Logger.getLogger(getClass()).error(e);
                                }
                            }
                        } catch (IOException e) {
                            Logger.getLogger(getClass())
                                  .error("unable to reader entry from KEGG Compound");
                        }
                    }
                    return !next.isEmpty();
                }

                @Override public AttributedEntry<E, String> next() {
                    hasNext();
                    AttributedEntry<E, String> tmp = next;
                    next = null;
                    return tmp;
                }

                @Override public void remove() {
                    throw new UnsupportedOperationException("removal of entries not allowed");
                }
            };
        } catch (IOException e) {
            Logger.getLogger(getClass())
                  .error("unable to create iterator for kegg compound parser");
        }

        return Collections.<AttributedEntry<E, String>>emptyList().iterator();
    }

    /**
     * Pad the provided key with trailing spaces up to length 12.
     *
     * @param key a key
     * @return padded key
     */
    static String pad(final String key) {
        return key + EMPTY_FIELD.substring(key.length());
    }

    public static ReactionEntry reaction(final String path) throws
                                                            IOException {
        if (path.startsWith("http://")) {
            return reaction(new URL(path));
        } else {
            return reaction(new FileInputStream(path));
        }
    }

    public static ReactionEntry reaction(final URL url) throws
                                                        IOException {
        return reaction(url.openStream());
    }

    public static ReactionEntry reaction(final InputStream in) throws
                                                               IOException {
        KeggFlatfile<KEGGReactionField> flatfile = new KeggFlatfile<KEGGReactionField>(new InputStreamReader(in),
                                                                                       REACTION_FIELDS);
        try {
            return new ReactionEntry(flatfile.read());
        } finally {
            flatfile.close();
        }
    }

    public static Iterable<ReactionEntry> reactions(final File f) throws
                                                                  FileNotFoundException {
        return reactions(new FileReader(f));
    }

    public static Iterable<ReactionEntry> reactions(final String path) throws
                                                                       FileNotFoundException {
        return reactions(new File(path));
    }

    public static Iterable<ReactionEntry> reactions(final InputStream in) throws
                                                                         IOException {
        return reactions(new InputStreamReader(in));
    }

    public static Iterable<ReactionEntry> reactions(final Reader r) throws
                                                                    FileNotFoundException {
        return FluentIterable.from(new KeggFlatfile<KEGGReactionField>(r,
                                                                       REACTION_FIELDS))
                             .transform(new Function<AttributedEntry<KEGGReactionField, String>, ReactionEntry>() {
                                 @Override
                                 public ReactionEntry apply(AttributedEntry<KEGGReactionField, String> e) {
                                     return new ReactionEntry(e);
                                 }
                             });
    }

    public static AttributedEntry<KEGGCompoundField, String> compound(final String path) throws
                                                                                         IOException {
        if (path.startsWith("http://")) {
            return compound(new URL(path));
        } else {
            return compound(new FileInputStream(path));
        }
    }

    public static AttributedEntry<KEGGCompoundField, String> compound(final URL url) throws
                                                                                     IOException {
        return compound(url.openStream());
    }

    public static AttributedEntry<KEGGCompoundField, String> compound(final InputStream in) throws
                                                                                            IOException {
        KeggFlatfile<KEGGCompoundField> flatfile = new KeggFlatfile<KEGGCompoundField>(new InputStreamReader(in),
                                                                                       COMPOUND_FIELDS);
        try {
            return flatfile.read();
        } finally {
            flatfile.close();
        }
    }

    public static Iterable<AttributedEntry<KEGGCompoundField, String>> compounds(final File f) throws
                                                                                               FileNotFoundException {
        return compounds(new FileReader(f));
    }

    public static Iterable<AttributedEntry<KEGGCompoundField, String>> compounds(final InputStream in) throws
                                                                                                       FileNotFoundException {
        return compounds(new InputStreamReader(in));
    }

    public static Iterable<AttributedEntry<KEGGCompoundField, String>> compounds(final String path) throws
                                                                                                    FileNotFoundException {
        return compounds(new File(path));
    }

    public static Iterable<AttributedEntry<KEGGCompoundField, String>> compounds(final Reader r) throws
                                                                                                 FileNotFoundException {
        Collection<KEGGCompoundField> fields = Arrays.asList(KEGGCompoundField
                                                                     .values());
        return new KeggFlatfile<KEGGCompoundField>(r, COMPOUND_FIELDS);
    }

}
