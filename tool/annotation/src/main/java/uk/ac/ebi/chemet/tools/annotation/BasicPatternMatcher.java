/*
 * Copyright (c) 2012. John May <jwmay@sf.net>
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

package uk.ac.ebi.chemet.tools.annotation;

import uk.ac.ebi.mdk.domain.annotation.primitive.StringAnnotation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An implementation of an annotation visitor that can visit string annotations and
 * extract parts matching a given regular expression pattern. This implementation requires the
 * regular expression contains a single pair of capturing parenthesis. The pattern should also
 * match the string from start to end.
 *
 * @author John May
 * @see java.util.regex.Matcher#matches()
 * @see Pattern
 * @see Matcher#group(int)
 */
public class BasicPatternMatcher<A extends StringAnnotation> extends VisitorAdapter<A, String> {

    private final Pattern pattern;

    /**
     * Construct a basic pattern match - this annotation visitor will visit
     * string annotations of exact type ('c') and return the first match.
     *
     * @param c       the class of annotation match
     * @param pattern the pattern to match against the StringAnnotation
     *                value
     *
     */
    public BasicPatternMatcher(Class<? extends A> c,
                               Pattern pattern){
        this(c, pattern, Boolean.FALSE);
    }

    /**
     * Construct a basic pattern match - this annotation visitor will visit
     * string annotations of type ('c') and return the first match. Subclasses
     * can be accepted by passing the 'subclass' parameter as true.
     *
     * @param c        the class of annotation match
     * @param pattern  the pattern to match against the StringAnnotation
     *                 value
     * @param subclass accept subclasses of the provided type 'c'
     *
     */
    public BasicPatternMatcher(Class<? extends A> c,
                               Pattern pattern,
                               Boolean subclass) {
        super(c, "", subclass);

        this.pattern = pattern;

        if(pattern == null)
            throw new IllegalArgumentException("Null pattern provided");

        if (pattern.matcher("").groupCount() != 1)
            throw new IllegalArgumentException("Pattern must contain one pair of capturing parentheses");

    }

    /**
     * Uses the pattern provided in the constructor to match the value on the
     * string annotation. If the pattern is matched the first captured group
     * is returned otherwise an empty string (no match).
     *
     * @param annotation the annotation to match
     * @return matched parentheses or an empty string
     * @see java.util.regex.Matcher#group(int)
     */
    @Override
    public String _visit(StringAnnotation annotation) {

        String  value   = annotation.getValue();
        Matcher matcher = pattern.matcher(value);

        // we check the group count in the constructor
        return matcher.matches() ? matcher.group(1) : "";

    }
}
