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

import com.google.common.collect.Multimap;
import org.apache.lucene.analysis.Analyzer;
import uk.ac.ebi.mdk.domain.identifier.UniProtIdentifier;
import uk.ac.ebi.deprecated.services.RemoteResource;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.prefs.Preferences;
import javax.xml.stream.XMLStreamException;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.io.xml.UniProtAnnoationLoader;
import uk.ac.ebi.deprecated.services.LuceneService;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;

/**
 *          UniProtCrossRefs - 2011.12.10 <br>
 *          Creates a lucene index for the uniprot cross references. Includes EC Numbers.
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class UniProtCrossRefs extends AbstrastRemoteResource implements RemoteResource, LuceneService {

    private static final Logger LOGGER = Logger.getLogger(UniProtCrossRefs.class);
    private static final String location = "ftp://ftp.ebi.ac.uk/pub/databases/uniprot/current_release/knowledgebase/complete/uniprot_trembl.xml.gz";
    private Analyzer analzyer;

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
    
    private void init() {
        this.analzyer = new KeywordAnalyzer();
    }

    public enum UniprotCrossRefsLuceneFields {

        UniprotAcc, ExtDB, ExtID;
    }

    public UniProtCrossRefs() {
        super(location, getFile());
        init();
    }

    public void update() throws IOException {
        LinkedList<Document> docs = new LinkedList();
        try {
            UniProtAnnoationLoader loader = new UniProtAnnoationLoader();
            loader.update();
            Multimap<UniProtIdentifier, Identifier> map = loader.getMap();
            for (UniProtIdentifier uniProtIdentifier : map.keySet()) {
                for (Identifier identifier : map.get(uniProtIdentifier)) {
                    if(identifier instanceof Taxonomy)
                        continue;
                    Document doc = new Document();
                    doc.add(new Field(UniprotCrossRefsLuceneFields.UniprotAcc.toString(), uniProtIdentifier.getAccession(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(UniprotCrossRefsLuceneFields.ExtDB.toString(), identifier.getShortDescription(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(UniprotCrossRefsLuceneFields.ExtID.toString(), identifier.getAccession(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    docs.add(doc);
                }
            }
        } catch (XMLStreamException e) {
            LOGGER.error("Problems parsing uniprot XML:", e);
        }

        // write the index
        Directory index = new SimpleFSDirectory(getLocal());
        IndexWriter writer = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_34, new KeywordAnalyzer()));
        writer.addDocuments(docs);
        writer.close();
        index.close();

    }

    public static File getFile() {
        String defaultFile = System.getProperty("user.home")
                + File.separator + "databases"
                + File.separator + "indexes"
                + File.separator + "uniprot-crossref";
        Preferences prefs = Preferences.userNodeForPackage(UniProtCrossRefs.class);
        return new File(prefs.get("uniprot.crossrefs.path", defaultFile));
    }

    public String getDescription() {
        return "Uniprot Cross-references";
    }
    
    public static void main(String[] args) throws MalformedURLException, IOException {
        new UniProtCrossRefs().update();
    }
}
