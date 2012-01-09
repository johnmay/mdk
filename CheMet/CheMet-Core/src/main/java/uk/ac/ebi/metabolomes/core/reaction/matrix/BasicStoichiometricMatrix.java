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

import java.util.Arrays;

/**
 * BasicStoichiometricMatrix.java – MetabolicDevelopmentKit – Jun 25, 2011
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public class BasicStoichiometricMatrix
        extends StoichiometricMatrix<String, String> {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(BasicStoichiometricMatrix.class);
    private Integer reactionCount = 0;

    public BasicStoichiometricMatrix() {
    }

    /**
     * {@inheritDoc}
     */
    public BasicStoichiometricMatrix(int n, int m) {
        super(n, m);
    }

    public boolean addReaction(String[] substrates,
                            String[] products) {
        String fluxChar = substrates.length == 0 || products.length == 0 ? "b" : "v";
        return addReaction(fluxChar + ++reactionCount, substrates, products);
    }

    public boolean addReaction(String rxn,
                            String[] substrates,
                            String[] products) {
        Double[] values = new Double[substrates.length + products.length];
        String[] molecules = new String[values.length];
        for (int i = 0; i < substrates.length; i++) {
            values[i] = -1d;
        }
        for (int i = substrates.length; i < values.length; i++) {
            values[i] = 1d;
        }
        System.arraycopy(substrates, 0, molecules, 0, substrates.length);
        System.arraycopy(products, 0, molecules, substrates.length, products.length);
        return addReaction(rxn, molecules, values);
    }

    public boolean addReaction(String reaction) {
        String[] compounds = reaction.split(" => ");
        return addReaction(compounds[0], compounds[1]);
    }

    public boolean addReactionWithName(String name, String reaction) {
        String[] compounds = reaction.split(" => ");
        return addReactionWithName(name, compounds[0], compounds[1]);
    }

    public boolean addReaction(String substrates, String products) {
        return addReaction(substrates.split(" \\+ "), products.split(" \\+ "));
    }
    public boolean addReactionWithName(String name, String substrates, String products) {
        return addReaction(name, substrates.split(" \\+ "), products.split(" \\+ "));
    }

    public static void main(String[] args) {


        BasicStoichiometricMatrix s = new BasicStoichiometricMatrix();

        // internal reactions
        s.addReaction("A => B");
        s.addReaction("B => C");
        s.addReaction("C => D");
        // exchange reactions
        s.addReaction(new String[]{}, new String[]{"A"});
        s.addReaction(new String[]{"D"}, new String[]{});


        s.display(System.out, ' ', "0", null, 2, 2);

        Object[][] sfixed = s.getFixedMatrix();
        for (int i = 0; i < sfixed.length; i++) {
            System.out.println(Arrays.asList(sfixed[i]));
        }

    }
}
