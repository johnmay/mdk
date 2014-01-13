/*
 * Copyright (c) 2014. EMBL, European Bioinformatics Institute
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

package org.openscience.cdk.isomorphism;

import org.openscience.cdk.graph.GraphUtil;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

/** @author John May */
final class Neutralise {

    private final IAtomContainer          container;
    private final GraphUtil.EdgeToBondMap bonds;
    private final int[][]                 graph;

    Neutralise(IAtomContainer container) {
        this.container = container;
        this.bonds = GraphUtil.EdgeToBondMap.withSpaceFor(container);
        this.graph = GraphUtil.toAdjList(container, bonds);
        correct();
    }

    private void correct() {
        for (int i = 0; i < container.getAtomCount(); i++) {
            int q = charge(container.getAtom(i));
            if (q != 0 && fix(i, q)) {
                
            }
        }
    }

    private boolean fix(int v, int q) {
        
        IAtom a = container.getAtom(v);
        
        int elem = elem(a);
        int h    = container.getAtom(v).getImplicitHydrogenCount();
        
        int deg     = h + graph[v].length;
        int valence = h;
        
        for (int w : graph[v]) {
            IBond bond = bonds.get(v, w);
            IBond.Order order = bond.getOrder();
            if (order == null || order == IBond.Order.UNSET)
                return false;
            valence += order.numeric();
        }
      
        switch (elem) {
            case 6: // carbon
                if (q < 0 && valence - q == 4) {
                    a.setImplicitHydrogenCount(h - q);
                    a.setFormalCharge(0);    
                } else if (q == 1) {
                    a.setImplicitHydrogenCount(h + 1);
                    a.setFormalCharge(0);
                }
                break;
            case 7: // nitrogen
                if (q < 0 ? valence - q == 3 : h >= q) {
                    a.setImplicitHydrogenCount(h - q);
                    a.setFormalCharge(0);
                    return true;
                }
                break;
            case 8: // oxygen
                if (q < 0 ? valence - q == 2 : h >= q) {
                    a.setImplicitHydrogenCount(h - q);
                    a.setFormalCharge(0);
                    return true;
                }
                break;
                
        }
        return false;
    }

    private static int charge(IAtom a) {
        Integer q = a.getFormalCharge();
        if (q != null)
            return q;
        return 0;
    }

    private static int elem(IAtom a) {
        Integer elem = a.getAtomicNumber();
        if (elem != null)
            return elem;
        throw new IllegalArgumentException("atom did not have an atom number");
    }

    private static int implh(IAtom a) {
        Integer h = a.getImplicitHydrogenCount();
        if (h != null)
            return h;
        return 0;
    }

}
