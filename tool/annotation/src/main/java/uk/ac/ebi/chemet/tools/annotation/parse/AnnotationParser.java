package uk.ac.ebi.chemet.tools.annotation.parse;

import uk.ac.ebi.mdk.domain.annotation.GibbsEnergy;
import uk.ac.ebi.mdk.domain.annotation.primitive.StringAnnotation;

/**
 * Given a string create an annotation.
 *
 * @author John May
 */
public abstract class AnnotationParser<A> {

    public static AnnotationParser<GibbsEnergy> gibbs() {
        return new GibbsAnnotationParser();
    }

    public static AnnotationParser<StringAnnotation> basic(StringAnnotation a) {
        return new StringAnnotationParser(a);
    }

    /**
     * Parse the str and return an annotation. If the str was invalid then null
     * is returned.
     *
     * @param str a string
     * @return parsed annotation
     */
    public abstract A parse(String str);

}
