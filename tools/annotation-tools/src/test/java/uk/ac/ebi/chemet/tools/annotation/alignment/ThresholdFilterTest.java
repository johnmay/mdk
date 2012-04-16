package uk.ac.ebi.chemet.tools.annotation.alignment;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.chemet.tools.annotation.AnnotationFilter;
import uk.ac.ebi.interfaces.Annotation;

import java.util.Arrays;
import java.util.Collection;

/**
 * ThresholdFilterTest - 06.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ThresholdFilterTest {

    private static final Logger LOGGER = Logger.getLogger(ThresholdFilterTest.class);

    @Test
    public void testFilter() throws Exception {

        Collection<Annotation> annotations = Arrays.asList(TopScoreFilterTest.create("1.1.1.1", 500, 550, 900),
                                                          TopScoreFilterTest.create("1.1.1.2", 500, 450, 700),
                                                          TopScoreFilterTest.create("1.1.1.85", 500, 450, 1050));

        AnnotationFilter filter = new ThresholdFilter(750);

        Collection<Annotation> filtered = filter.filter(annotations);

        Assert.assertEquals(2, filtered.size());



    }
}
