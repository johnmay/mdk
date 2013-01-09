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

import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.annotation.AnnotationVisitor;
import uk.ac.ebi.mdk.domain.annotation.primitive.StringAnnotation;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.IdentifierFactory;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Annotation visitor that can extract references from StringAnnotations. The
 * extractor can be configured with a custom pattern to match specific identifiers.
 *
 * @author John May
 */
public class ReferenceExtractor<A extends StringAnnotation>
        implements AnnotationVisitor<Identifier> {

    private final MultiGroupPatternMatcher<A> visitor;
    private final IdentifierFactory factory = DefaultIdentifierFactory.getInstance();

    public static final String DEFAULT_RESOURCE_PATTERN  = "(?:[A-z0-9]+(?:\\s[A-z0-9]+)*?)";
    public static final String DEFAULT_SEPARATOR_PATTERN = "(?:\\s(?:id|identifier|accession))?[-:=]\\s*";
    public static final String DEFAULT_ACCESSION_PATTERN = "[^ ]+";

    /**
     * Constructor a reference extractor for the given class. This method will use
     * the default pattern to match identifier names and accessions.
     *
     * @param c the class of annotation to extract from (e.g. Note.class)
     */
    public ReferenceExtractor(Class<? extends A> c) {
        this(c, DEFAULT_RESOURCE_PATTERN, DEFAULT_SEPARATOR_PATTERN, DEFAULT_ACCESSION_PATTERN);
    }

    public ReferenceExtractor(Class<? extends A> c,
                              String resourcePattern,
                              String separatorPattern,
                              String identifierPattern) {
        this.visitor = new MultiGroupPatternMatcher<A>(c,
                                                       getPattern(resourcePattern,
                                                                  separatorPattern,
                                                                  identifierPattern),
                                                       2);
    }

    /**
     * Visits the specified annotation if no match is found the extraction
     * pattern an {@link IdentifierFactory#EMPTY_IDENTIFIER} is returned.
     *
     * @param annotation the annotation to extract an identifier from
     * @return the extracted identifier or an {@link IdentifierFactory#EMPTY_IDENTIFIER}
     */
    @Override
    public Identifier visit(Annotation annotation) {

        List<String> groups = visitor.visit(annotation);

        if (groups.isEmpty())
            return IdentifierFactory.EMPTY_IDENTIFIER;

        return factory.ofSynonym(groups.get(0), groups.get(1));

    }

    /**
     * Utility to help compile the pattern from the three components.
     *
     * @param resource  pattern for the resource
     * @param separator pattern for the separator
     * @param accession pattern for the accession
     * @return the compiled pattern
     */
    private static Pattern getPattern(String resource, String separator, String accession) {
        return Pattern.compile("(" + resource + ")" + separator + "(" + accession + ")", Pattern.CASE_INSENSITIVE);
    }

}
