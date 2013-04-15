/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.tool.match;

import uk.ac.ebi.mdk.domain.annotation.Synonym;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;


/**
 * Class realises EntityMatcher using the names, abbreviations and synonyms of
 * the annotated entities.
 * <p/>
 * Compares the names, abbreviations and synonyms of two annotated entities. The
 * names are normalised to lower case (Locale.ENGLISH) and trimmed of excess
 * space. If any name/synonym matches then the metabolites are considered
 * matches.
 *
 * @author johnmay
 */
public class NameMatcher<E extends AnnotatedEntity>
        extends AbstractMatcher<E, Set<String>>
        implements EntityMatcher<E, Set<String>> {

    private Pattern pattern         = Pattern.compile("[^A-z0-9]");
    private Boolean normalise       = Boolean.FALSE;
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

        Set<String> names = new HashSet<String>();

        // add the entity name/abbreviation to the set
        names.add(normalise(entity.getName()));
        names.add(normalise(entity.getAbbreviation()));

        // if we include synonyms fetch all synonym annotations
        if (includeSynonyms) {
            for (Synonym synonym : entity.getAnnotationsExtending(Synonym.class)) {
                names.add(normalise(synonym.getValue()));
            }
        }

        // remove empty and null names
        names.remove(null);
        names.remove("");

        return names;

    }


    @Override
    public Boolean matchMetric(Set<String> queryMetric, Set<String> subjectMetric) {
        return matchAny(queryMetric, subjectMetric);
    }


    public String normalise(String name) {
        name = name.toLowerCase(Locale.ENGLISH).trim();
        return normalise ? pattern.matcher(name).replaceAll("") : name;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Name Match: [");
        sb.append("Normalised=").append(normalise.toString());
        sb.append(", Synonyms=").append(includeSynonyms.toString());
        sb.append("]");
        return sb.toString();
    }
}
