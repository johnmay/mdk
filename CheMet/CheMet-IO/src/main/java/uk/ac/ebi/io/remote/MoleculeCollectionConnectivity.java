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
public class MoleculeCollectionConnectivity extends AbstrastRemoteResource implements RemoteResource {

    private static final Logger LOGGER = Logger.getLogger(MoleculeCollectionConnectivity.class);
    private static final String location = "http://localhost/"; 
    // maybe this class should extend a different type of resource, as this URL is only to comply with the abstract class.
    private String collectionName;
    private Iterator<MoleculeConnectivity> molIterator;

    /**
     * Fields for the Lucene index.
     */
    public enum MoleculeCollectionConnectivityLuceneFields {

        CollectionName, Identifier, Connectivity;
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

    public void update() throws IOException {


        LinkedList<Document> docs = new LinkedList();
        MoleculeConnectivity entry;
        int counter=0;
        while (molIterator.hasNext()) {
            entry = molIterator.next();
            if (entry != null) {
                counter++;
                Document doc = new Document();
                doc.add(new Field(MoleculeCollectionConnectivityLuceneFields.CollectionName.toString(), this.collectionName, Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field(MoleculeCollectionConnectivityLuceneFields.Identifier.toString(), entry.getId(), Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field(MoleculeCollectionConnectivityLuceneFields.Connectivity.toString(),entry.getConnectivity(),Field.Store.YES,Field.Index.ANALYZED));
                docs.add(doc);
                if(counter % 500 == 0) {
                    LOGGER.info("Indexed "+counter+" mols.");
                }
            }
            
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
                + File.separator + "molConnectivity";
        Preferences prefs = Preferences.userNodeForPackage(MoleculeCollectionConnectivity.class);
        return new File(prefs.get("moleculeCollection.connectivity.path", defaultFile));
    }

    public String getDescription() {
        return collectionName + " Molecules Conectivity";
    }
    
    public static MoleculeConnectivity getMoleculeConnectivityInstance(String identifier, String connectivity) {
        return new MoleculeConnectivity(identifier, connectivity);
    }
    
    public static class MoleculeConnectivity {
        private String id;
        private String connectivity;
        
        /**
         * 
         * @param identifier
         * @param connectivity 
         */
        public MoleculeConnectivity(String identifier, String connectivity) {
            this.id = identifier;
            this.connectivity = connectivity;
        }

        /**
         * @return the id
         */
        public String getId() {
            return id;
        }

        /**
         * @return the connectivity
         */
        public String getConnectivity() {
            return connectivity;
        }
        
        
    }
}
