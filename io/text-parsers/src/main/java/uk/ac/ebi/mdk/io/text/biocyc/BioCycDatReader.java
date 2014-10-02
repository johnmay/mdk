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

package uk.ac.ebi.mdk.io.text.biocyc;


import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.io.text.attribute.Attribute;
import uk.ac.ebi.mdk.io.text.attribute.AttributedEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Efficient reader for BioCyc '.dat' files. Constructor takes one or more
 * {@see Attribute}'s and will only process lines that match the given attribute.
 * If an entry does not contain any of the provided attributes it will be skipped.
 * Reading of the dat file is done via iteration of returned entities:
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * <pre>{@code
 * File            reactions = new File("/databases/biocyc/MetaCyc/data/reactions.dat");
 * BioCycDatReader reader    = new BioCycDatReader(new FileInputStream(reactions), COMMON_NAME, SYNONYMS);
 *
 * while (reader.hasNext()) {
 *
 *    AttributedEntry<Attribute, String> entry = reader.readNext();
 *
 *    if (entry.has(COMMON_NAME) && entry.isSingular(COMMON_NAME))
 *        System.out.println(entry.get(COMMON_NAME).iterator().readNext());
 *    // safe
 *    for (String synonym : entry.get(SYNONYMS))
 *        System.out.println("\t" + synonym);
 * }
 * reader.close();
 * }</pre>
 *
 * @author John May
 * @see Attribute
 * @see CompoundAttribute
 * @see ReactionAttribute
 * @see EnzymaticReactionAttribute
 */
public class BioCycDatReader<A extends Enum & Attribute> {

    private static final Logger LOGGER = Logger.getLogger(BioCycDatReader.class);

    private final BufferedReader in;

    /* parsing patterns */
    private Pattern RECORD_SEPARATOR = Pattern.compile("\\A//");
    private Pattern COMMENT = Pattern.compile("\\A#");
    private Pattern ENTRY;

    private AttributedEntry<A, String> entry = new AttributedEntry<A, String>();
    private Map<String, A> attributes = new HashMap<String, A>();

    /**
     * Constructor which provides desired attributes to be parsed. This constructor is
     * still compatible with {@see add(Attribute)}.
     * <p/>
     * Example - single attribute
     * <pre>{@code
     * BioCycDatReader reader = new BioCycDatReader(in, CompoundAttribute.COMMON_NAME);
     * }</pre>
     * Example - multiple attributes
     * <pre>{@code
     * BioCycDatReader reader = new BioCycDatReader(in, CompoundAttribute.COMMON_NAME, CompoundName.SYNONYMS);
     * }</pre>
     * Example - all attributes of a given file type
     * <pre>{@code
     * BioCycDatReader reader = new BioCycDatReader(in, CompoundAttribute.values());
     * }</pre>
     * Example - add additional attributes
     * <pre>{@code
     * BioCycDatReader reader = new BioCycDatReader(in, CompoundAttribute.COMMON_NAME);
     * reader.add(CompoundAttribute.SYNONYMS);
     * }</pre>
     * <p/>
     * Tip: use static import when using enumeration to avoid cumbersome
     * method statements.
     * <pre>{@code
     * import static uk.ac.ebi.mdk.io.text.biocyc.attribute.CompoundAttribute.*;
     * ...
     * BioCycDatReader reader = new BioCycDatReader(in, COMMON_NAME);
     * reader.add(SYNONYMS);
     * }</pre>
     *
     * @param in         input stream to read from
     * @param attributes attributes that will be parsed
     */
    public BioCycDatReader(InputStream in, A... attributes) {
        this.in = new BufferedReader(new InputStreamReader(in));

        Set<A> attributeSet = new HashSet<A>(Arrays.asList(attributes));
        addAll(attributeSet);

    }

    /**
     * Constructor providing input stream only will not parse any entries. To allow
     * parsing attributes can be added via the {@see add(Attribute)} method.
     *
     * @param in desired input stream
     *
     * @see #add(A)
     */
    public BioCycDatReader(InputStream in) {
        this.in = new BufferedReader(new InputStreamReader(in));
        ENTRY = getPattern(Collections.<A>emptySet());
    }

    /**
     * Add a required attribute post-instantiation. This method allow adding of additional attributes
     * before or during parsing. Adding a new attribute will rebuild the attribute pattern matching
     * and so it is recommend to use the constructor or {@see addAll(Collection<Attribute>} when
     *adding numerous attributes.
     *
     * @param attribute new attribute to parse
     */
    public void add(A attribute) {
        attributes.put(attribute.getName(), attribute);
        // rebuild pattern
        ENTRY = getPattern(new HashSet<A>(attributes.values()));
    }

    /**
     * Add multiple attributes to accept when parsing the input.
     * <p/>
     * Example - adding an enumeration
     * <pre>{@code
     * reader.addAll(new ArrayList<Attribute>(Arrays.asList(CompoundAttribute.values())));
     * }</pre>
     * Example - adding select attributes
     * <pre>{@code
     * reader.addAll(new ArrayList<Attribute>(Arrays.asList(CompoundAttribute.COMMON_NAME,
     *CompoundAttribute.ABBREV_NAME)));
     * }</pre>
     *
     * @param attributes additional attributes to accept
     */
    public void addAll(Collection<A> attributes) {
        for (A attribute : attributes) {
            this.attributes.put(attribute.getName(), attribute);
        }
        ENTRY = getPattern(new HashSet<A>(attributes));
    }

    /**
     * Reads an entry from the input. If the entry contains no required
     * attributes it is skipped. An empty entry will only be returned if the end
     * of the file is reached.
     *
     * @return readNext filled entry
     *
     * @throws IOException low-level IO exception
     */
    private AttributedEntry<A, String> readEntry() throws IOException {

        String line;
        while ((line = in.readLine()) != null) {

            if (COMMENT.matcher(line).find())
                continue;

            // we only return entries that have content
            if (RECORD_SEPARATOR.matcher(line).find() && entry.isFilled())
                return entry;

            Matcher matcher = ENTRY.matcher(line);

            if (matcher.matches()) {
                entry.put(attributes.get(matcher.group(1).trim()), matcher.group(2).trim());
            }

        }

        return entry;

    }

    /**
     * Access the readNext entry in the reader. If no entry exists
     * a {@see NoSuchelementException} will be thrown
     *
     * @return the readNext entry
     */
    public AttributedEntry<A, String> next() {
        if (hasNext())
            return entry.renew();
        throw new NoSuchElementException("No readNext entry in BioCyc dat file");
    }

    /**
     * Determine whether there is an 'readNext' entry in the file
     *
     * @return whether there is an entry to access
     */
    public boolean hasNext() {

        try {
            // only read if the entry is empty
            if (entry.isEmpty())
                entry = readEntry();
        } catch (IOException ex) {
            LOGGER.error("Low level IO error occurred whilst reading entry: " + ex.getMessage());
        }

        return entry != null && entry.isFilled();

    }

    /**
     * Close the underlying input stream.
     *
     * @throws IOException low-level IO error
     */
    public void close() throws IOException {
        in.close();
    }


    /**
     * Builds a pattern to match the input with.
     *
     * @param attributes attributes to use
     *
     * @return pattern compiled pattern
     */
    private Pattern getPattern(Set<A> attributes) {

        StringBuilder sb = new StringBuilder();

        Iterator<A> iterator = attributes.iterator();

        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            sb.append(attribute.getPattern()).append(iterator.hasNext() ? "|" : "");
        }

        String keyPart = sb.toString();

        return Pattern.compile("(" + keyPart + ")\\s+-\\s(.+)");

    }


}
