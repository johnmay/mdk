/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

package uk.ac.ebi.mdk.tool.domain;

import com.google.common.base.Predicate;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author John May
 */
public class MissingStructureParticipantTest {

    private final Predicate<MetabolicReaction> predicate = new MissingStructureParticipant();

    @Test
    public void testApply_null() throws Exception {

        MetabolicReaction rxn = mock(MetabolicReaction.class);

        MetabolicParticipant p1 = mock(MetabolicParticipant.class);
        MetabolicParticipant p2 = mock(MetabolicParticipant.class);
        MetabolicParticipant p3 = mock(MetabolicParticipant.class);
        MetabolicParticipant p4 = mock(MetabolicParticipant.class);

        when(rxn.getParticipants()).thenReturn(Arrays.asList(p1, p2, p3, p4));

        assertTrue(predicate.apply(rxn));
    }

    @Test
    public void testApply() throws Exception {

        MetabolicReaction rxn = mock(MetabolicReaction.class);

        MetabolicParticipant p1 = mock(MetabolicParticipant.class);
        MetabolicParticipant p2 = mock(MetabolicParticipant.class);
        MetabolicParticipant p3 = mock(MetabolicParticipant.class);
        MetabolicParticipant p4 = mock(MetabolicParticipant.class);

        Metabolite m1 = mock(Metabolite.class);
        Metabolite m2 = mock(Metabolite.class);
        Metabolite m3 = mock(Metabolite.class);
        Metabolite m4 = mock(Metabolite.class);

        when(m1.hasStructure()).thenReturn(true);
        when(m2.hasStructure()).thenReturn(true);
        when(m3.hasStructure()).thenReturn(true);
        when(m4.hasStructure()).thenReturn(true);

        when(p1.getMolecule()).thenReturn(m1);
        when(p2.getMolecule()).thenReturn(m2);
        when(p3.getMolecule()).thenReturn(m3);
        when(p4.getMolecule()).thenReturn(m4);

        when(rxn.getParticipants()).thenReturn(Arrays.asList(p1, p2, p3, p4));

        assertFalse(predicate.apply(rxn));
    }

    @Test
    public void testApply_OneMissing() throws Exception {

        MetabolicReaction rxn = mock(MetabolicReaction.class);

        MetabolicParticipant p1 = mock(MetabolicParticipant.class);
        MetabolicParticipant p2 = mock(MetabolicParticipant.class);
        MetabolicParticipant p3 = mock(MetabolicParticipant.class);
        MetabolicParticipant p4 = mock(MetabolicParticipant.class);

        Metabolite m1 = mock(Metabolite.class);
        Metabolite m2 = mock(Metabolite.class);
        Metabolite m3 = mock(Metabolite.class);
        Metabolite m4 = mock(Metabolite.class);

        when(m1.hasStructure()).thenReturn(true);
        when(m2.hasStructure()).thenReturn(false);
        when(m3.hasStructure()).thenReturn(true);
        when(m4.hasStructure()).thenReturn(true);

        when(p1.getMolecule()).thenReturn(m1);
        when(p2.getMolecule()).thenReturn(m2);
        when(p3.getMolecule()).thenReturn(m3);
        when(p4.getMolecule()).thenReturn(m4);

        when(rxn.getParticipants()).thenReturn(Arrays.asList(p1, p2, p3, p4));

        assertTrue(predicate.apply(rxn));
    }

    @Test
    public void testApply_AllMissing() throws Exception {

        MetabolicReaction rxn = mock(MetabolicReaction.class);

        MetabolicParticipant p1 = mock(MetabolicParticipant.class);
        MetabolicParticipant p2 = mock(MetabolicParticipant.class);
        MetabolicParticipant p3 = mock(MetabolicParticipant.class);
        MetabolicParticipant p4 = mock(MetabolicParticipant.class);

        Metabolite m1 = mock(Metabolite.class);
        Metabolite m2 = mock(Metabolite.class);
        Metabolite m3 = mock(Metabolite.class);
        Metabolite m4 = mock(Metabolite.class);

        when(m1.hasStructure()).thenReturn(false);
        when(m2.hasStructure()).thenReturn(false);
        when(m3.hasStructure()).thenReturn(false);
        when(m4.hasStructure()).thenReturn(false);

        when(p1.getMolecule()).thenReturn(m1);
        when(p2.getMolecule()).thenReturn(m2);
        when(p3.getMolecule()).thenReturn(m3);
        when(p4.getMolecule()).thenReturn(m4);

        when(rxn.getParticipants()).thenReturn(Arrays.asList(p1, p2, p3, p4));

        assertTrue(predicate.apply(rxn));
    }
}
