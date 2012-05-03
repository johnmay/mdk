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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.lucene.analysis.Analyzer;
import uk.ac.ebi.chemet.resource.chemical.ChemSpiderIdentifier;
import uk.ac.ebi.chemet.resource.chemical.PubChemCompoundIdentifier;
import uk.ac.ebi.deprecated.services.RemoteResource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.chemet.ws.exceptions.WebServiceException;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
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
public class PubChemCompoundCrossRefs extends AbstrastRemoteResource implements LuceneService, RemoteResource {

    private static final Logger LOGGER = Logger.getLogger(PubChemCompoundCrossRefs.class);
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

    public enum PubChemCompoundsCrossRefsLuceneFields {

        PubChemCompID, ExtDB, ExtID;
    }

    /**
     * This class will take a list of pchem compound ids (not more than 5000 at a time, due to ELink restrictions), get
     * the associated pchem substances
     * @param pchemIds 
     */
    public PubChemCompoundCrossRefs(List<PubChemCompoundIdentifier> pchemIds) {
        super(location, getFile());
        this.pubChemCompoundIdentifiers.addAll(pchemIds);
        init();
    }

    public PubChemCompoundCrossRefs() {
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
            // We retrieve the mapping from compound ids to substance ids.
            Multimap<String, String> comp2subs = euwsc.getPubChemSubstanceFromPubChemCompoundIdents(pubChemCompoundIdentifiers);
            LOGGER.info("Retrieved " + comp2subs.keySet().size() + " compounds and " + comp2subs.values().size() + " substances associated..");
            Set<String> uniqueSubs = new HashSet<String>();
            uniqueSubs.addAll(comp2subs.values());
            Multimap<String, CrossReference> uniquePChemCompID2CrossRef = HashMultimap.create();

            List<String> substances = new ArrayList<String>(uniqueSubs);
            int step = 5000;
            for (int i = 0; i < substances.size(); i += step) {
                // we submit the unique list of substance ids to download the SDF (which is shorter than the XML apparently)
                List<String> subsToDownload = substances.subList(i, Math.min(substances.size(), i + step));


                //--  InputStream in = pcwsc.downloadFile((String[]) (subsToDownload.toArray(new String[subsToDownload.size()])), FormatType.eFormat_SDF, PCIDType.eID_SID);
                LOGGER.info("Retrieved getting external identifiers for " + subsToDownload.size() + " different substances...");
                Multimap<String, CrossReference> subs2CrossRefs = euwsc.getExternalIdentifiersForPubChemSubstances(subsToDownload);
                //--  SDF2MolFiles sdfmf = new SDF2MolFiles(in, null, null, "> <PUBCHEM_SUBSTANCE_ID>");
                //-- PubChemSubstanceSDFFieldExtractor sDFFieldExtractor = new PubChemSubstanceSDFFieldExtractor();
                //-- sdfmf.setFieldExtractor(sDFFieldExtractor);
                // we use the SDF2MolFiles to go through the SDF, using the SDFFieldExtractor to get the annotations
                // no Mol files are written as we don't give the 
                //-- sdfmf.splitSDFIntoMolFilesForIDs(null);
                //-- in.close();
                /*
                IndexSearcher searcher = null;
                boolean indexExists = false;
                try {
                if (index.listAll().length > 0) {
                searcher = new IndexSearcher(index);
                indexExists = true;
                }
                } catch (NoSuchDirectoryException e) {
                LOGGER.warn("The index doesn't exist.. we just write everything");
                }*/

                //LOGGER.info("Searching found cross refs in exiting index if any...");
                Multimap<PubChemCompoundIdentifier, CrossReference> crossRefsToAddNotInIndex = HashMultimap.create();
                for (String pubchemCompID : comp2subs.keySet()) {
                    Set<CrossReference> uniqueCrossRefs = new HashSet<CrossReference>();
                    //PubChemCompoundIdentifier identPCComp = new PubChemCompoundIdentifier(pubchemCompID);
                    for (String substanceID : comp2subs.get(pubchemCompID)) {
                        //-- SDFRecord rec = sDFFieldExtractor.getRecordFor(substanceID);
                        //-- uniqueCrossRefs.addAll(rec.getCrossRefs());
                        uniqueCrossRefs.addAll(subs2CrossRefs.get(substanceID));
                    }
                    for (CrossReference crossReference : uniqueCrossRefs) {
                        if(crossReference.getIdentifier() instanceof ChemSpiderIdentifier)
                            continue;
                        uniquePChemCompID2CrossRef.put(pubchemCompID, crossReference);
                        /*
                        if (!indexExists || !isCrossRefInIndex(searcher, identPCComp, crossReference.getIdentifier())) {
                        crossRefsToAddNotInIndex.put(identPCComp, crossReference);
                        } else if (indexExists && isCrossRefInIndex(searcher, identPCComp, crossReference.getIdentifier())) {
                        LOGGER.info("Existing entry " + identPCComp.getAccession() + " ExtDB:" + crossReference.getIdentifier().getShortDescription()
                        + " ID:" + crossReference.getIdentifier().getAccession());
                        }*/
                    }
                }
                /*if (indexExists) {
                searcher.close();
                }
                
                IndexWriter writer = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_34, getAnalzyer()));
                LOGGER.info("Compounds selected for writting new cross refs: " + crossRefsToAddNotInIndex.keySet().size());
                for (PubChemCompoundIdentifier pccompIdent : crossRefsToAddNotInIndex.keySet()) {
                for (CrossReference crossReference : crossRefsToAddNotInIndex.get(pccompIdent)) {
                Identifier crIdent = crossReference.getIdentifier();
                LOGGER.info("Adding: PubChemComp:" + pccompIdent.getAccession() + " ExtDB:" + crIdent.getShortDescription() + " ExtID:" + crIdent.getAccession());
                Document doc = new Document();
                doc.add(new Field(PubChemCompoundsCrossRefsLuceneFields.PubChemCompID.toString(), pccompIdent.getAccession(), Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field(PubChemCompoundsCrossRefsLuceneFields.ExtDB.toString(), crIdent.getShortDescription(), Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field(PubChemCompoundsCrossRefsLuceneFields.ExtID.toString(), crIdent.getAccession(), Field.Store.YES, Field.Index.ANALYZED));
                docs.add(doc);
                }
                }
                LOGGER.info("Finished writing " + crossRefsToAddNotInIndex.size() + " cross refs to index.");
                // write the index
                writer.addDocuments(docs);
                writer.close();*/
            }

            IndexWriter writer = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_34, getAnalzyer()));
            LOGGER.info("Compounds selected for writting new cross refs: " + uniquePChemCompID2CrossRef.keySet().size());
            for (String pccompId : uniquePChemCompID2CrossRef.keySet()) {
                Identifier pccompIdent = new PubChemCompoundIdentifier(pccompId); 
                for (CrossReference crossReference : uniquePChemCompID2CrossRef.get(pccompId)) {
                    Identifier crIdent = crossReference.getIdentifier();
                    LOGGER.info("Adding: PubChemComp:" + pccompIdent.getAccession() + " ExtDB:" + crIdent.getShortDescription() + " ExtID:" + crIdent.getAccession());
                    Document doc = new Document();
                    doc.add(new Field(PubChemCompoundsCrossRefsLuceneFields.PubChemCompID.toString(), pccompIdent.getAccession(), Field.Store.YES, Field.Index.ANALYZED));
                    doc.add(new Field(PubChemCompoundsCrossRefsLuceneFields.ExtDB.toString(), crIdent.getShortDescription(), Field.Store.YES, Field.Index.ANALYZED));
                    doc.add(new Field(PubChemCompoundsCrossRefsLuceneFields.ExtID.toString(), crIdent.getAccession(), Field.Store.YES, Field.Index.ANALYZED));
                    docs.add(doc);
                }
            }
            LOGGER.info("Finished writing " + uniquePChemCompID2CrossRef.size() + " cross refs to index.");
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
                + File.separator + "pubchem-compound-crossref";
        Preferences prefs = Preferences.userNodeForPackage(PubChemCompoundCrossRefs.class);
        return new File(prefs.get("pubchemCompound.crossrefs.path", defaultFile));
    }

    public String getDescription() {
        return "PubChem Compound Cross-references";
    }

    private boolean isCrossRefInIndex(IndexSearcher searcher, PubChemCompoundIdentifier identPCComp, Identifier externalIdentifier) throws IOException {
        Query queryExtDB = new TermQuery(new Term(PubChemCompoundsCrossRefsLuceneFields.ExtDB.toString(), externalIdentifier.getShortDescription()));
        Query queryExtID = new TermQuery(new Term(PubChemCompoundsCrossRefsLuceneFields.ExtID.toString(), externalIdentifier.getAccession()));
        Query queryPubChemComp = new TermQuery(new Term(PubChemCompoundsCrossRefsLuceneFields.PubChemCompID.toString(), identPCComp.getAccession()));
        BooleanQuery query = new BooleanQuery();
        query.add(queryExtDB, BooleanClause.Occur.MUST);
        query.add(queryExtID, BooleanClause.Occur.MUST);
        query.add(queryPubChemComp, BooleanClause.Occur.MUST);
        TopDocs topDocs = searcher.search(query, 1); // this search shouldn't have more than one hit normally.

        ScoreDoc[] scoreDosArray = topDocs.scoreDocs;
        return scoreDosArray.length > 0;

    }
}
