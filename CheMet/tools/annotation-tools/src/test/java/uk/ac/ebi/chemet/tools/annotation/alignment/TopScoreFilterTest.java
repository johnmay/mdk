package uk.ac.ebi.chemet.tools.annotation.alignment;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.annotation.crossreference.EnzymeClassification;
import uk.ac.ebi.chemet.tools.annotation.AnnotationFilter;
import uk.ac.ebi.interfaces.Annotation;
import uk.ac.ebi.observation.sequence.LocalAlignment;
import uk.ac.ebi.resource.classification.ECNumber;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * TopScoreFilterTest - 06.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class TopScoreFilterTest {

    private static final Logger LOGGER = Logger.getLogger(TopScoreFilterTest.class);

    private AnnotationFilter filter = new TopScoreFilter();


    @Test
    public void testSingleHighScore() throws Exception {

        Collection<Annotation> annotations = Arrays.asList(create("1.1.1.1", 500, 550, 450),
                                                           create("1.1.1.2", 700, 750, 650),
                                                           create("1.1.1.85", 1000, 990, 790));
        Collection<Annotation> filtered = filter.filter(annotations);
        Assert.assertEquals(1, filtered.size());
        
        Annotation annotation = filtered.iterator().next();
        String accession = ((EnzymeClassification)annotation).getIdentifier().getAccession();

        Assert.assertEquals("1.1.1.85", accession);

    }

    @Test
    public void testMultipleHighScores() throws Exception {

        Collection<Annotation> annotations = Arrays.asList(create("1.1.1.1", 500, 550, 450),
                                                           create("1.1.1.2", 700, 750, 650),
                                                           create("1.1.1.-", 550, 750));

        Collection<Annotation> filtered = filter.filter(annotations);

        Assert.assertEquals(2, filtered.size());

        Set<String> accessions = new HashSet<String>(5);
        for (Annotation annotation : filtered) {
            accessions.add(((EnzymeClassification)annotation).getIdentifier().getAccession());
        }


        Assert.assertTrue(accessions.contains("1.1.1.-"));
        Assert.assertTrue(accessions.contains("1.1.1.2"));
        Assert.assertFalse(accessions.contains("1.1.1.1"));

    }

    public static Annotation create(String accession, double... scores) {

        EnzymeClassification<LocalAlignment> annotation = new EnzymeClassification<LocalAlignment>(new ECNumber(accession));

        for (double score : scores) {
            LocalAlignment observation = new LocalAlignment();
            observation.setBitScore(score);
            annotation.addObservation(observation);
        }

        return annotation;

    }

}
