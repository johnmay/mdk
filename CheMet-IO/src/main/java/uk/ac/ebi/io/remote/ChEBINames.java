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

import uk.ac.ebi.interfaces.services.RemoteResource;
import au.com.bytecode.opencsv.CSVReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import uk.ac.ebi.interfaces.services.LuceneService;

/**
 *          ChEBISearch - 2011.10.25 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @deprecated replaced by ChEBINameLoader in the service package
 */
@Deprecated
public class ChEBINames
        extends AbstrastRemoteResource
        implements LuceneService, RemoteResource {
    
    private static final Logger LOGGER = Logger.getLogger(ChEBINames.class);
    private Analyzer analzyer;
    private static final String location = "ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/names_3star.tsv";
    private static final String locationAllStars = "ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/names.tsv";
    private final String priorityCompounds = "ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/compounds.tsv";
    
    /**
     * The names.tsv file doesn't have the assigned ChEBI name, but the source name from which the name assigned is derived.
     * For the case of different microspecies (different protonation/charges), the name in names.tsv (and surely in other cases
     * as well) is not the most accurate one. The ChEBI name, as it appears in the web page, seems to be the one shown in
     * compounds.tsv if not null. This method provides a map from Chebi numeric ids to the names in compounds.tsv, to override
     * whenever available the ones in names.tsv
     * 
     * @return map of chebi numeric ids to names.
     * @throws IOException 
     */
    private Map<Integer, String> getPriorityNames() throws IOException {
        CSVReader reader = new CSVReader(new InputStreamReader(getRemote(priorityCompounds).openStream()), '\t', '\0');
        String[] row = reader.readNext();
        Map<String, Integer> map = createMap(row);
        
        Map<Integer, String> chebiId2Name = new HashMap<Integer, String>();
        while ((row = reader.readNext()) != null) {
            try {
                int id = Integer.parseInt(row[map.get("ID")]);
                String name = row[map.get("NAME")];
                
                if (name == null || name.equalsIgnoreCase("null")) {
                    continue;
                }
                
                chebiId2Name.put(id, name);
            } catch (NumberFormatException e) {
                LOGGER.warn("Error in source file, skipping line for supposed id : " + row[map.get("ID")]);
            }
        }
        reader.close();
        return chebiId2Name;
    }
    
    public enum ChEBINameLuceneFields {

        id, name, type;
    }

    /**
     * Constructor uses by default 3 stars.
     * 
     */
    public ChEBINames() {
        super(location, getFile());
        analzyer = new KeywordAnalyzer();
    }

    /**
     * If given true, it will use the 3 stars location, else, it will use the file containing the all stars.
     * 
     * @param only3Stars 
     */
    public ChEBINames(Boolean only3Stars) {
        super(getFile());
        if (only3Stars) {
            super.setRemote(location);
        } else {
            super.setRemote(locationAllStars);
        }
        analzyer = new KeywordAnalyzer();
    }
    
    public void update() throws IOException {
        Map<Integer, String> chebiIds2PriorityName = getPriorityNames();
        CSVReader reader = new CSVReader(new InputStreamReader(getRemote().openStream()), '\t', '\0');
        
        String[] row = reader.readNext();
        Map<String, Integer> map = createMap(row);

        //Map<Integer, Document> docs = new HashMap();
        List<Document> docs = new ArrayList<Document>();
        String currentId = "";
        
        while ((row = reader.readNext()) != null) {
            int id = Integer.parseInt(row[map.get("COMPOUND_ID")]);
            String name = row[map.get("NAME")];
            String type = row[map.get("TYPE")];
            // if we found a proper name, see if we can override it with names from chebiIds2priorityName.
            if(type.equalsIgnoreCase("name")) {
                if(chebiIds2PriorityName.containsKey(id))
                    name = chebiIds2PriorityName.get(id);
            }
            //Document doc = docs.get(id);
            //if (doc == null) {
            Document doc = new Document();
            doc.add(new Field(ChEBINameLuceneFields.id.toString(), "CHEBI:" + id, Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
            doc.add(new Field(ChEBINameLuceneFields.type.toString(), type, Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field(ChEBINameLuceneFields.name.toString(), name, Field.Store.YES, Field.Index.ANALYZED));
            docs.add(doc);
            //}
            
        }
        reader.close();

        // write the index
        Directory index = new SimpleFSDirectory(getLocal());
        IndexWriter writer = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_34, analzyer));
        writer.addDocuments(docs);
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
    
    public static void main(String[] args) throws MalformedURLException, IOException {
        new ChEBINames(false).update();
    }
    
    private static File getFile() {
        String defaultFile = System.getProperty("user.home")
                + File.separator + "databases"
                + File.separator + "indexes"
                + File.separator + "chebi-names";
        Preferences prefs = Preferences.userNodeForPackage(ChEBINames.class);
        return new File(prefs.get("chebi.name.path", defaultFile));
    }
    
    public String getDescription() {
        return "ChEBI Names";
    }
}
