package uk.ac.ebi.chemet.io.annotation;

import uk.ac.ebi.annotation.reaction.GibbsEnergy;
import uk.ac.ebi.chemet.io.annotation.reaction.GibbsEnergyReader;
import uk.ac.ebi.chemet.io.identifier.IdentifierDataInputStream;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.*;
import uk.ac.ebi.annotation.chemical.*;
import uk.ac.ebi.annotation.crossreference.*;
import uk.ac.ebi.annotation.model.FluxLowerBound;
import uk.ac.ebi.annotation.model.FluxUpperBound;
import uk.ac.ebi.annotation.task.ExecutableParameter;
import uk.ac.ebi.annotation.task.FileParameter;
import uk.ac.ebi.annotation.task.Parameter;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.chemet.io.annotation.base.*;
import uk.ac.ebi.chemet.io.annotation.chemical.AtomContainerAnnotationReader;
import uk.ac.ebi.chemet.io.annotation.crossreference.CrossReferenceReader;
import uk.ac.ebi.chemet.io.annotation.general.AuthorCommentReader;
import uk.ac.ebi.chemet.io.annotation.task.ExecutableParameterReader;
import uk.ac.ebi.chemet.io.annotation.task.FileParameterReader;
import uk.ac.ebi.chemet.io.annotation.task.ParameterReader;
import uk.ac.ebi.chemet.io.core.AbstractDataInput;
import uk.ac.ebi.chemet.io.identifier.IdentifierInput;
import uk.ac.ebi.chemet.io.observation.ObservationInput;
import uk.ac.ebi.interfaces.Annotation;
import uk.ac.ebi.interfaces.FloatAnnotation;
import uk.ac.ebi.interfaces.IntegerAnnotation;
import uk.ac.ebi.interfaces.StringAnnotation;
import uk.ac.ebi.interfaces.annotation.BooleanAnnotation;
import uk.ac.ebi.interfaces.annotation.DoubleAnnotation;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * AnnotationDataInputStream - 09.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class AnnotationDataInputStream
        extends AbstractDataInput<AnnotationReader>
        implements AnnotationInput {

    private static final Logger LOGGER = Logger.getLogger(AnnotationDataInputStream.class);
    private DataInput in;
    private ObservationInput observationInput;
    private IdentifierInput  identifierInput;
    
    public AnnotationDataInputStream(DataInputStream in, Version v) {
        this(in, null, v);
    }

    public AnnotationDataInputStream(DataInput in,
                                     ObservationInput observationInput,
                                     Version v) {

        super(in, v);

        this.identifierInput = new IdentifierDataInputStream(in, v);


        this.in = in;
        this.observationInput = observationInput;

        // special readers
       add(AuthorAnnotation.class, new AuthorCommentReader(in));
       add(AtomContainerAnnotation.class, new AtomContainerAnnotationReader(in));
       add(GibbsEnergy.class, new GibbsEnergyReader(in));

       // special readers for tasks
       add(Parameter.class,           new ParameterReader(in));
       add(FileParameter.class,       new FileParameterReader(in));
       add(ExecutableParameter.class, new ExecutableParameterReader(in));


    }
    
    public AnnotationReader getReader(Class c){
        return hasMarshaller(c, getVersion()) ? getMarshaller(c, getVersion()) : add(c, createReader(c), getVersion());
    }


    public <A extends Annotation> A read() throws IOException, ClassNotFoundException {
        return (A) read(readClass());
    }

    public <A extends Annotation> A read(Class<A> c) throws IOException, ClassNotFoundException {
        Short id = readObjectId();
        
        AnnotationReader reader = getReader(c);
        return hasObject(id) ? (A) get(id) : (A) put(id, reader.readAnnotation());
    }

    public AnnotationReader createReader(Class c){

        if(StringAnnotation.class.isAssignableFrom(c)) {
            return new StringAnnotationReader(c, in);
        } else if (DoubleAnnotation.class.isAssignableFrom(c)){
            return new DoubleAnnotationReader(c, in);
        } else if (BooleanAnnotation.class.isAssignableFrom(c)){
            return new BooleanAnnotationReader(c, in);
        } else if (FloatAnnotation.class.isAssignableFrom(c)){
            return new FloatAnnotationReader(c, in);
        } else if (IntegerAnnotation.class.isAssignableFrom(c)){
            return new IntegerAnnotationReader(c, in);
        } else if (CrossReference.class.isAssignableFrom(c)){
            return new CrossReferenceReader(c, identifierInput, observationInput);
        }

        throw new InvalidParameterException("Could not create writer for " + c.getName() );

    }



}
