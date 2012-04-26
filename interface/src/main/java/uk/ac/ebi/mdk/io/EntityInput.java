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
 * EntityInput - 11.03.2012 <br/>
 * <p/>
 * The EntityInput interface provides reading of CheMet entity classes and
 * their data from a stream
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface EntityInput {

    /**
     * Read the next entity. This method will read the type of entity and the
     * object data for that entity. Use this method if the entity was written with
     * {@see EntityOutput#write(Entity)}
     *
     * @param <E> cast to this type of entity
     *
     * @return new entity read from the stream
     *
     * @throws IOException            low-level io error
     * @throws ClassNotFoundException thrown if a class is read
     *                                that cannot be found in the current scope
     */
    public <E extends Entity> E read() throws IOException, ClassNotFoundException;

    /**
     * Read the next entity data from the stream. This provides some minor efficiency
     * when we know the class of the entity. You would most likely use this method when
     * reading a collection of entities that are the same type. In this case the class of
     * the entities need only be read once and the data for each type is read in for each
     * entity in the collection. Use this method when data is written with
     * {@see EntityOutput#writeData(Class)}.
     *
     * @param c   Class of the entity that the data is for
     * @param <E> cast to this type of entity
     *
     * @return new entity with the read data
     *
     * @throws IOException            low-level io error
     * @throws ClassNotFoundException thrown if a class is read
     *                                that cannot be found in the current scope
     */
    public <E extends Entity> E read(Class<E> c) throws IOException, ClassNotFoundException;

}
