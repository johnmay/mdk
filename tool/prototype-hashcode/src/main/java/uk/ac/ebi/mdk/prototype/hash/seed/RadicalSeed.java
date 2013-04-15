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
 * A atomic hash seed that encode mono-, di- and tri- valent free radicals.
 *
 * @author johnmay
 * @see org.openscience.cdk.interfaces.IAtomContainer#getSingleElectron(int)
 */
public final class RadicalSeed implements AtomSeed {

    /*
     * default value when a null charge is accessed
     */
    private static final int NULL_VALUE = 13367;

    public RadicalSeed() {}

    public final int seed(IAtomContainer container, IAtom atom) {

        int sum = 0;

        for (ISingleElectron unpaired : container.singleElectrons()) {
            if (unpaired.getAtom().equals(atom)) {
                sum++;
            }
        }

        return sum > 0 ? sum * 413158511 : NULL_VALUE;

    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return "Radicals";
    }

}
