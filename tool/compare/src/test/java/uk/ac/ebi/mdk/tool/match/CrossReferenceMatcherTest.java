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

package uk.ac.ebi.mdk.tool.match;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.annotation.crossreference.KEGGCrossReference;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;

/**
 * @author John May
 */
public class CrossReferenceMatcherTest {

    private CrossReferenceMatcher<Metabolite> matcher = new CrossReferenceMatcher<Metabolite>();

    @Test
    @SuppressWarnings("unchecked")
    public void testEqual_direct() throws Exception {

        Metabolite m1 = DefaultEntityFactory.getInstance().ofClass(Metabolite.class, new BasicChemicalIdentifier("test-1"), "", "");
        Metabolite m2 = DefaultEntityFactory.getInstance().ofClass(Metabolite.class, new BasicChemicalIdentifier("test-2"), "", "");

        Assert.assertFalse(matcher.matches(m1, m2));

        m1.addAnnotation(new CrossReference(new BasicChemicalIdentifier("test-2")));

        Assert.assertTrue(matcher.matches(m1, m2));
        Assert.assertTrue(matcher.matches(m2, m1));

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testEqual_indirect() throws Exception {

        Metabolite m1 = DefaultEntityFactory.getInstance().ofClass(Metabolite.class, new BasicChemicalIdentifier("test-1"), "", "");
        Metabolite m2 = DefaultEntityFactory.getInstance().ofClass(Metabolite.class, new BasicChemicalIdentifier("test-2"), "", "");

        Assert.assertFalse(matcher.matches(m1, m2));

        m1.addAnnotation(new CrossReference(new KEGGCompoundIdentifier("C00009")));
        m2.addAnnotation(new CrossReference(new KEGGCompoundIdentifier("C00009")));

        Assert.assertTrue(matcher.matches(m1, m2));
        Assert.assertTrue(matcher.matches(m2, m1));

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testEqual_indirect_subclass() throws Exception {

        Metabolite m1 = DefaultEntityFactory.getInstance().ofClass(Metabolite.class, new BasicChemicalIdentifier("test-1"), "", "");
        Metabolite m2 = DefaultEntityFactory.getInstance().ofClass(Metabolite.class, new BasicChemicalIdentifier("test-2"), "", "");

        Assert.assertFalse(matcher.matches(m1, m2));

        m1.addAnnotation(new CrossReference(new KEGGCompoundIdentifier("C00009")));
        m2.addAnnotation(new KEGGCrossReference(new KEGGCompoundIdentifier("C00009")));

        Assert.assertTrue(matcher.matches(m1, m2));
        Assert.assertTrue(matcher.matches(m2, m1));

    }


}
