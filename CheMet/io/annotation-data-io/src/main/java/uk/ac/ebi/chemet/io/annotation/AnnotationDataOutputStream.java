package uk.ac.ebi.chemet.io.annotation;

import identifier.IdentifierDataOutputStream;
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
import uk.ac.ebi.chemet.io.identifier.IdentifierOutput;
import uk.ac.ebi.chemet.io.observation.ObservationOutput;
import uk.ac.ebi.interfaces.Annotation;
import uk.ac.ebi.interfaces.FloatAnnotation;
import uk.ac.ebi.interfaces.IntegerAnnotation;
import uk.ac.ebi.interfaces.StringAnnotation;
import uk.ac.ebi.interfaces.annotation.BooleanAnnotation;
import uk.ac.ebi.interfaces.annotation.DoubleAnnotation;

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

    private Version v;
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

        super(out);

        this.identifierOutput = new IdentifierDataOutputStream(out, v);

        this.out = out;
        this.v   = v;
        this.observationOutput = observationOutput;

        // default readers
        // general
//        add(Locus.class,            new StringAnnotationWriter(out));
//        add(Subsystem.class,        new StringAnnotationWriter(out));
//        add(Synonym.class,          new StringAnnotationWriter(out));
//        add(Source.class,           new StringAnnotationWriter(out));
        add(AuthorAnnotation.class, new AuthorCommentWriter(out));

        // model
//        add(FluxUpperBound.class, new DoubleAnnotationWriter(out));
//        add(FluxLowerBound.class, new DoubleAnnotationWriter(out));

        // chemical
//        add(MolecularFormula.class,        new StringAnnotationWriter(out));
//        add(InChI.class,                   new StringAnnotationWriter(out));
//        add(SMILES.class,                  new StringAnnotationWriter(out));
//        add(ExactMass.class,               new FloatAnnotationWriter(out));
//        add(Charge.class,                  new DoubleAnnotationWriter(out));
        add(AtomContainerAnnotation.class, new AtomContainerAnnotationWriter(out));

        // cross-reference (uses tagged output stream for identifier class)
//        add(CrossReference.class,       new CrossReferenceWriter(identifierOutput, observationOutput));
//        add(ChEBICrossReference.class,  new CrossReferenceWriter(identifierOutput, observationOutput));
//        add(KEGGCrossReference.class,   new CrossReferenceWriter(identifierOutput, observationOutput));
//        add(EnzymeClassification.class, new CrossReferenceWriter(identifierOutput, observationOutput));
//        add(Citation.class,             new CrossReferenceWriter(identifierOutput, observationOutput));
//        add(Classification.class,       new CrossReferenceWriter(identifierOutput, observationOutput));

        // task
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
        return hasMarshaller(c, v) ? getMarshaller(c, v) : add(c, createWriter(c), v);
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
