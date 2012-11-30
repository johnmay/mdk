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

package org.openscience.cdk.hash;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.prototype.hash.seed.AtomSeed;

import java.util.ArrayList;
import java.util.List;

/**
 * An atom hash generator which provides the intial hashes for each atom in the
 * provided container.
 *
 * @author John May
 */
public final class AtomSeedGenerator
        extends AbstractHashGenerator
        implements AtomHashGenerator {

    private final List<AtomSeed> methods = new ArrayList<AtomSeed>(10);

    public AtomSeedGenerator(List<AtomSeed> methods) {
        this.methods.addAll(methods);
    }


    @Override
    public Long[] generate(IAtomContainer container) {

        int n = container.getAtomCount();
        Long[] seeds = new Long[n];

        long initial = n > 1 ? 389 % n : 1;

        for (int i = 0; i < n; i++) {

            long hash = initial;

            // including seeding parameters from the various methods
            for (AtomSeed method : methods) {
                hash = 31 * hash + method.seed(container, container.getAtom(i));
            }

            seeds[i] = rotater.rotate(hash, lsb(hash));
        }

        return seeds;

    }


    private long seed(IAtomContainer container, IAtom atom, long hash) {

        // including seeding parameters from the various methods
        for (AtomSeed method : methods) {
            hash = 31 * hash + method.seed(container, atom);
        }

        // distribute the bits
        return rotater.rotate(hash, lsb(hash));

    }


}
