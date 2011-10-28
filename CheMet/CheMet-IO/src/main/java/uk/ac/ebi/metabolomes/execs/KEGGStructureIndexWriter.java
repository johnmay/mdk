/**
 * KEGGStructureIndexWriter.java
 *
 * 2011.10.28
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
import java.util.Arrays;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import uk.ac.ebi.interfaces.identifiers.KEGGIdentifier;
import uk.ac.ebi.resource.chemical.KEGGCompoundIdentifier;

/**
 *          KEGGStructureIndexWriter - 2011.10.28 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class KEGGStructureIndexWriter {

    private static final Logger LOGGER = Logger.getLogger(KEGGStructureIndexWriter.class);

    public static void main(String[] args) throws FileNotFoundException, IOException {
        File molDir = new File("/databases/kegg/ligand/mol");
        byte[] b = new byte[0];
        File[] files = molDir.listFiles();

        Directory directory = new SimpleFSDirectory(new File("/databases/indexes/kegg-mdl"));
        IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_34, new KeywordAnalyzer()));
        KEGGCompoundIdentifier keggid = new KEGGCompoundIdentifier();

        long start = System.currentTimeMillis();
        for (int i = 0; i < files.length; i++) {
            int length = (int) files[i].length();
            if (length > b.length) {
                b = Arrays.copyOf(b, length);
            }
            InputStream stream = new FileInputStream(files[i]);
            stream.read(b, 0, length);

            Document doc = new Document();
            keggid.setAccession(files[i].getName().substring(0,6));
            NumericField id = new NumericField("id");
            id.setIntValue(keggid.getValue());
            doc.add(id);
            doc.add(new Field("mdl", Arrays.copyOf(b, length)));
            writer.addDocument(doc);

            stream.close();
        }

        writer.close();

        long end = System.currentTimeMillis();
        System.out.println("completed reading: " + files.length + " in " + (end - start) + "ms");



    }
}
