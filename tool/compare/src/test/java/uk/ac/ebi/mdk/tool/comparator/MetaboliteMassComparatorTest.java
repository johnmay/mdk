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

package uk.ac.ebi.mdk.tool.comparator;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.ExactMass;
import uk.ac.ebi.mdk.domain.entity.Metabolite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author John May
 */
public class MetaboliteMassComparatorTest {

    @Test
    public void testCompare() throws Exception {
        Metabolite a = mock(Metabolite.class);
        Metabolite b = mock(Metabolite.class);
        when(a.getAnnotations(ExactMass.class)).thenReturn(Arrays.asList(new ExactMass(28.0f)));
        when(b.getAnnotations(ExactMass.class)).thenReturn(Arrays.asList(new ExactMass(35.0f)));
        assertThat(new MetaboliteMassComparator().compare(a, b), is(-1));
        assertThat(new MetaboliteMassComparator().compare(b, a), is(1));
    }

    @Test
    public void testCompare_Missing() throws Exception {
        Metabolite a = mock(Metabolite.class);
        Metabolite b = mock(Metabolite.class);
        when(a.getAnnotations(ExactMass.class)).thenReturn(Arrays.asList(new ExactMass(28.0f)));
        when(b.getAnnotations(ExactMass.class)).thenReturn(Collections.<ExactMass>emptyList());
        assertThat(new MetaboliteMassComparator().compare(a, b), is(-1));
        assertThat(new MetaboliteMassComparator().compare(b, a), is(1));
    }
}
