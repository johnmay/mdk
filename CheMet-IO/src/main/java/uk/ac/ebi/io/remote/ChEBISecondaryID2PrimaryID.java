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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
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
import uk.ac.ebi.resource.DefaultIdentifierFactory;

/**
 *          ChEBISecondaryID2PrimaryID - 2011.12.11 <br>
 *          Creates a Lucene index that allows to map secondary ChEBI identifiers to primary ChEBI Identifiers.
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 */
public class ChEBISecondaryID2PrimaryID
        extends AbstrastRemoteResource
        implements LuceneService, RemoteResource {

    private static final Logger LOGGER = Logger.getLogger(ChEBISecondaryID2PrimaryID.class);
    private Analyzer analzyer;
    private static final String location = "ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/database_accession.tsv";
    private static final DefaultIdentifierFactory FACTORY = DefaultIdentifierFactory.getInstance();

    public enum ChEBISecondary2PrimaryLuceneFields {

        ChebiIDSec, ChebiIDPrim;
    }

    public ChEBISecondaryID2PrimaryID() {
        super(location, getFile());
        analzyer = new KeywordAnalyzer();
    }

    public void update() throws IOException {
        // TODO Method getSecondaryToParentID could be moved to this class and be called statically from this class,
        // which seems more related to the task of resolving secondary to primary links.
        Map<String, String> sec2PrimaryID = ChEBICrossRefs.getSecondaryToParentID();

        List<Document> docs = new ArrayList<Document>();

        for (String chebiIDSec : sec2PrimaryID.keySet()) {

            String chebiIDPrim = sec2PrimaryID.get(chebiIDSec);
            Document doc = new Document();

            doc.add(new Field(ChEBISecondary2PrimaryLuceneFields.ChebiIDSec.toString(), chebiIDSec, Field.Store.YES, Field.Index.NOT_ANALYZED));
            doc.add(new Field(ChEBISecondary2PrimaryLuceneFields.ChebiIDPrim.toString(), chebiIDPrim, Field.Store.YES, Field.Index.NOT_ANALYZED));
            docs.add(doc);

        }

        // write the index
        Directory index = new SimpleFSDirectory(getLocal());
        IndexWriter writer = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_34, analzyer));
        writer.addDocuments(docs);
        writer.close();
        index.close();
        docs.clear();
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
    
    
    public static void main(String[] args) throws MalformedURLException, IOException {
        new ChEBISecondaryID2PrimaryID().update();
    }
    

    private static File getFile() {
        String defaultFile = System.getProperty("user.home")
                + File.separator + "databases"
                + File.separator + "indexes"
                + File.separator + "chebi-sec2prim";
        Preferences prefs = Preferences.userNodeForPackage(ChEBISecondaryID2PrimaryID.class);
        return new File(prefs.get("chebi.secondary2primary.path", defaultFile));
    }

    public String getDescription() {
        return "ChEBI Secondary to primary resolver";
    }
}
