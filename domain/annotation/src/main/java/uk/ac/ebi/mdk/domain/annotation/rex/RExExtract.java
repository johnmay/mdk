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

package uk.ac.ebi.mdk.domain.annotation.rex;

import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.IdentifierFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/** @author John May */
public final class RExExtract {

    private final Identifier   source;
    private final String       sentence;
    private final List<RExTag> tags;

    public RExExtract() {
        this(IdentifierFactory.EMPTY_IDENTIFIER, "", Collections.<RExTag>emptyList());
    }

    public RExExtract(Identifier source, String sentence, List<RExTag> tags) {
        this.source = source;
        this.sentence = sentence;
        this.tags = tags;
    }

    public Identifier source() {
        return source;
    }

    public String sentence() {
        return sentence;
    }

    public Collection<RExTag> tags() {
        return Collections.unmodifiableList(tags);
    }
}
