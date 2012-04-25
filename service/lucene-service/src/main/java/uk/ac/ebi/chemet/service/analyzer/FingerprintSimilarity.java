package uk.ac.ebi.chemet.service.analyzer;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.DefaultSimilarity;
import static uk.ac.ebi.service.query.StructureService.FINGERPRINT_BIT;

/**
 * @author John May
 */
public class FingerprintSimilarity extends DefaultSimilarity {

    @Override
    public float computeNorm(String field, FieldInvertState state) {
        return field.equals(FINGERPRINT_BIT.field()) ? 1.0f / state.getLength() : super.computeNorm(field, state);
    }

}
