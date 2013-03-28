package uk.ac.ebi.mdk.io.domain;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.ProteinProduct;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.io.EntityInput;
import uk.ac.ebi.mdk.io.EntityReader;
import uk.ac.ebi.mdk.io.SequenceSerializer;

import java.io.DataInput;
import java.io.IOException;
import java.util.UUID;

/**
 * ProteinProductDataReader - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("1.3.9")
public class ProteinProductDataReader_1_3_9
        implements EntityReader<ProteinProduct> {

    private static final Logger LOGGER = Logger.getLogger(ProteinProductDataReader_1_3_9.class);

    private DataInput in;
    private EntityFactory factory;
    private EntityInput ein;

    public ProteinProductDataReader_1_3_9(DataInput in, EntityFactory factory, EntityInput ein) {
        this.in = in;
        this.factory = factory;
        this.ein = ein;
    }

    public ProteinProduct readEntity(Reconstruction reconstruction) throws IOException, ClassNotFoundException {

        ProteinProduct p = factory.protein(UUID.fromString(in.readUTF()));

        int n = in.readInt();
        
        for(int i = 0 ; i < n; i++) {
            p.addSequence(SequenceSerializer.readProteinSequence(in));
        }

        return p;

    }

}
