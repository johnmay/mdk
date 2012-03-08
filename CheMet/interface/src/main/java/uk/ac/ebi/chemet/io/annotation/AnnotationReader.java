package uk.ac.ebi.chemet.io.annotation;

import uk.ac.ebi.interfaces.Annotation;

/**
 * AnnotationWriter - 08.03.2012 <br/>
 * <p/>
 * Describes a class tha can read annotations
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface AnnotationReader {

    public Annotation readAnnotation();
    
}
