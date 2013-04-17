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

package uk.ac.ebi.chemet.tools.annotation;

import org.junit.Test;
import org.mockito.Matchers;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.IdentifierFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author John May
 */
public class IdentifierMapperTest {

    @Test
    public void inferred() throws Exception {
        List<? extends AnnotatedEntity> es = Arrays
                .asList(mock(AnnotatedEntity.class),
                        mock(AnnotatedEntity.class),
                        mock(AnnotatedEntity.class));

        IdentifierMapper.KeyAccessor<String> accessor = mock(IdentifierMapper.KeyAccessor.class);
        IdentifierFactory factory = mock(IdentifierFactory.class);

        when(accessor.key(Matchers.<AnnotatedEntity>anyObject()))
                .thenReturn("a", "b", "c");

        IdentifierMapper.Handler handler = mock(IdentifierMapper.Handler.class);
        when(handler.handle(any(AnnotatedEntity.class),
                            any(Identifier.class))).thenReturn(true);

        IdentifierMapper<String> mapper = new IdentifierMapper<String>(es,
                                                                       accessor,
                                                                       handler,
                                                                       factory);
        Identifier id1    = mock(Identifier.class);
        when(id1.isValid()).thenReturn(true);

        Collection id1s   = mock(Collection.class);
        Iterator   id1sIt = mock(Iterator.class);

        when(id1s.size()).thenReturn(1);
        when(id1s.iterator()).thenReturn(id1sIt);
        when(id1sIt.next()).thenReturn(Identifier.class);

        when(factory.ofPattern("id1")).thenReturn(id1s);
        when(factory.ofClass(Identifier.class)).thenReturn(id1);

        assertTrue(mapper.map("b", "id1"));
        verify(handler).handle(es.get(1), id1);
    }

    @Test
    public void fromName() throws Exception {
        List<? extends AnnotatedEntity> es = Arrays
                .asList(mock(AnnotatedEntity.class),
                        mock(AnnotatedEntity.class),
                        mock(AnnotatedEntity.class));

        IdentifierMapper.KeyAccessor<String> accessor = mock(IdentifierMapper.KeyAccessor.class);
        IdentifierFactory factory = mock(IdentifierFactory.class);

        when(accessor.key(Matchers.<AnnotatedEntity>anyObject()))
                .thenReturn("a", "b", "c");

        IdentifierMapper.Handler handler = mock(IdentifierMapper.Handler.class);
        when(handler.handle(any(AnnotatedEntity.class),
                            any(Identifier.class))).thenReturn(true);

        IdentifierMapper<String> mapper = new IdentifierMapper<String>(es,
                                                                       accessor,
                                                                       handler,
                                                                       factory);

        Identifier id1 = mock(Identifier.class);
        when(id1.isValid()).thenReturn(true);
        Identifier id2 = mock(Identifier.class);
        when(id2.isValid()).thenReturn(true);

        when(factory.ofName("mock")).thenReturn(id1, id2);

        assertTrue(mapper.map("a", "id1", "mock"));
        verify(handler).handle(es.get(0), id1);
        assertTrue(mapper.map("c", "id2", "mock"));
        verify(handler).handle(es.get(2), id2);
    }


    @Test
    public void multipleMappings() throws Exception {
        List<? extends AnnotatedEntity> es = Arrays
                .asList(mock(AnnotatedEntity.class),
                        mock(AnnotatedEntity.class),
                        mock(AnnotatedEntity.class));

        IdentifierMapper.KeyAccessor<String> accessor = mock(IdentifierMapper.KeyAccessor.class);

        when(accessor.key(Matchers.<AnnotatedEntity>anyObject()))
                .thenReturn("a", "b", "a", "c");

        IdentifierMapper.Handler handler = mock(IdentifierMapper.Handler.class);
        when(handler.handle(any(AnnotatedEntity.class),
                            any(Identifier.class))).thenReturn(true);

        IdentifierMapper<String> mapper = new IdentifierMapper<String>(es,
                                                                       accessor,
                                                                       handler,
                                                                       mock(IdentifierFactory.class));


        assertTrue(mapper.map("a", mock(Identifier.class)));

        verify(handler, times(2))
                .handle(any(AnnotatedEntity.class), any(Identifier.class));
    }

