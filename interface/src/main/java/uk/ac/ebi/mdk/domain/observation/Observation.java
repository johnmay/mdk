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

package uk.ac.ebi.mdk.domain.observation;

import uk.ac.ebi.mdk.domain.Descriptor;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;

/**
 *
 * An observation is anything from a tool
 *
 * @author johnmay
 */
public interface Observation
        extends Descriptor {

    /**
     *
     * Accept an ObservationVisitor
     *
     * @param visitor
     *
     */
    public void accept(ObservationVisitor visitor);

    public AnnotatedEntity getEntity();

    public void setEntity(AnnotatedEntity entity);

    public AnnotatedEntity getSource();

    public void setSource(AnnotatedEntity entity);

    public Observation getInstance();

}
