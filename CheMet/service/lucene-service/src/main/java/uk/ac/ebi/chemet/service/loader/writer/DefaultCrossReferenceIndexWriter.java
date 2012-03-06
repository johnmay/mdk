package uk.ac.ebi.chemet.service.loader.writer;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.util.Version;
import uk.ac.ebi.interfaces.identifiers.Identifier;
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
public class DefaultCrossReferenceIndexWriter extends AbstractIndexWriter {


    public DefaultCrossReferenceIndexWriter(LuceneIndex index) throws IOException {
        super(index);
    }
    
    public void write(String identifier, Identifier xref) throws IOException{
        write(identifier, xref.getIndex(), xref.getAccession());
    }
    
    public void write(String identifier, Byte databaseName, String databaseAccession) throws IOException {

        Document document = new Document();

        document.add(create(QueryService.IDENTIFIER, identifier));
        document.add(create(CrossReferenceService.DATABASE_IDENTIFIER_INDEX, databaseName.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(create(CrossReferenceService.DATABASE_ACCESSION, databaseAccession));

        add(document);

    }

    
}
