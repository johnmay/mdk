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

package uk.ac.ebi.chemet.tools.annotation.alignment;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.crossreference.EnzymeClassification;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;
import uk.ac.ebi.chemet.tools.annotation.AnnotationFilter;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.observation.sequence.LocalAlignment;

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
