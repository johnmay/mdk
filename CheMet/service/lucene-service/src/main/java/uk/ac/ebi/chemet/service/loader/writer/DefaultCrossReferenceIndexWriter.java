package uk.ac.ebi.chemet.service.loader.writer;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.util.Version;
import uk.ac.ebi.service.index.LuceneIndex;
import uk.ac.ebi.service.query.CrossReferenceService;
import uk.ac.ebi.service.query.QueryService;

import java.io.IOException;

/**
 * DefaultCrossReferenceIndexWriter - 29.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class DefaultCrossReferenceIndexWriter {

    private static final Logger LOGGER = Logger.getLogger(DefaultCrossReferenceIndexWriter.class);

    private LuceneIndex index;
    private IndexWriter writer;

    /**
     * Create the index writer for the specified index
     *
     * @param index
     *
     * @throws java.io.IOException
     */
    public DefaultCrossReferenceIndexWriter(LuceneIndex index) throws IOException {
        this.index = index;
        this.writer = new IndexWriter(index.getDirectory(),
                                      new IndexWriterConfig(Version.LUCENE_34, index.getAnalyzer()));
    }
    
    public void write(String identifier, Byte databaseName, String databaseAccession) throws IOException {

        Document document = new Document();

        document.add(new Field(QueryService.IDENTIFIER.field(), identifier, Field.Store.YES, Field.Index.ANALYZED));
        document.add(new Field(CrossReferenceService.DATABASE_IDENTIFIER_INDEX.field(), databaseName.toString(), Field.Store.YES, Field.Index.ANALYZED));
        document.add(new Field(CrossReferenceService.DATABASE_ACCESSION.field(), databaseAccession, Field.Store.YES, Field.Index.ANALYZED));

        writer.addDocument(document);

    }
    
    public void close() throws IOException {
        writer.close();
    }
    
}
