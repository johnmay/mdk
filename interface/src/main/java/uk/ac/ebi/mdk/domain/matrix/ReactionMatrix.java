/*
 * Copyright (C) 2012  John May and Pablo Moreno
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
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

}
