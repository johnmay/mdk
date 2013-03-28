package uk.ac.ebi.mdk.io.domain;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.io.EntityWriter;
import uk.ac.ebi.mdk.io.EnumWriter;

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
@CompatibleSince("1.3.9")
public class GeneDataWriter_1_3_9
        implements EntityWriter<Gene> {

    private static final Logger LOGGER = Logger.getLogger(GeneDataWriter_1_3_9.class);

    private DataOutput out;
    private EnumWriter enumOut;

    public GeneDataWriter_1_3_9(DataOutput out){
        this.out = out;
        enumOut = new EnumWriter(out);
    }

    public void write(Gene gene) throws IOException {
        out.writeUTF(gene.uuid().toString());
        out.writeInt(gene.getStart());
        out.writeInt(gene.getEnd());
        enumOut.writeEnum(gene.getStrand());

    }

}
