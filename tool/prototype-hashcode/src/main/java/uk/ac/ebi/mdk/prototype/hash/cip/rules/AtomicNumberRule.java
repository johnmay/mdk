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

package uk.ac.ebi.mdk.prototype.hash.cip.rules;

import org.openscience.cdk.geometry.cip.ILigand;
import org.openscience.cdk.tools.periodictable.PeriodicTable;

class AtomicNumberRule implements ISequenceSubRule<ILigand> {

    /**
     * {@inheritDoc}
     */
    public int compare(ILigand ligand1, ILigand ligand2) {
        return getAtomicNumber(ligand1).compareTo(getAtomicNumber(ligand2));
    }

    private Integer getAtomicNumber(ILigand ligand) {
        Integer atomNumber = ligand.getLigandAtom().getAtomicNumber();
        if (atomNumber != null) return atomNumber;
        return PeriodicTable.getAtomicNumber(ligand.getLigandAtom().getSymbol());
    }
}
