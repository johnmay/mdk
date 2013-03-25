/*
 * Copyright (C) 2012  John May and Pablo Moreno
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package uk.ac.ebi.mdk.io;

import uk.ac.ebi.mdk.domain.entity.Entity;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;

import java.io.IOException;

/**
 * EntityReader - 12.03.2012 <br/>
 * <p/>
 * Provides reading of a single entity type. This is a read marshaller for
 * a single entity type. Each entity type should have a different reader.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface EntityReader<E extends Entity> {

    /**
     * Read the entity data from the stream and create a new entity
     *
     * @return read entity
     *
     * @throws IOException            low-level io error
     * @throws ClassNotFoundException low-level io error
     */
    public E readEntity(Reconstruction reconstruction) throws IOException, ClassNotFoundException;

}
