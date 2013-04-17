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

import uk.ac.ebi.mdk.domain.entity.Reaction;
import uk.ac.ebi.mdk.domain.entity.reaction.Direction;
import uk.ac.ebi.mdk.domain.entity.reaction.Participant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * BasicStoichiometricMatrix.java – MetabolicDevelopmentKit – Jun 25, 2011
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public class BasicStoichiometricMatrix
        extends StoichiometricMatrixImpl<String, String> {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(BasicStoichiometricMatrix.class);

    private Integer reactionCount = 0;


    protected BasicStoichiometricMatrix() {
    }


    /**
     * {@inheritDoc}
     */
    protected BasicStoichiometricMatrix(int n, int m) {
        super(n, m);
    }


    @Override
    public Class<? extends String> getReactionClass() {
        return String.class;
    }


    @Override
    public Class<? extends String> getMoleculeClass() {
        return String.class;
    }


    @Override
    public BasicStoichiometricMatrix init() {
        return (BasicStoichiometricMatrix) super.init();
    }


    public static BasicStoichiometricMatrix create() {
        return new BasicStoichiometricMatrix().init();
    }


    public static BasicStoichiometricMatrix create(int n, int m) {
        return new BasicStoichiometricMatrix(n, m).init();
    }


    public int addReaction(String[] substrates,
                           String[] products) {
        return addReaction(substrates, products, true);
    }


    public int addReaction(String[] substrates,
                           String[] products,
                           boolean reversible) {
        String fluxChar = substrates.length == 0 || products.length == 0 ? "b" : "v";
        return addReaction(fluxChar + ++reactionCount, substrates, products, reversible);
    }


    public int addReaction(Reaction<? extends Participant<String, Double>> reaction) {

        List<String> ms = new ArrayList<String>();
        List<Double> cs = new ArrayList<Double>();

        for (Participant<String, Double> p : reaction.getReactants()) {
            ms.add(p.getMolecule());
            cs.add(p.getCoefficient());
        }
        for (Participant<String, Double> p : reaction.getProducts()) {
            ms.add(p.getMolecule());
            cs.add(p.getCoefficient());
        }

        Double[] coefficients = cs.toArray(new Double[0]);
        String[] metabolites = ms.toArray(new String[0]);


        for (int i = 0; i < reaction.getReactantCount(); i++) {
            coefficients[i] = -coefficients[i];
        }


        return addReaction(reaction.getName(),
                           metabolites,
                           coefficients,
                           ((Direction) reaction.getDirection()).isReversible());
    }


    public int addReaction(String rxn,
                           String[] substrates,
                           String[] products) {
        return addReaction(rxn, substrates, products, true);
    }


    public int addReaction(String rxn,
                           String[] substrates,
                           String[] products,
                           boolean revserible) {
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
        return addReaction(rxn, molecules, values, revserible);
    }


    public int addReaction(String reaction) {
        return addReaction(reaction, true);
    }


    public int addReaction(String reaction, boolean reversible) {
        String[] compounds = reaction.split(" => ");
        return addReaction(compounds[0], compounds[1], reversible);
    }


    public int addReactionWithName(String name, String reaction) {
        String[] compounds = reaction.split(" => ");
        return addReactionWithName(name, compounds[0], compounds[1]);
    }

    public int addProduction(String compound, boolean reversible) {
        return addReaction(new String[0], new String[]{compound}, reversible);
    }

    public int addConsumption(String compound, boolean reversible) {
        return addReaction(new String[]{compound}, new String[0], reversible);
    }

    public int addReaction(String substrates, String products) {
        return addReaction(substrates, products, true);
    }


    public int addReaction(String substrates, String products, boolean reversible) {
        return addReaction(substrates.split(" \\+ "), products.split(" \\+ "), reversible);
    }


    public int addReactionWithName(String name, String substrates, String products) {
        return addReaction(name, substrates.split(" \\+ "), products.split(" \\+ "));
    }


    @Override
    public BasicStoichiometricMatrix newInstance() {
        return BasicStoichiometricMatrix.create();
    }


    @Override
    public BasicStoichiometricMatrix newInstance(int n, int m) {
        return BasicStoichiometricMatrix.create(n, m);
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


        s.display(System.out, ' ', "0", 2, 2);

        Object[][] sfixed = s.getFixedMatrix();
        for (int i = 0; i < sfixed.length; i++) {
            System.out.println(Arrays.asList(sfixed[i]));
        }

    }
}
