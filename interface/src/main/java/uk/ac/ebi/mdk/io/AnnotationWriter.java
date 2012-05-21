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

import uk.ac.ebi.mdk.domain.annotation.Annotation;

import java.io.IOException;

/**
 * AnnotationWriter - 08.03.2012 <br/>
 * <p/>
 * Describes a class that can write a single type annotation data to a stream
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface AnnotationWriter<A extends Annotation> {

    /**
     * Write the annotation data to a stream
     *
     * @param annotation the annotation type
     *
     * @throws IOException low-level io error
     */
    public void write(A annotation) throws IOException;

}
