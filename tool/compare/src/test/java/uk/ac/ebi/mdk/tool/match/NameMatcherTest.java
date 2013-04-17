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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.tool.match;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.Synonym;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;


/**
 *
 * @author johnmay
 */
public class NameMatcherTest {


    private EntityFactory factory;

    private Metabolite m1;

    private Metabolite m2;


    public NameMatcherTest() {

        factory = DefaultEntityFactory.getInstance();
    }


    @Before
    public void createNewMeatbolites() {
        m1 = factory.newInstance(Metabolite.class);
        m2 = factory.newInstance(Metabolite.class);
    }


    @Test
    public void testNameEquality() {

        EntityMatcher matcher = new NameMatcher();

        m1.setName("ATP");
        m2.setName("Adenosine TP");

        Assert.assertFalse(matcher.matches(m1, m2));

        m2.setName("ATP");

        Assert.assertTrue(matcher.matches(m1, m2));

    }

    @Test
    public void testNameEquality_inequalCase() {

        EntityMatcher matcher = new NameMatcher();

        m1.setName("ATP");
        m2.setName("Adenosine TP");

        Assert.assertFalse(matcher.matches(m1, m2));

        m2.setName("atp");

        Assert.assertTrue(matcher.matches(m1, m2));

    }


    @Test
         public void testSynonymEquality() {

        EntityMatcher matcher = new NameMatcher(false, true);

        m1.setName("ATP");
        m2.setName("Adenosine TP");

        Assert.assertFalse(matcher.matches(m1, m2));

        m2.addAnnotation(new Synonym("atp"));

        Assert.assertTrue(matcher.matches(m1, m2));

    }

    @Test
    public void testNormalisedEquality() {

        EntityMatcher matcher = new NameMatcher(false, false);

        m1.setName("Adenosine triphosphate");
        m2.setName("Adenosine-triphosphate");

        Assert.assertFalse(matcher.matches(m1, m2));

        matcher = new NameMatcher(true, false);

        Assert.assertTrue(matcher.matches(m1, m2));

    }

}
