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
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.interfaces.services.LuceneService;
/**
 *          Writes a Lucene index for a set chemical identifiers for which a property has been previously 
 *          calculated. This can be any single property-value type of property. 
 *          Objects should be suplied through the IdentifierProperty iterator.
 *          iterator.
 * 
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 */
public class IdentifierCurationStatus extends AbstrastRemoteResource implements RemoteResource, LuceneService {

    private static final Logger LOGGER = Logger.getLogger(IdentifierCuration.class);
    private static final String location = "http://localhost/"; 
    // maybe this class should extend a different type of resource, as this URL is only to comply with the abstract class.
    private String collectionName;
    private Iterator<IdentifierCuration> identifierPropertiesIt;
    private Analyzer analzyer;
    
    public enum CurationStatus {
        Manually, SemiManual, Automatic;
    }

    public IdentifierCurationStatus() {
        super(location,getFile());
        this.analzyer = new KeywordAnalyzer();
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

    /**
     * Fields for the Lucene index.
     */
    public enum IdentifierPropertiesLuceneFields {
        CollectionName, ExtDB, ExtID, CurationValue, CurationType;
    }

    /**
     * Constructor that takes a collectionName (like 9606DB or 9606TM) and a identifier property iterator that holds the data that will
     * be loaded to the index. The iterator should be filled using IdentifierProperty objects obtained through
     * the static method provided.
     * 
     * @param collectionName
     * @param identPropertyIterator 
     */
    public IdentifierCurationStatus(String collectionName, Iterator<IdentifierCuration> identPropertyIterator) {
        super(location, getFile());
        this.collectionName = collectionName;
        this.identifierPropertiesIt = identPropertyIterator;
        this.analzyer = new KeywordAnalyzer();
    }

    public void update() throws IOException {
        LinkedList<Document> docs = new LinkedList();
        IdentifierCuration entry;
        int counter=0;
        while (identifierPropertiesIt.hasNext()) {
            entry = identifierPropertiesIt.next();
            if (entry != null) {
                counter++;
                Document doc = new Document();
                doc.add(new Field(IdentifierPropertiesLuceneFields.CollectionName.toString(), this.collectionName, Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field(IdentifierPropertiesLuceneFields.ExtDB.toString(), entry.getIdentifier().getShortDescription(), Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field(IdentifierPropertiesLuceneFields.ExtID.toString(), entry.getIdentifier().getAccession(),Field.Store.YES,Field.Index.ANALYZED));
                doc.add(new Field(IdentifierPropertiesLuceneFields.CurationValue.toString(), entry.getCurationValue(),Field.Store.YES,Field.Index.ANALYZED));
                doc.add(new Field(IdentifierPropertiesLuceneFields.CurationType.toString(), entry.getCurationType(),Field.Store.YES,Field.Index.ANALYZED));
                docs.add(doc);
                if(counter % 500 == 0) {
                    LOGGER.info("Indexed "+counter+" mols.");
                }
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
                + File.separator + "identifierProperties";
        Preferences prefs = Preferences.userNodeForPackage(IdentifierCurationStatus.class);
        return new File(prefs.get("identifiers.curation.path", defaultFile));
    }

    public String getDescription() {
        return collectionName + " Curation for Identifiers";
    }
    
    public static IdentifierCuration getIdentifierCurationInstance(Identifier identifier, String curationValue, String curationType) {
        return new IdentifierCuration(identifier, curationValue, curationType);
    }
    
    public static class IdentifierCuration {
        private Identifier id;
        private String curationValue;
        private String curationType;
        
        /**
         * 
         * @param identifier
         * @param connectivity 
         */
        public IdentifierCuration(Identifier identifier, String curationValue, String curationType) {
            this.id = identifier;
            this.curationValue = curationValue;
            this.curationType = curationType;
        }

        /**
         * @return the id
         */
        public Identifier getIdentifier() {
            return id;
        }

        /**
         * @return the connectivity
         */
        public String getCurationValue() {
            return curationValue;
        }
        
        public String getCurationType() {
            return curationType;
        }
        
        
    }
}
