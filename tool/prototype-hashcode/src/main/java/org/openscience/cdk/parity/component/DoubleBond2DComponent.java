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

package org.openscience.cdk.parity.component;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.parity.ParityCalculator;
import org.openscience.cdk.parity.SP2Parity2DUnspecifiedCalculator;

import java.util.List;

/**
 * @author John May
 */
public class DoubleBond2DComponent extends AbstractStereoComponent<Long> {

    private ParityCalculator parityCalculator = new SP2Parity2DUnspecifiedCalculator();

    private final IAtomContainer container;

    private final IAtom left;
    private final IAtom right;

    private List<IBond> leftBonds;
    private List<IBond> rightBonds;

    private int[] leftNeighbours;
    private int[] rightNeighbours;

    /**
     * @param left
     * @param right
     * @param leftBonds  last bond = left=right
     * @param rightBonds last bond = right=left
     */
    public DoubleBond2DComponent(IAtom left, IAtom right,
                                 List<IBond> leftBonds, List<IBond> rightBonds,
                                 IAtomContainer container) {

        this.left = left;
        this.right = right;
        this.leftBonds = leftBonds;
        this.rightBonds = rightBonds;
        this.container = container;

        if (leftBonds.size() > 3)
            throw new IllegalArgumentException("too many left bonds");
        if (rightBonds.size() > 3)
            throw new IllegalArgumentException("too many right bonds");


        leftNeighbours = new int[]{
                -1,
                container.getAtomNumber(left)
        };
        for (int i = 0, n = leftBonds.size() - 1; i < n; i++) {
            leftNeighbours[i] = container.getAtomNumber(leftBonds.get(i).getConnectedAtom(left));
        }

        rightNeighbours = new int[]{
                -1,
                container.getAtomNumber(right)
        };
        for (int i = 0, n = rightBonds.size() - 1; i < n; i++) {
            rightNeighbours[i] = container.getAtomNumber(rightBonds.get(i).getConnectedAtom(right));
        }


    }

    @Override
    public boolean configure(Long[] current, Long[] configured) {

        int leftSortParity = leftBonds.size() == 2 ? 1 : permutationParity(current, leftNeighbours);
        int rightSortParity = rightBonds.size() == 2 ? 1 : permutationParity(current, rightNeighbours);

        int sortParity = leftSortParity * rightSortParity;

        if (sortParity == 0)
            return false;

        int parity = sortParity * getStorageParity(left, leftBonds) * getStorageParity(right, rightBonds);
        if (parity > 0) {
            configured[container.getAtomNumber(left)] *= 1300141;
            configured[container.getAtomNumber(right)] *= 1300141;
        } else if (parity < 0) {
            configured[container.getAtomNumber(left)] *= 105913;
            configured[container.getAtomNumber(right)] *= 105913;
        }

        return true;

    }

    private int getStorageParity(IAtom atom, List<IBond> connected) {
        IAtom[] atoms = new IAtom[3];
        atoms[0] = connected.get(0).getConnectedAtom(atom);
        atoms[1] = connected.size() == 3 ? connected.get(1).getConnectedAtom(atom) : atom;
        atoms[2] = connected.get(connected.size() - 1).getConnectedAtom(atom);
        return parityCalculator.parity(atoms, null);
    }

}
