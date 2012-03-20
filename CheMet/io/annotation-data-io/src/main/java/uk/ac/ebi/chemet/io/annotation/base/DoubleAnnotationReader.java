package uk.ac.ebi.chemet.io.annotation.base;


import com.sun.tools.javac.util.Version;
import uk.ac.ebi.annotation.base.AbstractDoubleAnnotation;
import uk.ac.ebi.annotation.base.AbstractStringAnnotation;
import uk.ac.ebi.annotation.util.AnnotationFactory;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.chemet.io.annotation.AnnotationReader;
import uk.ac.ebi.interfaces.annotation.DoubleAnnotation;

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
public class DoubleAnnotationReader implements AnnotationReader<DoubleAnnotation> {

    private DataInput in;
    private Class<? extends DoubleAnnotation> c;
    private static final AnnotationFactory FACTORY = AnnotationFactory.getInstance();

    public DoubleAnnotationReader(Class<? extends DoubleAnnotation> c, DataInput in){
        this.in = in;
        this.c  = c;
    }

    public DoubleAnnotation readAnnotation() throws IOException {
        DoubleAnnotation annotation = FACTORY.ofClass(c);
        annotation.setValue(in.readDouble());
        return annotation;
    }

}
