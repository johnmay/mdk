/**
 * ChEBISearch.java
 *
 * 2011.10.25
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.io.remote;

import au.com.bytecode.opencsv.CSVReader;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.prefs.Preferences;

import uk.ac.ebi.interfaces.services.LuceneService;
import uk.ac.ebi.interfaces.services.RemoteResource;
import uk.ac.ebi.resource.DefaultIdentifierFactory;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.*;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.*;
import org.apache.lucene.util.Version;

/**
 *          ChEBISearch - 2011.10.25 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @deprecated  use ChEBIDataLoader in the chemet-service module
 */
@Deprecated
public class ChEBIChemicalData
        extends AbstrastRemoteResource
        implements LuceneService, RemoteResource {

    private static final Logger LOGGER = Logger.getLogger(ChEBIChemicalData.class);
    private Analyzer analzyer;
    private static final String location = "ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/chemical_data.tsv";
    private static final DefaultIdentifierFactory FACTORY = DefaultIdentifierFactory.getInstance();

    public ChEBIChemicalData() {
        super(location, getFile());
        analzyer = new KeywordAnalyzer();
    }

    public static void main(String[] args) throws IOException {
        new ChEBIChemicalData().update();
    }

    public void update() throws IOException {

        CSVReader reader = new CSVReader(new InputStreamReader(getRemote().openStream()), '\t', '\0');



        String[] row = reader.readNext();
        Map<String, Integer> map = createMap(row);

        Map<String, Document> docs = new HashMap();

        while ((row = reader.readNext()) != null) {

            String id = row[map.get("COMPOUND_ID")];
            String type = row[map.get("TYPE")];
            String data = row[map.get("CHEMICAL_DATA")];


            Document doc = docs.get(id);
            if (doc == null) {
                doc = new Document();
                doc.add(new Field("Id", "CHEBI:" + id, Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
                docs.put(id, doc);
            }

            if (type.equals("FORMULA")) {
                if (!data.equals(".")) {
                    doc.add(new Field("Formula", data, Field.Store.YES, Field.Index.ANALYZED));
                }
            } else if (type.equals("CHARGE")) {
                doc.add(new Field("Charge", data, Field.Store.YES, Field.Index.ANALYZED));
            }

        }
        reader.close();

        // get the cross references (need to resolve the ChEBI Seconday IDs)
        Map<String, String> internalXref = ChEBICrossRefs.getSecondaryToParentID();
        Multimap<String, String> internvalXrefInv = HashMultimap.create();

        for (Entry<String, String> entry : internalXref.entrySet()) {
            internvalXrefInv.put(entry.getValue(), entry.getKey());
        }

        // normalise chemical data accross secondary ids
        List<String> keys = new ArrayList(docs.keySet());
        for (String id : keys) {
            String oid = internalXref.get(id);

            if (oid != null) {

                Document docI = docs.get(id);
                Document docO = docs.get(oid);

                if (docO == null) {
                    docO = new Document();
                    docO.add(new Field("Id", "CHEBI:" + oid, Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
                    docs.put(oid, docO);
                }

                merge(docI, docO, "Id");

            } else {

                for (String other : internvalXrefInv.get(id)) {

                    Document docI = docs.get(id);
                    Document docO = docs.get(other);

                    if (docO == null) {
                        docO = new Document();
                        docO.add(new Field("Id", "CHEBI:" + other, Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
                        docs.put(other, docO);
                    }

                    merge(docI, docO, "Id");
                }

            }

        }
// write the index
        Directory index = new SimpleFSDirectory(getLocal());
        IndexWriter writer = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_34, analzyer));
        writer.addDocuments(docs.values());
        writer.close();
        index.close();

    }

    public Analyzer getAnalzyer() {
        return analzyer;
    }

    public Directory getDirectory() {
        try {
            return new SimpleFSDirectory(getLocal());
        } catch (IOException ex) {
            throw new UnsupportedOperationException("Index can not fail to open! unsupported");
        }
    }

    private Map<String, Integer> createMap(String[] row) {
        Map<String, Integer> map = new HashMap();
        for (int i = 0; i < row.length; i++) {
            map.put(row[i], i);
        }
        return map;
    }

    private static File getFile() {
        String defaultFile = System.getProperty("user.home")
                             + File.separator + "databases"
                             + File.separator + "indexes"
                             + File.separator + "chebi-chemical-data";
        Preferences prefs = Preferences.userNodeForPackage(ChEBIChemicalData.class);






        return new File(prefs.get("chebi.chemicaldata.path", defaultFile));
    }

    public String getDescription() {
        return "ChEBI Chemical Data";
    }
}
