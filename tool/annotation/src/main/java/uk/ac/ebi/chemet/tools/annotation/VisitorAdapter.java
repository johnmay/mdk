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

package uk.ac.ebi.chemet.tools.annotation;

import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.annotation.AnnotationVisitor;

import java.util.HashMap;
import java.util.Map;

/**
 * An abstract class that allows simplified implementation of specialised
 * visitors.
 *
 * @author John May
 */
public abstract class VisitorAdapter<A extends Annotation, T>
        implements AnnotationVisitor<T> {

    private final Class<? extends A> c;
    private final T value;
    private final Boolean subclass;

    /**
     * Create an adapted for the given class 'c'. This will instantiate the adapter
     * with a 'null' default value and will not check for subclasses of the given class.
     *
     * @param c the class to process
     * @see #VisitorAdapter(Class, Object)
     * @see #VisitorAdapter(Class, Object, Boolean)
     */
    public VisitorAdapter(Class<? extends A> c) {
        this(c, null);
    }

    /**
     * Creates an adapted for the given annotation class and default value. Only
     * classes of the provided type will be processed.
     *
     * @param c     the class to process
     * @param value the default value to if the class can not be processed
     * @see #VisitorAdapter(Class, Object)
     * @see #VisitorAdapter(Class, Object, Boolean)
     */
    public VisitorAdapter(Class<? extends A> c,
                          T value) {
        this(c, value, Boolean.FALSE);
    }

    /**
     * Creates an adapted for the given annotation class, default value and
     * allows indication of subclass processing.
     *
     * @param c        the class to process
     * @param value    the default value to if the class can not be processed
     * @param subclass whether subclasses should be processed
     * @see #VisitorAdapter(Class, Object)
     * @see #VisitorAdapter(Class, Object, Boolean)
     */
    public VisitorAdapter(Class<? extends A> c,
                          T value,
                          Boolean subclass) {
        this.c        = c;
        this.value    = value;
        this.subclass = subclass != null ? subclass : Boolean.FALSE;

        if(this.c == null)
            throw new IllegalArgumentException("Null class type provided to constructor");

    }

    /**
     * Tests whether the annotation is accepted by this visitor.
     *
     * @param annotation the annotation to check
     * @return whether the annotation is accepted by this adapter
     */
    private Boolean accepts(Annotation annotation) {
        return subclass ? c.isInstance(annotation) : c == annotation.getClass();
    }

    /**
     * Visits the provided annotation if possible. If the type is not the
     * accepted the default value provided in the constructor is returned.
     *
     * @param annotation the annotation to visit
     * @return the value form the visit
     * @see #_visit(uk.ac.ebi.mdk.domain.annotation.Annotation)
     */
    @SuppressWarnings("unchecked")
    @Override
    public T visit(Annotation annotation) {
        // we should cache the class mapping
        return accepts(annotation) ? _visit((A) annotation) : value;
    }

    /**
     * Visit a specific annotation type
     */
    public abstract T _visit(A annotation);

    /**
     * Access the default value for the annotation visitor.
     * @return the value provided to the constructor
     */
    public T getValue() {
        return value;
    }
}
