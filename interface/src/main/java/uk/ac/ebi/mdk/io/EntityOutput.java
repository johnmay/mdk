/*
 * Copyright (c) 2012. EMBL-EBI
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.io;

import uk.ac.ebi.mdk.domain.entity.Entity;

import java.io.IOException;

/**
 * EntityOutput - 11.03.2012 <br/>
 * <p/>
 * The EntityOutput interfaces writes CheMet entities to a stream. The entity
 * can be written with the class data or without.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface EntityOutput {

    /**
     * Write the entity class to the stream
     *
     * @param c class of an entity
     *
     * @throws IOException low-level io error
     */
    public void writeClass(Class<? extends Entity> c) throws IOException;

    /**
     * Write the entity data to the stream. This provides minor savings when
     * writing collections of the same type {@see EntityInput#read(Class)}.
     *
     * @param entity the entity data to write
     *
     * @throws IOException low-level io error
     */
    public void writeData(Entity entity) throws IOException;

    /**
     * Write the entity class and it's data to the stream. This method is analogous
     * to {@see EntityInput#read()} and is used when we want to put entities of different
     * types into the stream (i.e. we have a mixed list or single entity case).
     *
     * @param entity the entity to write to the stream
     *
     * @throws IOException low-level io error
     */
    public void write(Entity entity) throws IOException;


}
