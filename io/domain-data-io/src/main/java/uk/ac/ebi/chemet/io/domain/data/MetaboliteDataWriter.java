package uk.ac.ebi.chemet.io.domain.data;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.chemet.io.core.EnumWriter;
import uk.ac.ebi.mdk.io.EntityWriter;
import uk.ac.ebi.mdk.domain.entity.Metabolite;

import java.io.DataOutput;
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
public class MetaboliteDataWriter
        implements EntityWriter<Metabolite> {

    private static final Logger LOGGER = Logger.getLogger(MetaboliteDataWriter.class);

    private DataOutput out;
    private EnumWriter enumOut;

    public MetaboliteDataWriter(DataOutput out){
        this.out = out;
        enumOut = new EnumWriter(out);
    }

    public void write(Metabolite metabolite) throws IOException {

        out.writeBoolean(metabolite.isGeneric());
        enumOut.writeEnum(metabolite.getType());

    }

}