    @Test
    public void directIdMapping() throws Exception {
        List<? extends AnnotatedEntity> es = Arrays
                .asList(mock(AnnotatedEntity.class),
                        mock(AnnotatedEntity.class),
                        mock(AnnotatedEntity.class));

        IdentifierMapper.KeyAccessor<String> accessor = mock(IdentifierMapper.KeyAccessor.class);

        when(accessor.key(Matchers.<AnnotatedEntity>anyObject()))
                .thenReturn("a", "b", "c");

        IdentifierMapper.Handler handler = mock(IdentifierMapper.Handler.class);
        when(handler.handle(any(AnnotatedEntity.class),
                            any(Identifier.class))).thenReturn(true);

        IdentifierMapper<String> mapper = new IdentifierMapper<String>(es,
                                                                       accessor,
                                                                       handler,
                                                                       mock(IdentifierFactory.class));


        assertTrue(mapper.map("a", mock(Identifier.class)));
        assertTrue(mapper.map("b", mock(Identifier.class)));

        verify(handler, times(2))
                .handle(any(AnnotatedEntity.class), any(Identifier.class));
    }

    @Test
    public void testUnmapped() throws Exception {

        Collection<? extends AnnotatedEntity> es = Arrays
                .asList(mock(AnnotatedEntity.class),
                        mock(AnnotatedEntity.class),
                        mock(AnnotatedEntity.class));

        IdentifierMapper.KeyAccessor<String> accessor = mock(IdentifierMapper.KeyAccessor.class);

        when(accessor.key(Matchers.<AnnotatedEntity>anyObject()))
                .thenReturn("a", "b", "c");

        IdentifierMapper<String> mapper = new IdentifierMapper<String>(es,
                                                                       accessor);

        assertTrue(mapper.map("a", mock(Identifier.class)));
        assertTrue(mapper.map("b", mock(Identifier.class)));
        assertTrue(mapper.map("c", mock(Identifier.class)));

        assertFalse(mapper.map("d", mock(Identifier.class)));
        assertFalse(mapper.map("e", mock(Identifier.class)));

        assertThat(mapper.unmapped(), hasItems("d", "e"));
        assertThat(mapper.unmapped(), not(hasItems("a", "b", "c")));
    }

    @Test
    public void testInvalid() throws Exception {
        Collection<? extends AnnotatedEntity> es = Arrays
                .asList(mock(AnnotatedEntity.class),
                        mock(AnnotatedEntity.class),
                        mock(AnnotatedEntity.class));

        IdentifierMapper.KeyAccessor<String> accessor = mock(IdentifierMapper.KeyAccessor.class);

        Identifier idMock = mock(Identifier.class);
        IdentifierFactory idFactoryMock = mock(IdentifierFactory.class);

        when(idMock.isValid()).thenReturn(false);
        when(idFactoryMock.ofName("mock")).thenReturn(idMock);

        when(accessor.key(Matchers.<AnnotatedEntity>anyObject()))
                .thenReturn("a", "b", "c");

        IdentifierMapper<String> mapper = new IdentifierMapper<String>(es,
                                                                       accessor,
                                                                       mock(IdentifierMapper.Handler.class),
                                                                       idFactoryMock);

        assertFalse(mapper.map("a", "mock accession", "mock"));

        assertThat(mapper.invalid(), not(hasItems("a")));
    }

    @Test
    public void testUnknown() throws Exception {

        Identifier idMock = mock(Identifier.class);
        IdentifierFactory idFactoryMock = mock(IdentifierFactory.class);

        when(idMock.isValid()).thenReturn(false);
        when(idFactoryMock.ofName("mock"))
                .thenReturn(IdentifierFactory.EMPTY_IDENTIFIER);

        IdentifierMapper<String> mapper = new IdentifierMapper<String>(Collections
                                                                               .<AnnotatedEntity>emptyList(),
                                                                       mock(IdentifierMapper.KeyAccessor.class),
                                                                       mock(IdentifierMapper.Handler.class),
                                                                       idFactoryMock);

        assertFalse(mapper.map("b", "mock"));

        assertThat(mapper.unknown(), not(hasItems("b")));
    }

    @Test
    public void testAmbiguous() throws Exception {

        Identifier idMock = mock(Identifier.class);
        IdentifierFactory idFactoryMock = mock(IdentifierFactory.class);

        Collection matchingIds = mock(Collection.class);
        when(matchingIds.size()).thenReturn(2);

        when(idMock.isValid()).thenReturn(false);
        when(idFactoryMock.ofPattern("mock")).thenReturn(matchingIds);

        IdentifierMapper<String> mapper = new IdentifierMapper<String>(Collections
                                                                               .<AnnotatedEntity>emptyList(),
                                                                       mock(IdentifierMapper.KeyAccessor.class),
                                                                       mock(IdentifierMapper.Handler.class),
                                                                       idFactoryMock);

        assertFalse(mapper.map("b", "mock"));

        assertThat(mapper.ambiguous(), not(hasItems("b")));
    }
}
