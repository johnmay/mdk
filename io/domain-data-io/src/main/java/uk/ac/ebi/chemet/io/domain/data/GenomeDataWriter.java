package uk.ac.ebi.chemet.io.domain.data;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.chemet.io.domain.EntityOutput;
import uk.ac.ebi.chemet.io.domain.EntityWriter;
import uk.ac.ebi.interfaces.Chromosome;
import uk.ac.ebi.interfaces.Genome;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;

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
public class GenomeDataWriter
        implements EntityWriter<Genome> {

    private static final Logger LOGGER = Logger.getLogger(GenomeDataWriter.class);

    private DataOutput out;
    private EntityOutput entityOut;

    public GenomeDataWriter(DataOutput out, EntityOutput entityOut){
        this.out = out;
        this.entityOut = entityOut;
    }

    public void write(Genome genome) throws IOException {


        Collection<Chromosome> chromosomes = genome.getChromosomes();

        // write the number of chromosomes
        out.writeInt(chromosomes.size());

        // write the chromosome objects
        for(Chromosome chromosome : chromosomes){
            entityOut.writeData(chromosome);
        }

    }

}
