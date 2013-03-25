package uk.ac.ebi.mdk.domain.entity;

import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * @author John May
 */
public class DefaultEntityFactoryTest {

    EntityFactory factory = DefaultEntityFactory.getInstance();

    @Test
    public void testMetabolite() throws Exception {
        Assert.assertNotNull(factory.metabolite());
    }

    @Test
    public void testMetabolite_UUID() throws Exception {
        UUID uuid = UUID.randomUUID();
        Entity e = factory.metabolite(uuid);
        Assert.assertNotNull(e);
        Assert.assertEquals(uuid, e.uuid());
    }

    @Test
    public void testProtein() throws Exception {
        Assert.assertNotNull(factory.protein());
    }

    @Test
    public void testProtein_UUID() throws Exception {
        UUID uuid = UUID.randomUUID();
        Entity e = factory.protein(uuid);
        Assert.assertNotNull(e);
        Assert.assertEquals(uuid, e.uuid());
    }

    @Test
    public void testRRNA() throws Exception {
        Assert.assertNotNull(factory.rRNA());
    }

    @Test
    public void testRRNA_UUID() throws Exception {
        UUID uuid = UUID.randomUUID();
        Entity e = factory.rRNA(uuid);
        Assert.assertNotNull(e);
        Assert.assertEquals(uuid, e.uuid());
    }

    @Test
    public void testTRNA() throws Exception {
        Assert.assertNotNull(factory.tRNA());
    }

    @Test
    public void testTRNA_UUID() throws Exception {
        UUID uuid = UUID.randomUUID();
        Entity e = factory.tRNA(uuid);
        Assert.assertNotNull(e);
        Assert.assertEquals(uuid, e.uuid());
    }

    @Test
    public void testGene() throws Exception {
        Assert.assertNotNull(factory.gene());
    }

    @Test
    public void testGene_UUID() throws Exception {
        UUID uuid = UUID.randomUUID();
        Entity e = factory.gene(uuid);
        Assert.assertNotNull(e);
        Assert.assertEquals(uuid, e.uuid());
    }

    @Test
    public void testReaction() throws Exception {
        Assert.assertNotNull(factory.reaction());
    }

    @Test
    public void testReaction_UUID() throws Exception {
        UUID uuid = UUID.randomUUID();
        Entity e = factory.reaction(uuid);
        Assert.assertNotNull(e);
        Assert.assertEquals(uuid, e.uuid());
    }
}
