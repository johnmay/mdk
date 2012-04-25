package uk.ac.ebi.chemet.service.loader.writer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.fingerprint.Fingerprinter;
import org.openscience.cdk.fingerprint.IFingerprinter;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.chemet.service.analyzer.ChemicalSimilarity;
import uk.ac.ebi.service.index.LuceneIndex;
import uk.ac.ebi.service.query.QueryService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

import static uk.ac.ebi.service.query.StructureService.ATOM_CONTAINER;
import static uk.ac.ebi.service.query.StructureService.FINGERPRINT_BIT;

/**
 * ${Name}.java - 20.02.2012 <br/> MetaInfo...
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class DefaultStructureIndexWriter extends AbstractIndexWriter {


    private IFingerprinter fingerprinter = new Fingerprinter();


    public DefaultStructureIndexWriter(LuceneIndex index) throws IOException {
        super(index);
        getWriter().getConfig().setSimilarity(new ChemicalSimilarity());
    }

    /**
     * Write a CDK molecule and it's identifier to the index
     *
     * @param identifier
     * @param molecule
     *
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



    /**
     * Calculates the fingerprint for the atomconatiner. If an exception occurs
     * during calculation an empty bit set is returned.
     * @param atomContainer
     * @return
     */
    public BitSet getFingerprint(IAtomContainer atomContainer) {
        try {
            return fingerprinter.getFingerprint(atomContainer);
        } catch (CDKException ex) {
            return new BitSet();
        }
    }


    private void write(String identifier,
                       byte[] molecule,
                       BitSet fp) throws IOException {

        Document document = new Document();
        document.add(new Field(QueryService.IDENTIFIER.field(),
                               identifier
                , Field.Store.YES, Field.Index.ANALYZED));

        document.add(new Field(ATOM_CONTAINER.field(), molecule));


        // add the finger print
        for (Fieldable field : getFingerprintFields(fp)) {
            document.add(field);
        }

        add(document);

    }

    /**
     * Converts a fingerprint bitset into lucene fields
     *
     * @param fp molecule finger print
     *
     * @return
     */
    public Set<Fieldable> getFingerprintFields(BitSet fp) {

        Set<Fieldable> fields = new HashSet<Fieldable>();

        for (int i = fp.nextSetBit(0); i != -1; i = fp.nextSetBit(i + 1)) {
            fields.add(create(FINGERPRINT_BIT, Integer.toString(i), Field.Store.NO, Field.Index.NOT_ANALYZED));
        }

        return fields;

    }


}
