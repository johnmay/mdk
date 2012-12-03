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

package org.openscience.cdk.parity.ncomponent;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.parity.ParityCalculator;
import org.openscience.cdk.parity.SP3Parity2DCalculator;
import org.openscience.cdk.parity.component.AbstractStereoComponent;
import org.openscience.cdk.parity.component.StereoComponent;

import java.util.List;

/**
 * @author John May
 */
public class Tetrahedral2DComponent extends AbstractStereoComponent<Long>
        implements StereoComponent<Long> {


    private int i;
    private int[] neighbours;

    // for geometry calculation
    private IAtom[] connected = new IAtom[4];
    private int[] planes = new int[4];

    private final ParityCalculator parity = new SP3Parity2DCalculator();

    public Tetrahedral2DComponent(IAtom central,
                                  List<IBond> connected,
                                  IAtomContainer container) {

        this.neighbours = new int[connected.size()];
        this.i = container.getAtomNumber(central);

        for (int i = 0; i < connected.size(); i++) {
            IBond bond = connected.get(i);
            this.connected[i] = bond.getConnectedAtom(central);
            this.neighbours[i] = container.getAtomNumber(this.connected[i]);
            this.planes[i] = getPlane(bond, central);
        }

        if (connected.size() < 4) {
            this.connected[3] = central;
        }


    }

    private int getPlane(IBond bond, IAtom central) {

        int plane = getPlane(bond.getStereo());

        if (plane == 0)
            return plane;
        if (bond.getAtom(0) == central)
            return plane;
        if (bond.getAtom(1) == central)
            return plane * -1; // invert

        // bond not in [0] or [1]... multi bond - log error if needed,
        // currently ignored

        return 0;
    }

    private int getPlane(IBond.Stereo stereo) {
        switch (stereo) {
            case UP:
            case DOWN_INVERTED:
                return +1;
            case DOWN:
            case UP_INVERTED:
                return -1;
            default:
                return 0;
        }
    }

    @Override
    public boolean configure(Long[] current, Long[] configured) {

        int sortParity = permutationParity(current, neighbours);

        // no invariance in our neighbours
        if (sortParity == 0) {
            return false;
        }

        // all our neighbours are different -> do geometry calculation
        int storageParity = parity.parity(connected, planes);
        int parity = sortParity * storageParity;

        // if the parity is +1/-1 set a configured value appropriately
        if (parity == 1) {
            configured[i] *= 1300141L;
        } else if (parity == -1) {
            configured[i] *= 105913L;
        }

        return true;

    }

}
