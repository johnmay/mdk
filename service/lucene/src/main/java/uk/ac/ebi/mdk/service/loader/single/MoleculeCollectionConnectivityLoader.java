/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
package uk.ac.ebi.mdk.service.loader.single;

import org.apache.lucene.analysis.Analyzer;
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
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import uk.ac.ebi.mdk.domain.identifier.type.ChemicalIdentifier;
import uk.ac.ebi.mdk.service.index.other.MoleculeCollectionConnectivityIndex;
import uk.ac.ebi.mdk.service.loader.AbstractSingleIndexResourceLoader;

/**
 *          Writes a Lucene index for a set molecules for which a unique connectivity string has been previously 
 *          calculated. This could be either the connectivity part of an InChI or a Smile or whatever way of representing
 *          the connectivity on a single string is picked. Objects should be suplied through the MoleculeConnectivity
 *          iterator.
 * 
 *          Class description
 * @version $Rev: 1915 $ : Last Changed $Date: 2012-04-02 15:17:20 +0100 (Mon, 02 Apr 2012) $
 * @author  pmoreno
 * @author  $Author: johnmay $ (this version)
 */
public class MoleculeCollectionConnectivityLoader extends AbstractSingleIndexResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(MoleculeCollectionConnectivityLoader.class);    
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
    public MoleculeCollectionConnectivityLoader(String collectionName, Iterator<MoleculeConnectivity> molIterator) {
        super(new MoleculeCollectionConnectivityIndex(collectionName));
        this.collectionName = collectionName;
        this.molIterator = molIterator;
    }
    
    public MoleculeCollectionConnectivityLoader(String collectionName) {
        super(new MoleculeCollectionConnectivityIndex(collectionName));
        this.collectionName = collectionName;
    }
    
    public void setMolIterator(Iterator<MoleculeConnectivity> molIterator) {
        this.molIterator = molIterator;
    }
    
    public void deleteCollection() throws IOException {
        Query queryCollection = new TermQuery(new Term(MoleculeCollectionConnectivityLuceneFields.CollectionName.toString(), this.collectionName));
        
        Directory indexDirectory = getIndex().getDirectory();
        IndexWriter writer = new IndexWriter(indexDirectory, new IndexWriterConfig(Version.LUCENE_34, getIndex().getAnalyzer()));        
        writer.deleteDocuments(queryCollection);
        writer.close();
    }

    @Override
    public void update() throws IOException {
        LinkedList<Document> docs = new LinkedList<Document>();
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
        Directory index = getIndex().getDirectory();
        IndexWriter writer = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_34, getIndex().getAnalyzer()));
        writer.addDocuments(docs);
        writer.close();
        index.close();

    }

    /*
    public static File getFile() {
        String defaultFile = System.getProperty("user.home")
                + File.separator + "databases"
                + File.separator + "indexes"
                + File.separator + "molConnectivity";
        Preferences prefs = Preferences.userNodeForPackage(MoleculeCollectionConnectivity.class);
        return new File(prefs.get("moleculeCollection.connectivity.path", defaultFile));
    }*/

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
