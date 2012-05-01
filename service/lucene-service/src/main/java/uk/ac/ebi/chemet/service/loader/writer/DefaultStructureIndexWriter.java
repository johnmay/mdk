package uk.ac.ebi.chemet.service.loader.writer;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.fingerprint.Fingerprinter;
import org.openscience.cdk.fingerprint.IFingerprinter;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.chemet.service.analyzer.FingerprintSimilarity;
import uk.ac.ebi.mdk.service.index.LuceneIndex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.BitSet;

import static org.apache.lucene.document.Field.Index.ANALYZED;
import static org.apache.lucene.document.Field.Store.NO;
import static uk.ac.ebi.mdk.service.query.structure.StructureService.*;

/**
 * ${Name}.java - 20.02.2012 <br/> MetaInfo...
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class DefaultStructureIndexWriter extends AbstractIndexWriter {

    private static final Logger LOGGER = Logger.getLogger(DefaultStructureIndexWriter.class);

    private IFingerprinter fingerprinter;

    public DefaultStructureIndexWriter(LuceneIndex index, IFingerprinter fingerprinter) throws IOException {
        super(index);
        this.fingerprinter = fingerprinter;
        getWriter().getConfig().setSimilarity(new FingerprintSimilarity());
    }

    public DefaultStructureIndexWriter(LuceneIndex index) throws IOException {
        this(index, new Fingerprinter());
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
        write(identifier, bos.toByteArray(), getFingerprint(molecule));


    }

    public BitSet getFingerprint(IAtomContainer molecule){
        try {
            return fingerprinter.getFingerprint(molecule);
        } catch (CDKException ex){
            LOGGER.warn("Fingerprint was not calculated for molecule: " + molecule );
            return new BitSet();
        }
    }

    private void write(String identifier,
                    byte[] molecule,
                    BitSet fp) throws IOException {

        Document document = new Document();

        // store the identifier and AtomContainer values
        document.add(create(IDENTIFIER, identifier));
        document.add(new Field(ATOM_CONTAINER.field(), molecule));

        // index the fingerprint
        for(int i = fp.nextSetBit(0); i != -1; i = fp.nextSetBit(i + 1)){
            document.add(create(FINGERPRINT_BIT, Integer.toString(i), NO, ANALYZED));
        }

        add(document);

    }


}
