package uk.ac.ebi.chemet.service.loader.writer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.Version;
import uk.ac.ebi.mdk.service.index.LuceneIndex;

import java.io.IOException;

/**
 * AbstractIndexWriter - 06.03.2012 <br/>
 * <p/>
 * Provides basic functionality for an index writer. An instance will create a new {@see IndexWriter}
 * for the provide {@see LucenIndex}. Documents are added to the index via the {@see add(Document)}
 * method. You can close the {@see IndexWriter} by invoking {@see close()}.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class AbstractIndexWriter {

    private IndexWriter writer;

    /**
     * Create a new AbstractIndexWriter with the directory and anlayzer
     * provided by the {@see LuceneIndex}
     *
     * @param index storage of the directory and analyzer
     *
     * @throws IOException low level io error
     */
    public AbstractIndexWriter(LuceneIndex index) throws IOException {

        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_34,
                                                         index.getAnalyzer());
        config.setRAMBufferSizeMB(50.0d);
        writer = new IndexWriter(index.getDirectory(),
                                 config);

    }

    /**
     * Utility method for stored and indexed lucene field creation. This method allows
     * you to specified the field name with a term. The field is stored (Field.Store.YES)
     * and index with (Field.Index.ANALYZED)
     *
     * @param term  term for the field you want to create
     * @param value string value to store
     *
     * @return instance of new field
     */
    public Fieldable create(Term term, String value) {
        return new Field(term.field(), value, Field.Store.YES, Field.Index.ANALYZED);
    }

    /**
     * Utility method for indexed lucene field creation. This method allows you to specified
     * the field name with a term. The field is index with (Field.Index.ANALYZED)
     *
     * @param term  term for the field you want to create
     * @param value string value to store
     * @param store whether to store the value
     *
     * @return instance of new field
     */
    public Fieldable create(Term term, String value, Field.Store store) {
        return new Field(term.field(), value, store, Field.Index.ANALYZED);
    }

    /**
     * Utility method for lucene field creation. This method allows you to specified
     * the field name with a term.
     *
     * @param term    term for the field you want to create
     * @param value   string value to store
     * @param store   whether to store the value
     * @param indexed whether to index the value
     *
     * @return instance of new field
     */
    public Fieldable create(Term term, String value, Field.Store store, Field.Index indexed) {
        return new Field(term.field(), value, store, indexed);
    }

    /**
     * Add a document to the {@see IndexWriter}. This method wraps
     * a call to {@see IndexWriter#addDocument(Document)};
     *
     * @param document the document to add
     *
     * @throws IOException if there is a low-level IO error
     */
    public void add(Document document) throws IOException {
        writer.addDocument(document);
    }

    /**
     * Access the underlying IndexWriter
     *
     * @return
     */
    public IndexWriter getWriter() {
        return writer;
    }

    /**
     * Close the underlying index writer
     *
     * @throws IOException low-level IO error
     */
    public void close() throws IOException {
        writer.close();
    }

}
