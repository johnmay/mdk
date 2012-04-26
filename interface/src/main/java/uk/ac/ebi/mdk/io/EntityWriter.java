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

import java.io.IOException;

/**
 * EntityWriter - 12.03.2012 <br/>
 * <p/>
 * <p/>
 * The EntityWriter interface describes something that can
 * write entity data to a stream. There should be a different
 * writer for each entity (i.e. MetaboliteWriter, ReactionWriter).
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface EntityWriter<E extends Entity> {

    /**
     * Write the entity data to the stream
     *
     * @param entity the entity data to write
     *
     * @throws IOException low-level io error
     */
    public void write(E entity) throws IOException;

}
