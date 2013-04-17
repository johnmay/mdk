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

package uk.ac.ebi.mdk.tool.match;

import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author John May
 */
public class CrossReferenceMatcher<E extends AnnotatedEntity>
        extends AbstractMatcher<E, Set<Identifier>>
        implements EntityMatcher<E, Set<Identifier>> {

    @Override
    public Boolean matchMetric(Set<Identifier> queryMetric, Set<Identifier> subjectMetric) {
        return matchAny(queryMetric, subjectMetric);
    }

    @Override
    public Set<Identifier> calculatedMetric(E entity) {

        Set<Identifier> identifiers = new HashSet<Identifier>();

        Collection<CrossReference> references = entity.getAnnotationsExtending(CrossReference.class);

        for (CrossReference reference : references) {
            identifiers.add(reference.getIdentifier());
        }

        identifiers.add(entity.getIdentifier());

        return identifiers;

    }

}
