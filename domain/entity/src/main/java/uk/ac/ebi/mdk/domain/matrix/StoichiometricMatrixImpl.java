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
import com.google.common.collect.HashBiMap;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;


/**
 * StoichiometricMatrixImpl.java – MetabolicDevelopmentKit – Jun 26, 2011 Class
 * extends the abstract reaction matrix and stores the indices of which
 * reactions are reversible, irreversible or extracellular Note custom objects
 * should override the generic hashCode and equals methods
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public abstract class StoichiometricMatrixImpl<M, R>
        extends AbstractReactionMatrix<Double, M, R>
        implements StoichiometricMatrix<M, R> {

    private Map<Integer, Boolean> reversibilityMap = new HashMap<Integer, Boolean>();


    /**
     * {@inheritDoc}
     */
    protected StoichiometricMatrixImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    protected StoichiometricMatrixImpl(int n, int m) {
        super(n, m);
    }


    @Override
    public int addReaction(R reaction, M[] newMolecules, Double[] values) {


        int index = super.addReaction(reaction, newMolecules, values);
        reversibilityMap.put(index, true);
        return index;

    }


    public int addReaction(R reaction, M[] newMolecules, Double[] values, Boolean isReversible) {


        if (isReversible == null) {
            return this.addReaction(reaction, newMolecules, values);
        }
        int index = super.addReaction(reaction, newMolecules, values);

        reversibilityMap.put(index, isReversible);


        return index;
    }


    public Boolean isReversible(Integer j) {
        return reversibilityMap.get(j);
    }


    @Override
    public Double get(int i, int j) {
        Double value = super.get(i, j);
        return value == null ? 0 : value;
    }


    @Override
    public Class<? extends Double> getValueClass() {
        return Double.class;
    }


    /**
     * Assigns the values and sets all reactions reversibility to that present
     * in the 'other' matrix
     *
     * @param other
     *
     * @return
     */
    public BiMap<Integer, Integer> assign(StoichiometricMatrix<? extends M, ? extends R> other) {

        // ensure there is enough space
        this.ensure(getMoleculeCount() + other.getReactionCount(),
                    getReactionCount() + other.getReactionCount());

        BiMap<Integer, Integer> map = HashBiMap.create();

        /**
         * Need whole loop as the called to this addReaction(R,M[],T[]) will set
         * reversible to true (default) leading to clash on checking for
         * duplicate reactions
         */
        for (int j = 0; j < other.getReactionCount(); j++) {
            map.put(j, this.addReaction(other.getReaction(j),
                                        other.getReactionMolecules(j),
                                        other.getReactionValues(j),
                                        other.isReversible(j)));
        }


        return map;

    }


    @Override
    public void display(PrintStream stream, char separator) {
        super.display(stream, separator, "0", 4, 4);
    }


    /**
     * Assigns the values and sets all reactions to reversible
     *
     * @param other
     *
     * @return
     */
    @Override
    public BiMap<Integer, Integer> assign(AbstractReactionMatrix<Double, M, R> other) {

        if (other instanceof StoichiometricMatrixImpl) {
            return this.assign((StoichiometricMatrix<M, R>) other);
        }

        BiMap<Integer, Integer> map = super.assign(other);
        for (int j = 0; j < other.getReactionCount(); j++) {

            Integer key = map.get(j);

            if (!reversibilityMap.containsKey(key)) {
                reversibilityMap.put(key, true);
            }

        }
        return map;
    }


    /**
     * Create a new composite of s1 and s2
     */
    @SuppressWarnings("unchecked")
    public StoichiometricMatrix merge(StoichiometricMatrix<M, R> s1,
                                      StoichiometricMatrix<M, R> s2) {

        StoichiometricMatrix s = s1.newInstance(s1.getMoleculeCount() + s2.getMoleculeCount(),
                                                s1.getReactionCount() + s2.getReactionCount());

        for (int j = 0; j < s1.getReactionCount(); j++) {
            s.addReaction(s1.getReaction(j), s1.getReactionMolecules(j), s1.getReactionValues(j));
        }

        for (int j = 0; j < s2.getReactionCount(); j++) {
            s.addReaction(s2.getReaction(j), s2.getReactionMolecules(j), s2.getReactionValues(j));
        }


        return s;

    }


}
