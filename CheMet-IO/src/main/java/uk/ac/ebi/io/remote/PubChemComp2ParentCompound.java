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
import uk.ac.ebi.chemet.resource.chemical.PubChemCompoundIdentifier;
import uk.ac.ebi.deprecated.services.RemoteResource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NoSuchDirectoryException;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import uk.ac.ebi.chemet.ws.exceptions.WebServiceException;
import uk.ac.ebi.deprecated.services.LuceneService;
import uk.ac.ebi.metabolomes.webservices.EUtilsWebServiceConnection;

/**
 *          PubChemCompoundCrossRefs - 2011.10.25 <br>
 *          This class is slightly different to the other ones of its class in the sense that it doesn't build an index
 *          for the entire database of PubChem compounds, but only for a provided set. When run with an existent index,
 *          it should only add new cross references (but this is not currently working, as that part is commented for some reason). 
 *          This should not be executed with more than 5000 records at once, 
 *          which is the suggested
 *          record limit for ELink at NCBI (ELink is part of the EUtils).
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 */
public class PubChemComp2ParentCompound extends AbstrastRemoteResource implements LuceneService, RemoteResource {

    private static final Logger LOGGER = Logger.getLogger(PubChemComp2ParentCompound.class);
    private static final String location = "http://pubchem.ncbi.nlm.nih.gov/"; // just for reference, this is not being used in this impl.
    private final List<PubChemCompoundIdentifier> pubChemCompoundIdentifiers = new ArrayList<PubChemCompoundIdentifier>();
    private Analyzer analzyer;
    private IndexSearcher searcher;

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

    private boolean foundInIndex(String compoundID, String parentID) {
        if (searcher != null) {
            Query qComp = new TermQuery(new Term(PubChemComp2ParentComp.PubChemCompID.toString(), compoundID));
            Query qParent = new TermQuery(new Term(PubChemComp2ParentComp.ParentPChemCompID.toString(), parentID));

            BooleanQuery query = new BooleanQuery();
            query.add(qComp, Occur.MUST);
            query.add(qParent, Occur.MUST);

            TopScoreDocCollector collector = TopScoreDocCollector.create(1, true);
            try {
                searcher.search(query, collector);
                ScoreDoc[] hits = collector.topDocs().scoreDocs;
                return hits.length > 0;
            } catch (IOException e) {
                LOGGER.warn("Could not search index");
            }
        }
        return false;
    }

    private void closeSearcher() throws IOException {
        if (searcher != null) {
            searcher.close();
        }
    }

    public enum PubChemComp2ParentComp {

        PubChemCompID, ParentPChemCompID;
    }

    /**
     * This class will take a list of pchem compound ids (not more than 5000 at a time, due to ELink restrictions), get
     * the associated pchem substances
     * @param pchemIds 
     */
    public PubChemComp2ParentCompound(List<PubChemCompoundIdentifier> pchemIds) {
        super(location, getFile());
        this.pubChemCompoundIdentifiers.addAll(pchemIds);
        init();
    }

    public PubChemComp2ParentCompound() {
        super(location, getFile());
        init();
    }

    private void init() {
        this.analzyer = new KeywordAnalyzer();
    }

    public void update() throws IOException {
        LinkedList<Document> docs = new LinkedList();

        //PubChemWebServiceConnection pcwsc = new PubChemWebServiceConnection();
        EUtilsWebServiceConnection euwsc = new EUtilsWebServiceConnection();

        Directory index = getDirectory();
        try {
            if (index != null) {
                searcher = new IndexSearcher(index);
            }
        } catch (NoSuchDirectoryException e) {
            // the directory doesn't exist yet.
            searcher = null;
        }

        try {
            // We retrieve the mapping from compound ids to substance ids.
            Multimap<String, String> comp2parent = euwsc.getPubChemCompoundParentFromPubChemCompoundIdents(pubChemCompoundIdentifiers);
            LOGGER.info("Retrieved " + comp2parent.keySet().size() + " compounds and " + comp2parent.values().size() + " parents associated..");
            Set<String> uniqueComps = comp2parent.keySet();


            List<String> compounds = new ArrayList<String>(uniqueComps);
            Map<String, String> comp2Parent2Add = new HashMap<String, String>();

            for (String compoundID : compounds) {
                for (String parentID : comp2parent.get(compoundID)) {
                    if (parentID.equals(compoundID)) {
                        continue;
                    }

                    if (!foundInIndex(compoundID, parentID)) {
                        comp2Parent2Add.put(compoundID, parentID);
                    }
                }
            }
            closeSearcher();

            IndexWriter writer = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_34, getAnalzyer()));
            LOGGER.info("Compounds selected for writting new parent relations: " + comp2Parent2Add.keySet().size());
            for (String pccompId : comp2Parent2Add.keySet()) {
                String parent = comp2Parent2Add.get(pccompId);
                PubChemCompoundIdentifier pccompIdent = new PubChemCompoundIdentifier(pccompId);
                PubChemCompoundIdentifier pccompParentIdent = new PubChemCompoundIdentifier(parent);
                LOGGER.info("Adding: PubChemComp:" + pccompIdent.getAccession() + " parent:" + pccompParentIdent.getAccession());
                Document doc = new Document();
                doc.add(new Field(PubChemComp2ParentComp.PubChemCompID.toString(), pccompIdent.getAccession(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                doc.add(new Field(PubChemComp2ParentComp.ParentPChemCompID.toString(), pccompParentIdent.getAccession(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                docs.add(doc);

            }
            LOGGER.info("Finished writing " + comp2Parent2Add.size() + " parent links to index.");
            // write the index
            writer.addDocuments(docs);
            writer.close();

            index.close();
        } catch (IOException e) {
            LOGGER.error("Problems reading the ouput of the PubChem web service", e);
            index.close();
        } catch (WebServiceException w) {
            LOGGER.error("Problems in retrieving the output from PubChem web service", w);
            index.close();
        }

    }

    public static File getFile() {
        String defaultFile = System.getProperty("user.home")
                + File.separator + "databases"
                + File.separator + "indexes"
                + File.separator + "pubchem-compound-toParent";
        Preferences prefs = Preferences.userNodeForPackage(PubChemComp2ParentCompound.class);
        return new File(prefs.get("pubchemCompound.comp2parent.path", defaultFile));
    }

    public String getDescription() {
        return "PubChem Compound Parent Resolver";
    }
}
