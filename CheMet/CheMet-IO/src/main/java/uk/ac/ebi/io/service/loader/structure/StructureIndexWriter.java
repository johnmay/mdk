package uk.ac.ebi.io.service.loader.structure;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import uk.ac.ebi.interfaces.services.LuceneService;
import uk.ac.ebi.io.service.index.LuceneIndex;

import java.io.IOException;

/**
 * ${Name}.java - 20.02.2012 <br/> Description...
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class StructureIndexWriter implements LuceneService {

    private Directory directory;
    private IndexWriter writer;
    private Analyzer analyzer;

    public enum FieldName {
        IDENTIFIER,
        MOLECULE
    }

    @Override
    public Analyzer getAnalzyer() {
        return analyzer;
    }

    @Override
    public Directory getDirectory() {
        return directory;
    }

    private StructureIndexWriter(LuceneIndex location) throws IOException {
        directory = location.getDirectory();
        analyzer = location.getAnalyzer();
        writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_34, analyzer));
    }

    public static StructureIndexWriter create(LuceneIndex location) throws IOException {
        return new StructureIndexWriter(location);
    }

    public void add(String identifier,
                    byte[] molecule)
            throws IOException {

        Document document = new Document();
        document.add(new Field(FieldName.IDENTIFIER.name(), identifier, Field.Store.NO, Field.Index.ANALYZED));
        document.add(new Field(FieldName.MOLECULE.name(), molecule));
        writer.addDocument(document);

    }

    public void close() throws IOException {
        writer.close();
    }

}
