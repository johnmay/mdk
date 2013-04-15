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
import uk.ac.ebi.chemet.tools.annotation.AnnotationFilter;
import uk.ac.ebi.mdk.domain.annotation.Annotation;

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
