package uk.ac.ebi.chemet.io.domain.data;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.chemet.io.core.EnumReader;
import uk.ac.ebi.mdk.io.EntityReader;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;

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
public class MetaboliteDataReader
        implements EntityReader<Metabolite> {

    private static final Logger LOGGER = Logger.getLogger(MetaboliteDataReader.class);

    private DataInput in;
    private EnumReader enumIn;
    private EntityFactory factory;

    public MetaboliteDataReader(DataInput in, EntityFactory factory) {
        this.in = in;
        this.factory = factory;
        this.enumIn = new EnumReader(in);
    }

    public Metabolite readEntity() throws IOException, ClassNotFoundException {

        Metabolite m = factory.newInstance(Metabolite.class);

        m.setGeneric(in.readBoolean());
        m.setType(enumIn.readEnum());

        return m;

    }

}
