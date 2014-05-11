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

package org.openscience.cdk.hash.stereo;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IPseudoAtom;

import java.util.BitSet;

/**
 * @author John May
 */
public final class AtomStereoType {

    private final IAtomContainer container;
    private final int[][]        g;
    private final BondIndex      bonds;

    public AtomStereoType(IAtomContainer ac, int[][] graph, BondIndex bonds) {
        this.container = ac;
        this.g = graph;
        this.bonds = bonds;
    }

    Type typeOf(int i) {

        // WTF!
        if (container.getAtom(i).getImplicitHydrogenCount() == null) {
            return Type.None;
        }
        
        // determine hydrogen count, connectivity and valence 
        int h = container.getAtom(i).getImplicitHydrogenCount();
        int x = g[i].length + h;
        int v = h;

        if (x < 2 || x > 4 || h > 1)
            return Type.None;

        int piNeighbor = 0;
        for (int w : g[i]) {
            if (atomicNumber(container.getAtom(w)) == 1)
                h++;
            switch (bonds.get(i, w).getOrder()) {
                case SINGLE:
                    v++;
                    break;
                case DOUBLE:
                    v += 2;
                    piNeighbor = w;
                    break;
                default:
                    // triple, quadruple or unset? can't be a stereo centre
                    return Type.None;
            }
        }                                
        
        return supportedType(i, v, h, x);        
    }
    
    /**
     * Determine the type of stereo chemistry (if any) which could be supported
     * by the atom at index 'i'. The rules used to define the types of
     * stereochemistry are encoded from the InChI Technical Manual.
     *
     * @param i atom index
     * @param v valence
     * @param h hydrogen
     * @param x connectivity
     * @return type of stereo chemistry
     */
    private Type supportedType(int i, int v, int h, int x) {

        IAtom atom = container.getAtom(i);

        // the encoding a bit daunting and to be concise short variable names
        // are used. these parameters make no distinction between implicit/
        // explicit values and allow complete (and fast) characterisation of
        // the type of stereo atom 
        //
        // i: atom index
        // v: valence (bond order sum)
        // h: total hydrogen count
        // x: connected atoms
        // q: formal charge


        int q = charge(atom);

        // more than one hydrogen
        if (h > 1)
            return Type.None;

        switch (atomicNumber(atom)) {
            case 0: // stop the nulls on pseudo atoms messing up anything else
                return Type.None;
            case 5: // boron
                return q == -1 && v == 4 && x == 4 ? Type.Tetrahedral : Type.None;

            case 6: // carbon
                if (v != 4 || q != 0)
                    return Type.None;
                if (x == 3)
                    return Type.Tricoordinate;
                if (x == 4)
                    return Type.Tetrahedral;
                return Type.None;
            case 7: // nitrogen
                if (x == 2 && v == 3 && h == 0 && q == 0)
                    return Type.Tricoordinate;
                if (x == 3 && v == 4 && q == 1)
                    return Type.Tricoordinate;
                if (x == 4 && h == 0 && (q == 0 && v == 5 || q == 1 && v == 4))
                    return verifyTerminalHCount(i) ? Type.Tetrahedral : Type.None;
                return x == 3 && h == 0 && inThreeMemberRing(i) ? Type.Tetrahedral : Type.None;


            case 14: // silicon
                if (v != 4 || q != 0)
                    return Type.None;
                if (x == 3)
                    return Type.Tricoordinate;
                if (x == 4)
                    return Type.Tetrahedral;
                return Type.None;
            case 15: // phosphorus
                if (x == 4 && (q == 0 && v == 5 && h == 0 || q == 1 && v == 4))
                    return verifyTerminalHCount(i) ? Type.Tetrahedral : Type.None;
                // note 3 valent phosphorus not documented as accepted 
                // by InChI tech manual but tests show it is
                if (x == 3 && q == 0 && v == 3 && h == 0)
                    return verifyTerminalHCount(i) ? Type.Tetrahedral : Type.None;
            case 16: // sulphur
                if (h > 0)
                    return Type.None;
                if (q == 0 && ((v == 4 && x == 3) || (v == 6 && x == 4)))
                    return verifyTerminalHCount(i) ? Type.Tetrahedral : Type.None;
                if (q == 1 && ((v == 3 && x == 3) || (v == 5 && x == 4)))
                    return verifyTerminalHCount(i) ? Type.Tetrahedral : Type.None;
                return Type.None;

            case 32: // germanium
                if (v != 4 || q != 0)
                    return Type.None;
                if (x == 3)
                    return Type.Tricoordinate;
                if (x == 4)
                    return Type.Tetrahedral;
                return Type.None;
            case 33: // arsenic
                if (x == 4 && q == 1 && v == 4)
                    return verifyTerminalHCount(i) ? Type.Tetrahedral : Type.None;
                return Type.None;
            case 34: // selenium
                if (h > 0)
                    return Type.None;
                if (q == 0 && ((v == 4 && x == 3) || (v == 6 && x == 4)))
                    return verifyTerminalHCount(i) ? Type.Tetrahedral : Type.None;
                if (q == 1 && ((v == 3 && x == 3) || (v == 5 && x == 4)))
                    return verifyTerminalHCount(i) ? Type.Tetrahedral : Type.None;
                return Type.None;

            case 50: // tin
                return q == 0 && v == 4 && x == 4 ? Type.Tetrahedral : Type.None;
        }

        return Type.None;
    }

