package uk.ac.ebi.mdk.io;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.io.domain.MetaboliteDataReader;
import uk.ac.ebi.mdk.io.domain.ReactionDataReader;
import uk.ac.ebi.mdk.io.domain.ReconstructionDataReader_0_9;
import uk.ac.ebi.mdk.io.domain.ProteinProductDataReader;
import uk.ac.ebi.mdk.io.domain.ReconstructionDataReader_1_3_3;
import uk.ac.ebi.mdk.io.domain.RibosomalRNADataReader;
import uk.ac.ebi.mdk.io.domain.TransferRNADataReader;
import uk.ac.ebi.mdk.io.domain.ChromosomeDataReader;
import uk.ac.ebi.mdk.io.domain.GeneDataReader;
import uk.ac.ebi.mdk.io.domain.GenomeDataReader;
import uk.ac.ebi.mdk.domain.entity.*;
import uk.ac.ebi.mdk.domain.entity.collection.Chromosome;
import uk.ac.ebi.mdk.domain.entity.collection.Genome;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.io.domain.AnnotatedEntityDataReader;
import uk.ac.ebi.mdk.io.domain.EntityDataReader;

import java.io.DataInputStream;
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
public class EntityDataInputStream
        extends AbstractDataInput<EntityReader>
        implements EntityInput {

    private static final Logger LOGGER = Logger.getLogger(EntityDataInputStream.class);

    private DataInputStream in;
    private EntityFactory factory;
    private AnnotationInput annotationIn;
    private ObservationInput observationInn;
    private IdentifierInput identifierInput;


    private EntityDataReader entityReader;
    private AnnotatedEntityDataReader annotatedEntityReader;

    public EntityDataInputStream(Version version,
                                 DataInputStream in,
                                 EntityFactory factory,
                                 AnnotationInput annotationIn,
                                 ObservationInput observationInn) {

        super(in, version);

        this.in = in;
        this.annotationIn = annotationIn;
        this.observationInn = observationInn;
        this.identifierInput = new IdentifierDataInputStream(in, version);
        this.factory = factory;

        entityReader = new EntityDataReader(in, identifierInput);
        annotatedEntityReader = new AnnotatedEntityDataReader(in, annotationIn, observationInn);


        add(Metabolite.class, new MetaboliteDataReader(in, factory));
        add(MetabolicReaction.class, new ReactionDataReader(in, factory, this));

        // gene
        add(Gene.class, new GeneDataReader(in, factory));

        // gene products
        add(ProteinProduct.class, new ProteinProductDataReader(in, factory, this));
        add(RibosomalRNA.class, new RibosomalRNADataReader(in, factory, this));
        add(TransferRNA.class, new TransferRNADataReader(in, factory, this));

        add(Chromosome.class, new ChromosomeDataReader(in, factory, this));
        add(Genome.class, new GenomeDataReader(in, factory, this));

        add(Reconstruction.class, new ReconstructionDataReader_0_9(in, factory, identifierInput, this));
        add(Reconstruction.class, new ReconstructionDataReader_1_3_3(in, factory, identifierInput, this));

    }


    @Override
    public Entity read() throws IOException, ClassNotFoundException {
        return read(readClass());
    }

    @Override
    public <E extends Entity> E read(Class<E> c) throws IOException, ClassNotFoundException {

        Integer id = readObjectId();

        return hasObject(id) ? (E) get(id) : (E) put(id, readNewEntity(c));
    }

    public Entity readNewEntity(Class c) throws IOException, ClassNotFoundException {

        EntityReader reader = getMarshaller(c, getVersion());
        Entity       entity = reader.readEntity();

        if (entity instanceof AnnotatedEntity) {

            annotatedEntityReader.setEntity((AnnotatedEntity) entity);
            annotatedEntityReader.readEntity();

            entityReader.setEntity(entity);
            entityReader.readEntity();

        }

        return entity;

    }
}
