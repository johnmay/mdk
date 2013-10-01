package uk.ac.ebi.mdk.apps.tool;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Simple program to filter the blast results + mappings produced by TCDB
 * mapping to give a shortened list potential transporters.
 *
 * @author John May
 */
public class TCDBResultFilter {

    private static final int BIT_SCORE_IDX = 11;
    private static final int THRESHOLD     = 200;

    public static void main(String[] args) throws IOException {
        CSVReader csvIn = new CSVReader(new FileReader(args[0]), '\t', '\0');
        CSVWriter csvOut = new CSVWriter(new FileWriter(args[0] + ".fltr"), '\t', '\0');
        String[] row = csvIn.readNext();
        Arrays.copyOf(row, row.length + 1);
        csvOut.writeNext(row);
        while ((row = csvIn.readNext()) != null) {
            double bitScore = Double.parseDouble(row[BIT_SCORE_IDX]);
            if (bitScore > 200) {
                csvOut.writeNext(row);
            }
        }
        csvIn.close();
        csvOut.close();
    }
}
