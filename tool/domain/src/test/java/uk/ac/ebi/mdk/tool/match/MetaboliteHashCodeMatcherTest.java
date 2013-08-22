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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.prototype.hash.seed.AtomicNumberSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.BondOrderSumSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.ChargeSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.ConnectedAtomSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.SeedFactory;
import uk.ac.ebi.mdk.prototype.hash.TestMoleculeFactory;


/**
 *
 * @author johnmay
 */
public class MetaboliteHashCodeMatcherTest {

    private EntityMatcher matcher;

    private EntityMatcher comparatorWithCharge;

    private EntityFactory factory;

    private Metabolite m1;

    private Metabolite m2;


    public MetaboliteHashCodeMatcherTest() {
        matcher = new MetaboliteHashCodeMatcher();
        comparatorWithCharge = new MetaboliteHashCodeMatcher(SeedFactory.getInstance().getSeeds(ChargeSeed.class,
                                                                                                   ConnectedAtomSeed.class,
                                                                                                   AtomicNumberSeed.class,
                                                                                                   BondOrderSumSeed.class));
        factory = DefaultEntityFactory.getInstance();
    }


    @Before
    public void createNewMeatbolites() {
        m1 = factory.newInstance(Metabolite.class);
        m2 = factory.newInstance(Metabolite.class);
    }


    @Test
    public void testHashCodeEquality_WithCharge() {

        m1.setName("ATP"); // 4-
        m2.setName("ATP"); // 3-

        // InChI for ATP(4-) 
        m1.addAnnotation(new AtomContainerAnnotation(TestMoleculeFactory.atp_minus_4()));
        // InChI for ATP(3-) 
        m2.addAnnotation(new AtomContainerAnnotation(TestMoleculeFactory.atp_minus_3()));

        Assert.assertFalse(comparatorWithCharge.matches(m1, m2));

        // InChI for ATP(3-) 
        m1.addAnnotation(new AtomContainerAnnotation(TestMoleculeFactory.atp_minus_3()));

        Assert.assertTrue(comparatorWithCharge.matches(m1, m2));

    }


    @Test
    public void testHashCodeEquality_NoCharge() {

        m1.setName("ATP"); // 4-
        m2.setName("ATP"); // 3-

        // InChI for ATP(4-) 
        m1.addAnnotation(new AtomContainerAnnotation(TestMoleculeFactory.atp_minus_4()));
        // InChI for ATP(3-)
        m2.addAnnotation(new AtomContainerAnnotation(TestMoleculeFactory.atp_minus_3()));

        Assert.assertTrue(matcher.matches(m1, m2));

    }
}
