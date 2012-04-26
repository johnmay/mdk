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

/**
 * Indicate an 'singleton' annotation that flags an entity either by the presence or
 * absence of the annotation. An example would be the 'Lumped' flag which indicates
 * a metabolite or reaction is a 'lumped' average or many metabolites or reactions.
 * These annotation contain no object data and serve as marker's for quick filtering
 * and querying. The Flag annotations should be singleton's to avoid duplication.
 * The {@see matches(AnnotatedEntity)} method provides a way of testing whether an entity
 * matches some set criteria of the flag. In the case of of 'Lumped' class the test is
 * whether the number of atom's of a metabolite is greater then a determined threshold.
 * This method plugs into the AnnotationFactory.getMatchingFlags(AnnotatedEntity) method
 * and provides prediction of flags. It should however be used with caution and required
 * user confirmation for case that cannot be exactly determined computationally.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface Flag extends Annotation {

    /**
     * Provides a way of testing whether an entity matches some set criteria of the flag.
     *
     * @param entity the entity to test a match for
     *
     * @return whether the flag is likely to apply to the given entity
     */
    public boolean matches(AnnotatedEntity entity);

}
