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
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.name.PreferredNameAccess;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** @author John May */
public class NameAnnotatorTest {

    @Test
    public void testApply() throws Exception {
        Metabolite m = mock(Metabolite.class);
        PreferredNameAccess<Identifier> access = mock(PreferredNameAccess.class);
        Identifier id = mock(Identifier.class);
        when(id.getAccession()).thenReturn("my-id");
        when(m.getIdentifier()).thenReturn(id);
        when(access.getPreferredName(id)).thenReturn("named compound");
        new NameAnnotator(access).apply(m);
        verify(m).setName("named compound");
    }
}
