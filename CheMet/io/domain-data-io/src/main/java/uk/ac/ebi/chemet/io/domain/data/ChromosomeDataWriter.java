package uk.ac.ebi.chemet.io.domain.data;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.chemet.io.domain.EntityOutput;
import uk.ac.ebi.chemet.io.domain.EntityWriter;
import uk.ac.ebi.chemet.io.util.SequenceSerializer;
import uk.ac.ebi.interfaces.Chromosome;
import uk.ac.ebi.interfaces.Gene;

import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

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
public class ChromosomeDataWriter
        implements EntityWriter<Chromosome> {

    private static final Logger LOGGER = Logger.getLogger(ChromosomeDataWriter.class);

    private DataOutput out;
    private EntityOutput entityOut;

    public ChromosomeDataWriter(DataOutput out, EntityOutput entityOut){
        this.out = out;
        this.entityOut = entityOut;
    }

    public void write(Chromosome chromosome) throws IOException {

        // write the sequence (DNA)
        SequenceSerializer.writeDNASequence(chromosome.getSequence(), out);
        
        List<Gene> genes = chromosome.getGenes();

        // write the number of genes
        out.writeInt(genes.size());

        // write the gene objects
        for(Gene gene : genes){
            entityOut.writeData(gene);
        }

    }

}
