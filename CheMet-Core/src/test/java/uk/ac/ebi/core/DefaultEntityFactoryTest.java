/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.core;

import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipantImplementation;
import uk.ac.ebi.mdk.domain.entity.*;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;


/**
 *
 * @author johnmay
 */
public class DefaultEntityFactoryTest {

    public DefaultEntityFactoryTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testNewInstance() {

        EntityFactory factory = DefaultEntityFactory.getInstance();

        Gene gene = factory.newInstance(Gene.class);
        Assert.assertEquals(GeneImpl.class, gene.getClass());

        Metabolite metabolite = factory.newInstance(Metabolite.class);
        Assert.assertEquals(MetaboliteImpl.class, metabolite.getClass());

        Entity p = factory.newInstance(MetabolicParticipant.class);
        Assert.assertEquals(MetabolicParticipantImplementation.class, p.getClass());

        // remember to check the newInstance method of the entity if something is not working!

    }


    @Test
    public void testGetEntityClass() {

        EntityFactory factory = DefaultEntityFactory.getInstance();

        Assert.assertEquals(Gene.class, factory.getEntityClass(GeneImpl.class));
        Assert.assertEquals(Metabolite.class, factory.getEntityClass(MetaboliteImpl.class));

    }


    @Test
    public void testGetRootEntityClass() {

        System.out.println("testGetRootEntityClass");
        EntityFactory factory = DefaultEntityFactory.getInstance();

        Assert.assertEquals(GeneProduct.class, factory.getRootClass(RibosomalRNA.class));
        Assert.assertEquals(GeneProduct.class, factory.getRootClass(RibosomalRNAImpl.class));
        Assert.assertEquals(GeneProduct.class, factory.getRootClass(TransferRNA.class));
        Assert.assertEquals(GeneProduct.class, factory.getRootClass(TransferRNAImpl.class));
        Assert.assertEquals(GeneProduct.class, factory.getRootClass(ProteinProductImpl.class));
        Assert.assertEquals(GeneProduct.class, factory.getRootClass(MultimerImpl.class));

    }
}
