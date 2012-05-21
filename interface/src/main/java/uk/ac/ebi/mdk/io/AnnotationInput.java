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
 * AnnotationInput - 11.03.2012 <br/>
 * <p/>
 * The AnnotationInput interface describes a class that can read annotations from a stream.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface AnnotationInput {

    /**
     * Read the next annotation (class and data)
     *
     * @param <A> convenience constructor to cast to
     *
     * @return new instance of the read annotation
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public <A extends Annotation> A read() throws IOException, ClassNotFoundException;

    /**
     * Read the next annotation (data only). This method is used when we need to read a collection
     * of annotations that are the same type by removing the small overhead of writing the class
     *
     * @param c   class of the annotation to read
     * @param <A> cast to this type
     *
     * @return new instance of a read annotation (of type c)
     *
     * @throws IOException            low-level io error
     * @throws ClassNotFoundException low-level io error
     */
    public <A extends Annotation> A read(Class<A> c) throws IOException, ClassNotFoundException;

    /**
     * Read the next class of annotation
     *
     * @return the class of the next annotation
     *
     * @throws IOException            low-level io error
     * @throws ClassNotFoundException low-level io error
     */
    public Class readClass() throws IOException, ClassNotFoundException;

}
