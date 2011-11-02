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
import java.security.InvalidParameterException;
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
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.interfaces.services.LuceneService;
import uk.ac.ebi.resource.IdentifierFactory;

/**
 *          ChEBISearch - 2011.10.25 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ChEBICrossRefs
        extends AbstrastRemoteResource
        implements LuceneService, RemoteResource {

    private static final Logger LOGGER = Logger.getLogger(ChEBICrossRefs.class);
    private Analyzer analzyer;
    private static final String location = "ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/database_accession.tsv";
    private static final String location3star = "ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/database_accession_3star.tsv";
    private static final IdentifierFactory FACTORY = IdentifierFactory.getInstance();

    public enum ChEBICrossRefsLuceneFields {

        ChebiID, ExtDB, ExtID;
    }

    public ChEBICrossRefs() {
        super(location, getFile());
        analzyer = new KeywordAnalyzer();
    }

    public ChEBICrossRefs(boolean only3star) {
        super(getFile());
        if (only3star) {
            super.setRemote(location3star);
        } else {
            super.setRemote(location);
        }
        analzyer = new KeywordAnalyzer();
    }

    public void update() throws IOException {
        CSVReader reader = new CSVReader(new InputStreamReader(getRemote().openStream()), '\t', '\0');

        String[] row = reader.readNext();
        Map<String, Integer> map = createMap(row);

        List<Document> docs = new ArrayList<Document>();
        String currentId = "";

        while ((row = reader.readNext()) != null) {
            String id = row[map.get("COMPOUND_ID")];
            String type = row[map.get("TYPE")];
            String accession = row[map.get("ACCESSION_NUMBER")];
            Identifier extIdent = null;
            try {
                extIdent = FACTORY.ofSynonym(type);
            } catch (InvalidParameterException e) {
                if (type.equalsIgnoreCase("PubMed citation")) {
                    continue;
                }
                if (type.equalsIgnoreCase("Wikipedia accession")) {
                    continue;
                }
                LOGGER.warn("Could not recognize db: " + type + " skipping.");
                continue;
            }
            extIdent.setAccession(accession);

            Document doc = new Document();

            doc.add(new Field(ChEBICrossRefsLuceneFields.ChebiID.toString(),id,Field.Store.YES,Field.Index.NOT_ANALYZED));
            doc.add(new Field(ChEBICrossRefsLuceneFields.ExtDB.toString(), extIdent.getShortDescription(), Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field(ChEBICrossRefsLuceneFields.ExtID.toString(), extIdent.getAccession(), Field.Store.YES, Field.Index.NOT_ANALYZED));
            docs.add(doc);
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
        new ChEBICrossRefs(false).update();
    }

    private static File getFile() {
        String defaultFile = System.getProperty("user.home")
                + File.separator + "databases"
                + File.separator + "indexes"
                + File.separator + "chebi-crossrefs";
        Preferences prefs = Preferences.userNodeForPackage(ChEBICrossRefs.class);
        return new File(prefs.get("chebi.crossrefs.path", defaultFile));
    }

    public String getDescription() {
        return "ChEBI CrossRefs";
    }
}
