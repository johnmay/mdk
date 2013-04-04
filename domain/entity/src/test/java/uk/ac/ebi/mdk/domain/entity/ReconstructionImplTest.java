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
import uk.ac.ebi.mdk.domain.entity.reaction.BiochemicalReaction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

import java.util.UUID;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author John May
 */
public class ReconstructionImplTest {

    @Test public void testAddBioChemReaction() {

        MetabolicReaction delegate = mock(MetabolicReaction.class);

        BiochemicalReaction biochem = mock(BiochemicalReaction.class);
        when(biochem.getMetabolicReaction()).thenReturn(delegate);
        when(delegate.uuid()).thenReturn(uuid("r1"));
        when(biochem.uuid()).thenReturn(uuid("br1"));

        Reconstruction recon = new ReconstructionImpl();
        recon.addReaction(biochem);

        assertNull(recon.entity(uuid("br1")));
        assertNotNull(recon.entity(uuid("r1")));
    }

    private static final UUID uuid(String x){
        return UUID.nameUUIDFromBytes(x.getBytes());
    }

    @Test public void testRegister() {
        Reconstruction recon = new ReconstructionImpl();
        Metabolite m = new MetaboliteImpl();
        assertTrue(recon.register(m));
        assertFalse(recon.register(m));
        assertNotNull(recon.entity(m.uuid()));
    }

    @Test public void testDeregister() {
        Reconstruction recon = new ReconstructionImpl();
        Metabolite m = new MetaboliteImpl();
        assertTrue(recon.register(m));
        assertNotNull(recon.entity(m.uuid()));
        assertFalse(recon.register(m));
        assertTrue(recon.deregister(m));
        assertFalse(recon.deregister(m));
        assertNull(recon.entity(m.uuid()));
    }

    @Test
    public void testRemove_Metabolite_Null() throws Exception {
        new ReconstructionImpl().remove((Metabolite) null);
    }
}
