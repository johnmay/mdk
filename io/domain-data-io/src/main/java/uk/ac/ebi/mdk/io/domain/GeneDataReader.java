package uk.ac.ebi.mdk.io.domain;

import org.apache.log4j.Logger;
import org.biojava3.core.sequence.Strand;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.EnumReader;
import uk.ac.ebi.mdk.io.EntityReader;
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;

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
public class GeneDataReader
        implements EntityReader<Gene> {

    private static final Logger LOGGER = Logger.getLogger(GeneDataReader.class);

    private DataInput in;
    private EnumReader enumIn;
    private EntityFactory factory;

    public GeneDataReader(DataInput in, EntityFactory factory) {
        this.in = in;
        this.factory = factory;
        this.enumIn = new EnumReader(in);
    }

    public Gene readEntity() throws IOException, ClassNotFoundException {

        Gene gene = factory.newInstance(Gene.class);

        gene.setStart(in.readInt());
        gene.setEnd(in.readInt());
        gene.setStrand((Strand) enumIn.readEnum());

        return gene;

    }

}
