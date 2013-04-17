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

package uk.ac.ebi.mdk.prototype.hash.seed;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

import java.util.BitSet;

/**
 * A simple adapter that wraps a simple atom seed and ignores the provided mask
 *
 * @author John May
 */
public class MaskedSeedAdapter implements MaskedSeed {

    private final AtomSeed seed;

    public MaskedSeedAdapter(AtomSeed seed) {
        this.seed = seed;
    }

    @Override
    public int seed(IAtomContainer molecule, IAtom atom, BitSet mask) {
        return seed.seed(molecule, atom);
    }

    @Override
    public String toString() {
        return "Mask(" + seed.toString() + ")";
    }
}
