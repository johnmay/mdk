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
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 * Atomic number seed uses the bond order sum to provide a seed
 * for the an atom. The bond order sum is calculated using
 * {@see AtomContainerManipulator#getBondOrderSum(IAtomContainer, IAtom)}).
 * The seed is used in the MolecularHashFactory when creating a hash for a
 * molecule.
 *
 * Exclude explicit hydrogens
 *
 * @author johnmay
 * @see org.openscience.cdk.tools.manipulator.AtomContainerManipulator#getBondOrderSum(org.openscience.cdk.interfaces.IAtomContainer, org.openscience.cdk.interfaces.IAtom)
 * @see uk.ac.ebi.mdk.prototype.hash.MolecularHashFactory
 */
public class NonHydrogenBondOrderSumSeed implements AtomSeed {

    public NonHydrogenBondOrderSumSeed() {
    }

    public int seed(IAtomContainer molecule, IAtom atom) {

        // could be improved if bonds were mapped
        double value = AtomContainerManipulator.getBondOrderSum(molecule, atom);
        value -= AtomContainerManipulator.countExplicitHydrogens(molecule, atom);

        return Double.valueOf(value).hashCode();

    }

    @Override
    public String toString() {
        return "Bond Order Sum (no hydrogens)";
    }

}
