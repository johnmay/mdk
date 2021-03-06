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

import uk.ac.ebi.mdk.domain.annotation.AbstractAnnotation;
import uk.ac.ebi.mdk.domain.entity.Reaction;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.IdentifierFactory;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.lang.annotation.Description;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A tagged sentence extracted using Jan's REx text-mining tool.
 *
 * @author John May
 * @see <a href="http://www.biomedcentral.com/1471-2105/13/172">A text-mining
 *      system for extracting metabolic reactions from full-text articles</a>
 */
@Context(Reaction.class)
@Brief("REx Extract")
@Description("A sentence extract from literature using the REx tool.")
public final class RExExtract extends AbstractAnnotation {

    private final Identifier   source;
    private final String       sentence;
    private final List<RExTag> tags;
    private final boolean isInCorrectOrganism;
    private final int totalSeedMetabolitesInSource;

    public RExExtract() {
        this(IdentifierFactory.EMPTY_IDENTIFIER, "",
             Collections.<RExTag>emptyList(), false, 0);
    }

    public RExExtract(Identifier source, String sentence, List<RExTag> tags, boolean isInCorrectOrganism,
                      int totalSeedMetabolitesInSource) {
        this.source = source;
        this.sentence = sentence;
        this.tags = tags;
        this.isInCorrectOrganism = isInCorrectOrganism;
        this.totalSeedMetabolitesInSource = totalSeedMetabolitesInSource;
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

    public boolean isInCorrectOrganism()
    {
        return isInCorrectOrganism;
    }

    public int totalSeedMetabolitesInSource()
    {
        return totalSeedMetabolitesInSource;
    }

    @Override public RExExtract newInstance() {
        return new RExExtract();
    }
}
