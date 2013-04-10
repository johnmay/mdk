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

package uk.ac.ebi.mdk.service.analyzer;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.DefaultSimilarity;

import static uk.ac.ebi.mdk.service.query.structure.StructureService.FINGERPRINT_BIT;

/**
 * @author John May
 */
public class FingerprintSimilarity extends DefaultSimilarity {

    @Override
    public float computeNorm(String field, FieldInvertState state) {
        return isFingerprintField(field) ? fingerprintNorm(state) : super.computeNorm(field, state);
    }

    @Override
    public float idf(int docFreq, int numDocs) {
        return 1.0f; // we don't want higher contribution from rare bit's
    }

    private boolean isFingerprintField(String field) {
        return field.equals(FINGERPRINT_BIT.field());
    }

    private float fingerprintNorm(FieldInvertState state) {
        return state.getBoost() * (1.0f / state.getLength());
    }


}
