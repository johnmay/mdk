package uk.ac.ebi.mdk.io.domain;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.io.EntityReader;
import uk.ac.ebi.mdk.io.EnumReader;

import java.io.DataInput;
import java.io.IOException;
import java.util.UUID;

/**
 * ProteinProductDataWriter - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("1.3.9")
public class MetaboliteDataReader_1_3_9
        implements EntityReader<Metabolite> {

    private static final Logger LOGGER = Logger.getLogger(MetaboliteDataReader_1_3_9.class);

    private DataInput in;
    private EnumReader enumIn;
    private EntityFactory factory;

    public MetaboliteDataReader_1_3_9(DataInput in, EntityFactory factory) {
        this.in = in;
        this.factory = factory;
        this.enumIn = new EnumReader(in);
    }

    public Metabolite readEntity(Reconstruction reconstruction) throws IOException, ClassNotFoundException {

        Metabolite m = factory.metabolite(UUID.fromString(in.readUTF()));

        m.setGeneric(in.readBoolean());
        m.setType(enumIn.readEnum());

        return m;

    }

}
