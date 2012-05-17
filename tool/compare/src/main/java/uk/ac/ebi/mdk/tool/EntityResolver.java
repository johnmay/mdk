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

package uk.ac.ebi.mdk.tool;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.Entity;
import uk.ac.ebi.mdk.tool.match.EntityMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

/**
 * Entity resolver controls matching of query entities to a reference using a stack of matching
 * methods. Methods at the top of the stack have the highest priority and are tested first
 *
 * @author John May
 */
public class EntityResolver<E extends Entity> {

    private static final Logger LOGGER = Logger.getLogger(EntityResolver.class);

    private Stack<EntityMatcher<E, ?>> matchers = new Stack<EntityMatcher<E, ?>>();
    private Collection<E> references;

    public EntityResolver() {
        this(new ArrayList<E>());
    }

    public EntityResolver(Collection<E> references) {
        // take a shallow copy
        this.references = new ArrayList<E>(references);
    }

    /**
     * Add a reference entity to the reference collection
     *
     * @param reference
     */
    public void addReference(E reference) {
        this.references.add(reference);
    }

    /**
     * Add a method to the top of the stack (highest priority)
     *
     * @param matcher new method
     *
     * @see Stack#push(Object)
     */
    public void push(EntityMatcher<E,?> matcher) {
        matchers.push(matcher);
    }

    /**
     * Remove a matcher from the top of the stack (highest priority)
     *
     * @return
     *
     * @see java.util.Stack#pop()
     */
    public EntityMatcher<E,?> pop() {
        return matchers.pop();
    }

    /**
     * Peak at the matcher at the top of the stack (highest priority)
     *
     * @return matcher at the top of the stack
     *
     * @see java.util.Stack#peek()
     */
    public EntityMatcher<E,?> peek() {
        return matchers.peek();
    }

    public List<E> getMatches(E entity) {

        List<E> matching = new ArrayList<E>();

        for (EntityMatcher matcher : matchers) {

            for (E reference : references) {
                if (matcher.matches(entity, reference)) {
                    matching.add(reference);
                }
            }

            if (!matching.isEmpty()) {
                return matching;
            }

        }

        return matching;
    }

    /**
     * Clear the method stack
     */
    public void clear() {
        matchers.clear();
    }


}
