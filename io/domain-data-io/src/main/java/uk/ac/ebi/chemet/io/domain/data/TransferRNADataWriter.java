package uk.ac.ebi.chemet.io.domain.data;

import org.apache.log4j.Logger;
import org.biojava3.core.sequence.RNASequence;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.chemet.io.domain.EntityOutput;
import uk.ac.ebi.chemet.io.domain.EntityWriter;
import uk.ac.ebi.chemet.io.util.SequenceSerializer;
import uk.ac.ebi.interfaces.Gene;
import uk.ac.ebi.interfaces.entities.TransferRNA;

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
public class TransferRNADataWriter
        implements EntityWriter<TransferRNA> {

    private static final Logger LOGGER = Logger.getLogger(TransferRNADataWriter.class);

    private DataOutput out;
    private EntityOutput eout;

    public TransferRNADataWriter(DataOutput out, EntityOutput eout){
        this.out = out;
        this.eout = eout;
    }

    public void write(TransferRNA trna) throws IOException {

        // number of sequences
        out.writeInt(trna.getSequences().size());

        // write the sequences
        for (RNASequence sequence : trna.getSequences()) {
            SequenceSerializer.writeRNASequence(sequence, out);
        }

        // write associated genes
        Collection<Gene> genes = trna.getGenes();
        out.writeByte(genes.size());
        for(Gene gene : genes){
            eout.writeData(gene);
        }

    }

}
