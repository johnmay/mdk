package uk.ac.ebi.chemet.io.domain.data;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.chemet.io.core.AbstractDataOutput;
import uk.ac.ebi.chemet.io.domain.EntityOutput;
import uk.ac.ebi.chemet.io.domain.EntityWriter;
import uk.ac.ebi.chemet.io.identifier.IdentifierOutput;
import uk.ac.ebi.interfaces.Genome;
import uk.ac.ebi.interfaces.entities.GeneProduct;
import uk.ac.ebi.interfaces.entities.IReconstruction;
import uk.ac.ebi.interfaces.entities.MetabolicReaction;
import uk.ac.ebi.interfaces.entities.Metabolite;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;

/**
 * ReconstructionDataWriter - 12.03.2012 <br/>
 * <p/>
 * Writes a reconstruction to a data output stream.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class ReconstructionDataWriter
        implements EntityWriter<IReconstruction> {

    private static final Logger LOGGER = Logger.getLogger(ReconstructionDataWriter.class);

    private DataOutput out;
    private EntityOutput entityOut;
    private IdentifierOutput identifierOutput;
    private AbstractDataOutput output;

    public ReconstructionDataWriter(DataOutput out,
                                    IdentifierOutput identifierOutput,
                                    EntityOutput entityOut){
        this.out = out;
        this.identifierOutput = identifierOutput;
        this.entityOut = entityOut;
    }

    public void write(IReconstruction reconstruction) throws IOException {

        // write taxonomy identifier
        identifierOutput.write(reconstruction.getTaxonomy());

        // container
        out.writeUTF(reconstruction.getContainer().getAbsolutePath());

        // GENOME
        Genome genome = reconstruction.getGenome();
        entityOut.writeData(genome);

        // METABOLOME
        Collection<Metabolite> metabolites = reconstruction.getMetabolome();
        out.writeInt(metabolites.size());
        for(Metabolite m : metabolites){
            entityOut.writeData(m);
        }

        // PROTEOME
        Collection<GeneProduct> products = reconstruction.getProducts();
        out.writeInt(products.size());
        for(GeneProduct p : products){
            entityOut.write(p);
        }

        // REACTOME
        Collection<MetabolicReaction> reactions = reconstruction.getReactome();
        out.writeInt(reactions.size());
        for(MetabolicReaction r : reactions){
            entityOut.writeData(r);
        }

    }

}
