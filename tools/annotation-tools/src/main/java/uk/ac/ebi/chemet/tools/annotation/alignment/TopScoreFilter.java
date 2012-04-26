package uk.ac.ebi.chemet.tools.annotation.alignment;

import uk.ac.ebi.chemet.tools.annotation.AnnotationFilter;
import uk.ac.ebi.mdk.domain.annotation.ObservationBasedAnnotation;
import uk.ac.ebi.observation.sequence.LocalAlignment;

import java.util.ArrayList;
import java.util.Collection;

/**
 * TopScoreFilter - 06.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class TopScoreFilter implements AnnotationFilter<ObservationBasedAnnotation<LocalAlignment>> {

    /**
     * Filters a collection of observations and leaves only those with the highest
     * bit score. If multiple annotations have the same high bit score
     *
     * @param annotations a collection of ObservationBasedAnnotations which hold LocalAlignment observations
     *
     * @return filltered collection of the input
     */
    public Collection<ObservationBasedAnnotation<LocalAlignment>> filter(Collection<ObservationBasedAnnotation<LocalAlignment>> annotations) {

        Collection<ObservationBasedAnnotation<LocalAlignment>> filtered = new ArrayList<ObservationBasedAnnotation<LocalAlignment>>();
        double max = 0d;

        for (ObservationBasedAnnotation<LocalAlignment> annotation : annotations) {
            for (LocalAlignment alignment : annotation.getObservations()) {

                double score = alignment.getBitScore();

                if (score >= max) {

                    // score is larger clear collection
                    if (score != max)
                        filtered.clear();

                    // add to the returned collection and set the max score
                    filtered.add(annotation);
                    max = score;

                }


            }
        }

        return filtered;

    }
}
