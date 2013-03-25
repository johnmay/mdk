package uk.ac.ebi.mdk.io.domain;

import org.apache.log4j.Logger;
import org.biojava3.core.sequence.Strand;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Gene;
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
public class GeneDataReader_1_3_9
        implements EntityReader<Gene> {

    private static final Logger LOGGER = Logger.getLogger(GeneDataReader_1_3_9.class);

    private DataInput in;
    private EnumReader enumIn;
    private EntityFactory factory;

    public GeneDataReader_1_3_9(DataInput in, EntityFactory factory) {
        this.in = in;
        this.factory = factory;
        this.enumIn = new EnumReader(in);
    }

    public Gene readEntity(Reconstruction reconstruction) throws IOException, ClassNotFoundException {

        Gene gene = factory.gene(UUID.fromString(in.readUTF()));

        gene.setStart(in.readInt());
        gene.setEnd(in.readInt());
        gene.setStrand((Strand) enumIn.readEnum());

        return gene;

    }

}
