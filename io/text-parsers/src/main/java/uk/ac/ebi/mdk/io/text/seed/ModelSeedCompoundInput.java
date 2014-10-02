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
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Input of model-SEED compounds from 'tsv' file. From the seed viewer download
 * the file with the 'export table' button.
 *
 * @author John May
 */
public final class ModelSeedCompoundInput implements Closeable {

    private final CSVReader csv;
    private static final int
            ID      = 0,
            NAME    = 1,
            FORMULA = 2,
            MASS    = 3,
            KEGG_ID = 5;

    /**
     * Internal construction.
     *
     * @param r reader
     * @throws java.io.IOException low-level io error
     */
    ModelSeedCompoundInput(Reader r) throws IOException {
        this.csv = new CSVReader(checkNotNull(r, "no reader provided"),
                                 '\t', '\0');
        csv.readNext(); // headers
    }

    /**
     * Read a model-SEED compound from the reactions file.
     *
     * @return model-SEED compound (null if none)
     * @throws java.io.IOException low-level io exception
     */
    private ModelSeedCompound read() throws IOException {

        String[] row = csv.readNext();
        if (row == null)
            return null;

        String id = row[ID];
        String names = row[NAME].replaceAll("<br>", " ").trim();
        String formula = row[FORMULA];
        String mass = row[MASS].trim();
        String keggId = row[KEGG_ID].trim();
        List<String> synonyms = Lists.newArrayList(names.split(", "));

        return new ModelSeedCompound(id, synonyms.remove(0),
                                     Sets.newHashSet(synonyms),
                                     formula,
                                     Sets.newHashSet(keggId.split(",")),
                                     mass.isEmpty() ? 0
                                                    : Double.parseDouble(mass));

    }

    /** @inheritDoc */
    @Override public void close() throws IOException {
        if (csv != null)
            csv.close();
    }

    /** Read all compounds from a file at the specified path. */
    public static List<ModelSeedCompound> fromPath(String path) throws
                                                                IOException {
        return from(new FileReader(path));
    }

    /** Read all compounds from a stream. */
    public static List<ModelSeedCompound> fromStream(InputStream in) throws
                                                                     IOException {
        return from(new InputStreamReader(in));
    }

    /** Read all compounds from a reader. */
    public static List<ModelSeedCompound> from(Reader reader) throws
                                                              IOException {
        List<ModelSeedCompound> rxns = new ArrayList<ModelSeedCompound>();
        ModelSeedCompoundInput in = new ModelSeedCompoundInput(reader);
        try {
            ModelSeedCompound rxn = null;
            while ((rxn = in.read()) != null) {
                rxns.add(rxn);
            }
        } finally {
            in.close();
        }
        return rxns;
    }
}
