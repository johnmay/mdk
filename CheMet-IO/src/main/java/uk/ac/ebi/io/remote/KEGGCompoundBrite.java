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
import uk.ac.ebi.chemet.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.deprecated.services.RemoteResource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import uk.ac.ebi.deprecated.services.LuceneService;
import uk.ac.ebi.io.plain.KEGGBriteEntry;
import uk.ac.ebi.io.plain.KEGGBriteReader;

/**
 *          KEGGCompound - 2011.11.19 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 */
public class KEGGCompoundBrite extends AbstrastRemoteResource implements LuceneService, RemoteResource {

    private static final Logger LOGGER = Logger.getLogger(KEGGCompoundBrite.class);
    private static final String location = "http://www.genome.jp/kegg-bin/download_htext?htext=<BRITECAT>&format=htext&filedir=";
    private final List<String> briteFiles = new ArrayList<String>();

    public Analyzer getAnalzyer() {
        return new KeywordAnalyzer();
    }

    public Directory getDirectory() {
        try {
            return new SimpleFSDirectory(getLocal());
        } catch (IOException ex) {
            throw new UnsupportedOperationException("Index can not fail to open! unsupported");
        }
    }

    public enum KEGGCompBriteLuceneFields {

        KeggCompID, CompoundName, BriteEntry, BriteName, BriteDefinition;
    }

    public KEGGCompoundBrite() {
        super(location, getFile());
        briteFiles.add("br08001.keg");
        briteFiles.add("br08002.keg");
        briteFiles.add("br08003.keg");
        briteFiles.add("br08005.keg");
        briteFiles.add("br08006.keg");
        briteFiles.add("br08007.keg");
        briteFiles.add("br08008.keg");
    }

    public void update() throws IOException {

        for (String fileName : briteFiles) {
            KEGGBriteReader reader = new KEGGBriteReader(new URL(location.replace("<BRITECAT>", fileName)).openStream());
            KEGGBriteEntry entry = reader.readEntry();

            // write the index
            Directory index = getDirectory();
            IndexWriter writer = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_34, getAnalzyer()));
            
            while (entry != null) {
                KEGGCompoundIdentifier ident = entry.getIdentifier();
                Document doc = new Document();
                doc.add(new Field(KEGGCompBriteLuceneFields.KeggCompID.toString(), ident.getAccession(), Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field(KEGGCompBriteLuceneFields.BriteEntry.toString(), reader.getBriteEntryHeader(), Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field(KEGGCompBriteLuceneFields.BriteDefinition.toString(), reader.getBriteDefinitionHeader(), Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field(KEGGCompBriteLuceneFields.BriteName.toString(), reader.getBriteNameHeader(), Field.Store.YES, Field.Index.ANALYZED));

                boolean addDoc = false;
                for (KEGGBriteEntry.KEGGCompBriteCategories category : KEGGBriteEntry.KEGGCompBriteCategories.values()) {
                    if (entry.hasCategory(category.toString())) {
                        doc.add(new Field(category.toString(), entry.getCategory(category.toString()), Field.Store.YES, Field.Index.ANALYZED));
                        addDoc = true;
                    }
                }
                if (addDoc) {
                    writer.addDocument(doc);
                }
                entry = reader.readEntry();
            }
            
            writer.close();
            index.close();
            reader.close();
            
        }



    }

    public static File getFile() {
        String defaultFile = System.getProperty("user.home")
                + File.separator + "databases"
                + File.separator + "indexes"
                + File.separator + "kegg-cpd-brite";
        Preferences prefs = Preferences.userNodeForPackage(KEGGCompoundBrite.class);
        return new File(prefs.get("kegg.cpd.brite.path", defaultFile));
    }

    public String getDescription() {
        return "KEGG Compound Brite Hierarchy";
    }
}
