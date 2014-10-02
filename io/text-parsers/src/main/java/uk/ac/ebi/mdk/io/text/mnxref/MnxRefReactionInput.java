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

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A parser for MnxRef Reaction information. The parser loads the MnxRef
 * reactions into a map with lookup to {@link uk.ac.ebi.mdk.io.text.mnxref.MnxRefReaction}
 * entries.
 *
 * @author John May
 */
public class MnxRefReactionInput {

    private final Map<String, MnxRefReaction> reactions;

    private static final char SEPARATOR = '\t';
    private static final char QUOTE     = '\0';
    private static final char COMMENT   = '#';


    /**
     * Create a new parser for the given reaction properties and cross-reference
     * file.
     *
     * @param reacProp chemical properties (reac_props.tsv)
     * @param reacXref chemical cross-references (reac_xref.tsv)
     */
    public MnxRefReactionInput(File reacProp, File reacXref) throws
                                                             IOException {
        this(new FileReader(checkNotNull(reacProp, "no reac_prop.tsv provided")),
             new FileReader(checkNotNull(reacXref, "no reac_xref.tsv provided")));
    }

    /**
     * Create a new parser for the given reaction properties and cross-reference
     * readers.
     *
     * @param reacProp chemical properties reader (reac_props.tsv)
     * @param reacXref chemical cross-references reader (reac_xref.tsv)
     */
    public MnxRefReactionInput(Reader reacProp, Reader reacXref) throws
                                                                 IOException {
        reactions = load(checkNotNull(reacProp, "no reac_prop.tsv reader"),
                         checkNotNull(reacXref, "no reac_xref.tsv reader"));
    }

    private Map<String, MnxRefReaction> load(Reader propertyReader, Reader referenceReader) throws
                                                                                            IOException {


        Map<String, MnxRefReaction> reactions = Maps.newTreeMap();

        {
            CSVReader tsv = new CSVReader(propertyReader, SEPARATOR, QUOTE);
            try {
                String[] line;
                while ((line = tsv.readNext()) != null) {
                    if (line[0].charAt(0) != COMMENT) {
                        reactions.put(line[0], MnxRefReaction.parse(line));
                    }
                }
            } finally {
                tsv.close();
            }
        }

        {
            CSVReader tsv = new CSVReader(referenceReader, SEPARATOR, QUOTE);
            try {
                String[] line;
                while ((line = tsv.readNext()) != null) {
                    if (line[0].charAt(0) != COMMENT) {
                        String mnxId = line[1];
                        MnxRefReaction c = reactions.get(mnxId);
                        if (c == null) {
                            Logger logger = Logger.getLogger(getClass());
                            logger.warn("cound not find MNX id " + line[1]);
                        } else {
                            c.add(line[1]);
                        }
                    }
                }
            } finally {
                tsv.close();
            }
        }

        return reactions;
    }


    /**
     * Access a reaction by id.
     *
     * @param id identifier of the compound
     * @return an optional MnxRefReaction or absent optional
     */
    public Optional<MnxRefReaction> entry(String id) {
        checkArgument(Strings.nullToEmpty(id).startsWith("MNX"),
                      "Identifier should start with MNX");
        return Optional.fromNullable(reactions.get(id));
    }

    /**
     * Access all entries.
     *
     * @return iterable of entries
     */
    public Iterable<MnxRefReaction> entries() {
        return reactions.values();
    }

}
