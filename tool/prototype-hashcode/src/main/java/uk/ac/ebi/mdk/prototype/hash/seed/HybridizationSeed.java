/*
 * Copyright (c) 2012. John May <jwmay@sf.net>
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

import static org.openscience.cdk.interfaces.IAtomType.Hybridization;
import static org.openscience.cdk.interfaces.IAtomType.Hybridization.PLANAR3;

/**
 * An atom seed that hashes the orbital hybridization. This is useful as using
 * the bond order sum can not catch all cases when bonds are de-localised.
 * <p/>
 * Not that as the hash can provide false positives but NOT false negatives some
 * molecules may calculate the same hash even though they are actually
 * different. This can be rectified by increasing the depth of the hash.
 *
 * @author John May
 * @see <a href="http://en.wikipedia.org/wiki/Orbital_hybridisation">Orbital
 *      Hybridisation</a>
 */
public class HybridizationSeed implements AtomSeed {

    public HybridizationSeed() {
    }

    /**
     * Access the ordinal of the hybridization enumeration.
     *
     * @param molecule target molecule
     * @param atom     an atom from the target
     * @return a seed for this atom
     */
    public int seed(IAtomContainer molecule, IAtom atom) {
        return atom.getHybridization() != null
                ? (1 + normalize(atom.getHybridization()).ordinal()) * 646133386 // arbitrary prime
                : 0;
    }

    private Hybridization normalize(Hybridization hybridization) {

        // CDK Hybridization model is too specific for our needs and we need to
        // normalise the values
        if (hybridization == PLANAR3)
            return Hybridization.SP2;

        return hybridization;

    }

    @Override
    public String toString() {
        return "Hybridization (nullable)";
    }
}
