package uk.ac.ebi.chemet.io.annotation;

import uk.ac.ebi.annotation.reaction.GibbsEnergy;
import uk.ac.ebi.chemet.io.annotation.reaction.GibbsEnergyWriter;
import uk.ac.ebi.chemet.io.identifier.IdentifierDataOutputStream;
import uk.ac.ebi.annotation.*;
import uk.ac.ebi.annotation.chemical.*;
import uk.ac.ebi.annotation.crossreference.*;
import uk.ac.ebi.annotation.task.ExecutableParameter;
import uk.ac.ebi.annotation.task.FileParameter;
import uk.ac.ebi.annotation.task.Parameter;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.chemet.io.annotation.base.*;
import uk.ac.ebi.chemet.io.annotation.chemical.AtomContainerAnnotationWriter;
import uk.ac.ebi.chemet.io.annotation.crossreference.CrossReferenceWriter;
import uk.ac.ebi.chemet.io.annotation.general.AuthorCommentWriter;
import uk.ac.ebi.chemet.io.annotation.task.ExecutableParameterWriter;
import uk.ac.ebi.chemet.io.annotation.task.FileParameterWriter;
import uk.ac.ebi.chemet.io.annotation.task.ParameterWriter;
import uk.ac.ebi.chemet.io.core.AbstractDataOutput;
import uk.ac.ebi.mdk.io.IdentifierOutput;
import uk.ac.ebi.mdk.io.ObservationOutput;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.annotation.primitive.FloatAnnotation;
import uk.ac.ebi.mdk.domain.annotation.primitive.IntegerAnnotation;
import uk.ac.ebi.mdk.domain.annotation.primitive.StringAnnotation;
import uk.ac.ebi.mdk.domain.annotation.primitive.BooleanAnnotation;
import uk.ac.ebi.mdk.domain.annotation.primitive.DoubleAnnotation;
import uk.ac.ebi.mdk.io.AnnotationOutput;
import uk.ac.ebi.mdk.io.AnnotationWriter;

import java.io.DataOutput;
import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * AnnotationDataOutputStream - 09.03.2012 <br/>
 * <p/>
 * Writes annotations to a data output stream. Each annotation class has a writer that
 * will write the data in the annotation to the stream. The writers marshall the annotation
 * data.
 *
 * Usage:
 * {@code
 * <pre>
 *      AnnotationOutput out =  ...;
 *      out.write(new SynonymAnnotation("atp"));
 *
 *      // save space by writing the class and
 *      // the only writing the object data
 *      out.write(Synonym.class);
 *      out.write(Synonym.class, synonym1);
 *      out.write(Synonym.class, synonym2);
 *      out.write(Synonym.class, synonym3);
 *      out.write(Synonym.class, synonym4);
 *
 * </pre>}
 *
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class AnnotationDataOutputStream
        extends AbstractDataOutput<AnnotationWriter>
        implements AnnotationOutput {

    private DataOutput out;
    private ObservationOutput observationOutput;
    private IdentifierOutput  identifierOutput;

    public AnnotationDataOutputStream(DataOutput out,
                                      Version v) {
        this(out, null, v);
    }

    public AnnotationDataOutputStream(DataOutput out,
                                      ObservationOutput observationOutput,
                                      Version v) {

        super(out, v);

        this.identifierOutput = new IdentifierDataOutputStream(out, v);

        this.out = out;
        this.observationOutput = observationOutput;

        // specialised writers
        add(AuthorAnnotation.class, new AuthorCommentWriter(out));
        add(AtomContainerAnnotation.class, new AtomContainerAnnotationWriter(out));
        add(GibbsEnergy.class, new GibbsEnergyWriter(out));

        // specialised specialised readers for tasks
        add(Parameter.class,           new ParameterWriter(out));
        add(FileParameter.class,       new FileParameterWriter(out));
        add(ExecutableParameter.class, new ExecutableParameterWriter(out));

    }


    public void write(Annotation annotation) throws IOException {

        Class c = annotation.getClass();
        writeClass(c);
        writeData(annotation);

    }

    public void writeData(Annotation annotation) throws IOException {

        if( writeObjectId(annotation) ) {
            Class c                 = annotation.getClass();
            AnnotationWriter writer = getWriter(c);
            writer.write(annotation);
        }
    }

    /**
     * Access the writer for the provided class. This method will
     * fetch a writer for the appropriate version the output stream
     * was initialised with.
     * @param c class to get the writer for
     * @return
     */
    public AnnotationWriter getWriter(Class c) {
        return hasMarshaller(c, getVersion()) ? getMarshaller(c, getVersion()) : add(c, createWriter(c), getVersion());
    }

    public AnnotationWriter createWriter(Class c){

        if(StringAnnotation.class.isAssignableFrom(c)) {
            return new StringAnnotationWriter(out);
        } else if (DoubleAnnotation.class.isAssignableFrom(c)){
            return new DoubleAnnotationWriter(out);
        } else if (BooleanAnnotation.class.isAssignableFrom(c)){
            return new BooleanAnnotationWriter(out);
        } else if (FloatAnnotation.class.isAssignableFrom(c)){
            return new FloatAnnotationWriter(out);
        } else if (IntegerAnnotation.class.isAssignableFrom(c)){
            return new IntegerAnnotationWriter(out);
        } else if (CrossReference.class.isAssignableFrom(c)){
            return new CrossReferenceWriter(identifierOutput, observationOutput);
        }

        throw new InvalidParameterException("Could not create writer for " + c.getName() );

    }

}
