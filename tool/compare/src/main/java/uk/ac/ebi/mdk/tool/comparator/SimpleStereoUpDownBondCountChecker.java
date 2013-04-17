/*
 * Copyright (c) 2013. Pablo Moreno
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
package uk.ac.ebi.mdk.tool.comparator;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

/**
 * Compares if the wedge (up) and hatch (down) bond counts of two molecules match.
 *
 * @author pmoreno
 * @deprecated method offers no indication as to whether two molecules are
 *             similar
 */
@Deprecated
public class SimpleStereoUpDownBondCountChecker {

    private SimpleStereoUpDownBondCountChecker(){};

    private static int getUpBonds(IAtomContainer mol) {
        int count = 0;
        for (IBond iBond : mol.bonds()) {
            if (iBond.getStereo().equals(IBond.Stereo.UP) ||
                    iBond.getStereo().equals(IBond.Stereo.UP_INVERTED)) // XXX: UP should count with DOWN_INVERTED
                count++;
        }
        return count;
    }

    private static int getDownBonds(IAtomContainer mol) {
        int count = 0;
        for (IBond iBond : mol.bonds()) {
            if (iBond.getStereo().equals(IBond.Stereo.DOWN) ||
                    iBond.getStereo().equals(IBond.Stereo.DOWN_INVERTED)) // XXX: DOWN should count with UP_INVERTED
                count++;
        }
        return count;
    }

    public static boolean equals(IAtomContainer molA, IAtomContainer molB) {
        int upA = getUpBonds(molA);
        int doA = getDownBonds(molA);

        int upB = getUpBonds(molB);
        int doB = getDownBonds(molB);

        // if the number of up and downs bonds and different, they are clearly different.
        if ((upA + doA) != (upB + doB))
            return false;

        return ((upA == upB) && (doA == doB)) || ((upA == doB) && (doA == upB));
    }
}
