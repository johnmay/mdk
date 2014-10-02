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

package uk.ac.ebi.mdk.hsql.handler;

import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.structure.StructureService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/** @author John May */
public class StructureAnnotatorTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testApply() throws Exception {
        Metabolite m = mock(Metabolite.class);
        StructureService<Identifier> access = mock(StructureService.class);
        IAtomContainer ac = mock(IAtomContainer.class);
        Identifier id = mock(Identifier.class);
        when(id.getAccession()).thenReturn("my-id");
        when(m.getIdentifier()).thenReturn(id);
        when(access.getStructure(id)).thenReturn(ac);
        new StructureAnnotator(access).apply(m);
        verify(m).addAnnotation(any(AtomContainerAnnotation.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testApply_empty() throws Exception {
        Metabolite m = mock(Metabolite.class);
        StructureService<Identifier> access = mock(StructureService.class);
        IAtomContainer ac = mock(IAtomContainer.class);
        Identifier id = mock(Identifier.class);

        // empty
        when(ac.isEmpty()).thenReturn(true);

        when(id.getAccession()).thenReturn("my-id");
        when(m.getIdentifier()).thenReturn(id);
        when(access.getStructure(id)).thenReturn(ac);
        new StructureAnnotator(access).apply(m);
        verify(m, never()).addAnnotation(any(AtomContainerAnnotation.class));
    }
}
