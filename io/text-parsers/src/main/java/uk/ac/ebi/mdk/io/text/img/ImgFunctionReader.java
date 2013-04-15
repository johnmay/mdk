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

package uk.ac.ebi.mdk.io.text.img;


import au.com.bytecode.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A reader for IMG function tables (tsv).
 *
 * @author John May
 */
public final class ImgFunctionReader {

    private final CSVReader reader;

    ImgFunctionReader(final CSVReader reader) throws IOException {
        this.reader = reader;
        if (!isHeader(reader.readNext())) {
            throw new IOException("first row was not the expected header");
        }
    }

    public static Collection<ImgFunction> fromInputStream(final InputStream in) throws
                                                                                IOException {
        return fromReader(new InputStreamReader(in));
    }

    public static Collection<ImgFunction> fromFile(final File f) throws
                                                                 IOException {
        return fromReader(new FileReader(f));
    }

    public static Collection<ImgFunction> fromReader(final Reader in) throws
                                                                      IOException {
        ImgFunctionReader functionReader = new ImgFunctionReader(new CSVReader(in,
                                                                               '\t', '"'));
        List<ImgFunction> functions = new ArrayList<ImgFunction>(2000);
        String[] row = null;
        while ((row = functionReader.reader.readNext()) != null) {
            functions.add(parse(row));
        }

        return functions;
    }

    /**
     * Parse the row consigning, "IMG Class", "Gene Object Id", "Product"
     * (optional) and "IMG EC" (optional).
     *
     * @param row the table row
     * @return ImgFunction class
     * @throws IOException low-level io exception
     */
    static ImgFunction parse(String[] row) throws IOException {
        String clazz = row[0];
        int geneId = Integer.parseInt(row[1]);
        String product = row.length > 2 ? row[2] : "unknown";
        String ecs = row.length > 3 ? row[3] : "";
        return new ImgFunction(geneId, product.trim(),
                               ecs.isEmpty() ? Collections.<String>emptyList()
                                             : Arrays.asList(ecs.split(", ")));
    }

    /** container class for an IMG Function Entry. */
    static final class ImgFunction {

        private final int id;
        private final String product;
        private final List<String> ecs;

        /**
         * Create the new entry with id, product and ecs.
         *
         * @param id      gene object id
         * @param product name of the product
         * @param ecs     enzyme classification numbers
         */
        ImgFunction(int id, String product, List<String> ecs) {
            this.id = id;
            this.product = product;
            this.ecs = ecs;
        }

        /**
         * Gene Object Id.
         *
         * @return the id
         */
        int id() {
            return id;
        }

        /**
         * Product.
         *
         * @return the product
         */
        String product() {
            return product;
        }

        /**
         * IMG ECs.
         *
         * @return list of ECs.
         */
        List<String> ec() {
            return ecs;
        }

        @Override public String toString() {
            return id + ": " + product + "(" + ecs + ")";
        }
    }

    /**
     * Check whether the provided row is the expected header.
     *
     * @param row row
     * @return whether the row is the header
     */
    static boolean isHeader(final String[] row) {
        if (row.length != 4)
            return false;
        if (!"IMG Class".equals(row[0]))
            return false;
        if (!"Gene Object ID".equals(row[1]))
            return false;
        if (!"Gene Product Name".equals(row[2]))
            return false;
        if (!"IMG EC".equals(row[3]))
            return false;
        return true;
    }

}
