package uk.ac.ebi.mdk.domain.matrix;

public interface StoichiometricMatrix<M,R> extends ReactionMatrix<Double,M,R> {

    public Boolean isReversible(Integer j);
}
