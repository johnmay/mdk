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

package uk.ac.ebi.mdk.io.text.seed;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.Sets;

import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Input of model-SEED reactions from 'tsv' file. From the seed viewer download
 * the file with the 'export table' button.
 *
 * @author John May
 */
public final class ModelSeedReactionInput implements Closeable {

    private final CSVReader csv;
    private static final int
            ID         = 0,
            NAME       = 1,
            EQUATION   = 2,
            ROLES      = 3,
            SUBSYSTEMS = 4,
            ENZYME     = 6,
            KEGG_ID    = 7;

    /**
     * Internal construction.
     *
     * @param r reader
     * @throws IOException low-level io error
     */
    ModelSeedReactionInput(Reader r) throws IOException {
        this.csv = new CSVReader(checkNotNull(r, "no reader provided"),
                                 '\t', '\0');
        csv.readNext(); // headers
    }

    /**
     * Read a model-SEED reaction from the reactions file.
     *
     * @return model-SEED reaction (null if none)
     * @throws IOException low-level io exception
     */
    private ModelSeedReaction read() throws IOException {

        String[] row = csv.readNext();
        if (row == null)
            return null;

        String id = row[ID];
        String name = row[NAME];
        String equation = row[EQUATION];
        String enzyme = row[ENZYME];
        String keggId = row[KEGG_ID];

        Set<String> roles = Sets.newHashSet(row[ROLES].trim()
                                                      .split("<br><br>"));
        Set<String> subSystems = Sets.newHashSet(row[SUBSYSTEMS]
                                                         .trim()
                                                         .split("\\|"));
        roles.remove("None");
        subSystems.remove("None");
        return new ModelSeedReaction(id, name, equation, enzyme,
                                     keggId, subSystems, roles);

    }

    /** @inheritDoc */
    @Override public void close() throws IOException {
        if (csv != null)
            csv.close();
    }

    /** Read all reactions from a file at the specified path. */
    public static List<ModelSeedReaction> fromPath(String path) throws
                                                                IOException {
        return from(new FileReader(path));
    }

    /** Read all reactions from a stream. */
    public static List<ModelSeedReaction> fromStream(InputStream in) throws
                                                                     IOException {
        return from(new InputStreamReader(in));
    }

    /** Read all reactions from a reader. */
    public static List<ModelSeedReaction> from(Reader reader) throws
                                                              IOException {
        List<ModelSeedReaction> rxns = new ArrayList<ModelSeedReaction>();
        ModelSeedReactionInput in = new ModelSeedReactionInput(reader);
        try {
            ModelSeedReaction rxn = null;
            while ((rxn = in.read()) != null) {
                rxns.add(rxn);
            }
        } finally {
            in.close();
        }
        return rxns;
    }
}
