package uk.ac.ebi.mdk.domain.matrix;

/**
 * @author John May
 */
public interface ReactionMatrix<T,M,R> {

    public int getMoleculeCount();

    public int getReactionCount();

    public M getMolecule(Integer i);

    public R getReaction(Integer j);

    public T get(int i, int j);

    public int getNonNullCount();

}
