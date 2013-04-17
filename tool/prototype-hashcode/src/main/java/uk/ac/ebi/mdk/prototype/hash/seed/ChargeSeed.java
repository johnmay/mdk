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

/**
 * An {@link AtomSeed} that uses the formal charge of the atom as a seeding
 * value. This seed allows {IAtom#getFormalCharge()} to return a null value.
 *
 * @author johnmay
 */
public class ChargeSeed implements AtomSeed {

    /*
     * default value when a null charge is accessed
     */
    private static final int NULL_VALUE = 4427107;

    public ChargeSeed() {
    }

    /**
     * Utilise the formal charge of an atom as a seed for a hashing function.
     *
     * @param container a container that holds the provided atom
     * @param atom      an atom to seed
     * @return the seed value
     */
    public int seed(IAtomContainer container, IAtom atom) {
        return atom.getFormalCharge() != null ? atom.getFormalCharge().hashCode() : NULL_VALUE;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return "Formal Charge";
    }

}
