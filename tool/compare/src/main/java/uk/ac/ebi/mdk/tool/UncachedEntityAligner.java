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

package uk.ac.ebi.mdk.tool;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.Entity;
import uk.ac.ebi.mdk.tool.match.EntityMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author John May
 */
public class UncachedEntityAligner<E extends Entity> extends AbstractEntityAligner<E> {

    private static final Logger LOGGER = Logger.getLogger(UncachedEntityAligner.class);


    public UncachedEntityAligner(Collection<E> references) {
        super(references, Boolean.TRUE, Boolean.FALSE);
    }

    @Override
    public List<E> getMatching(E query, EntityMatcher matcher) {
        List<E> matching = new ArrayList<E>();
        for (E reference : references) {
            if (matcher.matches(query, reference)) {
                matching.add(query);
            }
        }
        return matching;
    }

}
