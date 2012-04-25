package uk.ac.ebi.core.tools.compare;

import uk.ac.ebi.annotation.Synonym;
import uk.ac.ebi.interfaces.AnnotatedEntity;

import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;


/**
 * Class realises EntityComparator using the names, abbreviations and synonyms of
 * the annotated entities.
 *
 * @author johnmay
 */
public class NameComparator<E extends AnnotatedEntity>
        implements EntityComparator<E> {

    /**
     * Compares the names, abbreviations and synonyms of two annotated entities. The names
     * are normalised to lower case (Locale.ENGLISH) and trimmed of excess space. If
     * any name/synonym matches then the metabolites are considered equal.
     *
     * @inheritDoc
     */
    public Boolean equal(E query, E subject) {

        Set<String> queryNames   = new TreeSet<String>();
        Set<String> subjectNames = new TreeSet<String>();

        queryNames.add(query.getName().trim().toLowerCase(Locale.ENGLISH));
        subjectNames.add(subject.getName().trim().toLowerCase(Locale.ENGLISH));

        queryNames.add(query.getAbbreviation().trim().toLowerCase(Locale.ENGLISH));
        subjectNames.add(subject.getAbbreviation().trim().toLowerCase(Locale.ENGLISH));

        if(containsMatch(queryNames, subjectNames)){
            return true;
        }

        // no match found yet... include the synonyms

        for (Synonym synonym : query.getAnnotations(Synonym.class)) {
            queryNames.add(synonym.getValue().trim().toLowerCase(Locale.ENGLISH));
        }
        for (Synonym synonym : subject.getAnnotations(Synonym.class)) {
            subjectNames.add(synonym.getValue().trim().toLowerCase(Locale.ENGLISH));
        }

        return containsMatch(queryNames, subjectNames);

    }

    private Boolean containsMatch(Set<String> queryNames, Set<String> subjectNames){
        for (String subjectName : subjectNames) {
            if (queryNames.contains(subjectName)) {
                return true;
            }
        }
        return false;
    }
}
