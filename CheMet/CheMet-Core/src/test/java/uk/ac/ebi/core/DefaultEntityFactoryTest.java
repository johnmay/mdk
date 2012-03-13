/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.core;

import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;
import uk.ac.ebi.interfaces.Gene;
import uk.ac.ebi.interfaces.entities.*;


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
        Assert.assertEquals(GeneImplementation.class, gene.getClass());

        Metabolite metabolite = factory.newInstance(Metabolite.class);
        Assert.assertEquals(uk.ac.ebi.core.MetaboliteImplementation.class, metabolite.getClass());

        Entity p = factory.newInstance(MetabolicParticipant.class);
        Assert.assertEquals(MetabolicParticipant.class, p.getClass());

        // remember to check the newInstance method of the entity if something is not working!

    }


    @Test
    public void testGetEntityClass() {

        EntityFactory factory = DefaultEntityFactory.getInstance();

        Assert.assertEquals(Gene.class, factory.getEntityClass(GeneImplementation.class));
        Assert.assertEquals(Metabolite.class, factory.getEntityClass(MetaboliteImplementation.class));

    }


    @Test
    public void testGetRootEntityClass() {

        System.out.println("testGetRootEntityClass");
        EntityFactory factory = DefaultEntityFactory.getInstance();

        Assert.assertEquals(GeneProduct.class, factory.getRootClass(RibosomalRNA.class));
        Assert.assertEquals(GeneProduct.class, factory.getRootClass(RibosomalRNAImplementation.class));
        Assert.assertEquals(GeneProduct.class, factory.getRootClass(TransferRNA.class));
        Assert.assertEquals(GeneProduct.class, factory.getRootClass(TransferRNAImplementation.class));
        Assert.assertEquals(GeneProduct.class, factory.getRootClass(ProteinProductImplementation.class));
        Assert.assertEquals(GeneProduct.class, factory.getRootClass(MultimerImplementation.class));

    }
}
