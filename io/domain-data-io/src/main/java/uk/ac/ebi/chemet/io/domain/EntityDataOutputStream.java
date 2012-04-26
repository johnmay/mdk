package uk.ac.ebi.chemet.io.domain;

import uk.ac.ebi.chemet.io.identifier.IdentifierDataOutputStream;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.io.AnnotationOutput;
import uk.ac.ebi.chemet.io.core.AbstractDataOutput;
import uk.ac.ebi.chemet.io.domain.data.*;
import uk.ac.ebi.chemet.io.domain.data.ProteinProductDataWriter;
import uk.ac.ebi.chemet.io.domain.data.RibosomalRNADataWriter;
import uk.ac.ebi.chemet.io.domain.data.TransferRNADataWriter;
import uk.ac.ebi.chemet.io.domain.data.MetaboliteDataWriter;
import uk.ac.ebi.chemet.io.domain.data.ReactionDataWriter;
import uk.ac.ebi.chemet.io.domain.data.ReconstructionDataWriter;
import uk.ac.ebi.chemet.io.domain.data.ChromosomeDataWriter;
import uk.ac.ebi.chemet.io.domain.data.GeneDataWriter;
import uk.ac.ebi.chemet.io.domain.data.GenomeDataWriter;
import uk.ac.ebi.mdk.io.IdentifierOutput;
import uk.ac.ebi.mdk.io.ObservationOutput;
import uk.ac.ebi.mdk.domain.entity.*;
import uk.ac.ebi.mdk.domain.entity.collection.Chromosome;
import uk.ac.ebi.mdk.domain.entity.collection.Genome;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.io.EntityOutput;
import uk.ac.ebi.mdk.io.EntityWriter;
import uk.ac.ebi.mdk.tool.EntityFactory;

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
        add(MetabolicReaction.class, new ReactionDataWriter(out, this));

        // gene
        add(Gene.class, new GeneDataWriter(out));

        // gene products
        add(ProteinProduct.class, new ProteinProductDataWriter(out, this));
        add(RibosomalRNA.class, new RibosomalRNADataWriter(out, this));
        add(TransferRNA.class, new TransferRNADataWriter(out, this));

        add(Chromosome.class, new ChromosomeDataWriter(out, this));
        add(Genome.class, new GenomeDataWriter(out, this));

        add(Reconstruction.class, new ReconstructionDataWriter(out, identifierOutput,  this));

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
