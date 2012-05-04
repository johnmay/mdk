package uk.ac.ebi.chemet.tools.annotation;


import uk.ac.ebi.mdk.domain.annotation.Annotation;

import java.util.Collection;

/**
 * AnnotationFilter - 06.03.2012 <br/>
 * <p/>
 * Class will filter annotations
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface AnnotationFilter<A extends Annotation> {

    public Collection<A> filter(Collection<A> annotations);

}
