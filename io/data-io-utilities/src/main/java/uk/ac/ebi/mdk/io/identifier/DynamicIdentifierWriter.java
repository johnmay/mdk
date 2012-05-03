package uk.ac.ebi.mdk.io.identifier;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.IdentifierWriter;
import uk.ac.ebi.chemet.resource.base.DynamicIdentifier;

import java.io.DataOutput;
import java.io.IOException;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class DynamicIdentifierWriter implements IdentifierWriter<DynamicIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(DynamicIdentifierWriter.class);

    private DataOutput out;
    
    public DynamicIdentifierWriter(DataOutput out){
        this.out = out;
    }
    
    @Override
    public void write(DynamicIdentifier identifier) throws IOException {
        out.writeUTF(identifier.getShortDescription());
        out.writeUTF(identifier.getLongDescription());
        out.writeUTF(identifier.getAccession());
    }
}
