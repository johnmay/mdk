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

package uk.ac.ebi.mdk.domain.annotation;

import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author John May
 */
public interface AnnotationFactory {

    public List<Annotation> ofContext(AnnotatedEntity entity);


    /**
     * Access a list of annotations that are specific to the
     * provided entity class. Note: the annotations contained
     * with in the list should only be used to construct
     * new entities and not to store information. The instances_old
     * instead of the classes are returned to avoid extra object
     * creation.
     *
     * @param entityClass class of the entity you which to get context
     *                    annotations for
     *
     * @return List of annotations that can be added to that class which
     *         are intended for new instantiation only
     */
    public List<Annotation> ofContext(Class<? extends AnnotatedEntity> entityClass);


    /**
     * Construct an empty annotation of the given class type. Note there is an
     * overhead off using this method over {@ofIndex(Byte)} as the Byte index is
     * first looked up in the AnnotationLoader. The average speed reduction is
     * 1800 % slower (note this is still only about 1/3 second for 100 000
     * objects).
     *
     * @param c
     *
     * @return
     */
    public <A extends Annotation> A ofClass(Class<? extends A> c);


    /**
     * Access a set of annotation flags that could match this entity. This provides suggestion
     * of matching flag's for this entity using the {@see AbstractFlag.matches(AnnotatedEntity)}
     *
     * @param entity the entity to collect matching flags for
     *
     * @return annotations which could be added to the entity (may need user prompt)
     *
     * @see uk.ac.ebi.mdk.domain.annotation.Flag#matches(uk.ac.ebi.mdk.domain.entity.AnnotatedEntity)
     */
    public Set<Flag> getMatchingFlags(AnnotatedEntity entity);


    /**
     * Access a collection of all known sub-classes of the annotation. e.g. providing CrossReference
     * should return KEGGCompoundCrossReference, ChEBICrossReference, EnzymeClassification etc..
     *
     * @param c super-class
     *
     * @return all known sub-classes and the provided class
     */
    public Collection<Class<? extends Annotation>> getSubclasses(Class<? extends Annotation> c);

    /**
     * Creates an appropriate cross-reference
     *
     * @param identifier
     * @param <I>
     *
     * @return
     */
    public <I extends Identifier> Annotation getCrossReference(I identifier);

}
