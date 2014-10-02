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

package uk.ac.ebi.mdk.service;

import com.google.common.base.Joiner;
import uk.ac.ebi.mdk.domain.entity.reaction.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple container for bare-bones reaction information. There is no slot of
 * annotations, names etc and the participant information is store as strings.
 *
 * @author John May
 */
public final class ReactionDescription {

    private final List<MyParticipant> reactants = new ArrayList<MyParticipant>(3);
    private final List<MyParticipant> products = new ArrayList<MyParticipant>(3);
    private final Direction direction;

    /**
     * Create a new reaction with the given direction.
     *
     * @param direction the direction of the reaction
     */
    public ReactionDescription(Direction direction) {
        this.direction = direction;
    }

    /**
     * Add a reactant.
     *
     * @param compound    describe a compound (possibly accession but could be
     *                    name).
     * @param compartment describe a compartment ('c' or 'cytoplasm').
     * @param coefficient coefficient of the participant.
     */
    public void addReactant(String compound, String compartment, double coefficient) {
        reactants.add(new MyParticipant(compound, compartment, coefficient));
    }

    /**
     * Add a product.
     *
     * @param compound    describe a compound (possibly accession but could be
     *                    name).
     * @param compartment describe a compartment ('c' or 'cytoplasm').
     * @param coefficient coefficient of the participant.
     */
    public void addProduct(String compound, String compartment, double coefficient) {
        products.add(new MyParticipant(compound, compartment, coefficient));
    }

    public Iterable<MyParticipant> reactants() {
        return Collections.unmodifiableList(reactants);
    }

    public Iterable<MyParticipant> products() {
        return Collections.unmodifiableList(products);
    }

    /** Access reactant {@literal i}. */
    public MyParticipant reactant(int i) {
        return reactants.get(i);
    }

    /** Access product {@literal i}. */
    public MyParticipant product(int i) {
        return products.get(i);
    }

    /** Number of reactants. */
    public int nReactants() {
        return reactants.size();
    }

    /** Number of products. */
    public int nProducts() {
        return products.size();
    }

    /**
     * String representation of the reaction.
     *
     * @return a string representation
     */
    @Override public String toString() {
        return Joiner.on(" + ").join(reactants)
                + ' ' + direction + ' ' +
                Joiner.on(" + ").join(products);
    }

    /** Inner class for holding participants */
    public static class MyParticipant {
        private final String compound;
        private final String compartment;
        private final double coefficient;

        MyParticipant(String compound, String compartment, double coefficient) {
            this.compound = compound;
            this.compartment = compartment;
            this.coefficient = coefficient;
        }

        String compound() {
            return compound;
        }

        String compartment() {
            return compartment;
        }

        double coefficient() {
            return coefficient;
        }

        @Override public String toString() {
            return "(" + coefficient + ") " + compound + " [" + compartment + "]";
        }
    }
}
