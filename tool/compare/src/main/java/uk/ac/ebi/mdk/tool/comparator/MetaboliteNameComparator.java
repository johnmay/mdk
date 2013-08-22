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

import uk.ac.ebi.mdk.domain.entity.Metabolite;

/**
 * Compare metabolites by name.
 * @author pmoreno
 */
public final class MetaboliteNameComparator
        extends AbstractLinkedComparator<Metabolite> {

    /**
     * @inheritDoc
     */
    @Override public int compare(Metabolite a, Metabolite b) {
        return name(a).compareTo(name(b));
    }

    private String name(Metabolite m) {
        return m.getName() != null ? m.getName() : "";
    }

    /**
     * @inheritDoc
     */
    @Override
    @Deprecated
    protected int internalComparator(Metabolite s, Metabolite s1) {
       return compare(s, s1);
    }
}
