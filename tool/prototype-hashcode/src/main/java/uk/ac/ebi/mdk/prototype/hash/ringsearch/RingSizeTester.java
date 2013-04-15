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

package uk.ac.ebi.mdk.prototype.hash.ringsearch;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

import java.util.Arrays;

/**
 * Test where atoms are in a ring of a given size or smaller.
 * This all testing as to whether for example an atom is ridged (smaller then
 * a ring of size 8).
 *
 * @author John May
 */
public class RingSizeTester implements RingTester {

    private final RingTester tester;
    private final IAtomContainer container;

    public RingSizeTester(IAtomContainer container, int ringSize){

        int n = container.getAtomCount();

        this.container = container;

        if(container.getAtomCount() < 64)
            tester = new RegularRingSizeTester(create(container), ringSize);
        else
            tester = new JumboRingSizeTester(create(container), ringSize);

    }

    @Override
    public boolean isInRing(int i) {
        return tester.isInRing(i);
    }


    public int[][] create(IAtomContainer container) {

        int n = container.getAtomCount();
        int[][] graph   = new int[n][12];
        int[] connected = new int[n];

        // ct table only
        for (IBond bond : container.bonds()) {
            int a1 = container.getAtomNumber(bond.getAtom(0));
            int a2 = container.getAtomNumber(bond.getAtom(1));
            graph[a1][connected[a1]++] = a2;
            graph[a2][connected[a2]++] = a1;
        }

        // trim the neighbours
        for (int i = 0; i < n; i++) {
            graph[i] = Arrays.copyOf(graph[i], connected[i]);
        }

        return graph;
    }

}
