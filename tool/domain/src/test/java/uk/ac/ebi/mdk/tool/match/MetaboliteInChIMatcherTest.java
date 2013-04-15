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
import uk.ac.ebi.mdk.domain.annotation.InChI;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;


/**
 *
 * @author johnmay
 */
public class MetaboliteInChIMatcherTest {

    private EntityMatcher matcher;

    private EntityMatcher comparatorLessSpecific;

    private EntityFactory factory;

    private Metabolite m1;

    private Metabolite m2;


    public MetaboliteInChIMatcherTest() {
        matcher = new MetaboliteInChIMatcher();
        comparatorLessSpecific = new MetaboliteInChIMatcher(true);
        factory = DefaultEntityFactory.getInstance();
    }


    @Before
    public void createNewMeatbolites() {
        m1 = factory.newInstance(Metabolite.class);
        m2 = factory.newInstance(Metabolite.class);
    }


    @Test
    public void testInChIEquality() {

        m1.setName("ATP"); // 4-
        m2.setName("ATP"); // 3-

        // InChI for ATP(4-) 
        m1.addAnnotation(new InChI("InChI=1S/"
                                   + "C10H16N5O13P3/"
                                   + "c11-8-5-9(13-2-12-8)15(3-14-5)10-7(17)6(16)4(26-10)1-25-30(21,22)28-31(23,24)27-29(18,19)20/"
                                   + "h2-4,6-7,10,16-17H,1H2,(H,21,22)(H,23,24)(H2,11,12,13)(H2,18,19,20)/"
                                   + "p-4/t4-,6-,7-,10-/m1/s1"));
        // InChI for ATP(3-) 
        m2.addAnnotation(new InChI("InChI=1S/"
                                   + "C10H16N5O13P3/"
                                   + "c11-8-5-9(13-2-12-8)15(3-14-5)10-7(17)6(16)4(26-10)1-25-30(21,22)28-31(23,24)27-29(18,19)20/"
                                   + "h2-4,6-7,10,16-17H,1H2,(H,21,22)(H,23,24)(H2,11,12,13)(H2,18,19,20)/"
                                   + "p-3/t4-,6-,7-,10-/m1/s1"));

        Assert.assertFalse(matcher.matches(m1, m2));

        // InChI for ATP(3-) 
        m1.addAnnotation(new InChI("InChI=1S/"
                                   + "C10H16N5O13P3/"
                                   + "c11-8-5-9(13-2-12-8)15(3-14-5)10-7(17)6(16)4(26-10)1-25-30(21,22)28-31(23,24)27-29(18,19)20/"
                                   + "h2-4,6-7,10,16-17H,1H2,(H,21,22)(H,23,24)(H2,11,12,13)(H2,18,19,20)/"
                                   + "p-3/t4-,6-,7-,10-/m1/s1"));

        Assert.assertTrue(matcher.matches(m1, m2));

    }


    @Test
    public void testInChIEquality_Connectivity() {

        m1.setName("ATP"); // 4-
        m2.setName("ATP"); // 3-

        // InChI for ATP(4-) 
        m1.addAnnotation(new InChI("InChI=1S/"
                                   + "C10H16N5O13P3/"
                                   + "c11-8-5-9(13-2-12-8)15(3-14-5)10-7(17)6(16)4(26-10)1-25-30(21,22)28-31(23,24)27-29(18,19)20/"
                                   + "h2-4,6-7,10,16-17H,1H2,(H,21,22)(H,23,24)(H2,11,12,13)(H2,18,19,20)/"
                                   + "p-4/t4-,6-,7-,10-/m1/s1"));
        // InChI for ATP(3-)
        m2.addAnnotation(new InChI("InChI=1S/"
                                   + "C10H16N5O13P3/"
                                   + "c11-8-5-9(13-2-12-8)15(3-14-5)10-7(17)6(16)4(26-10)1-25-30(21,22)28-31(23,24)27-29(18,19)20/"
                                   + "h2-4,6-7,10,16-17H,1H2,(H,21,22)(H,23,24)(H2,11,12,13)(H2,18,19,20)/"
                                   + "p-3/t4-,6-,7-,10-/m1/s1"));

        Assert.assertTrue(comparatorLessSpecific.matches(m1, m2));

    }
}
