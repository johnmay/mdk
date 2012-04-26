package uk.ac.ebi.chemet.io.annotation.base;


import uk.ac.ebi.annotation.util.DefaultAnnotationFactory;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.AnnotationReader;
import uk.ac.ebi.mdk.domain.annotation.primitive.FloatAnnotation;

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
public class FloatAnnotationReader
        implements AnnotationReader<FloatAnnotation> {

    private DataInput in;
    private Class<? extends FloatAnnotation> c;
    private static final DefaultAnnotationFactory FACTORY = DefaultAnnotationFactory.getInstance();

    public FloatAnnotationReader(Class<? extends FloatAnnotation> c, DataInput in){
        this.in = in;
        this.c  = c;
    }

    public FloatAnnotation readAnnotation() throws IOException {
        FloatAnnotation annotation = FACTORY.ofClass(c);
        annotation.setValue(in.readFloat());
        return annotation;
    }

}
