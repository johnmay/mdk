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
package uk.ac.ebi.mdk.prototype.hash.seed;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * An {@link AtomSeed} that uses the formal charge of the atom as a seeding
 * value. This seed expects {IAtom#getFormalCharge()} to return a non-null
 * value. If this guarantee cannot be met then the safer {@link ChargeSeed} can
 * be used.
 *
 * @author johnmay
 * @see ChargeSeed
 * @see org.openscience.cdk.interfaces.IAtom#getFormalCharge()
 */
public class NonNullChargeSeed implements AtomSeed {

    protected NonNullChargeSeed() {
    }

    /**
     * Utilise the formal charge of an atom as a seed for a hashing function.
     *
     * @param container a container that holds the provided atom
     * @param atom      an atom with a non-null formal charge
     * @return the seed value
     */
    public int seed(IAtomContainer container, IAtom atom) {
        return atom.getFormalCharge().hashCode();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return "Formal Charge (non-null)";
    }

}
