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

package uk.ac.ebi.mdk.tree;

import org.openscience.cdk.hash.MoleculeHashGenerator;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IPseudoAtom;

/**
 * Create a key which describes the formula of the structure. The key only looks
 * at certain element types based on frequency of occurrence in metabolomes. The
 * key can optionally encode hydrogen count in which case structures are
 * different protonation states and or bonding patterns (i.e. butane/but-2-ene)
 * are distinguished.
 *
 * @author John May
 */
public enum FormulaHash implements MoleculeHashGenerator {

    /** Encodes with hydrogen counts. */
    WithHydrogens(true),

    /** Encode without hydrogen counts. */
    WithoutHydrogens(false);

    private boolean hydrogens = false;

    private FormulaHash(boolean hydrogens) {
        this.hydrogens = hydrogens;
    }

    /**
     * Encode the provided atom container.
     *
     * @param m input structure
     * @return encoded key based on formula
     */
    public long generate(final IAtomContainer m) {
        final int[] count = new int[118];
        for (final IAtom a : m.atoms()) {
            count[key(a)]++;
            if (hydrogens)
                count[0] += implH(a);

        }
        return hashCode(count, hydrogens ? 0 : 1);
    }

    /**
     * Encode the counts from the structure as a hash code.
     *
     * @param counts array of counts
     * @param from   the index at which to start
     * @return the hash code
     */
    private static long hashCode(int[] counts, int from) {
        long hash = (1L << 31) - 1;
        for (int i = from; i < counts.length; i++)
            hash = 31 * (hash + counts[i]);
        return hash;
    }

    /**
     * Define a key to count this atom by - the key is atomic number adjusted
     * such that hydrogens are 0 and pseudo atoms are 1.
     *
     * @param atom an atom
     * @return the key
     */
    private static int key(final IAtom atom) {
        int elem = atomicNum(atom);
        if (elem < 2)
            return elem ^ 0x1; // 0 -> 1, 1 -> 0
        return elem;
    }

    // safe access to implicit hydrogen count
    private static int implH(final IAtom atom) {
        Integer h = atom.getImplicitHydrogenCount();
        if (h != null)
            return h;
        if (atom instanceof IPseudoAtom)
            return 0;
        throw new IllegalArgumentException("a non-pseudo atom did not have a defined hydrogen count");
    }

    // safe access to atomic number
    private static int atomicNum(final IAtom atom) {
        Integer an = atom.getAtomicNumber();
        if (an != null)
            return an;
        if (atom instanceof IPseudoAtom)
            return 0;
        throw new IllegalArgumentException("atom had unset atomic number");
    }

}
