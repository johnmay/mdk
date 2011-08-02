/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.parser.hmdb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author pmoreno
 */
public class HMDBReader extends BufferedReader {

    private HMDBEntry entry;
    private final String NA = "Not Available";
    private HMDBTissueFreeText2OntologyID tissueFreeText2OntologyID;

    public HMDBReader(FileReader hmdbMetaboCardReader) {
        super(hmdbMetaboCardReader);
    }

    public HMDBEntry readEntry() throws IOException {
        String line = super.readLine();
        if (line == null) {
            return null;
        }
        entry = new HMDBEntry();
        while (line != null) {
            if (line.startsWith("#BEGIN_METABOCARD")) {
                line = super.readLine();
                continue;
            }
            if (line.startsWith("# ")) {
                String header = line.replace("# ", "");
                line = super.readLine();
                this.parseLine(header, line);
                line = super.readLine();
                continue;
            }
            if (line.startsWith("#END_METABOCARD")) {
                line = super.readLine();
                break;
            }
            line = super.readLine();

        }
        return entry;
    }

    private void parseLine(String header, String line) {

        if (header.startsWith("hmdb_id:")) {
            entry.setHmdb_id(line);
        } else if (header.startsWith("name:")) {
            entry.setName(line);
        } else if (header.startsWith("synonyms:")) {
            String tokens[] = line.split("; ");
            for (String syn : tokens) {
                entry.addSynonyms(syn);
            }
        } else if (header.startsWith("chebi_id:") && !line.startsWith(this.NA)) {
            entry.setChebiID(line);
        } else if (header.startsWith("biocyc_id:") && !line.startsWith(this.NA)) {
            entry.setBiocycID(line);
        } else if (header.startsWith("cas_number:") && !line.startsWith(this.NA)) {
            entry.setCas(line);
        } else if (header.startsWith("iupac:") && !line.startsWith(this.NA)) {
            entry.setIupacName(line);
        } else if (header.startsWith("metlin_id:") && !line.startsWith(this.NA)) {
            entry.setMetlinID(line);
        } else if (header.startsWith("pubchem_compound_id:") && !line.startsWith(this.NA)) {
            entry.setPubChemID(line);
        } else if (header.startsWith("tissue_location") && !line.startsWith(this.NA)) {
            entry.addTissue(line);
        }
    }


}
