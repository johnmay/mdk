package uk.ac.ebi.chemet.service.loader.writer;

import org.apache.commons.lang.mutable.MutableByte;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.index.LuceneIndex;
import uk.ac.ebi.mdk.service.query.QueryService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static uk.ac.ebi.mdk.service.query.CrossReferenceService.*;

/**
 * Unified writing of cross-references to a lucene-index
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
        document.add(create(DATABASE_IDENTIFIER_INDEX,
                            getIndex(xref).toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(create(DATABASE_ACCESSION,
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

        Byte id = ticker.byteValue();
        map.put(c, id);
        ticker.increment();

        Document document = new Document();

        document.add(create(CLASS_NAME,
                            c.getName(),
                            Field.Store.YES,
                            Field.Index.NOT_ANALYZED));
        document.add(create(CLASS_ID,
                            id.toString(),
                            Field.Store.YES,
                            Field.Index.NOT_ANALYZED));

        add(document);

        return id;
    }


}
