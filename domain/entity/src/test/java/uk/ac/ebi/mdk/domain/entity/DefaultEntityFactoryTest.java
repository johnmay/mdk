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
