package uk.ac.ebi.chemet.io.annotation.crossreference;

import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.annotation.util.DefaultAnnotationFactory;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.chemet.io.annotation.AnnotationReader;
import uk.ac.ebi.chemet.io.identifier.IdentifierInput;
import uk.ac.ebi.chemet.io.observation.ObservationInput;
import uk.ac.ebi.interfaces.identifiers.Identifier;

import java.io.IOException;

/**
 * CrossReferenceReader - 10.03.2012 <br/>
 * <p/>
 * Reads a cross-reference annotation from a tagged input stream. The
 * cross-reference class is provided to the constructor allowing it
 * to read into the specified type (i.e. ChEBICrossReference,
 * EnzymeClassification, etc.). Note: observations are not stored
 * at the moment
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class CrossReferenceReader<X extends CrossReference>
        implements AnnotationReader<X> {

    private static final DefaultAnnotationFactory ANNOTATION_FACTORY = DefaultAnnotationFactory.getInstance();

    private Class<? extends CrossReference> c;
    private IdentifierInput in;
    private ObservationInput observationInput;


    /**
     * Create a new cross-reference reader on the specified tagged input
     * stream
     * @param c   class of the cross-reference (e.g. KEGGCompoundCrossReference.class
     * @param in  tagged input stream to read from
     */
    public CrossReferenceReader(Class<? extends CrossReference> c,
                                IdentifierInput in){
        this(c, in, null);
    }


    public CrossReferenceReader(Class<? extends CrossReference> c,
                                IdentifierInput in,
                                ObservationInput observationInput){
        this.c = c;
        this.in = in;
        this.observationInput = observationInput;
    }

    /**
     * Read a cross-reference annotation from the stream
     *
     * @return CrossReference annotation
     *
     * @throws IOException            low-level io error
     * @throws ClassNotFoundException thrown if the class of identifier cannot be found
     */
    @Override
    public X readAnnotation() throws IOException, ClassNotFoundException {

         Identifier identifier = in.read();

         CrossReference xref = ANNOTATION_FACTORY.ofClass(c);
         xref.setIdentifier(identifier);

         if(observationInput != null) {
            xref.addObservations(observationInput.readCollection());
         }

         return (X) xref;

    }
}
