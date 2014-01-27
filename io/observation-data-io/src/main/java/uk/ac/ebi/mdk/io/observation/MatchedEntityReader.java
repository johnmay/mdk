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
import uk.ac.ebi.mdk.domain.observation.MatchedEntity;
import uk.ac.ebi.mdk.io.IdentifierInput;
import uk.ac.ebi.mdk.io.IdentifierReader;
import uk.ac.ebi.mdk.io.ObservationReader;

import java.io.IOException;


@CompatibleSince("1.4.0")
public class MatchedEntityReader implements ObservationReader<MatchedEntity> {

    private IdentifierInput in;

    public MatchedEntityReader(IdentifierInput identifierReader) {
        this.in = identifierReader;
    }

    @Override
    public MatchedEntity readObservation() throws IOException {
        try {
            return new MatchedEntity(in.read(), in.read());
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }
}
