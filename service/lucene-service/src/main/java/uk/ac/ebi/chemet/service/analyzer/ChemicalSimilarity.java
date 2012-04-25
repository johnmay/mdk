package uk.ac.ebi.chemet.service.analyzer;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.DefaultSimilarity;

/**
 * @author John May
 */
public class ChemicalSimilarity extends DefaultSimilarity {

    @Override
    public float computeNorm(String field, FieldInvertState state) {
        return 1.0f / state.getLength();
    }
}
