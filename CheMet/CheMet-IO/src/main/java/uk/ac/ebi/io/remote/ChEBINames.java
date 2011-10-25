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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

/**
 *          ChEBISearch - 2011.10.25 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ChEBINames extends AbstrastRemoteResource {

    private static final Logger LOGGER = Logger.getLogger(ChEBINames.class);

    public ChEBINames() throws MalformedURLException {
        super(new URL("ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/names_3star.tsv"),
              getFile());
    }

    public void update() throws IOException {
        CSVReader reader = new CSVReader(new InputStreamReader(getRemote().openStream()), '\t', '\0');

        String[] row = reader.readNext();
        Map<String, Integer> map = createMap(row);

        LinkedList<Document> docs = new LinkedList();
        String currentId = "";

        while ((row = reader.readNext()) != null) {
            String id = row[map.get("COMPOUND_ID")];
            String name = row[map.get("NAME")];

            if (currentId.equals(id) == false) {
                docs.add(new Document());
                currentId = id;
            }
            Document doc = docs.get(docs.size() - 1);
            doc.add(new Field("Id", id, Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("Name", name, Field.Store.YES, Field.Index.ANALYZED));
        }
        reader.close();

        // write the index
        Directory index = new SimpleFSDirectory(getLocal());
        IndexWriter writer = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_34, new SimpleAnalyzer(Version.LUCENE_34)));
        writer.addDocuments(docs);
        writer.close();
        index.close();

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
                             + File.separator + "chebi-names";
        Preferences prefs = Preferences.systemNodeForPackage(ChEBINames.class);
        return new File(prefs.get("chebi.name.path", defaultFile));
    }
}
