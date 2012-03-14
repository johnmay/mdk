package uk.ac.ebi.chemet.io.annotation;

import uk.ac.ebi.interfaces.Annotation;

import java.io.IOException;

/**
 * AnnotationWriter - 08.03.2012 <br/>
 * <p/>
 * Describes a class that can read annotations
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface AnnotationReader<A extends Annotation> {

    public A readAnnotation() throws IOException, ClassNotFoundException;
    
}
