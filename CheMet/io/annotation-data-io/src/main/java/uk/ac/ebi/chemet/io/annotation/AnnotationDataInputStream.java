package uk.ac.ebi.chemet.io.annotation;

import identifier.IdentifierDataInputStream;
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
    private Version v;
    private DataInput in;
    private ObservationInput observationInput;
    private IdentifierInput  identifierInput;
    
    public AnnotationDataInputStream(DataInputStream in, Version v) {
        this(in, null, v);
    }

    public AnnotationDataInputStream(DataInput in,
                                     ObservationInput observationInput,
                                     Version v) {

        super(in);

        this.identifierInput = new IdentifierDataInputStream(in, v);


        this.in = in;
        this.v  = v;
        this.observationInput = observationInput;

        // default readers
        // general
        add(Locus.class,            new StringAnnotationReader(Locus.class, in));
        add(Subsystem.class,        new StringAnnotationReader(Subsystem.class, in));
        add(Synonym.class,          new StringAnnotationReader(Synonym.class, in));
        add(Source.class,           new StringAnnotationReader(Source.class, in));
        add(AuthorAnnotation.class, new AuthorCommentReader(in));

        // model
        add(FluxUpperBound.class, new DoubleAnnotationReader(FluxUpperBound.class, in));
        add(FluxLowerBound.class, new DoubleAnnotationReader(FluxLowerBound.class, in));

        // chemical
        add(MolecularFormula.class, new StringAnnotationReader(MolecularFormula.class, in));
        add(InChI.class,            new StringAnnotationReader(InChI.class, in));
        add(SMILES.class,           new StringAnnotationReader(SMILES.class, in));
        add(ExactMass.class,        new FloatAnnotationReader(ExactMass.class, in));
        add(Charge.class,           new DoubleAnnotationReader(Charge.class, in));
        add(AtomContainerAnnotation.class, new AtomContainerAnnotationReader(in));

        // cross-reference
        add(CrossReference.class,       new CrossReferenceReader(CrossReference.class, identifierInput, observationInput));
        add(ChEBICrossReference.class,  new CrossReferenceReader(ChEBICrossReference.class, identifierInput, observationInput));
        add(KEGGCrossReference.class,   new CrossReferenceReader(KEGGCrossReference.class, identifierInput, observationInput));
        add(EnzymeClassification.class, new CrossReferenceReader(EnzymeClassification.class, identifierInput, observationInput));
        add(Citation.class,             new CrossReferenceReader(Citation.class, identifierInput, observationInput));
        add(Classification.class,       new CrossReferenceReader(Classification.class, identifierInput, observationInput));
        
        // task
        add(Parameter.class,           new ParameterReader(in));
        add(FileParameter.class,       new FileParameterReader(in));
        add(ExecutableParameter.class, new ExecutableParameterReader(in));


    }
    
    public AnnotationReader getReader(Class c){
        return hasMarshaller(c, v) ? getMarshaller(c, v) : add(c, createReader(c), v);
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
