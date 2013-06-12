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
import static uk.ac.ebi.mdk.io.text.mnxref.MnxRefCompound.Xref;

/**
 * A parser for MnxRef Compound information. The parser loads the MnxRef
 * compounds into a map with lookup to {@link MnxRefCompound} entries.
 *
 * @author John May
 */
public class MnxRefCompoundInput {

    private final Map<String, MnxRefCompound> compounds;

    private static final char SEPARATOR = '\t';
    private static final char QUOTE     = '\0';
    private static final char COMMENT   = '#';


    /**
     * Create a new parser for the given chemical properties and cross-reference
     * file.
     *
     * @param chemProps chemical properties (chem_props.tsv)
     * @param chemXref  chemical cross-references (chem_xref.tsv)
     */
    public MnxRefCompoundInput(File chemProps, File chemXref) throws
                                                              IOException {
        this(new FileReader(checkNotNull(chemProps, "no chem_prop.tsv provided")),
             new FileReader(checkNotNull(chemXref, "no chem_xref.tsv provided")));
    }

    /**
     * Create a new parser for the given chemical properties and cross-reference
     * readers.
     *
     * @param chemProps chemical properties reader (chem_props.tsv)
     * @param chemXref  chemical cross-references reader (chem_xref.tsv)
     */
    public MnxRefCompoundInput(Reader chemProps, Reader chemXref) throws
                                                                  IOException {
        compounds = load(checkNotNull(chemProps, "no chem_prop.tsv reader"),
                         checkNotNull(chemXref, "no chem_xref.tsv reader"));
    }

    private Map<String, MnxRefCompound> load(Reader propertyReader, Reader referenceReader) throws
                                                                                            IOException {


        Map<String, MnxRefCompound> compounds = Maps.newTreeMap();

        {
            CSVReader tsv = new CSVReader(propertyReader, SEPARATOR, QUOTE);
            try {
                String[] line;
                while ((line = tsv.readNext()) != null) {
                    if (line[0].charAt(0) != COMMENT) {
                        compounds.put(line[0], MnxRefCompound.parse(line));
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
                        MnxRefCompound c = compounds.get(mnxId);
                        if (c == null) {
                            Logger logger = Logger.getLogger(getClass());
                            logger.warn("cound not find MNX id " + line[1]);
                        } else {
                            c.add(Xref.parse(line));
                        }
                    }
                }
            } finally {
                tsv.close();
            }
        }

        return compounds;
    }


    /**
     * Access a compound by id.
     *
     * @param id identifier of the compound
     * @return an optional MnxRefCompound or absent optional
     */
    public Optional<MnxRefCompound> entry(String id) {
        checkArgument(Strings.nullToEmpty(id).startsWith("MNX"),
                      "Identifier should start with MNX");
        return Optional.fromNullable(compounds.get(id));
    }

    /**
     * Access all entries.
     *
     * @return iterable of entries
     */
    public Iterable<MnxRefCompound> entries() {
        return compounds.values();
    }

}
