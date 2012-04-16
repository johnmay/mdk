package uk.ac.ebi.chemet.io.annotation.base;


import uk.ac.ebi.annotation.util.AnnotationFactory;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.chemet.io.annotation.AnnotationReader;
import uk.ac.ebi.interfaces.IntegerAnnotation;

import java.io.DataInput;
import java.io.IOException;

/**
 * StringAnnotationReader - 09.03.2012 <br/>
 * <p/>
 * Read's a string annotation from {@see DataInput}
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class IntegerAnnotationReader
    implements AnnotationReader<IntegerAnnotation> {

    private DataInput in;
    private Class<? extends IntegerAnnotation> c;
    private static final AnnotationFactory FACTORY = AnnotationFactory.getInstance();

    public IntegerAnnotationReader(Class<? extends IntegerAnnotation> c, DataInput in){
        this.in = in;
        this.c  = c;
    }

    public IntegerAnnotation readAnnotation() throws IOException {
        IntegerAnnotation annotation = FACTORY.ofClass(c);
        annotation.setValue(in.readInt());
        return annotation;
    }

}
