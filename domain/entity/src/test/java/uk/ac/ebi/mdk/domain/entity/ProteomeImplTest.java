package uk.ac.ebi.mdk.domain.entity;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import uk.ac.ebi.mdk.domain.entity.collection.Proteome;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author John May
 */
public class ProteomeImplTest {

    @Test
    public void testAdd() throws Exception {
        Reconstruction reconstruction = mock(Reconstruction.class);
        when(reconstruction.register(any(Entity.class))).thenReturn(true);
        Proteome proteome = new ProteomeImpl(reconstruction);

        GeneProduct p1 = mock(GeneProduct.class);
        GeneProduct p2 = mock(GeneProduct.class);

        proteome.add(p1);
        proteome.add(p2);

        verify(reconstruction).register(p1);
        verify(reconstruction).register(p2);
        assertThat(proteome.size(), is(2));
    }

    @Test
    public void testAdd_registerFalse() throws Exception {
        Reconstruction reconstruction = mock(Reconstruction.class);
        when(reconstruction.register(any(Entity.class)))
                .thenReturn(true, false);
        Proteome proteome = new ProteomeImpl(reconstruction);

        GeneProduct p1 = mock(GeneProduct.class);
        GeneProduct p2 = mock(GeneProduct.class);

        proteome.add(p1);
        proteome.add(p2);

        verify(reconstruction).register(p1);

        assertThat(proteome.size(), is(1));
    }

    @Test
    public void testRemove() throws Exception {
        Reconstruction reconstruction = mock(Reconstruction.class);
        when(reconstruction.register(any(Entity.class))).thenReturn(true);
        Proteome proteome = new ProteomeImpl(reconstruction);

        GeneProduct p1 = mock(GeneProduct.class);
        GeneProduct p2 = mock(GeneProduct.class);

        proteome.add(p1);
        proteome.add(p2);

        verify(reconstruction).register(p1);
        verify(reconstruction).register(p2);
        assertThat(proteome.size(), is(2));

        // even when false the gene product should be removed
        when(reconstruction.register(any(Entity.class))).thenReturn(false);
        assertTrue(proteome.remove(p1));
        assertThat(proteome.size(), is(1));
    }

    private static UUID uuid(String str){
        return UUID.nameUUIDFromBytes(str.getBytes());
    }
}
