package uk.ac.ebi.mdk.tool.match;

import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author John May
 */
public class CrossReferenceMatcher<E extends AnnotatedEntity>
        extends AbstractMatcher<E, Set<Identifier>>
        implements EntityMatcher<E, Set<Identifier>> {

    private static Comparator<Identifier> COMPARATOR = new CrossReferenceComparator();

    @Override
    public Boolean matches(Set<Identifier> queryMetric, Set<Identifier> subjectMetric) {
        return matchAny(queryMetric, subjectMetric);
    }

    @Override
    public Set<Identifier> calculatedMetric(E entity) {

        Set<Identifier> identifiers = new TreeSet<Identifier>(COMPARATOR);

        Collection<CrossReference> references = entity.getAnnotationsExtending(CrossReference.class);

        for (CrossReference reference : references) {
            identifiers.add(reference.getIdentifier());
        }

        identifiers.add(entity.getIdentifier());

        return identifiers;

    }

    private static class CrossReferenceComparator implements Comparator<Identifier> {
        @Override
        public int compare(Identifier o1, Identifier o2) {
            return o1.getAccession().compareTo(o2.getAccession());
        }
    }


}
