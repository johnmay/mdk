package uk.ac.ebi.chemet.service.loader.writer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.util.Version;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.service.index.LuceneIndex;
import uk.ac.ebi.service.query.QueryService;
import uk.ac.ebi.service.query.StructureService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * ${Name}.java - 20.02.2012 <br/> Description...
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class DefaultStructureIndexWriter {


    private IndexWriter writer;


    public DefaultStructureIndexWriter(LuceneIndex index) throws IOException {
        writer = new IndexWriter(index.getDirectory(),
                                 new IndexWriterConfig(Version.LUCENE_34,
                                                       index.getAnalyzer()));
    }

    /**
     * Write a CDK molecule and it's identifier to the index
     * @param identifier
     * @param molecule
     * @throws IOException
     */
    public void write(String identifier,
                      IAtomContainer molecule) throws IOException {

        // Serialize to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(molecule);
        out.close();

        // Get the bytes of the serialized object
        write(identifier, bos.toByteArray());


    }

    private void write(String identifier,
                    byte[] molecule) throws IOException {

        Document document = new Document();
        document.add(new Field(QueryService.IDENTIFIER.field(),
                               identifier
                , Field.Store.NO, Field.Index.ANALYZED));
        document.add(new Field(StructureService.ATOM_CONTAINER.field(), molecule));
        writer.addDocument(document);

    }

    public void close() throws IOException {
        writer.close();
    }

}
