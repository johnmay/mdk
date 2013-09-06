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

package org.openscience.cdk.hash_mdk;

import org.openscience.cdk.interfaces.IAtomContainer;

import java.util.Arrays;

/**
 * A basic molecule hash combines the individual atom hashes into a single
 * value.
 *
 * @author John May
 */
public class BasicMoleculeHash extends AbstractHashGenerator
        implements MoleculeHashGenerator {

    /* prime number to start our combined hash from */
    private static final long INITIAL_HASH_VALUE = 49157L;

    /* a generator for our individual atom hashes */
    private AtomHashGenerator atomGenerator;

    public BasicMoleculeHash(AtomHashGenerator atomGenerator) {
        this.atomGenerator = atomGenerator;
    }

    @Override
    public long generate(IAtomContainer container) {

        long[] hashes = atomGenerator.generate(container);
        long hash = INITIAL_HASH_VALUE;

        int n = hashes.length;

        if (n == 0) return hash;

        Arrays.sort(hashes);
        long[] rotated = Arrays.copyOf(hashes, n);

        hash ^= (rotated[0] = hashes[0]);
        for (int i = 1; i < n; i++) {
            hash ^= (rotated[i] =
                    (hashes[i] == hashes[i - 1]) ? generate(rotated[i - 1])
                                                 : hashes[i]);
        }

        return hash;

    }
}
