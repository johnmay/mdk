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

package org.openscience.cdk.hash.stereo;

import com.google.common.collect.Maps;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

import java.util.Map;

/**
 * @author John May
 */
public final class BondIndex {

    private final Map<Tuple, IBond> map;

    public BondIndex(IAtomContainer ac, Map<IAtom, Integer> atom2idx) {
        map = Maps.newHashMapWithExpectedSize(ac.getAtomCount());
        for (IBond bnd : ac.bonds()) {
            IAtom a = bnd.getAtom(0);
            IAtom b = bnd.getAtom(1);
            int u = atom2idx.get(a);
            int v = atom2idx.get(b);
            map.put(new Tuple(u, v), bnd);
        }         
    }
    
    IBond get(int u, int v) {
        return map.get(new Tuple(u, v));
    }
    
    IBond[] getAll(int u, int[] vs) {
        IBond[] bonds = new IBond[vs.length];
        for (int i = 0; i <vs.length; i++)
            bonds[i] = get(u, vs[i]);
        return bonds;
    }

    private static final class Tuple {
        private final int u, v;

        /**
         * Create a new tuple with the specified values.
         * @param u a value
         * @param v another value
         */
        private Tuple(int u, int v) {
            this.u = u;
            this.v = v;
        }

        /**
         * @inheritDoc
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Tuple that = (Tuple) o;

            return this.u == that.u && this.v == that.v ||
                    this.u == that.v && this.v == that.u;
        }

        /**
         * @inheritDoc
         */
        @Override
        public int hashCode() {
            return u ^ v;
        }
    }
}
