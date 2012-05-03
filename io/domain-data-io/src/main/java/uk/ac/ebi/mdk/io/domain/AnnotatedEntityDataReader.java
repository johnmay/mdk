package uk.ac.ebi.mdk.io.domain;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.AnnotationInput;
import uk.ac.ebi.mdk.io.EnumReader;
import uk.ac.ebi.mdk.io.EntityReader;
import uk.ac.ebi.mdk.io.ObservationInput;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.entity.Entity;
import uk.ac.ebi.resource.DefaultIdentifierFactory;

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
public class AnnotatedEntityDataReader
        implements EntityReader<Entity> {

    private static final Logger LOGGER = Logger.getLogger(AnnotatedEntityDataReader.class);

    private static final DefaultIdentifierFactory factory = DefaultIdentifierFactory.getInstance();

    private DataInput in;
    private EnumReader enumReader;

    private AnnotatedEntity entity;

    private AnnotationInput annotationIn;
    private ObservationInput observationIn;

    public AnnotatedEntityDataReader(DataInput in) {
        this(in, null, null);
    }

    public AnnotatedEntityDataReader(DataInput in,
                                     AnnotationInput annotationIn,
                                     ObservationInput observationIn) {
        this.in = in;
        this.enumReader = new EnumReader(in);
        this.annotationIn = annotationIn;
        this.observationIn = observationIn;
    }

    public void setEntity(AnnotatedEntity entity) {
        this.entity = entity;
    }

    public AnnotatedEntity readEntity() throws IOException, ClassNotFoundException {

        entity.setRating(enumReader.readEnum());

        readAnnotations();
        readObservations();

        return entity;

    }

    public void readAnnotations() throws IOException, ClassNotFoundException {


        int n = in.readInt();


        for (int i = 0; i < n; i++) {

            int ntype = in.readInt();

            if (annotationIn != null) {
                Class c = annotationIn.readClass();

                for (int j = 0; j < ntype; j++) {
                    Annotation annotation = annotationIn.read(c);
                    entity.addAnnotation(annotation);
                }
            }

        }

    }

    public void readObservations() throws IOException, ClassNotFoundException {

        int n = in.readInt();
        for (int i = 0; i < n; i++) {

            int ntype = in.readInt();

            if (observationIn != null) {
                Class c = observationIn.readClass();
                for (int j = 0; j < ntype; j++) {
                    entity.addObservation(observationIn.read(c));
                }
            }

        }

    }

}
