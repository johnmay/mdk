package uk.ac.ebi.mdk.io.domain;

import org.apache.log4j.Logger;
import org.biojava3.core.sequence.ProteinSequence;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.EntityOutput;
import uk.ac.ebi.mdk.io.EntityWriter;
import uk.ac.ebi.mdk.io.SequenceSerializer;
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.ProteinProduct;

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
public class ProteinProductDataWriter
        implements EntityWriter<ProteinProduct> {

    private static final Logger LOGGER = Logger.getLogger(ProteinProductDataWriter.class);

    private DataOutput out;
    private EntityOutput eout;

    public ProteinProductDataWriter(DataOutput out, EntityOutput eout){
        this.out  = out;
        this.eout = eout;
    }

    public void write(ProteinProduct protein) throws IOException {

        // number of sequences
        out.writeInt(protein.getSequences().size());

        // write the sequences
        for (ProteinSequence sequence : protein.getSequences()) {
            SequenceSerializer.writeProteinSequence(sequence, out);
        }
        
        // write associated genes
        // now done in reaction
//        Collection<Gene> genes = protein.genes();
//        out.writeByte(genes.size());
//        for(Gene gene : genes){
//            eout.writeData(gene);
//        }

    }

}
