package uk.ac.ebi.mdk.domain.identifier;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.type.ProteinIdentifier;
import uk.ac.ebi.mdk.deprecated.MIR;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * NCBIReferenceSequence - 14.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@MIR(39)
public class NCBIReferenceSequence
        extends AbstractIdentifier
        implements ProteinIdentifier {

    private static final Logger LOGGER = Logger.getLogger(NCBIReferenceSequence.class);

    public NCBIReferenceSequence() {
        super();
    }

    public NCBIReferenceSequence(String accession){
        super(accession);
    }

    @Override
    public NCBIReferenceSequence ofHeader(Iterator<String> token) {

        String accession = token.hasNext() ? token.next() : "";
        String name      = token.hasNext() ? token.next() : "";

        return new NCBIReferenceSequence(accession);
    }


    @Override
    public Collection<String> getHeaderCodes() {
        return Arrays.asList("ref");
    }

    @Override
    public Identifier newInstance() {
        return new NCBIReferenceSequence();
    }
}
