package uk.ac.ebi.mdk.tool.match;

import uk.ac.ebi.annotation.Synonym;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;

import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;


/**
 * Class realises EntityMatcher using the names, abbreviations and synonyms of
 * the annotated entities.
 *
 * @author johnmay
 */
public class NameMatcher<E extends AnnotatedEntity>
        implements EntityMatcher<E> {

    private Pattern pattern = Pattern.compile("[^A-z0-9]");
    private Boolean normalise = Boolean.FALSE;
    private Boolean includeSynonyms = Boolean.FALSE;

    public NameMatcher() {
        this(Boolean.FALSE, Boolean.FALSE);
    }

    public NameMatcher(Boolean normalise, Boolean includeSynonyms) {
        this.normalise = normalise;
        this.includeSynonyms = includeSynonyms;
    }

    public NameMatcher(Pattern pattern) {
        this(Boolean.TRUE, Boolean.TRUE);
        this.pattern = pattern;
    }

    /**
     * Compares the names, abbreviations and synonyms of two annotated entities. The names
     * are normalised to lower case (Locale.ENGLISH) and trimmed of excess space. If
     * any name/synonym matches then the metabolites are considered matches.
     *
     * @inheritDoc
     */
    public Boolean matches(E query, E subject) {

        Set<String> queryNames = new TreeSet<String>();
        Set<String> subjectNames = new TreeSet<String>();

        queryNames.add(normalise(query.getName()));
        subjectNames.add(normalise(subject.getName()));

        queryNames.add(normalise(query.getAbbreviation()));
        subjectNames.add(normalise(subject.getAbbreviation()));

        if (containsMatch(queryNames, subjectNames)) {
            return true;
        }

        if (!includeSynonyms) {
            return false;
        }
        // no match found yet... include the synonyms

        for (Synonym synonym : query.getAnnotations(Synonym.class)) {
            queryNames.add(normalise(synonym.getValue()));
        }
        for (Synonym synonym : subject.getAnnotations(Synonym.class)) {
            subjectNames.add(normalise(synonym.getValue()));
        }

        return containsMatch(queryNames, subjectNames);

    }

    public String normalise(String name) {
        name = name.toLowerCase(Locale.ENGLISH).trim();
        return normalise ? pattern.matcher(name).replaceAll("") : name;
    }

    private Boolean containsMatch(Set<String> queryNames, Set<String> subjectNames) {
        for (String query : queryNames) {
            if (!query.isEmpty() && subjectNames.contains(query)) {
                return true;
            }
        }
        return false;
    }
}
