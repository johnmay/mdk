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

package uk.ac.ebi.mdk.io.text.kegg;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * Enumeration of all possible KEGG Reaction fields. These fields can be used in
 * combination with the {@link uk.ac.ebi.mdk.io.text.kegg.KEGGCompoundParser} to
 * load specific parts of an entry and ignore others.
 *
 * @author John May
 * @see KeggFlatfile
 * @see <a href="http://www.kegg.jp/kegg/rest/dbentry.html">DB Entry Format</a>
 */
public enum KEGGReactionField implements KEGGField {
    ENTRY,
    NAME,
    DEFINITION,
    EQUATION,
    REMARK,
    COMMENT,
    RPAIR,
    ENZYME,
    PATHWAY,
    ORTHOLOGY,
    REFERENCE;

    private final Set<String> names;

    private KEGGReactionField(String... names) {
        ImmutableSet.Builder<String> ns = new ImmutableSet.Builder<String>();
        for (String name : names) {
            ns.add(name);
        }
        ns.add(name());
        this.names = ns.build();
    }

    /** @inheritDoc */
    @Override public Set<String> names() {
        return names;
    }
}
