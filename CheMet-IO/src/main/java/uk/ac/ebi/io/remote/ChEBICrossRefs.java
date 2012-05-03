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

import org.apache.lucene.index.CorruptIndexException;
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.deprecated.services.RemoteResource;
import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.prefs.Preferences;
import java.util.zip.ZipInputStream;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import uk.ac.ebi.chemet.ws.exceptions.WebServiceException;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.deprecated.services.LuceneService;
import uk.ac.ebi.metabolomes.webservices.EUtilsWebServiceConnection;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;

/**
 *          ChEBISearch - 2011.10.25 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @deprecated use ChEBICrossReferenceLoader in chemet-service. Note: the ChEBICrossReferenceLoader loader is still lacking PubChem referecnes
 */
@Deprecated
public class ChEBICrossRefs
        extends AbstrastRemoteResource
        implements LuceneService, RemoteResource {

    private static final Logger LOGGER = Logger.getLogger(ChEBICrossRefs.class);
    private Analyzer analzyer;
    private static final String location = "ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/database_accession.tsv";
    private static final String location3star = "ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/database_accession_3star.tsv";
    private static final String compoundsLocation =
            "ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/compounds.tsv"; // get parent - child compound
    private static final String referenceFile =
            "ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/reference.tsv.zip"; // for pubchem substance
    private static final DefaultIdentifierFactory FACTORY = DefaultIdentifierFactory.getInstance();

    private List<Identifier> getIdentsAssocToChEBIID(IndexSearcher searcher, String get) throws CorruptIndexException, IOException {
        Query chebiIDQuery = new TermQuery(new Term(ChEBICrossRefsLuceneFields.ChebiID.toString(), get));
        TopDocs topDocs = searcher.search(chebiIDQuery, 50); // this search shouldn't have more than one hit normally.

        ScoreDoc[] scoreDosArray = topDocs.scoreDocs;
        List<Identifier> idents = new ArrayList<Identifier>();
        for (ScoreDoc scoreDoc : scoreDosArray) {
            Document doc = searcher.doc(scoreDoc.doc);
            Identifier ident = FACTORY.ofSynonym(doc.get(ChEBICrossRefsLuceneFields.ExtDB.toString()));
            ident.setAccession(doc.get(ChEBICrossRefsLuceneFields.ExtID.toString()));
            idents.add(ident);
        }
        return idents;

    }

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
        Map<String, String> sec2PrimaryID = getSecondaryToParentID();

        List<Document> docs = new ArrayList<Document>();

        Multimap<String, String> chebi2PubChemComp = getPubChemCompoundFromReferenceFile();

        for (String chebiID : chebi2PubChemComp.keySet()) {
            List<String> chebiIDsToLinkToRef = new ArrayList<String>();
            if (sec2PrimaryID.containsKey(chebiID)) {
                chebiIDsToLinkToRef.add(sec2PrimaryID.get(chebiID));
            }
            chebiIDsToLinkToRef.add(chebiID);
            
            for (String pchemComp : chebi2PubChemComp.get(chebiID)) {
                PubChemCompoundIdentifier extIdent = new PubChemCompoundIdentifier();
                extIdent.setAccession(pchemComp);

                for (String chebiID2Link : chebiIDsToLinkToRef) {
                    Document doc = new Document();

                    doc.add(new Field(ChEBICrossRefsLuceneFields.ChebiID.toString(), chebiID2Link, Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(ChEBICrossRefsLuceneFields.ExtDB.toString(), extIdent.getShortDescription(), Field.Store.YES, Field.Index.ANALYZED));
                    doc.add(new Field(ChEBICrossRefsLuceneFields.ExtID.toString(), extIdent.getAccession(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    docs.add(doc);
                }
            }
        }
        
        // write the index
        Directory index = new SimpleFSDirectory(getLocal());
        IndexWriter writer = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_34, analzyer));
        writer.addDocuments(docs);
        docs.clear();

        String currentId = "";

        CSVReader reader = new CSVReader(new InputStreamReader(getRemote().openStream()), '\t', '\0');

        String[] row = reader.readNext();
        Map<String, Integer> map = createMap(row);

        while ((row = reader.readNext()) != null) {
            String id = row[map.get("COMPOUND_ID")];
            String type = row[map.get("TYPE")];
            String accession = row[map.get("ACCESSION_NUMBER")];
            List<String> chebiIDsToLinkToRef = new ArrayList<String>();
            if (sec2PrimaryID.containsKey(id)) {
                chebiIDsToLinkToRef.add(sec2PrimaryID.get(id));
            }
            chebiIDsToLinkToRef.add(id);
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

            for (String chebiID2Link : chebiIDsToLinkToRef) {
                Document doc = new Document();

                doc.add(new Field(ChEBICrossRefsLuceneFields.ChebiID.toString(), chebiID2Link, Field.Store.YES, Field.Index.NOT_ANALYZED));
                doc.add(new Field(ChEBICrossRefsLuceneFields.ExtDB.toString(), extIdent.getShortDescription(), Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field(ChEBICrossRefsLuceneFields.ExtID.toString(), extIdent.getAccession(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                docs.add(doc);
            }
        }
        reader.close();

        
        writer.addDocuments(docs);
        writer.close();

        writer = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_34, analzyer));
        //writer.close();

        // Now using each secondary chebi ids, we get the primary ids, and see whether each secondary id has all the cross
        // refs that the primary id has.
        IndexSearcher searcher = new IndexSearcher(index, true);
        docs.clear();
        for (String secondaryID : sec2PrimaryID.keySet()) {
            List<Identifier> identsAssocToPrimary = getIdentsAssocToChEBIID(searcher, sec2PrimaryID.get(secondaryID));
            List<Identifier> identsAssocToSecondary = getIdentsAssocToChEBIID(searcher, secondaryID);
            for (Identifier identifier2Add : identsAssocToPrimary) {
                if (!identsAssocToSecondary.contains(identifier2Add)) {
                    Document doc = new Document();
                    doc.add(new Field(ChEBICrossRefsLuceneFields.ChebiID.toString(), secondaryID, Field.Store.YES, Field.Index.NOT_ANALYZED));
                    doc.add(new Field(ChEBICrossRefsLuceneFields.ExtDB.toString(), identifier2Add.getShortDescription(), Field.Store.YES, Field.Index.ANALYZED));
                    doc.add(new Field(ChEBICrossRefsLuceneFields.ExtID.toString(), identifier2Add.getAccession(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    docs.add(doc);
                }
            }
        }
        searcher.close();
        writer.addDocuments(docs);
        writer.close();
        index.close();

    }

    /**
     * This method should read the reference file in the ftp, load all the chebi id 2 pubchem substances, and then
     * retrieve, with eutils, the compounds corresponding to those substances. Finally generates a chebi2pubchemCompound.
     * @return 
     */
    private Multimap<String, String> getPubChemCompoundFromReferenceFile() throws IOException {
        ZipInputStream zipInput = new ZipInputStream((new URL(referenceFile)).openStream());
        zipInput.getNextEntry();
        CSVReader compsReader = new CSVReader(new InputStreamReader(zipInput), '\t', '\0');
        String[] rowComps = compsReader.readNext();
        Map<String, Integer> compsMap = createMap(rowComps);
        ListMultimap<String, String> chebi2pubchemSubs = ArrayListMultimap.create();

        while ((rowComps = compsReader.readNext()) != null) {
            if (rowComps[compsMap.get("REFERENCE_DB_NAME")].equalsIgnoreCase("PubChem")) {
                String chebiID = rowComps[compsMap.get("COMPOUND_ID")];
                String pchemSubs = rowComps[compsMap.get("REFERENCE_ID")];
                chebi2pubchemSubs.put(chebiID, pchemSubs);
            }
        }

        EUtilsWebServiceConnection euwsc = new EUtilsWebServiceConnection();
        Set<String> substances = new HashSet<String>(chebi2pubchemSubs.values());
        List<String> substanceList = new ArrayList<String>(substances);
        SetMultimap<String, String> chebi2pubchemComps = HashMultimap.create();
        for (int i = 0; i < substanceList.size(); i += euwsc.MAX_RECORDS_PER_QUERY) {
            List<String> substance2Submit = substanceList.subList(i, Math.min(substanceList.size(), i + euwsc.MAX_RECORDS_PER_QUERY));
            try {
                Multimap<String, String> subs2Comps = euwsc.getPubChemCompoundFromPubChemSubstance(substance2Submit);
                for (String chebiId : chebi2pubchemSubs.keySet()) {
                    for (String substance : chebi2pubchemSubs.get(chebiId)) {
                        if (subs2Comps.containsKey(substance)) {
                            for (String compound : subs2Comps.get(substance)) {
                                chebi2pubchemComps.put(chebiId, compound);
                            }
                        }
                    }
                }
            } catch (WebServiceException e) {
                LOGGER.error("Could not load substance to compounds from EUtils", e);
            }
        }

        return chebi2pubchemComps;

    }

    /**
     * Retrieves the secondary chebi id to parent chebi ids relations from the compounds.tsv file.
     * @return
     * @throws IOException 
     */
    protected static Map<String, String> getSecondaryToParentID() throws IOException {
        CSVReader compsReader = new CSVReader(new InputStreamReader((new URL(compoundsLocation)).openStream()), '\t', '\0');
        String[] rowComps = compsReader.readNext();
        Map<String, Integer> compsMap = createMap(rowComps);
        Map<String, String> secondaryToParentID = new HashMap<String, String>();
        while ((rowComps = compsReader.readNext()) != null) {
            String parent = rowComps[compsMap.get("PARENT_ID")].replaceFirst("CHEBI:", "");
            if (parent.equals("null")) {
                continue;
            }
            String chebiAcc = rowComps[compsMap.get("CHEBI_ACCESSION")].replaceFirst("CHEBI:", "");
            //String source = rowComps[compsMap.get("SOURCE")];
            secondaryToParentID.put(chebiAcc, parent);
        }
        compsReader.close();
        return secondaryToParentID;
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

    private static Map<String, Integer> createMap(String[] row) {
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
