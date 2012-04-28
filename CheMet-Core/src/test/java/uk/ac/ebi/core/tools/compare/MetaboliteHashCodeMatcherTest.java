/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.core.tools.compare;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.annotation.chemical.AtomContainerAnnotation;
import uk.ac.ebi.chemet.TestMoleculeFactory;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.tool.compare.EntityMatcher;
import uk.ac.ebi.mdk.tool.domain.hash.*;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;


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
