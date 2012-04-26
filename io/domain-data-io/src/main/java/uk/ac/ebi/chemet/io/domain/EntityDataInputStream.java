package uk.ac.ebi.chemet.io.domain;

import uk.ac.ebi.chemet.io.identifier.IdentifierDataInputStream;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.io.AnnotationInput;
import uk.ac.ebi.chemet.io.core.AbstractDataInput;
import uk.ac.ebi.chemet.io.domain.data.*;
import uk.ac.ebi.chemet.io.domain.data.MetaboliteDataReader;
import uk.ac.ebi.chemet.io.domain.data.ReactionDataReader;
import uk.ac.ebi.chemet.io.domain.data.ReconstructionDataReader;
import uk.ac.ebi.chemet.io.domain.data.ProteinProductDataReader;
import uk.ac.ebi.chemet.io.domain.data.RibosomalRNADataReader;
import uk.ac.ebi.chemet.io.domain.data.TransferRNADataReader;
import uk.ac.ebi.chemet.io.domain.data.ChromosomeDataReader;
import uk.ac.ebi.chemet.io.domain.data.GeneDataReader;
import uk.ac.ebi.chemet.io.domain.data.GenomeDataReader;
import uk.ac.ebi.mdk.io.IdentifierInput;
import uk.ac.ebi.mdk.io.ObservationInput;
import uk.ac.ebi.mdk.domain.entity.*;
import uk.ac.ebi.mdk.domain.entity.collection.Chromosome;
import uk.ac.ebi.mdk.domain.entity.collection.Genome;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.io.EntityInput;
import uk.ac.ebi.mdk.io.EntityReader;
import uk.ac.ebi.mdk.tool.EntityFactory;

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

        add(Reconstruction.class, new ReconstructionDataReader(in, factory, identifierInput, this));

    }


    @Override
    public Entity read() throws IOException, ClassNotFoundException {
        return read(readClass());
    }

    @Override
    public <E extends Entity> E read(Class<E> c) throws IOException, ClassNotFoundException {

        short id = readObjectId();

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
