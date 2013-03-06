/*
 * Copyright (c) 2013. Pablo Moreno
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
package uk.ac.ebi.mdk.tool.comparator;

import uk.ac.ebi.mdk.domain.annotation.ExactMass;
import uk.ac.ebi.mdk.domain.entity.Metabolite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Compare metabolties by their exact mass.
 * @author pmoreno
 * @see ExactMass
 */
public final class MetaboliteMassComparator
        extends AbstractLinkedComparator<Metabolite> {

    /**
     * @inheritDoc
     */
    @Override public int compare(Metabolite a, Metabolite b) {
        return mass(a).compareTo(mass(b));
    }

    /**
     * @inheritDoc
     */
    @Override
    protected int internalComparator(Metabolite s, Metabolite s1) {
        return compare(s, s1);
    }

    private Float mass(Metabolite s) {
        Iterator<ExactMass> masses = s.getAnnotations(ExactMass.class).iterator();
        return masses.hasNext() ? masses.next().getValue() : Float.MAX_VALUE;
    }

    /**
     * Convenience comparator conjunction.
     *
     * @param other comparator to invoke if this comparator ties
     * @return a new comparator composing this and <i>next</i>.
     */
    public static Comparator<Metabolite> and(Comparator<Metabolite> other) {
        return new ComparatorConjunction<Metabolite>(new MetaboliteMassComparator(),
                                                     other);
    }
}
