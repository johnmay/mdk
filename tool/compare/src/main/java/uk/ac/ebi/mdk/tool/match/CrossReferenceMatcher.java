package uk.ac.ebi.mdk.tool.match;

import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author John May
 */
public class CrossReferenceMatcher<E extends AnnotatedEntity>
        extends AbstractMatcher<E, Set<Identifier>>
        implements EntityMatcher<E, Set<Identifier>> {


    @Override
    public Boolean matches(Set<Identifier> query, Set<Identifier> subject) {
        for (Identifier reference : query) {
            if (subject.contains(reference)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<Identifier> calculatedMetric(E entity) {


        Set<Identifier> identifiers = new HashSet<Identifier>();

        Collection<CrossReference> references = entity.getAnnotationsExtending(CrossReference.class);

        for (CrossReference reference : references) {
            identifiers.add(reference.getIdentifier());
        }

        identifiers.add(entity.getIdentifier());

        return identifiers;

    }


}
