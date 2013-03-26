package uk.ac.ebi.mdk.io.domain;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.io.EntityInput;
import uk.ac.ebi.mdk.io.EntityReader;
import uk.ac.ebi.mdk.io.SequenceSerializer;
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.ProteinProduct;

import java.io.DataInput;
import java.io.IOException;

/**
 * ProteinProductDataReader - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class ProteinProductDataReader
        implements EntityReader<ProteinProduct> {

    private static final Logger LOGGER = Logger.getLogger(ProteinProductDataReader.class);

    private DataInput in;
    private EntityFactory factory;
    private EntityInput ein;

    public ProteinProductDataReader(DataInput in, EntityFactory factory, EntityInput ein) {
        this.in = in;
        this.factory = factory;
        this.ein = ein;
    }

    public ProteinProduct readEntity(Reconstruction reconstruction) throws IOException, ClassNotFoundException {

        ProteinProduct p = factory.newInstance(ProteinProduct.class);

        int n = in.readInt();
        
        for(int i = 0 ; i < n; i++) {
            p.addSequence(SequenceSerializer.readProteinSequence(in));
        }

        int nGenes = in.readByte();
        for(int i = 0; i < nGenes; i++){
            reconstruction.associate(ein.read(Gene.class, reconstruction), p);
        }

        return p;

    }

}
