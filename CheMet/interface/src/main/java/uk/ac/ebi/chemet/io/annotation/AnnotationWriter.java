package uk.ac.ebi.chemet.io.annotation;

import uk.ac.ebi.interfaces.Annotation;

import java.io.InputStream;

/**
 * AnnotationWriter - 08.03.2012 <br/>
 * <p/>
 * Describes a class that can write annotations
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface AnnotationWriter {

    public void write(Annotation annotation);
    
}
