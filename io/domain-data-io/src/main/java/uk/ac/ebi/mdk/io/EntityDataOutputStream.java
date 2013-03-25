package uk.ac.ebi.mdk.io;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.io.domain.GeneDataWriter_1_3_9;
import uk.ac.ebi.mdk.io.domain.MetaboliteDataWriter_1_3_9;
import uk.ac.ebi.mdk.io.domain.ProteinProductDataWriter;
import uk.ac.ebi.mdk.io.domain.ProteinProductDataWriter_1_3_9;
import uk.ac.ebi.mdk.io.domain.ReactionDataWriter_1_3_9;
import uk.ac.ebi.mdk.io.domain.ReconstructionDataWriter_1_3_3;
import uk.ac.ebi.mdk.io.domain.RibosomalRNADataWriter;
import uk.ac.ebi.mdk.io.domain.RibosomalRNADataWriter_1_3_9;
import uk.ac.ebi.mdk.io.domain.TransferRNADataWriter;
import uk.ac.ebi.mdk.io.domain.MetaboliteDataWriter;
import uk.ac.ebi.mdk.io.domain.ReactionDataWriter;
import uk.ac.ebi.mdk.io.domain.ReconstructionDataWriter_0_9;
import uk.ac.ebi.mdk.io.domain.ChromosomeDataWriter;
import uk.ac.ebi.mdk.io.domain.GeneDataWriter;
import uk.ac.ebi.mdk.io.domain.GenomeDataWriter;
import uk.ac.ebi.mdk.domain.entity.*;
import uk.ac.ebi.mdk.domain.entity.collection.Chromosome;
import uk.ac.ebi.mdk.domain.entity.collection.Genome;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.io.domain.AnnotatedEntityDataWriter;
import uk.ac.ebi.mdk.io.domain.EntityDataWriter;
import uk.ac.ebi.mdk.io.domain.TransferRNADataWriter_1_3_9;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * EntityDataOutputStream - 11.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class EntityDataOutputStream
        extends AbstractDataOutput<EntityWriter>
        implements EntityOutput {

    private static final Logger LOGGER = Logger.getLogger(EntityDataOutputStream.class);

    private DataOutputStream out;
    private EntityFactory factory;
    private AnnotationOutput  annotationOut;
    private ObservationOutput observationOut;
    private IdentifierOutput  identifierOutput;


    private EntityDataWriter entityWriter;
    private AnnotatedEntityDataWriter annotatedEntityWriter;

    public EntityDataOutputStream(Version version,
                                  DataOutputStream out,
                                  EntityFactory factory,
                                  AnnotationOutput annotationOut,
                                  ObservationOutput observationOut) {

        super(out, version);

        this.out = out;
        this.annotationOut = annotationOut;
        this.observationOut = observationOut;
        this.identifierOutput = new IdentifierDataOutputStream(out, version);
        this.factory = factory;

        entityWriter = new EntityDataWriter(out, identifierOutput);
        annotatedEntityWriter = new AnnotatedEntityDataWriter(out, annotationOut, observationOut);


        // metabolome/reactome
        add(Metabolite.class, new MetaboliteDataWriter(out));
        add(Metabolite.class, new MetaboliteDataWriter_1_3_9(out));
        add(MetabolicReaction.class, new ReactionDataWriter(out, this));
        add(MetabolicReaction.class, new ReactionDataWriter_1_3_9(out, this));

        // gene
        add(Gene.class, new GeneDataWriter(out));
        add(Gene.class, new GeneDataWriter_1_3_9(out));

        // gene products
        add(ProteinProduct.class, new ProteinProductDataWriter(out, this));
        add(ProteinProduct.class, new ProteinProductDataWriter_1_3_9(out, this));
        add(RibosomalRNA.class, new RibosomalRNADataWriter(out, this));
        add(RibosomalRNA.class, new RibosomalRNADataWriter_1_3_9(out, this));
        add(TransferRNA.class, new TransferRNADataWriter(out, this));
        add(TransferRNA.class, new TransferRNADataWriter_1_3_9(out, this));

        add(Chromosome.class, new ChromosomeDataWriter(out, this));
        add(Genome.class, new GenomeDataWriter(out, this));

        add(Reconstruction.class, new ReconstructionDataWriter_0_9(out, identifierOutput,  this));
        add(Reconstruction.class, new ReconstructionDataWriter_1_3_3(out, identifierOutput,  this));

    }


    @Override
    public void writeData(Entity entity) throws IOException {

        if (writeObjectId(entity)) {

            Class c = factory.getEntityClass(entity.getClass());

            EntityWriter writer = getMarshaller(c, getVersion());

            writer.write(entity);                                  // entity specifics
            if (entity instanceof AnnotatedEntity) {
                annotatedEntityWriter.write((AnnotatedEntity) entity); // annotations and observations
                entityWriter.write(entity);                            // name, abbr and id
            }

        }

    }

    @Override
    public void write(Entity entity) throws IOException {

        Class c = factory.getEntityClass(entity.getClass());
        writeClass(c);
        writeData(entity);

    }

}
