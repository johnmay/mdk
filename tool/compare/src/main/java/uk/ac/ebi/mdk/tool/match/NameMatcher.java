package uk.ac.ebi.mdk.tool.match;

import uk.ac.ebi.mdk.domain.annotation.Synonym;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;

import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;


/**
 * Class realises EntityMatcher using the names, abbreviations and synonyms of
 * the annotated entities.
 * <p/>
 * Compares the names, abbreviations and synonyms of two annotated entities. The names
 * are normalised to lower case (Locale.ENGLISH) and trimmed of excess space. If
 * any name/synonym matches then the metabolites are considered matches.
 *
 * @author johnmay
 */
public class NameMatcher<E extends AnnotatedEntity>
        extends AbstractMatcher<E, Set<String>>
        implements EntityMatcher<E, Set<String>> {

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

    @Override
    public Set<String> calculatedMetric(E entity) {

        Set<String> names = new TreeSet<String>();

        // add the entity name/abbreviation to the set
        names.add(normalise(entity.getName()));
        names.add(normalise(entity.getAbbreviation()));

        // if we include synonyms fetch all synonym annotations
        if (includeSynonyms) {
            for (Synonym synonym : entity.getAnnotationsExtending(Synonym.class)) {
                names.add(normalise(synonym.getValue()));
            }
        }

        return names;

    }

    @Override
    public Boolean matches(Set<String> queryMetric, Set<String> subjectMetric) {
        return matchAny(queryMetric, subjectMetric);
    }


    public String normalise(String name) {
        name = name.toLowerCase(Locale.ENGLISH).trim();
        return normalise ? pattern.matcher(name).replaceAll("") : name;
    }

}
