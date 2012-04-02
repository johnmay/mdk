package uk.ac.ebi.chemet.service.loader.writer;

import org.apache.commons.lang.mutable.MutableByte;
import org.apache.commons.lang.mutable.MutableShort;
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
import java.util.HashMap;
import java.util.Map;

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

    private MutableByte ticker = new MutableByte(-128);
    private Map<Class, Byte> map = new HashMap<Class, Byte>();

    public DefaultCrossReferenceIndexWriter(LuceneIndex index) throws IOException {
        super(index);
    }

    public void write(String accession, Identifier xref) throws IOException {

        Document document = new Document();

        document.add(create(QueryService.IDENTIFIER, accession));
        document.add(create(CrossReferenceService.DATABASE_IDENTIFIER_INDEX,
                            getIndex(xref).toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(create(CrossReferenceService.DATABASE_ACCESSION,
                            xref.getAccession()));

        add(document);

    }

    public Byte getIndex(Identifier identifier) throws IOException {

        Class c = identifier.getClass();

        if (map.containsKey(c)) {
            return map.get(c);
        }

        return newIdentifierDocument(c);

    }


    public Byte newIdentifierDocument(Class c) throws IOException {

        Byte value = ticker.byteValue();
        map.put(c, value);
        ticker.increment();

        Document document = new Document();
        document.add(new Field("class", c.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
        document.add(new Field("class-index", value.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));

        add(document);

        return value;
    }


}
