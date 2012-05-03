package uk.ac.ebi.mdk.io.identifier;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.IdentifierReader;
import uk.ac.ebi.chemet.resource.base.DynamicIdentifier;

import java.io.DataInput;
import java.io.IOException;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
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
