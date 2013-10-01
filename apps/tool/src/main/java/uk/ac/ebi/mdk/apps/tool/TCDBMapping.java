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

package uk.ac.ebi.mdk.apps.tool;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Map blast results to TCDB using the TCDB mapping file.
 *
 * @author John May
 */
public class TCDBMapping {

    private static final String TCDB_MAPPING_PATH = "/databases/tcdb/tcdb.dr";

    public static void main(String[] args) throws IOException {

        Map<String, String> map = buildMap();

        CSVReader csvIn = new CSVReader(new FileReader(args[0]), '\t', '\0');
        CSVWriter csvOut = new CSVWriter(new FileWriter(args[0] + ".map"), '\t', '\0');
        String[] row = csvIn.readNext();
        Arrays.copyOf(row, row.length + 1);
        row[row.length - 1] = "tcdb_mapping";
        csvOut.writeNext(row);
        while ((row = csvIn.readNext()) != null) {
            String key = row[1].substring(10);
            String desc = map.get(key);
            if (desc == null) {
                System.err.println("Could not find " + key);
            }
            else {
                row = Arrays.copyOf(row, row.length + 1);
                row[row.length - 1] = desc;
                csvOut.writeNext(row);
            }
        }
        csvIn.close();
        csvOut.close();

    }

    private static Map<String, String> buildMap() throws IOException {
        Map<String, String> mapping = new HashMap<String, String>();
        BufferedReader r = new BufferedReader(new FileReader(TCDB_MAPPING_PATH));
        String row;
        while ((row = r.readLine()) != null) {
            String key = row.substring(0, 6);
            mapping.put(key, row);
        }
        r.close();
        return mapping;
    }
}
