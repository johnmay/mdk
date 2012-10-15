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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An implementation of an annotation visitor that can visit string annotations and
 * extract parts matching a given regular expression pattern. This implementation allows
 * the regular expression to contain a multiple pairs of capturing parenthesis. The pattern
 * should also match the string from start to end. If no match is found or the annotation
 * type does not match an immutable list the size of the expected group count it returned
 * which contains empty strings for the unmatched groups.
 * Due to this the pattern should not contain an optional group count. A pattern such as
 * '(1)(2)|(3)' will return two groups for the string '12' and one group for the string '3'.
 * This problem requires the expected group count to be explicitly stated as a constructor
 * parameter.
 * <p/>
 * <i>sample code:</i><br/>
 * <pre> {@code
 * AnnotationVisitor<List<String>> visitor = new MultiGroupPatternMatcher<Note>(Note.class,
 *                                                                              Pattern.compile("(a) simple (test)"),
 *                                                                              2);
 * <p/>
 * Note pass = new Note("a simple test");     // pattern will match
 * Note fail = new Note("not a simple test"); // pattern does not match from start to end
 * pass.accept(visitor);                      // fixed size list (length=2) with "a" and "test"
 * fail.accept(visitor);                      // fixed size list (length=2) with empty string values
 * }
 * </pre>
 *
 * @author John May
 * @see java.util.regex.Matcher#matches()
 * @see java.util.regex.Pattern
 * @see java.util.regex.Matcher#group(int)
 */
public class MultiGroupPatternMatcher<A extends StringAnnotation> extends VisitorAdapter<A, List<String>> {

    private final Pattern pattern;
    private final Integer groupCount;

    /**
     * Construct a multi-group pattern matching visitor for an exact class
     * type (i.e. subclasses are not accepted).
     *
     * @param c          class type to visit
     * @param pattern    pattern to match
     * @param groupCount the expected number of groups to capture
     */
    public MultiGroupPatternMatcher(Class<? extends A> c,
                                    Pattern pattern,
                                    Integer groupCount) {
        this(c, pattern, groupCount, Boolean.FALSE);
    }

    /**
     * Construct a multi-group pattern matching visitor that can
     * optionally accept subclasses.
     *
     * @param c          class type to visit
     * @param pattern    pattern to match
     * @param groupCount the expected number of groups to capture
     * @param subclass   whether the visitor accepts subclasses
     */
    public MultiGroupPatternMatcher(Class<? extends A> c,
                                    Pattern pattern,
                                    Integer groupCount,
                                    Boolean subclass) {
        super(c, makeList(groupCount), subclass);

        this.pattern = pattern;

        if (pattern == null)
            throw new IllegalArgumentException("Null pattern provided");

        this.groupCount = pattern.matcher("").groupCount();

        // we double check the groupCount as some patterns may have variable
        // groups e.g. (?:(first)(second)|(third)) can have 1 or 2
        if (!this.groupCount.equals(groupCount))
            throw new IllegalArgumentException("Expected group count did not match provided group count. This may " +
                                                       "mean the provided pattern has option groups e.g. '(\\d)(\\d)|(\\w)'");

    }

    /**
     * Uses the pattern provided in the constructor to match the value on the
     * string annotation. If the pattern is matched the first captured group
     * is returned otherwise an empty string (no match).
     *
     * @param annotation the annotation to match
     * @return matched parentheses place in a immutable list
     * @see java.util.regex.Matcher#group(int)
     */
    @Override
    public List<String> _visit(StringAnnotation annotation) {

        String  value   = annotation.getValue();
        Matcher matcher = pattern.matcher(value);

        return matcher.matches() ? getCaptureList(matcher) : getValue();

    }

    /**
     * Extracts the captured groups from a matched matcher and creates
     * an immutable list of string for each captured group.
     *
     * @param matcher a matcher which has been 'matched()'
     * @return immutable list with group contents
     */
    private List<String> getCaptureList(Matcher matcher) {

        List<String> groups = new ArrayList<String>(groupCount);

        for (int i = 1; i <= groupCount; i++) {
            groups.add(matcher.group(i));
        }

        return Collections.unmodifiableList(groups);

    }

    /**
     * Constructs a fixed size list of empty strings and allows us to pass
     * a fixed size list to the super-class for a default value.
     *
     * @param n the size of the list
     * @return immutable list
     */
    private static List<String> makeList(int n) {

        List<String> list = new ArrayList<String>(n);

        for (int i = 0; i < n; i++)
            list.add("");

        // make sure no one tries to add anything to it
        return Collections.unmodifiableList(list);

    }

}
