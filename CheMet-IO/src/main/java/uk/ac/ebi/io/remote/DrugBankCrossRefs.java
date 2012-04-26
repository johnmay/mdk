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

import uk.ac.ebi.chemet.resource.chemical.DrugBankIdentifier;
import uk.ac.ebi.deprecated.services.RemoteResource;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.prefs.Preferences;
import java.util.zip.ZipInputStream;
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
import uk.ac.ebi.io.xml.DrugBankEntry;
import uk.ac.ebi.io.xml.DrugBankXMLReader;

/**
 *          ChEBISearch - 2011.10.25 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class DrugBankCrossRefs extends AbstrastRemoteResource implements RemoteResource {

    private static final Logger LOGGER = Logger.getLogger(DrugBankCrossRefs.class);
    private static final String location = "http://drugbank.ca/system/downloads/current/drugbank.xml.zip";

    public enum DrugBankCrossRefsLuceneFields {

        DrugBankID, ExtDB, ExtID;
    }

    public DrugBankCrossRefs() {
        super(location, getFile());
    }

    public void update() throws IOException {


        LinkedList<Document> docs = new LinkedList();

        DrugBankEntry entry;

        try {
            ZipInputStream zin = new ZipInputStream(getRemote().openStream());
            zin.getNextEntry();
            DrugBankXMLReader reader = new DrugBankXMLReader(zin);
            while ((entry = reader.getNext()) != null) {
                DrugBankIdentifier ident = entry.getIdentifier();
                for (Identifier extIdent : entry.getCrossReferences().getIdentifiers()) {
                    LOGGER.info("Adding: DrugBank:"+ident.getAccession()+" ExtDB:"+extIdent.getShortDescription()+" ExtID:"+extIdent.getAccession());
                    Document doc = new Document();
                    doc.add(new Field(DrugBankCrossRefsLuceneFields.DrugBankID.toString(),ident.getAccession(),Field.Store.YES, Field.Index.ANALYZED));
                    doc.add(new Field(DrugBankCrossRefsLuceneFields.ExtDB.toString(),extIdent.getShortDescription(),Field.Store.YES,Field.Index.ANALYZED));
                    doc.add(new Field(DrugBankCrossRefsLuceneFields.ExtID.toString(),extIdent.getAccession(),Field.Store.YES,Field.Index.ANALYZED));
                    docs.add(doc);
                }
                

            }
            reader.close();
        } catch (XMLStreamException e) {
            LOGGER.error("Problems parsing drugbank XML:", e);
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
                             + File.separator + "drugbank-crossref";
        Preferences prefs = Preferences.userNodeForPackage(DrugBankCrossRefs.class);
        return new File(prefs.get("drugbank.crossrefs.path", defaultFile));
    }

    public String getDescription() {
        return "DrugBank Cross-references";
    }
    
    public static void main(String[] args) throws IOException {
        new DrugBankCrossRefs().update();
    }
}
