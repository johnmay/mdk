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
import org.openscience.cdk.interfaces.ISingleElectron;

/**
 * A atomic hash seed that indicates whether the atom is a free radical.
 * Currently there is no distinction between monovalent, divalent and trivalent
 * radicals. Due to this the seed is boolean and will return a true/false
 * indication as to whether there exists an unpaired electron on the provided
 * atom.
 *
 * @author johnmay
 * @see IAtomContainer#getSingleElectron(int)
 */
@Deprecated
public class BooleanRadicalSeed implements AtomSeed {

    /*
     * default value when a null charge is accessed
     */
    private static final int NULL_VALUE = 13367;

    /*
     * value to use when the atom is a radical
     */
    private static final int RADICAL = 413158511;

    public BooleanRadicalSeed() {
    }

    /**
     * Utilise the formal charge of an atom as a seed for a hashing function.
     *
     * @param container a container that holds the provided atom
     * @param atom      an atom to seed
     * @return the seed value
     */
    public int seed(IAtomContainer container, IAtom atom) {

        for (ISingleElectron unpaired : container.singleElectrons()) {

            if (unpaired.getAtom().equals(atom)) {
                return RADICAL;
            }
        }

        return NULL_VALUE;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return "Radical (boolean)";
    }

}
