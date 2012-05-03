package uk.ac.ebi.mdk.io.domain;

import org.apache.log4j.Logger;
import org.biojava3.core.sequence.ChromosomeSequence;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.EnumReader;
import uk.ac.ebi.mdk.io.EntityInput;
import uk.ac.ebi.mdk.io.EntityReader;
import uk.ac.ebi.mdk.io.SequenceSerializer;
import uk.ac.ebi.mdk.domain.entity.collection.Chromosome;
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
public class ChromosomeDataReader
        implements EntityReader<Chromosome> {

    private static final Logger LOGGER = Logger.getLogger(ChromosomeDataReader.class);

    private DataInput in;
    private EnumReader enumIn;
    private EntityFactory factory;
    private EntityInput   entityIn;

    public ChromosomeDataReader(DataInput in, EntityFactory factory, EntityInput entityIn) {
        this.in = in;
        this.factory = factory;
        this.enumIn = new EnumReader(in);
        this.entityIn = entityIn;
    }

    public Chromosome readEntity() throws IOException, ClassNotFoundException {

        Chromosome chromosome = factory.newInstance(Chromosome.class);

        chromosome.setSequence(new ChromosomeSequence(SequenceSerializer.readDNASequence(in).toString()));

        int n = in.readInt(); // n genes
        for(int i = 0 ; i < n; i++){
            chromosome.add(entityIn.read(Gene.class));
        }
        
        return chromosome;

    }

}
