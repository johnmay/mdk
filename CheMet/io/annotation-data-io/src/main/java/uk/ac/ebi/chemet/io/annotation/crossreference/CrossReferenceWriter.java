package uk.ac.ebi.chemet.io.annotation.crossreference;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.chemet.io.annotation.AnnotationWriter;
import uk.ac.ebi.chemet.io.identifier.IdentifierOutput;
import uk.ac.ebi.chemet.io.observation.ObservationOutput;

import java.io.IOException;

/**
 * CrossReferenceWriter - 10.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class CrossReferenceWriter<X extends CrossReference> implements AnnotationWriter<X> {

    private static final Logger LOGGER = Logger.getLogger(CrossReferenceWriter.class);

    private IdentifierOutput  out;
    private ObservationOutput observationOutput;

    public CrossReferenceWriter(IdentifierOutput out,
                                ObservationOutput observationOutput){
        this.out = out;
        this.observationOutput = observationOutput;
    }

    public CrossReferenceWriter(IdentifierOutput out){
        this(out, null);
    }

    @Override
    public void write(X annotation) throws IOException {

         out.write(annotation.getIdentifier());

         if(observationOutput != null) {
            observationOutput.writeObservations(annotation.getObservations());
         }


    }
}
