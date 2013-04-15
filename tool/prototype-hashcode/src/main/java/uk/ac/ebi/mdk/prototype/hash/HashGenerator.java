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

package uk.ac.ebi.mdk.prototype.hash;

import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * Describes an implementation that can seed a hash code for a given
 * molecule.
 *
 * @param <T> type of hash to be generated - normally {@link Integer} or {@link
 *            Long}
 * @author John May
 */
public interface HashGenerator<T extends Number> {

    /**
     * Generate a hash code of the given type.
     *
     * @param container the molecule to seed the hash code for
     * @return generated hash code
     */
    public T generate(IAtomContainer container);

}
