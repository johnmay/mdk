package uk.ac.ebi.mdk.io.domain;

import org.apache.log4j.Logger;
import org.biojava3.core.sequence.ChromosomeSequence;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.collection.Chromosome;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.io.EntityInput;
import uk.ac.ebi.mdk.io.EntityReader;
import uk.ac.ebi.mdk.io.EnumReader;
import uk.ac.ebi.mdk.io.IdentifierInput;
import uk.ac.ebi.mdk.io.SequenceSerializer;

import java.io.DataInput;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ProteinProductDataWriter - 12.03.2012 <br/> <p/> Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class ReconstructionDataReader_0_9
        implements EntityReader<Reconstruction> {

    private static final Logger LOGGER = Logger
            .getLogger(ReconstructionDataReader_0_9.class);

    private DataInput in;
    private EntityInput entityIn;
    private IdentifierInput identifierInput;
    private EnumReader enumReader;
    private EntityFactory factory;

    public ReconstructionDataReader_0_9(DataInput in,
                                        EntityFactory factory,
                                        IdentifierInput identifierInput,
                                        EntityInput entityIn) {
        this.in = in;
        this.identifierInput = identifierInput;
        this.entityIn = entityIn;
        this.enumReader = new EnumReader(in);
        this.factory = factory;
    }

    public Reconstruction readEntity(Reconstruction reconstruction) throws
                                                                    IOException,
                                                                    ClassNotFoundException {

        Reconstruction recon = factory.newInstance(Reconstruction.class);

        recon.setTaxonomy(identifierInput.read());

        // container
        recon.setContainer(new File(in.readUTF())); // set container

        // GENOME
        readGenome(recon);

        // METABOLOME
        int metabolites = in.readInt();
        for (int i = 0; i < metabolites; i++) {
            recon.addMetabolite(entityIn.read(Metabolite.class, recon));
        }

        // PROTEOME
        int products = in.readInt();
        for (int i = 0; i < products; i++) {
            recon.addProduct((GeneProduct) entityIn.read(recon));
        }

        // REACTOME
        int reactions = in.readInt();
        for (int i = 0; i < reactions; i++) {
            recon.addReaction(entityIn.read(MetabolicReaction.class, recon));
        }

        return recon;

    }

    public void readGenome(Reconstruction recon) throws IOException,
                                                        ClassNotFoundException {
        in.readInt(); // genome object id
        int nChromosomes = in.readInt();
        for (int i = 0; i < nChromosomes; i++) {

            in.readInt(); // chromosome object id
            ChromosomeSequence seq = new ChromosomeSequence(SequenceSerializer
                                                                    .readDNASequence(in)
                                                                    .toString());

            int nGenes = in.readInt();
            List<Gene> genes = new ArrayList<Gene>(nGenes);
            for (int g = 0; g < nGenes; g++) {
                Gene gene = entityIn.read(Gene.class, recon);
                genes.add(gene);
                // todo, add association
            }

            // annotated entity fields
            in.readInt(); // enum object id
            int nAnnotations = in.readInt();
            if (nAnnotations != 0)
                throw new IOException("chromosomes from 1.3.3 should not have annotations, contact devs");
            int nObservations = in.readInt();
            if (nObservations != 0)
                throw new IOException("chromosomes from 1.3.3 should not have observations, contact devs");


            in.readUTF(); // name
            in.readUTF(); // abrv
            identifierInput.read(); // chromosome id -> we lose the number

            Chromosome chromosome = recon.getGenome().createChromosome(i + 1, seq);
            chromosome.addAll(genes);
        }
    }

}
