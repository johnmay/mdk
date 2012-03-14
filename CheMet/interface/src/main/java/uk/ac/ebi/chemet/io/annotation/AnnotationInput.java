package uk.ac.ebi.chemet.io.annotation;

import uk.ac.ebi.interfaces.Annotation;

import java.io.IOException;

/**
 * AnnotationInput - 11.03.2012 <br/>
 * <p/>
 * Reads a stream of annotations
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface AnnotationInput {

    public <A extends Annotation> A read() throws IOException, ClassNotFoundException;

    public <A extends Annotation> A read(Class<A> c) throws IOException, ClassNotFoundException;

    public Class readClass() throws IOException, ClassNotFoundException;

}
