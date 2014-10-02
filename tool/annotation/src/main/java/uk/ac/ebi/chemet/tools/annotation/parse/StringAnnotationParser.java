package uk.ac.ebi.chemet.tools.annotation.parse;

import uk.ac.ebi.mdk.domain.annotation.primitive.StringAnnotation;

/**
 * Simple function for creating string annotations.
 *
 * @author John May
 */
final class StringAnnotationParser
        extends AnnotationParser<StringAnnotation> {

    private final StringAnnotation template;

    /**
     * Parse for the given annotation.
     *
     * @param annotation an annotation
     */
    StringAnnotationParser(StringAnnotation annotation) {
        template = annotation;
    }

    /** @inheritDoc */
    @Override public StringAnnotation parse(String str) {
        if (str == null || str.isEmpty())
            return null;
        return template.getInstance(str);
    }
}
