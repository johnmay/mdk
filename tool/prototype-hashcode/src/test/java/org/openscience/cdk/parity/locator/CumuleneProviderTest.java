/*
 * Copyright (c) 2012. John May <jwmay@users.sf.net>
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

package org.openscience.cdk.parity.locator;

import org.junit.Test;
import org.openscience.cdk.hash.graph.AdjacencyList;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.silent.SilentChemObjectBuilder;

import static org.openscience.cdk.interfaces.IBond.Order.DOUBLE;
import static org.openscience.cdk.interfaces.IBond.Order.SINGLE;
import static org.openscience.cdk.interfaces.IBond.Stereo.UP;

/**
 * @author John May
 */
public class CumuleneProviderTest {

    @Test
    public void testAllene() {

        IChemObjectBuilder builder = SilentChemObjectBuilder.getInstance();

        IAtom c1 = builder.newInstance(IAtom.class, "C");
        IAtom c2 = builder.newInstance(IAtom.class, "C");
        IAtom c3 = builder.newInstance(IAtom.class, "C");
        IAtom o4 = builder.newInstance(IAtom.class, "O");
        IAtom o5 = builder.newInstance(IAtom.class, "O");

        IAtomContainer container = builder.newInstance(IAtomContainer.class);
        container.addAtom(c1);
        container.addAtom(c2);
        container.addAtom(c3);
        container.addAtom(o4);
        container.addAtom(o5);

        container.addBond(builder.newInstance(IBond.class, c1, c2, DOUBLE));
        container.addBond(builder.newInstance(IBond.class, c2, c3, DOUBLE));
        container.addBond(builder.newInstance(IBond.class, c1, o4, SINGLE, UP));
        container.addBond(builder.newInstance(IBond.class, c1, o5, SINGLE));

        new CumuleneProvider<Integer>(null)
                .getComponents(new AdjacencyList(container));

    }

    @Test
    public void testCumulene() {

        IChemObjectBuilder builder = SilentChemObjectBuilder.getInstance();

        IAtom c1 = builder.newInstance(IAtom.class, "C");
        IAtom c2 = builder.newInstance(IAtom.class, "C");
        IAtom c3 = builder.newInstance(IAtom.class, "C");
        IAtom c4 = builder.newInstance(IAtom.class, "C");
        IAtom o5 = builder.newInstance(IAtom.class, "O");
        IAtom o6 = builder.newInstance(IAtom.class, "O");

        IAtomContainer container = builder.newInstance(IAtomContainer.class);
        container.addAtom(c1);
        container.addAtom(c2);
        container.addAtom(c3);
        container.addAtom(c4);
        container.addAtom(o5);
        container.addAtom(o6);

        container.addBond(builder.newInstance(IBond.class, c1, c2, DOUBLE));
        container.addBond(builder.newInstance(IBond.class, c2, c3, DOUBLE));
        container.addBond(builder.newInstance(IBond.class, c1, c4, DOUBLE));
        container.addBond(builder.newInstance(IBond.class, c4, o5, SINGLE, UP));
        container.addBond(builder.newInstance(IBond.class, c3, o6, SINGLE));

        new CumuleneProvider<Integer>(null)
                .getComponents(new AdjacencyList(container));

    }

    @Test
    public void test4Cumulene() {

        IChemObjectBuilder builder = SilentChemObjectBuilder.getInstance();

        IAtom c1 = builder.newInstance(IAtom.class, "C");
        IAtom c2 = builder.newInstance(IAtom.class, "C");
        IAtom c3 = builder.newInstance(IAtom.class, "C");
        IAtom c4 = builder.newInstance(IAtom.class, "C");
        IAtom c5 = builder.newInstance(IAtom.class, "C");
        IAtom o6 = builder.newInstance(IAtom.class, "O");
        IAtom o7 = builder.newInstance(IAtom.class, "O");

        IAtomContainer container = builder.newInstance(IAtomContainer.class);
        container.addAtom(c1);
        container.addAtom(c2);
        container.addAtom(c3);
        container.addAtom(c4);
        container.addAtom(c5);
        container.addAtom(o6);
        container.addAtom(o7);

        container.addBond(builder.newInstance(IBond.class, c1, c2, DOUBLE));
        container.addBond(builder.newInstance(IBond.class, c2, c3, DOUBLE));
        container.addBond(builder.newInstance(IBond.class, c3, c4, DOUBLE));
        container.addBond(builder.newInstance(IBond.class, c4, c5, DOUBLE));
        container.addBond(builder.newInstance(IBond.class, c5, o6, SINGLE));
        container.addBond(builder.newInstance(IBond.class, c1, o7, SINGLE, UP));

        new CumuleneProvider<Integer>(null)
                .getComponents(new AdjacencyList(container));

    }

    /**
     * <pre>
     * c1
     *   \
     *    n3 = n4 = n5
     *
     *
     * </pre>
     */
    @Test
    public void testNonCumulene() {

        IChemObjectBuilder builder = SilentChemObjectBuilder.getInstance();

        IAtom c1 = builder.newInstance(IAtom.class, "C");
        IAtom n3 = builder.newInstance(IAtom.class, "N");
        IAtom n4 = builder.newInstance(IAtom.class, "N");
        IAtom n5 = builder.newInstance(IAtom.class, "N");

        IAtomContainer container = builder.newInstance(IAtomContainer.class);
        container.addAtom(c1);
        container.addAtom(n3);
        container.addAtom(n4);
        container.addAtom(n5);

        container.addBond(builder.newInstance(IBond.class, c1, n3, SINGLE, UP));
        container.addBond(builder.newInstance(IBond.class, n3, n4, DOUBLE));
        container.addBond(builder.newInstance(IBond.class, n4, n5, DOUBLE));

        new CumuleneProvider<Integer>(null)
                .getComponents(new AdjacencyList(container));

    }



}
