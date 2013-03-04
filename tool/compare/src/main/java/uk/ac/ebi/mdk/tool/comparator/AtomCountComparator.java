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

import java.util.Comparator;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.AtomContainerComparator;

/**
 * Compare atom containers by their atom count and sorts the atoms low to high.
 * @author  pmoreno
 */
public final class AtomCountComparator implements Comparator<IAtomContainer> {

    /**
     * @inheritDoc
     */
    public int compare(IAtomContainer t, IAtomContainer t1) {
        return nAtoms(t).compareTo(nAtoms(t1));
    }


    private Integer nAtoms(IAtomContainer c) {
        return c != null ? c.getAtomCount() : Integer.MAX_VALUE;
    }

    /**
     * Convenience comparator conjunction.
     * @param other comparator to invoke if this comparator ties
     * @return a new comparator composing this and <i>next</i>.
     */
    public static Comparator<IAtomContainer> and(Comparator<IAtomContainer> other) {
        return new ComparatorConjunction<IAtomContainer>(new AtomContainerComparator(),
                                                         other);
    }
}
