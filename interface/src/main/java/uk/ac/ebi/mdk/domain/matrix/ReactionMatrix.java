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

package uk.ac.ebi.mdk.domain.matrix;

import java.util.Map;
import java.util.Set;

/**
 * @author John May
 */
public interface ReactionMatrix<T, M, R> {

    public int getMoleculeCount();

    public int getReactionCount();

    public M getMolecule(Integer i);

    public R getReaction(Integer j);

    public T get(int i, int j);

    public int getNonNullCount();

    public void ensure(int n, int m);

    public boolean setReaction(int J, R rxn);

    public boolean setMolecule(int I, M molecule);

    public boolean setValue(int I, int J, T value);

    public int addReaction(R rxn, M[] molecules, T[] values);
    
    public int addReaction(R rxn, M[] molecules, T[] values, boolean reversible);

    public T[][] getFixedMatrix();

    public Set<M> getHighlyConnectedMolecules(int threshold);

    public M[] getReactionMolecules(Integer j);

    public T[] getReactionValues(Integer i);

    public M[] getMolecules();

    public R[] getReactions();

    public Map<Integer, T> getReactions(M molecule);

    public Integer getIndex(M molecule);

    public ReactionMatrix<T, M, R> newInstance();

    public ReactionMatrix<T, M, R> newInstance(int moleculeCount, int reactionCount);

    public Set<Map.Entry<IndexKey,T>> entries();
    
    public static final class IndexKey {
        private final int i, j;

        public IndexKey(int i, int j) {
            this.i = i;
            this.j = j;
        }
        
        public int i() {
            return i;
        }
        
        public int j() {
            return j;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            IndexKey indexKey = (IndexKey) o;

            if (i != indexKey.i) return false;
            if (j != indexKey.j) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = i;
            result = 31 * result + j;
            return result;
        }

        @Override public String toString() {
            return i + ", " + j;
        }
    }
    
}
