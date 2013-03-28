package uk.ac.ebi.mdk.domain.entity;

import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author John May
 */
public class AssociationMapTest {

    private GeneProduct product(String id) {
        GeneProduct p = mock(GeneProduct.class);
        when(p.uuid()).thenReturn(UUID.nameUUIDFromBytes(id.getBytes()));
        return p;
    }

    private Reaction reaction(String id) {
        Reaction r = mock(Reaction.class);
        when(r.uuid()).thenReturn(UUID.nameUUIDFromBytes(id.getBytes()));
        return r;
    }

    @Test
    public void testAssociate() throws Exception {
        AssociationMap pToR = AssociationMap.create(10);

        GeneProduct p1 = product("p1");
        GeneProduct p2 = product("p2");
        GeneProduct p3 = product("p3");

        Reaction r1 = reaction("r1");
        Reaction r2 = reaction("r2");
        Reaction r3 = reaction("r3");

        pToR.associate(p1, r1);
        pToR.associate(p1, r2);
        pToR.associate(p2, r3);
        pToR.associate(p3, r1);

        assertThat(pToR.associations(p1).size(), is(2));
        assertThat(pToR.associations(p1), hasItems(r1.uuid(), r2.uuid()));
        assertThat(pToR.associations(p2).size(), is(1));
        assertThat(pToR.associations(p2), hasItems(r3.uuid()));
        assertThat(pToR.associations(p3).size(), is(1));
        assertThat(pToR.associations(p3), hasItems(r1.uuid()));

        assertThat(pToR.associations(r1).size(), is(2));
        assertThat(pToR.associations(r1), hasItems(p1.uuid(), p3.uuid()));
        assertThat(pToR.associations(r2).size(), is(1));
        assertThat(pToR.associations(r2), hasItems(p1.uuid()));
        assertThat(pToR.associations(r3).size(), is(1));
        assertThat(pToR.associations(r3), hasItems(p2.uuid()));
    }

    @Test
    public void testDissociate() throws Exception {
        AssociationMap pToR = AssociationMap.create(10);

        GeneProduct p1 = product("p1");
        GeneProduct p2 = product("p2");
        GeneProduct p3 = product("p3");

        Reaction r1 = reaction("r1");
        Reaction r2 = reaction("r2");
        Reaction r3 = reaction("r3");

        pToR.associate(p1, r1);
        pToR.associate(p1, r2);
        pToR.associate(p2, r3);
        pToR.associate(p3, r1);

        assertThat(pToR.associations(p1).size(), is(2));
        assertThat(pToR.associations(p1), hasItems(r1.uuid(), r2.uuid()));
        assertThat(pToR.associations(p2).size(), is(1));
        assertThat(pToR.associations(p2), hasItems(r3.uuid()));
        assertThat(pToR.associations(p3).size(), is(1));
        assertThat(pToR.associations(p3), hasItems(r1.uuid()));

        assertThat(pToR.associations(r1).size(), is(2));
        assertThat(pToR.associations(r1), hasItems(p1.uuid(), p3.uuid()));
        assertThat(pToR.associations(r2).size(), is(1));
        assertThat(pToR.associations(r2), hasItems(p1.uuid()));
        assertThat(pToR.associations(r3).size(), is(1));
        assertThat(pToR.associations(r3), hasItems(p2.uuid()));

        pToR.dissociate(p1, r1);

        assertThat(pToR.associations(p1).size(), is(1));
        assertThat(pToR.associations(p1), hasItem(r2.uuid()));
        assertThat(pToR.associations(p2).size(), is(1));
        assertThat(pToR.associations(p2), hasItems(r3.uuid()));
        assertThat(pToR.associations(p3).size(), is(1));
        assertThat(pToR.associations(p3), hasItems(r1.uuid()));

        assertThat(pToR.associations(r1).size(), is(1));
        assertThat(pToR.associations(r1), hasItems(p3.uuid()));
        assertThat(pToR.associations(r2).size(), is(1));
        assertThat(pToR.associations(r2), hasItem(p1.uuid()));
        assertThat(pToR.associations(r3).size(), is(1));
        assertThat(pToR.associations(r3), hasItems(p2.uuid()));
    }

    @Test
    public void testClear() throws Exception {
        AssociationMap pToR = AssociationMap.create(10);

        GeneProduct p1 = product("p1");
        GeneProduct p2 = product("p2");
        GeneProduct p3 = product("p3");

        Reaction r1 = reaction("r1");
        Reaction r2 = reaction("r2");
        Reaction r3 = reaction("r3");

        pToR.associate(p1, r1);
        pToR.associate(p1, r2);
        pToR.associate(p2, r3);
        pToR.associate(p3, r1);

        assertThat(pToR.associations(p1).size(), is(2));
        assertThat(pToR.associations(p1), hasItems(r1.uuid(), r2.uuid()));
        assertThat(pToR.associations(p2).size(), is(1));
        assertThat(pToR.associations(p2), hasItems(r3.uuid()));
        assertThat(pToR.associations(p3).size(), is(1));
        assertThat(pToR.associations(p3), hasItems(r1.uuid()));

        assertThat(pToR.associations(r1).size(), is(2));
        assertThat(pToR.associations(r1), hasItems(p1.uuid(), p3.uuid()));
        assertThat(pToR.associations(r2).size(), is(1));
        assertThat(pToR.associations(r2), hasItems(p1.uuid()));
        assertThat(pToR.associations(r3).size(), is(1));
        assertThat(pToR.associations(r3), hasItems(p2.uuid()));

        // remove p1 -> r1 and p1 -> r2
        pToR.clear(p1);

        assertThat(pToR.associations(p1).size(), is(0));
        assertThat(pToR.associations(p2).size(), is(1));
        assertThat(pToR.associations(p2), hasItems(r3.uuid()));
        assertThat(pToR.associations(p3).size(), is(1));
        assertThat(pToR.associations(p3), hasItems(r1.uuid()));

        assertThat(pToR.associations(r1).size(), is(1));
        assertThat(pToR.associations(r1), hasItems(p3.uuid()));
        assertThat(pToR.associations(r2).size(), is(0));
        assertThat(pToR.associations(r3).size(), is(1));
        assertThat(pToR.associations(r3), hasItems(p2.uuid()));
    }
}
