/**
 * ChEBISecondaryToPrimaryIDLoader.java
 *
 * 2013.02.28
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


import au.com.bytecode.opencsv.CSVReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import uk.ac.ebi.mdk.service.index.other.ChEBISecondaryToPrimaryIDIndex;
import uk.ac.ebi.mdk.service.index.other.ChEBISecondaryToPrimaryIDIndex.ChEBISecondary2PrimaryLuceneFields;
import uk.ac.ebi.mdk.service.loader.AbstractSingleIndexResourceLoader;
import uk.ac.ebi.mdk.service.loader.location.RemoteLocation;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;

/**
 * @name    ChEBISecondaryToPrimaryIDLoader
 * @date    2013.02.28
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class ChEBISecondaryToPrimaryIDLoader extends AbstractSingleIndexResourceLoader {

    private static final Logger LOGGER = Logger.getLogger( ChEBISecondaryToPrimaryIDLoader.class );
    private static final String compoundsLocation =
            "ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/compounds.tsv"; // get parent - child compound
    
    public ChEBISecondaryToPrimaryIDLoader() throws IOException {
        
        super(new ChEBISecondaryToPrimaryIDIndex());
        
        addRequiredResource("ChEBI Database Accessions List",
                            "",
                            ResourceFileLocation.class,
                            new RemoteLocation("ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/database_accession.tsv"));

    }

    @Override
     public void update() throws IOException {
        // TODO Method getSecondaryToParentID could be moved to this class and be called statically from this class,
        // which seems more related to the task of resolving secondary to primary links.
        Map<String, String> sec2PrimaryID = getSecondaryToParentID();

        List<Document> docs = new ArrayList<Document>();

        for (String chebiIDSec : sec2PrimaryID.keySet()) {

            String chebiIDPrim = sec2PrimaryID.get(chebiIDSec);
            Document doc = new Document();

            doc.add(new Field(ChEBISecondary2PrimaryLuceneFields.ChebiIDSec.toString(), chebiIDSec, Field.Store.YES, Field.Index.NOT_ANALYZED));
            doc.add(new Field(ChEBISecondary2PrimaryLuceneFields.ChebiIDPrim.toString(), chebiIDPrim, Field.Store.YES, Field.Index.NOT_ANALYZED));
            docs.add(doc);

        }

        // write the index
        Directory index = getIndex().getDirectory();
        IndexWriter writer = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_34, getIndex().getAnalyzer()));
        writer.addDocuments(docs);
        writer.close();
        index.close();
        docs.clear();
    }
    
        /**
     * Retrieves the secondary chebi id to parent chebi ids relations from the compounds.tsv file.
     * @return
     * @throws IOException 
     */
    protected Map<String, String> getSecondaryToParentID() throws IOException {
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

    private Map<String, Integer> createMap(String[] row) {
        Map<String, Integer> map = new HashMap();
        for (int i = 0; i < row.length; i++) {
            map.put(row[i], i);
        }
        return map;
    }

}
