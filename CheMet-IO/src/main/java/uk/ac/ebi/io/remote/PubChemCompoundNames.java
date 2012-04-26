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

import org.apache.lucene.analysis.Analyzer;
import uk.ac.ebi.chemet.resource.chemical.PubChemCompoundIdentifier;
import uk.ac.ebi.deprecated.services.RemoteResource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.deprecated.services.LuceneService;
import uk.ac.ebi.metabolomes.webservices.EUtilsWebServiceConnection;
import uk.ac.ebi.metabolomes.webservices.eutils.PubChemNamesResult;

/**
 *          PubChemCompoundNames - 2011.10.25 <br>
 *          This class is slightly different to the other ones of its class in the sense that it doesn't build an index
 *          for the entire database of PubChem compounds, but only for a provided set. This should not be executed with 
 *          more than 5000 records at once, which is the suggested
 *          record limit for ELink at NCBI (ELink is part of the EUtils). A class for running the update, with a reasonable
 *          collection of PubChem compound identifiers, can be found in uk.ac.ebi.metabolomes.exec in the MetabolomeInference
 *          maven project.
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 */
public class PubChemCompoundNames extends AbstrastRemoteResource implements LuceneService, RemoteResource {

    private static final Logger LOGGER = Logger.getLogger(PubChemCompoundNames.class);
    private static final String location = "http://pubchem.ncbi.nlm.nih.gov/"; // just for reference, this is not being used in this impl.
    private final List<PubChemCompoundIdentifier> pubChemCompoundIdentifiers = new ArrayList<PubChemCompoundIdentifier>();
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

    public enum PCCompNamesLuceneFields {

        PubChemCompID, Type, Name;
    }
    
    public enum PCCompNameTypes {
        Synonym, Name, IUPACName;
    }

    /**
     * This class will take a list of pchem compound ids (not more than 5000 at a time, due to ELink restrictions), get
     * the associated pchem substances
     * @param pchemIds 
     */
    public PubChemCompoundNames(List<PubChemCompoundIdentifier> pchemIds) {
        super(location, getFile());
        this.pubChemCompoundIdentifiers.addAll(pchemIds);
        init();
    }

    public PubChemCompoundNames() {
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
            PubChemNamesResult namesRes = euwsc.getNamesForPubChemCompoundIdentifiers(pubChemCompoundIdentifiers);
           
            IndexWriter writer = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_34, getAnalzyer()));
            LOGGER.info("Writing compound names: ");
            for (String pccompId : namesRes.getCompoundIds()) {
                Identifier pccompIdent = new PubChemCompoundIdentifier(pccompId); 
                for (String synonym : namesRes.getSynonyms(pccompId)) {
                    //LOGGER.info("Adding synonyms: PubChemComp:" + pccompIdent.getAccession());
                    Document doc = new Document();
                    doc.add(new Field(PCCompNamesLuceneFields.PubChemCompID.toString(), pccompIdent.getAccession(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(PCCompNamesLuceneFields.Type.toString(), PCCompNameTypes.Synonym.toString(), Field.Store.YES, Field.Index.ANALYZED));
                    doc.add(new Field(PCCompNamesLuceneFields.Name.toString(), synonym, Field.Store.YES, Field.Index.ANALYZED));
                    docs.add(doc);
                }
                String iupacName = namesRes.getIUPACName(pccompId);
                if(iupacName!=null) {
                    Document doc = new Document();
                    doc.add(new Field(PCCompNamesLuceneFields.PubChemCompID.toString(),pccompId,Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(PCCompNamesLuceneFields.Type.toString(),PCCompNameTypes.IUPACName.toString(),Field.Store.YES,Field.Index.ANALYZED));
                    doc.add(new Field(PCCompNamesLuceneFields.Name.toString(),iupacName, Field.Store.YES, Field.Index.ANALYZED));
                    docs.add(doc);
                }
                String preferredName = namesRes.getPreferredName(pccompId);
                if(preferredName!=null) {
                    Document doc = new Document();
                    doc.add(new Field(PCCompNamesLuceneFields.PubChemCompID.toString(),pccompId,Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(PCCompNamesLuceneFields.Type.toString(),PCCompNameTypes.Name.toString(),Field.Store.YES,Field.Index.ANALYZED));
                    doc.add(new Field(PCCompNamesLuceneFields.Name.toString(),preferredName, Field.Store.YES, Field.Index.ANALYZED));
                    docs.add(doc);
                }
            }
            LOGGER.info("Finished writing names to index.");
            // write the index
            writer.addDocuments(docs);
            writer.close();

            index.close();
        } catch (IOException e) {
            LOGGER.error("Problems reading the ouput of the PubChem web service", e);
            index.close();
        } 

    }

    public static File getFile() {
        String defaultFile = System.getProperty("user.home")
                + File.separator + "databases"
                + File.separator + "indexes"
                + File.separator + "pubchem-compound-names";
        Preferences prefs = Preferences.userNodeForPackage(PubChemCompoundNames.class);
        return new File(prefs.get("pubchemCompound.names.path", defaultFile));
    }

    public String getDescription() {
        return "PubChem Compound Names";
    }

    private boolean isCrossRefInIndex(IndexSearcher searcher, PubChemCompoundIdentifier identPCComp, String name, PCCompNameTypes type) throws IOException {
        Query queryExtDB = new TermQuery(new Term(PCCompNamesLuceneFields.Name.toString(), name));
        Query queryExtID = new TermQuery(new Term(PCCompNamesLuceneFields.Type.toString(), type.toString()));
        Query queryPubChemComp = new TermQuery(new Term(PCCompNamesLuceneFields.PubChemCompID.toString(), identPCComp.getAccession()));
        BooleanQuery query = new BooleanQuery();
        query.add(queryExtDB, BooleanClause.Occur.MUST);
        query.add(queryExtID, BooleanClause.Occur.MUST);
        query.add(queryPubChemComp, BooleanClause.Occur.MUST);
        TopDocs topDocs = searcher.search(query, 1); // this search shouldn't have more than one hit normally.

        ScoreDoc[] scoreDosArray = topDocs.scoreDocs;
        return scoreDosArray.length > 0;

    }
}
