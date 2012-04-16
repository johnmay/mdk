/**
 * KEGGIndexWriter.java
 *
 * 2011.10.27
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
package uk.ac.ebi.metabolomes.execs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import uk.ac.ebi.metabolomes.parser.KEGGCompoundParser;
import uk.ac.ebi.metabolomes.parser.KEGGCompoundParser.KEGGField;

/**
 *          KEGGIndexWriter - 2011.10.27 <br>
 *          Creates a lucene index for KEGG names
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class KEGGIndexWriter {

    private static final Logger LOGGER = Logger.getLogger(KEGGIndexWriter.class);

    public static void main(String[] args) throws FileNotFoundException, IOException {

        InputStream stream = new FileInputStream(new File("/databases/kegg/ligand/compound"));

        KEGGCompoundParser keggCompound = new KEGGCompoundParser(stream,
                                                                 KEGGField.NAME, KEGGField.ENTRY);
        Directory directory = new SimpleFSDirectory(new File("/databases/indexes/kegg-names"));
        IndexWriter writer = new IndexWriter(directory,
                                             new IndexWriterConfig(Version.LUCENE_34,
                                                                   new KeywordAnalyzer()));

        Map entry;
        while ((entry = keggCompound.readNext()) != null) {
            Integer id = Integer.parseInt(entry.get(KEGGField.ENTRY).toString().substring(1, 6));

            Document doc = new Document();
            NumericField field = new NumericField("id", 1, Store.YES, true);
            field.setIntValue(id);
            doc.add(field);

            boolean skip = false;

            for (String name : entry.get(KEGGField.NAME).toString().split(";")) {
                if (name.startsWith("Transferred to")) {
                    skip = true;
                }
                doc.add(new Field("name", name.trim(), Field.Store.YES, Field.Index.ANALYZED));
            }

            if (!skip) {
                writer.addDocument(doc);
            }

        }

        stream.close();
        writer.close();
        directory.close();

    }
}
