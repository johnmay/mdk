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

import com.google.common.base.Function;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.reaction.Compartment;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.compartment.Organelle;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.service.query.ParticipantHandler;
import uk.ac.ebi.mdk.tool.CompartmentResolver;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/** @author John May */
public class MetabolicParticipantHandlerTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testHandle_Compartment() throws Exception {
        EntityFactory factory = mock(EntityFactory.class);
        CompartmentResolver resolver = mock(CompartmentResolver.class);
        Function<Metabolite, Metabolite> annotator = mock(Function.class);

        Metabolite mock = mock(Metabolite.class);

        when(resolver.getCompartment("c")).thenReturn(Organelle.CYTOPLASM);
        when(factory.metabolite()).thenReturn(mock);

        ParticipantHandler<MetabolicParticipant> handler = new MetabolicParticipantHandler(factory,
                                                                                           new KEGGCompoundIdentifier(),
                                                                                           resolver,
                                                                                           annotator,
                                                                                           MoleculeCache.<Metabolite>empty());
        MetabolicParticipant m = handler.handle("C00001", "c", 1.0);
        verify(resolver).getCompartment("c");
        assertThat(m.getCompartment(), is((Compartment) Organelle.CYTOPLASM));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testHandle_Annotator() throws Exception {
        EntityFactory factory = mock(EntityFactory.class);
        CompartmentResolver resolver = mock(CompartmentResolver.class);
        Function<Metabolite, Metabolite> annotator = mock(Function.class);

        Metabolite mock = mock(Metabolite.class);

        when(factory.metabolite()).thenReturn(mock);

        ParticipantHandler<MetabolicParticipant> handler = new MetabolicParticipantHandler(factory,
                                                                                           new KEGGCompoundIdentifier(),
                                                                                           resolver,
                                                                                           annotator,
                                                                                           MoleculeCache.<Metabolite>empty());
        MetabolicParticipant m = handler.handle("C00001", "c", 1.0);
        verify(annotator).apply(mock);
    }
}
