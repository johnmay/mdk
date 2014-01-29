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
import com.google.common.collect.Sets;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


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

    private Set<Integer> reversible = new HashSet<Integer>();


    /** {@inheritDoc} */
    protected StoichiometricMatrixImpl() {
        super();
    }


    /** {@inheritDoc} */
    protected StoichiometricMatrixImpl(int n, int m) {
        super(n, m);
    }


    @Override
    public int addReaction(R reaction, M[] newMolecules, Double[] values) {


        int index = super.addReaction(reaction, newMolecules, values);
        reversible.add(index);
        return index;

    }

    @Override
    public int addReaction(R reaction, M[] molecule, Double[] coefs, boolean isReversible) {

        // fix duplicate entries
        Map<M, Double> unique = new HashMap<M, Double>();
        for (int i = 0; i < molecule.length; i++) {
            Double prev = unique.put(molecule[i], coefs[i]);
            if (prev != null) {
                unique.put(molecule[i], prev + coefs[i]);
            }
        }

        int n = 0;
        for (Map.Entry<M, Double> e : unique.entrySet()) {
            molecule[n] = e.getKey();
            coefs[n] = e.getValue();
            n++;
        }

        if (n != molecule.length) {
            molecule = Arrays.copyOf(molecule, n);
            coefs = Arrays.copyOf(coefs, n);
        }

        int index = super.addReaction(reaction, molecule, coefs, isReversible);

        // now update the value -> note the new value replaces any old reversibility
        if (isReversible) 
            reversible.add(index);
        else 
            reversible.remove(index);

        return index;
    }


    @Override
    public Boolean isReversible(Integer j) {
        return reversible.contains(j);
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

            if (!reversible.contains(key)) {
                reversible.add(key);
            }

        }
        return map;
    }


    /** Create a new composite of s1 and s2 */
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

    @Override boolean identical(int j, M[] ms1, Double[] vs1, M[] ms2, Double[] vs2, boolean reversible) {

        Set<M> ms1_1 = Sets.newHashSetWithExpectedSize(ms1.length);
        Set<M> ms1_2 = Sets.newHashSetWithExpectedSize(ms1.length);

        Set<M> ms2_1 = Sets.newHashSetWithExpectedSize(ms2.length);
        Set<M> ms2_2 = Sets.newHashSetWithExpectedSize(ms2.length);

        for (int i = 0; i < ms1.length; i++) {
            if (vs1[i] < 0)
                ms1_1.add(ms1[i]);
            else
                ms1_2.add(ms1[i]);
        }

        for (int i = 0; i < ms2.length; i++) {
            if (vs2[i] < 0)
                ms2_1.add(ms2[i]);
            else
                ms2_2.add(ms2[i]);
        }

        if (ms1_1.equals(ms2_1) && ms1_2.equals(ms2_2))
            return true;

        if ((isReversible(j) || reversible) && ms1_1.equals(ms2_2) && ms1_2.equals(ms2_1)) {
            if (!reversible) {
                // update coefficients - it's going to be overwritten as an
                // irreversible reaction. We need to make sure the negative
                // 'consuming' is on the correct side
                for (M m : ms1) {
                    int i = getIndex(m);
                    setValue(i, j, - _get(i, j));
                }
            }
            return true;
        }

        return false;
    }
}
