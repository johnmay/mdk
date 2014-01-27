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

package uk.ac.ebi.mdk.io.observation;

import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.observation.MatchedEntity;
import uk.ac.ebi.mdk.io.IdentifierOutput;
import uk.ac.ebi.mdk.io.IdentifierWriter;
import uk.ac.ebi.mdk.io.ObservationWriter;

import java.io.IOException;

@CompatibleSince("1.4.0")
public class MatchedEntityWriter implements ObservationWriter<MatchedEntity> {

    private IdentifierOutput out;

    @SuppressWarnings("unchecked")
    public MatchedEntityWriter(IdentifierOutput identifierReader) {
        this.out = identifierReader;
    }

    @Override public void write(MatchedEntity observation) throws IOException {
        out.write(observation.reconId());
        out.write(observation.entityId());
    }
}
