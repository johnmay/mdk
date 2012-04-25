package uk.ac.ebi.core.tools.compare;

import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.interfaces.AnnotatedEntity;

import java.util.Collection;

/**
 * @author John May
 */
public class CrossReferenceComparator<E extends AnnotatedEntity> implements EntityComparator<E> {

    @Override
    public Boolean equal(E query, E subject) {

        Collection<CrossReference> queryXrefs   = query.getAnnotationsExtending(CrossReference.class);
        Collection<CrossReference> subjectXrefs = subject.getAnnotationsExtending(CrossReference.class);

        // add self
        queryXrefs.add(new CrossReference(query.getIdentifier()));
        subjectXrefs.add(new CrossReference(subject.getIdentifier()));

        // find match
        for(CrossReference xref : queryXrefs){
            if(subjectXrefs.contains(xref)){
                return true;
            }
        }

        return false;

    }
}
