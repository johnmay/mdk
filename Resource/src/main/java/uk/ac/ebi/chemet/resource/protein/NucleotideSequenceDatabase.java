package uk.ac.ebi.chemet.resource.protein;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.resource.base.AbstractIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.type.ProteinIdentifier;
import uk.ac.ebi.resource.MIR;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * NCBIReferenceSequence - 14.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@MIR(value =29)
public class NucleotideSequenceDatabase
        extends AbstractIdentifier
        implements ProteinIdentifier {

    private static final Logger LOGGER = Logger.getLogger(NucleotideSequenceDatabase.class);

    private static final Pattern HEADER_CODE = Pattern.compile("gp|ddbj|emb");
    
    public NucleotideSequenceDatabase() {
        super();
    }

    public NucleotideSequenceDatabase(String accession){
        super(accession);
    }

    @Override
    public NucleotideSequenceDatabase ofHeader(Iterator<String> token) {

        String accession = token.hasNext() ? token.next() : "";
        String locus     = token.hasNext() ? token.next() : "";

        return new NucleotideSequenceDatabase(accession);

    }


    @Override
    public Collection<String> getHeaderCodes() {
        return Arrays.asList("gb", "ddbj", "emb");
    }

    @Override
    public Identifier newInstance() {
        return new NucleotideSequenceDatabase();
    }
}
