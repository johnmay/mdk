package uk.ac.ebi.chemet.tools.annotation.alignment;

import uk.ac.ebi.chemet.tools.annotation.AnnotationFilter;
import uk.ac.ebi.interfaces.annotation.ObservationBasedAnnotation;
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
public class ThresholdFilter implements AnnotationFilter<ObservationBasedAnnotation<LocalAlignment>> {

    private double threshold = 500d;

    public ThresholdFilter(){

    }

    public ThresholdFilter(double threshold) {
        this.threshold = threshold;
    }

    /**
     * Filters a collection of observations and leaves only those above the threshold
     *
     * @param annotations a collection of ObservationBasedAnnotations which hold LocalAlignment observations
     *
     * @return annotation above the threshold
     */
    public Collection<ObservationBasedAnnotation<LocalAlignment>> filter(Collection<ObservationBasedAnnotation<LocalAlignment>> annotations) {

        Collection<ObservationBasedAnnotation<LocalAlignment>> filtered = new ArrayList<ObservationBasedAnnotation<LocalAlignment>>();

        for (ObservationBasedAnnotation<LocalAlignment> annotation : annotations) {

            OBSERVATION:
            for (LocalAlignment alignment : annotation.getObservations()) {

                double score = alignment.getBitScore();

                if (score > threshold) {
                    // add to the returned collection and break
                    filtered.add(annotation);
                    break OBSERVATION;
                }

            }
        }

        return filtered;

    }
}
