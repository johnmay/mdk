package uk.ac.ebi.mdk.io.domain;

import org.apache.log4j.Logger;
import org.biojava3.core.sequence.RNASequence;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.TransferRNA;
import uk.ac.ebi.mdk.io.EntityOutput;
import uk.ac.ebi.mdk.io.EntityWriter;
import uk.ac.ebi.mdk.io.SequenceSerializer;

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
@CompatibleSince("1.3.9")
public class TransferRNADataWriter_1_3_9
        implements EntityWriter<TransferRNA> {

    private static final Logger LOGGER = Logger.getLogger(TransferRNADataWriter_1_3_9.class);

    private DataOutput out;
    private EntityOutput eout;

    public TransferRNADataWriter_1_3_9(DataOutput out, EntityOutput eout){
        this.out = out;
        this.eout = eout;
    }

    public void write(TransferRNA trna) throws IOException {

        out.writeUTF(trna.uuid().toString());

        // number of sequences
        out.writeInt(trna.getSequences().size());

        // write the sequences
        for (RNASequence sequence : trna.getSequences()) {
            SequenceSerializer.writeRNASequence(sequence, out);
        }


    }

}
