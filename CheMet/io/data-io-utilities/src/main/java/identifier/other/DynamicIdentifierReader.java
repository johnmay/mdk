package identifier.other;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.io.core.TaggedOutputStream;
import uk.ac.ebi.chemet.io.identifier.IdentifierReader;
import uk.ac.ebi.chemet.resource.base.DynamicIdentifier;
import uk.ac.ebi.resource.organism.Taxonomy;

import java.io.DataInput;
import java.io.IOException;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class DynamicIdentifierReader implements IdentifierReader<DynamicIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(DynamicIdentifierReader.class);

    private DataInput in;

    public DynamicIdentifierReader(DataInput in){
        this.in = in;
    }

    @Override
    public DynamicIdentifier readIdentifier() throws IOException, ClassNotFoundException {
        return new DynamicIdentifier(in.readUTF(), in.readUTF(), in.readUTF());
    }
}
