package uk.ac.ebi.chemet.io.identifier.basic;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.IdentifierReader;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.resource.DefaultIdentifierFactory;

import java.io.DataInput;
import java.io.IOException;

/**
 * TaxonomyWriter - 13.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class BasicIdentifierReader implements IdentifierReader<Identifier> {

    private static final Logger LOGGER = Logger.getLogger(BasicIdentifierReader.class);

    private static final DefaultIdentifierFactory FACTORY = DefaultIdentifierFactory.getInstance();

    private DataInput in;
    private Class     c;

    public BasicIdentifierReader(Class c, DataInput in) {
        this.c  = c;
        this.in = in;
    }

    @Override
    public Identifier readIdentifier() throws IOException {

        Identifier identifier = FACTORY.ofClass(c);
        String     accession  = in.readUTF();

        identifier.setAccession(accession);

        return identifier;

    }
}
