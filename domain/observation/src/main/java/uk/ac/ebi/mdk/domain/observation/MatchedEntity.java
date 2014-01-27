/*
 * Copyright (c) 2014. EMBL, European Bioinformatics Institute
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

package uk.ac.ebi.mdk.domain.observation;

import uk.ac.ebi.mdk.domain.identifier.Identifier;

/**
 * Defines a entity to which this entry is matched.  This match only holds the
 * identifiers and so the reconstruction / entity may not be loaded or present
 * by the manager.
 *
 * @author John May
 */
public final class MatchedEntity extends AbstractObservation {

    final Identifier reconId, entityId;

    public MatchedEntity(Identifier reconId, Identifier entityId) {
        this.reconId = reconId;
        this.entityId = entityId;
    }

    /**
     * The reconstruction identifier.
     *
     * @return recon id
     */
    public Identifier reconId() {
        return reconId;
    }

    /**
     * The entity identifier.
     *
     * @return recon id
     */
    public Identifier entityId() {
        return entityId;
    }

    /** @inheritDoc */
    @Override public MatchedEntity getInstance() {
        return new MatchedEntity(null, null);
    }

    @Override public String toString() {
        return entityId.toString() + " (" + reconId.getAccession() + ")";
    }
}
