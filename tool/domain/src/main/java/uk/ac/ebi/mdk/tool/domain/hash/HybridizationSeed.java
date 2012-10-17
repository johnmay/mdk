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
package uk.ac.ebi.mdk.tool.domain.hash;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;

import static org.openscience.cdk.interfaces.IAtomType.Hybridization;
import static org.openscience.cdk.interfaces.IAtomType.Hybridization.PLANAR3;
import static org.openscience.cdk.interfaces.IAtomType.Hybridization.SP3D1;
import static org.openscience.cdk.interfaces.IAtomType.Hybridization.SP3D2;
import static org.openscience.cdk.interfaces.IAtomType.Hybridization.SP3D3;
import static org.openscience.cdk.interfaces.IAtomType.Hybridization.SP3D4;
import static org.openscience.cdk.interfaces.IAtomType.Hybridization.SP3D5;

/**
 * An atom seed that hashes the orbital hybridization. This is useful as using the bond
 * order sum can not catch all cases when bonds are de-localised.
 *
 * @author John May
 * @see <a href="http://en.wikipedia.org/wiki/Orbital_hybridisation">Orbital Hybridisation</a>
 */
public class HybridizationSeed implements AtomSeed {

    protected HybridizationSeed() {
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
                ? (1 + normalize(atom.getHybridization()).ordinal()) * 14144419 // arbitrary prime
                : 0;
    }

    private Hybridization normalize(Hybridization hybridization){

        // CDK Hybridization model is too specific for our needs and we need to
        // normalise the values
        if(hybridization == PLANAR3)
            return Hybridization.SP2;
        if(hybridization == SP3D1
                || hybridization == SP3D2
                || hybridization == SP3D3
                || hybridization == SP3D4
                || hybridization == SP3D5)
            return Hybridization.SP3;

        return hybridization;

    }

    @Override
    public String toString() {
        return "Hybridization";
    }
}
