package uk.ac.ebi.chemet.io.domain.data;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.EntityReader;
import uk.ac.ebi.mdk.io.IdentifierInput;
import uk.ac.ebi.mdk.domain.entity.Entity;
import uk.ac.ebi.resource.DefaultIdentifierFactory;

import java.io.DataInput;
import java.io.IOException;

/**
 * ProteinProductDataWriter - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class EntityDataReader
        implements EntityReader<Entity> {

    private static final Logger LOGGER = Logger.getLogger(EntityDataReader.class);

    private static final DefaultIdentifierFactory factory = DefaultIdentifierFactory.getInstance();

    private DataInput in;
    private IdentifierInput identifierInput;
    
    private Entity entity;

    public EntityDataReader(DataInput in,
                            IdentifierInput identifierInput) {
        this.in = in;
        this.identifierInput = identifierInput;
    }

    public void setEntity(Entity entity){
        this.entity = entity;
    }
    
    public Entity readEntity() throws IOException, ClassNotFoundException {

        entity.setName(in.readUTF());
        entity.setAbbreviation(in.readUTF());
        entity.setIdentifier(identifierInput.read());

        return entity;

    }

}
