/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

package uk.ac.ebi.mdk.domain.entity.collection;

import uk.ac.ebi.mdk.domain.entity.Reconstruction;

import java.util.Collection;

/**
 * Manage reconstructions.
 * @author johnmay
 */
public interface ReconstructionManager {

    public Collection<Reconstruction> reconstructions();

    /**
     * Is the manager empty?
     * @return whether there are no reconstructions present
     */
    public boolean isEmpty();

    /**
     * The active reconstruction, or null if no reconstruction is active
     * @return the active reconstruction
     */
    public Reconstruction active();

    /**
     * Activate the provided reconstruction, if no reconstruction is provided
     * it is added to the list of reconstructions.
     * @param reconstruction a reconstruction to activate
     */
    public void activate(Reconstruction reconstruction);

    /**
     * Add a reconstruction without making it the active project
     *
     * @param reconstruction  the reconstruction to add
     */
    public void add(Reconstruction reconstruction);
}
