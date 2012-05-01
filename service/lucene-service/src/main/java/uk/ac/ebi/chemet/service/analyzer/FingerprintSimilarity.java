package uk.ac.ebi.chemet.service.analyzer;

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
