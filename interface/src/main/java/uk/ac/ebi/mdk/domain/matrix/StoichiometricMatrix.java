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

import com.google.common.collect.BiMap;

public interface StoichiometricMatrix<M, R> extends ReactionMatrix<Double, M, R> {

    public Boolean isReversible(Integer j);
    
    public void setReversible(int j, boolean reversible);

    public BiMap<Integer, Integer> assign(StoichiometricMatrix<? extends M, ? extends R> other);

    @Override
    public StoichiometricMatrix<M, R> newInstance();

    @Override
    public StoichiometricMatrix<M, R> newInstance(int moleculeCount, int reactionCount);

}