    /**
     * Verify that there are is not 2 terminal heavy atoms (of the same element)
     * which have a hydrogen count > 0. This follows the InChI specification
     * that - An atom or positive ion N, P, As, S, or Se is not treated as
     * stereogenic if it has (a) A terminal H atom neighbor or (b) At least two
     * terminal neighbors, XHm and XHn, (n+m>0) connected by any kind of bond,
     * where X is O, S, Se, Te, or N. This avoids the issue that under
     * Cahn-Ingold-Prelog (or canonicalisation) the oxygens in 'P(=O)(O)(*)*'
     * would not be found to be equivalent and a parity/winding would be
     * assigned.
     *
     * @param v the vertex (atom index) to check
     * @return the atom does not have > 2 terminal neighbors with a combined
     *         hydrogen count of > 0
     */
    private boolean verifyTerminalHCount(int v) {

        int[] counts = new int[6];
        int[][] atoms = new int[6][g[v].length];

        boolean found = false;

        for (int w : g[v]) {
            int idx = indexNeighbor(container.getAtom(w));
            atoms[idx][counts[idx]++] = w;
            found = found || (idx > 0 && counts[idx] > 1);
        }

        if (!found)
            return true;

        for (int i = 1; i < counts.length; i++) {
            if (counts[i] < 2)
                continue;

            int terminalCount = 0;
            int terminalHCount = 0;

            for (int j = 0; j < counts[i]; j++) {
                int hCount = 0;
                int[] ws = g[atoms[i][j]];
                for (int w : g[atoms[i][j]]) {
                    if (atomicNumber(container.getAtom(w)) == 1)
                        hCount++;
                }

                // is terminal?
                if (ws.length - hCount == 1) {
                    terminalCount++;
                    terminalHCount += hCount + container.getAtom(atoms[i][j])
                                                        .getImplicitHydrogenCount();
                }
            }

            if (terminalCount > 1 && terminalHCount > 0)
                return false;
        }

        return true;
    }

    /**
     * Index the atom by element to a number between 0-5. This allows us to
     * quickly count up neighbors we need to and the ignore those we don't
     * (defaults to 0).
     *
     * @param atom an atom to get the element index of
     * @return the element index
     */
    private static int indexNeighbor(IAtom atom) {
        switch (atomicNumber(atom)) {
            case 7:  // N
                return 1;
            case 8:  // O
                return 2;
            case 16: // S
                return 3;
            case 34: // Se
                return 4;
            case 52: // Te
                return 5;
            default:
                return 0;
        }
    }

    /**
     * Check if the {@code atom} at index {@code v} is a member of a small ring
     * (n=3). This is the only time a 3 valent nitrogen is allowed by InChI to
     * be potentially stereogenic.
     *
     * @param v atom index
     * @return the atom is a member of a 3 member ring
     */
    private boolean inThreeMemberRing(int v) {
        BitSet adj = new BitSet();
        for (int w : g[v])
            adj.set(w);
        // is a neighbors neighbor adjacent?
        for (int w : g[v])
            for (int u : g[w])
                if (adj.get(u))
                    return true;
        return false;
    }

    /**
     * Safely obtain the atomic number of an atom. If the atom has undefined
     * atomic number and is not a pseudo-atom it is considered an error. Pseudo
     * atoms with undefined atomic number default to 0.
     *
     * @param a an atom
     * @return the atomic number of the atom
     */
    private static int atomicNumber(IAtom a) {
        Integer elem = a.getAtomicNumber();
        if (elem != null)
            return elem;
        if (a instanceof IPseudoAtom)
            return 0;
        throw new IllegalArgumentException("an atom had an undefieind atomic number");
    }

    /**
     * Safely obtain the formal charge on an atom. If the atom had undefined
     * formal charge it is considered as neutral (0).
     *
     * @param a an atom
     * @return the formal charge
     */
    private static int charge(IAtom a) {
        Integer chg = a.getFormalCharge();
        return chg != null ? chg : 0;
    }

    /**
     * Convert an array of long (64-bit) values to an array of (32-bit)
     * integrals.
     *
     * @param org the original array of values
     * @return the array cast to int values
     */
    private static int[] toIntArray(long[] org) {
        int[] cpy = new int[org.length];
        for (int i = 0; i < cpy.length; i++)
            cpy[i] = (int) org[i];
        return cpy;
    }

    /** Defines the type of a stereocenter. */
    public enum Stereocenter {

        /** Atom is a true stereo-centre. */
        True,

        /** Atom resembles a stereo-centre (para). */
        Para,

        /** Atom is a potential stereo-centre */
        Potential,

        /** Non stereo-centre. */
        Non
    }

    public enum Type {

        /** An atom within a cumulated system. (not yet supported) */
        Bicoordinate,

        /**
         * A potentially stereogenic atom with 3 neighbors - one atom in a
         * geometric centres or cumulated system (allene, cumulene).
         */
        Tricoordinate,

        /**
         * A potentially stereogenic atom with 4 neighbors - tetrahedral
         * centres.
         */
        Tetrahedral,

        /** A non-stereogenic atom. */
        None
    }
}
