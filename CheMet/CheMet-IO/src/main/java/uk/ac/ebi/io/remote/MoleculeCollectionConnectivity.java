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
import uk.ac.ebi.interfaces.services.RemoteResource;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import uk.ac.ebi.interfaces.services.LuceneService;
import uk.ac.ebi.resource.chemical.ChemicalIdentifier;
import uk.ac.ebi.resource.organism.Taxonomy;

/**
 *          Writes a Lucene index for a set molecules for which a unique connectivity string has been previously 
 *          calculated. This could be either the connectivity part of an InChI or a Smile or whatever way of representing
 *          the connectivity on a single string is picked. Objects should be suplied through the MoleculeConnectivity
 *          iterator.
 * 
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 */
public class MoleculeCollectionConnectivity extends AbstrastRemoteResource implements RemoteResource, LuceneService {

    private static final Logger LOGGER = Logger.getLogger(MoleculeCollectionConnectivity.class);
    private static final String location = "http://localhost/"; 
    // maybe this class should extend a different type of resource, as this URL is only to comply with the abstract class.
    private String collectionName;
    private Iterator<MoleculeConnectivity> molIterator;
    private final Analyzer analyzer = new KeywordAnalyzer();

    private boolean checkEntry(MoleculeConnectivity entry) {
        if(entry==null)
            return false;
        if(entry.getId()==null)
            return false;
        if(entry.getConnectivity()==null)
            return false;
        if(entry.getDB()==null)
            return false;
        return true;
    }

    public Analyzer getAnalzyer() {
        return analyzer;
    }

    public Directory getDirectory() {
        try {
            return new SimpleFSDirectory(getLocal());
        } catch (IOException ex) {
            throw new UnsupportedOperationException("Index can not fail to open! unsupported");
        }
    }
    
    /**
     * Fields for the Lucene index.
     */
    public enum MoleculeCollectionConnectivityLuceneFields {

        CollectionName, Identifier, Connectivity, DB;
    }

    /**
     * Constructor that takes a collectionName (like 9606DB or 9606TM) and a mol iterator that holds the data that will
     * be loaded to the index. The mol iterator should be filled using MoleculeConnectivity objects obtained through
     * the static method provided.
     * 
     * @param collectionName
     * @param molIterator 
     */
    public MoleculeCollectionConnectivity(String collectionName, Iterator<MoleculeConnectivity> molIterator) {
        super(location, getFile());
        this.collectionName = collectionName;
        this.molIterator = molIterator;
    }
    
    public MoleculeCollectionConnectivity(String collectionName) {
        super(location,getFile());
        this.collectionName = collectionName;
    }
    
    public void setMolIterator(Iterator<MoleculeConnectivity> molIterator) {
        this.molIterator = molIterator;
    }

    public void update() throws IOException {
        LinkedList<Document> docs = new LinkedList();
        MoleculeConnectivity entry;
        int counter=0;
        while (molIterator.hasNext()) {
            entry = molIterator.next();
            if (checkEntry(entry)) {
                counter++;
                Document doc = new Document();
                doc.add(new Field(MoleculeCollectionConnectivityLuceneFields.CollectionName.toString(), this.collectionName, Field.Store.YES, Field.Index.NOT_ANALYZED));
                doc.add(new Field(MoleculeCollectionConnectivityLuceneFields.Identifier.toString(), entry.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                doc.add(new Field(MoleculeCollectionConnectivityLuceneFields.DB.toString(), entry.getDB(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                doc.add(new Field(MoleculeCollectionConnectivityLuceneFields.Connectivity.toString(),entry.getConnectivity(),Field.Store.YES,Field.Index.NOT_ANALYZED));
                docs.add(doc);
                if(counter % 500 == 0) {
                    LOGGER.info("Indexed "+counter+" mols.");
                }
            } else {
                LOGGER.warn("Could not add entry "+entry.getId()+" "+entry.getDB()+" "+entry.getConnectivity());
            }
            
        }


        // write the index
        Directory index = getDirectory();
        IndexWriter writer = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_34, getAnalzyer()));
        writer.addDocuments(docs);
        writer.close();
        index.close();

    }

    public static File getFile() {
        String defaultFile = System.getProperty("user.home")
                + File.separator + "databases"
                + File.separator + "indexes"
                + File.separator + "molConnectivity";
        Preferences prefs = Preferences.userNodeForPackage(MoleculeCollectionConnectivity.class);
        return new File(prefs.get("moleculeCollection.connectivity.path", defaultFile));
    }

    public String getDescription() {
        return collectionName + " Molecules Conectivity";
    }
    
    public static MoleculeConnectivity getMoleculeConnectivityInstance(ChemicalIdentifier identifier, String connectivity) {
        return new MoleculeConnectivity(identifier, connectivity);
    }
    
    public static class MoleculeConnectivity {
        private ChemicalIdentifier identifier;
        private String connectivity;
        /**
         * 
         * @param identifier
         * @param connectivity 
         */
        public MoleculeConnectivity(ChemicalIdentifier identifier, String connectivity) {
            this.identifier = identifier;
            this.connectivity = connectivity;
        }

        /**
         * @return the id
         */
        public String getId() {
            return identifier.getAccession();
        }

        /**
         * @return the connectivity
         */
        public String getConnectivity() {
            return connectivity;
        }

        private String getDB() {
            return identifier.getShortDescription();
        }
        
        
    }
}
