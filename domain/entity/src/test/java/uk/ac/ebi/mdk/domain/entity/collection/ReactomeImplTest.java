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

package uk.ac.ebi.mdk.domain.entity.collection;

import org.junit.Test;
import uk.ac.ebi.mdk.domain.entity.Entity;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author John May
 */
public class ReactomeImplTest {

    @Test
    public void testAdd() throws Exception {
        Reconstruction reconstruction = mock(Reconstruction.class);
        when(reconstruction.register(any(Entity.class))).thenReturn(true);
        Reactome reactome = new ReactomeImpl(reconstruction);


        MetabolicReaction r1 = mock(MetabolicReaction.class);
        MetabolicReaction r2 = mock(MetabolicReaction.class);
        MetabolicReaction r3 = mock(MetabolicReaction.class);

        reactome.add(r1);
        reactome.add(r2);
        reactome.add(r3);

        verify(reconstruction).register(r1);
        verify(reconstruction).register(r2);
        verify(reconstruction).register(r3);

        assertThat(reactome.size(), is(3));
    }

    @Test
    public void testAdd_Participants() throws Exception {
        Reconstruction reconstruction = mock(Reconstruction.class);
        when(reconstruction.register(any(Entity.class))).thenReturn(true);
        Reactome reactome = new ReactomeImpl(reconstruction);

        MetabolicReaction r1 = mock(MetabolicReaction.class);
        MetabolicParticipant p1 = mock(MetabolicParticipant.class);
        MetabolicParticipant p2 = mock(MetabolicParticipant.class);
        MetabolicParticipant p3 = mock(MetabolicParticipant.class);
        Metabolite m1 = mock(Metabolite.class);
        Metabolite m2 = mock(Metabolite.class);
        Metabolite m3 = mock(Metabolite.class);
        when(p1.getMolecule()).thenReturn(m1);
        when(p2.getMolecule()).thenReturn(m2);
        when(p3.getMolecule()).thenReturn(m3);

        when(r1.getParticipants()).thenReturn(Arrays.asList(p1, p2, p3));

        reactome.add(r1);
        verify(reconstruction).register(r1);
        verify(reconstruction).associate(m1, r1);
        verify(reconstruction).associate(m2, r1);
        verify(reconstruction).associate(m3, r1);
        assertThat(reactome.size(), is(1));
    }

    @Test
    public void testRemove() throws Exception {
        Reconstruction reconstruction = mock(Reconstruction.class);
        when(reconstruction.register(any(Entity.class))).thenReturn(true);
        Reactome reactome = new ReactomeImpl(reconstruction);


        MetabolicReaction r1 = mock(MetabolicReaction.class);
        MetabolicReaction r2 = mock(MetabolicReaction.class);
        MetabolicReaction r3 = mock(MetabolicReaction.class);

        reactome.add(r1);
        reactome.add(r2);
        reactome.add(r3);

        verify(reconstruction).register(r1);
        verify(reconstruction).register(r2);
        verify(reconstruction).register(r3);

        assertThat(reactome.size(), is(3));

        reactome.remove(r2);
        verify(reconstruction).deregister(r2);

        assertThat(reactome.size(), is(2));
    }

    @Test
    public void testRemove_Participants() throws Exception {
        Reconstruction reconstruction = mock(Reconstruction.class);
        when(reconstruction.register(any(Entity.class))).thenReturn(true);
        Reactome reactome = new ReactomeImpl(reconstruction);

        MetabolicReaction r1 = mock(MetabolicReaction.class);
        MetabolicParticipant p1 = mock(MetabolicParticipant.class);
        MetabolicParticipant p2 = mock(MetabolicParticipant.class);
        MetabolicParticipant p3 = mock(MetabolicParticipant.class);
        Metabolite m1 = mock(Metabolite.class);
        Metabolite m2 = mock(Metabolite.class);
        Metabolite m3 = mock(Metabolite.class);
        when(p1.getMolecule()).thenReturn(m1);
        when(p2.getMolecule()).thenReturn(m2);
        when(p3.getMolecule()).thenReturn(m3);

        when(r1.getParticipants()).thenReturn(Arrays.asList(p1, p2, p3));

        reactome.add(r1);

        verify(reconstruction).register(r1);
        verify(reconstruction).associate(m1, r1);
        verify(reconstruction).associate(m2, r1);
        verify(reconstruction).associate(m3, r1);
        assertThat(reactome.size(), is(1));

        reactome.remove(r1);

        verify(reconstruction).deregister(r1);
        verify(reconstruction).dissociate(m1, r1);
        verify(reconstruction).dissociate(m2, r1);
        verify(reconstruction).dissociate(m3, r1);
        assertThat(reactome.size(), is(0));
    }

    @Test public void immutableToList() {
        Reconstruction reconstruction = mock(Reconstruction.class);
        when(reconstruction.register(any(Entity.class))).thenReturn(true);
        Reactome reactome = new ReactomeImpl(reconstruction);


        MetabolicReaction r1 = mock(MetabolicReaction.class);
        MetabolicReaction r2 = mock(MetabolicReaction.class);
        MetabolicReaction r3 = mock(MetabolicReaction.class);

        reactome.add(r1);
        reactome.add(r2);
        reactome.add(r3);

        verify(reconstruction).register(r1);
        verify(reconstruction).register(r2);
        verify(reconstruction).register(r3);

        assertThat(reactome.size(), is(3));

        List<MetabolicReaction> reactions = reactome.toList();

        assertThat(reactions.size(), is(3));
        reactome.remove(r2);
        assertThat(reactions.size(), is(3));
    }
}
