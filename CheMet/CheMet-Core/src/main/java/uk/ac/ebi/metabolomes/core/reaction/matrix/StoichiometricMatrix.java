/*
 *     This file is part of Metabolic Network Builder
 *
 *     Metabolic Network Builder is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.metabolomes.core.reaction.matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


/**
 * StoichiometricMatrix.java – MetabolicDevelopmentKit – Jun 26, 2011 Class
 * extends the abstract reaction matrix and stores the indices of which
 * reactions are reversible, irreversible or extracellular Note custom objects
 * should override the generic hashCode and equals methods
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public abstract class StoichiometricMatrix<M, R>
        extends AbstractReactionMatrix<Double, M, R> {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(StoichiometricMatrix.class);

    private Map<Integer, ReactionDirection> reversibilityMap = new HashMap<Integer, ReactionDirection>();

    private HashSet<M> extraCellular = new HashSet<M>();


    public enum ReactionDirection {

        REVERSIBLE,
        IRREVERSIBLE,
        UNKNOWN
    }


    /**
     * {@inheritDoc}
     */
    protected StoichiometricMatrix() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    protected StoichiometricMatrix(int n, int m) {
        super(n, m);
    }


    @Override
    public boolean addReaction(R reaction, M[] newMolecules, Double[] values) {


        boolean modified = super.addReaction(reaction, newMolecules, values);
        if (modified) {
            int index = getReactionCount() - 1;
            reversibilityMap.put(index, ReactionDirection.UNKNOWN);
            // quick hack for extra-cellular metabolites
            if (values.length == 1) {
                extraCellular.add(newMolecules[0]);
            }
        }
        return modified;
    }


    public boolean addReaction(R reaction, M[] newMolecules, Double[] values, Boolean isReversible) {


        if (isReversible == null) {
            return this.addReaction(reaction, newMolecules, values);
        }
        boolean modified = super.addReaction(reaction, newMolecules, values);
        if (modified) {
            int index = getReactionCount() - 1;

            // quick hack for extra-cellular metabolites
            if (values.length == 1) {
                extraCellular.add(newMolecules[0]);
            }

            // add to reversibility sets the index of the last added reaction
            if (isReversible) {
                reversibilityMap.put(index, ReactionDirection.REVERSIBLE);
            } else if (isReversible == Boolean.FALSE) {
                reversibilityMap.put(index, ReactionDirection.IRREVERSIBLE);
            }
        }
        return modified;
    }


    public boolean isExtraCellular(M molecule) {
        return extraCellular.contains(molecule);
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
     * Merges this matrix with the specified other matrix
     *
     * @param other
     * @return
     */
    public StoichiometricMatrix<M, R> merge(StoichiometricMatrix<M, R> other) {

        List<Double> coefs = new ArrayList<Double>();
        List<M> localMols = new ArrayList<M>();

        for (int j = 0; j < other.getReactionCount(); j++) {

            Object[] tmpCoefs = other.getColumn(j);

            for (int i = 0; i < tmpCoefs.length; i++) {
                if (tmpCoefs[i] != null) {
                    coefs.add((Double) tmpCoefs[i]);
                    localMols.add(other.getMolecule(i));
                }
            }

            this.addReaction(other.getReaction(j),
                             (M[]) localMols.toArray(),
                             coefs.toArray(new Double[0]));

            coefs.clear();
            localMols.clear();
        }

        return this;

    }
}
