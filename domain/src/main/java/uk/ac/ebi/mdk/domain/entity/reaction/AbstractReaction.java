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
package uk.ac.ebi.mdk.domain.entity.reaction;

import com.google.common.base.Joiner;
import org.apache.commons.lang.mutable.MutableInt;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.AbstractAnnotatedEntity;
import uk.ac.ebi.mdk.domain.entity.Reaction;
import uk.ac.ebi.mdk.domain.entity.reaction.filter.AbstractParticipantFilter;
import uk.ac.ebi.mdk.domain.entity.reaction.filter.AcceptAllFilter;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.util.*;


/**
 * AbstractReaction –  2011.08.08 <br>
 * <p/>
 * A base reaction class that allows the specification of different
 * types of participant. Two important participant types are
 * {@see Participant} and {@see CompartmentalisedParticipant}. Due
 * to the verboseness of the later when provided with the three
 * parameter types like {@see MetabolicParticipant} are usable. <br><br>
 * <p/>
 * {@code Reaction<Participant<String,Integer>> rxn = new AbstractReaction<Participant<String,Integer>>(); } <br><br>
 * {@code rxn.addReactant(new ParticipantImplementation<String, Integer>("NAD+", 1));                        } <br>
 * {@code rxn.addReactant(new ParticipantImplementation<String, Integer>("primary-alcohol", 1));             } <br><br>
 * {@code rxn.addProduct(new ParticipantImplementation<String, Integer>("NADH", 1));                         } <br>
 * {@code rxn.addProduct(new ParticipantImplementation<String, Integer>("H+", 1));                           } <br>
 * {@code rxn.addProduct(new ParticipantImplementation<String, Integer>("aldehyde", 1));                     } <br>
 * <p/>
 * <br>
 * <p/>
 * If you intend to use this library's {@see Metabolite} and
 * {@see Compartment} implementations please use the concrete
 * class {@see MetabolicReaction}.
 * <p/>
 * <pre>
 *
 *          </pre>
 *
 * @param <P> Reaction participant type
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class AbstractReaction<P extends Participant>
        extends AbstractAnnotatedEntity
        implements Reaction<P> {

    private static final Logger LOGGER = Logger.getLogger(AbstractReaction.class);

    // flags whether the reaction is generic or not
    transient private Boolean generic = false;
    // new class

    private List<P> reactants;

    private List<P> products;
    // whether the reaction is reversible

    private Direction direction = Direction.BIDIRECTIONAL;

    private AbstractParticipantFilter filter = new AcceptAllFilter(); // accepts all


    /**
     * Constructor for a generic reaction. The constructor must provide a comparator for
     * class of molecule used in the generic reaction. Ideally this should be a singleton class.
     *

     */
    public AbstractReaction() {
        reactants = new LinkedList<P>();
        products = new LinkedList<P>();
    }


    public AbstractReaction(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
        reactants = new LinkedList<P>();
        products = new LinkedList<P>();
    }


    /**
     * Constructor to build a reaction and use the provided filter to trim down included molecules.
     * Note – the reaction doesn't need a new instance of the filter every-time and it is better to
     * provide all required reactions with the same filter
     *
     * @param filter The filter to use
     */
    public AbstractReaction(AbstractParticipantFilter filter) {
        this();
        this.filter = filter;
    }


    /**
     * Sets the reversibility of the reaction. By default the reaction reversibility is unknown.
     * The reversibility is not tested in the {@see equals(M} method as this method treats reactions
     *with same products (coefficients and compartments), different sides as being the same.
     *
     * @param reversibility The reaction reversibility
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }


    /**
     * Get the direction of the reaction
     *
     * @return Reaction reversibility enumeration
     */
    public Direction getDirection() {
        return direction;
    }


    //    /**
    //     * Accessor for all the reactant compartments of the reaction
    //     * @return Fixed size array of reactant compartments
    //     */
    //    public List<C> getReactantCompartments() {
    //        List<C> compartments = new ArrayList();
    //        for (CompartmentalisedParticipant<M, S, C> p : getReactantParticipants()) {
    //            compartments.add(p.getCompartment());
    //        }
    //        return compartments;
    //    }
    //
    //
    //    /**
    //     * Accessor for all the reactant coefficients of the reaction
    //     * @return Fixed size array of reactant coefficients
    //     */
    public List getReactantStoichiometries() {
        List coefficients = new ArrayList();
        for (P p : getReactantParticipants()) {
            coefficients.add(p.getCoefficient());
        }
        return coefficients;
    }
    //
    //


    /**
     * Accessor for all the reactants of the reaction
     *
     * @return Fixed size array of reactants of class 'M'
     */
    public List getReactantMolecules() {
        List molecules = new ArrayList();
        for (Participant p : getReactantParticipants()) {
            molecules.add(p.getMolecule());
        }
        return molecules;
    }


    public List<P> getReactantParticipants() {
        return Collections.unmodifiableList(reactants);
    }


    public List<P> getReactants() {
        return Collections.unmodifiableList(reactants);
    }


    /**
     * Add a reactant (left side) to the reaction
     *
     * @param participant The reactant to add
     */
    public boolean addReactant(P participant) {
        //        if (filter.reject(participant)) {
        //            return;
        //        }
        return this.reactants.add(participant);
    }


    //    /**
    //     * Accessor for all the product compartments of the reaction
    //     * @return Fixed size array of compartments
    //     */
    //    public List<C> getProductCompartments() {
    //        List<C> compartments = new ArrayList();
    //        for (CompartmentalisedParticipant<M, S, C> p : getProductParticipants()) {
    //            compartments.add(p.getCompartment());
    //        }
    //        return compartments;
    //    }
    //
    //
    //    /**
    //     * Accessor for all the product coefficients of the reaction
    //     * @return Fixed size array of coefficients
    //     */
    public List getProductStoichiometries() {
        List coefficients = new ArrayList();
        for (P p : getProductParticipants()) {
            coefficients.add(p.getCoefficient());
        }
        return coefficients;
    }

    //

    /**
     * Accessor for all the products of the reaction
     *
     * @return Fixed size array of products of class 'M'
     */
    public List getProductMolecules() {
        List molecules = new ArrayList();
        for (Participant p : getProductParticipants()) {
            molecules.add(p.getMolecule());
        }
        return molecules;
    }


    public List<P> getProductParticipants() {
        return Collections.unmodifiableList(products);
    }


    public List<P> getProducts() {
        return Collections.unmodifiableList(products);
    }


    public List<P> getParticipants() {
        List<P> participants = new ArrayList<P>();
        participants.addAll(getReactantParticipants());
        participants.addAll(getProductParticipants());
        return participants;
    }


    /**
     * Add a product (right side) to the reaction
     *
     * @param participant The product to add
     */
    public boolean addProduct(P participant) {
        //        if (filter.reject(participant)) {
        //            return;
        //        }
        return this.products.add(participant);
    }


    /**
     * Accessor to all the reaction molecules
     *
     * @return shallow copy combined list of all products (ordered reactant, product)
     */
    public List getAllReactionMolecules() {
        List allMolecules = new ArrayList(getReactantMolecules());
        allMolecules.addAll(getProductMolecules());
        return allMolecules;
    }


    /**
     * Accessor to all the reaction participants
     *
     * @return shallow copy combined list of all products (ordered reactant, product)
     */
    public List<P> getAllReactionParticipants() {
        List<P> participants = new ArrayList<P>(reactants);
        participants.addAll(products);
        return participants;
    }


    /**
     * Accessor to all the reaction compartments
     *
     * @return shallow copy combined list of all coefficients (ordered reactant, product)
     */
    public List getAllReactionCoefficients() {
        List allMolecules = new ArrayList(getReactantStoichiometries());
        allMolecules.addAll(getProductStoichiometries());
        return allMolecules;
    }

    //
    //    /**
    //     * Accessor to all the reaction compartments
    //     * @return shallow copy combined list of all compartments (ordered reactant, product)
    //     */
    //    public List<C> getAllReactionCompartments() {
    //        List<C> allMolecules = new ArrayList<C>(getReactantCompartments());
    //        allMolecules.addAll(getProductCompartments());
    //        return allMolecules;
    //    }

    /**
     * Accessor for the number of reactants
     *
     * @return
     */
    public int getReactantCount() {
        return reactants.size();
    }


    /**
     * Accessor for the number of products
     *
     * @return
     */
    public int getProductCount() {
        return products.size();
    }


    public int getParticipantCount() {
        return getReactantCount() + getProductCount();
    }


    /**
     * Accessor to query whether the reaction is generic
     *
     * @return t/f
     */
    public Boolean isGeneric() {
        return generic;
    }


    /**
     * Indicate that this reaction contains GenericMolecules. This influences the
     * {@see equals(Object obj)} method into taking longer
     *
     * @param generic
     */
    public void setGeneric(Boolean generic) {
        this.generic = generic;
    }


    /**
     * Transposes the sides of the reaction. The method switches the reactants
     * for products and the products for reactants
     */
    public void transpose() {
        // transpose all the lists
        List tmp = reactants;
        reactants = products;
        products = tmp;
    }


    public Reaction newInstance() {
        return new AbstractReaction();
    }

    private Map<Integer, MutableInt> map = new HashMap<Integer, MutableInt>();


    /**
     * Calculates the hash code for the reaction in it's current state. As the molecules need to be sorted
     * this operation is more expensive and thus non-optimal. The hash is therefore cached in a the variable
     * this.cachedHash which is 'nullified' if the state of the object changes.
     * Warning: This hash code will not persist between different virtual machines if enumerations are used. However
     * as the hash code is meant to be calculated when needed this should not be a problem
     *
     * @return hash code the reaction, note – there is no guarantee of unique hash code and the equals method should
     *         be called if the the matching hashCodes are found
     */
    @Override
    public int hashCode() {

        int hash = 257;

        map.clear();
        map.put(hash, new MutableInt());

        for (Participant p : reactants) {
            int value = p.hashCode();
            if (!map.containsKey(value)) {
                map.put(value, new MutableInt());
            } else {
                map.get(value).increment();
            }
            hash ^= rotate(value, map);
            if (!map.containsKey(hash)) {
                map.put(hash, new MutableInt());
            } else {
                map.get(hash).increment();
            }

        }
        for (Participant p : products) {
            int value = p.hashCode();
            if (!map.containsKey(value)) {
                map.put(value, new MutableInt());
            } else {
                map.get(value).increment();
            }
            hash ^= rotate(value, map);
            if (!map.containsKey(hash)) {
                map.put(hash, new MutableInt());
            } else {
                map.get(hash).increment();
            }
        }

        //        List reac = new ArrayList(this.reactants);
        //        List prod = new ArrayList(this.products);
        //
        //        Collections.sort(reac);
        //        Collections.sort(prod);
        //
        //        Integer[] hashes = new Integer[]{reac.hashCode(),
        //                                         prod.hashCode()};
        //
        //        Arrays.sort(hashes);
        //
        //        hash = 59 * hash + hashes[0];
        //        hash = 59 * hash + hashes[1];

        return hash;

    }


    /**
     * Check equality of reactions based on state. Reactions are considered equals if their reactants and products
     * (coefficients and compartments) are equals regardless of the side they reside on. For example A + B <-> C + D is
     * considered equals to C + D <-> A + B. The participant stoichiometric coefficients and compartments are also
     * checked.
     * <p/>
     * To accomplish this the reactionParticipants and copied, sorted and then a hash for each side (four total) is
     * made.
     * Duplicate hashes are then removed via the {@See Set} interface and the query (this) and the other (obj) sides
     * are
     * tested for coefficient, compartment and finally molecule similarity
     *
     * @param obj The other object to test equality with
     *
     * @return Whether the objects are considered equals
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractReaction<P> other = (AbstractReaction<P>) obj;

        /* Make shallow copies of compounds, coeffcients and compartments and sort */
        // query compounds
        List<P> queryReactants = new ArrayList(this.reactants);
        List<P> queryProducts = new ArrayList(this.products);
        Collections.sort(queryReactants);
        Collections.sort(queryProducts);

        // other compounds
        List<P> otherReactants = new ArrayList(other.reactants);
        List<P> otherProducts = new ArrayList(other.products);
        Collections.sort(otherReactants);
        Collections.sort(otherProducts);

        if (this.generic == false && other.generic == false) {

            /* calculate the hashes for these all the reaction sides and flattern into a hashset to test the length */
            Set hashes = new HashSet<Integer>(Arrays.asList(queryReactants.hashCode(),
                                                            queryProducts.hashCode(),
                                                            otherReactants.hashCode(),
                                                            otherProducts.hashCode()));


            /* Check the correct number of side */
            if (hashes.size() != 2) {
                // not handling cases where reactants and products are the same.. could just be same hashcode
                if (hashes.size() == 1) {
                    if (queryReactants.equals(otherReactants) && queryReactants.equals(
                            otherReactants)
                            && queryProducts.equals(otherProducts)) {
                        return true;
                    }
                    throw new UnsupportedOperationException("Reaction.equals(): Unresolvable reaction comparision [1]\n\t"
                                                                    + this + "\n\t" + other);
                }
                return false;
            }

            // these are sorted so can do direct equals on the sets
            if (queryReactants.hashCode() == otherReactants.hashCode()) {
                return queryReactants.equals(otherReactants) && queryProducts.equals(otherProducts);
            } else if (queryReactants.hashCode() == otherProducts.hashCode()) {
                return queryReactants.equals(otherProducts) && queryProducts.equals(otherReactants);
            } else {
                return false; // this.reac == this.prod and other.reac == other.prod... and so must be false (2x hashe values)
            }

        } else {
            LOGGER.info("Using generic comparisson");
            // XXX May be a quicker way but for not this works
            if (genericEquals(queryReactants, otherReactants) && genericEquals(queryProducts,
                                                                               otherProducts)) {
                return true;
            }
            if (genericEquals(queryReactants, otherProducts) && genericEquals(queryProducts,
                                                                              otherReactants)) {
                return true;
            }
        }

        return false;

    }


    /**
     * A simple hack that checks will be improved in future. Checks whether the unsorted lists are matches
     *
     * @param query
     * @param other
     *
     * @return
     */
    private boolean genericEquals(List<P> query, List<P> other) {

        if (query.size() != other.size()) {
            return false;
        }

        for (P p : query) {

            if (containedIn(other, p) == Boolean.FALSE) {
                return false;
            }

            other.remove(p);
        }

        return true;

    }


    /**
     *
     * Rotates the seed if the seed has already been seen in the provided
     * occurrences map
     *
     * @param seed
     * @param occurences
     * @return
     */
    public static int rotate(int seed, Map<Integer, MutableInt> occurences) {
        if (occurences.get(seed) == null) {
            occurences.put(seed, new MutableInt());
        } else {
            occurences.get(seed).increment();
        }
        // System.out.printf("%10s", occMap.get(seed).value);
        return rotate(seed, occurences.get(seed).intValue());
    }

    /**
     *
     * Rotates the seed using xor shift (pseudo random number generation) the
     * specified number of times.
     *
     * @param seed the starting seed
     * @param rotation Number of xor rotations to perform
     * @return The starting seed rotated the specified number of times
     */
    public static int rotate(int seed, int rotation) {
        for (int j = 0; j < rotation; j++) {
            seed = xorShift(seed);
        }
        return seed;
    }


    public static int xorShift(int seed) {
        seed ^= seed << 6;
        seed ^= seed >>> 21;
        seed ^= (seed << 7);
        return seed;
    }

    public Boolean containedIn(List<P> list, P other) {
        for (P p : list) {
            if (other.equals(p)) {
                return true;
            }
        }
        // return list.contains(this); // this doens't work for some reason
        return false;
    }


    /**
     * Displays the objects stored in the reaction in string form prefixed with the stoichiometric
     * coefficients and post fixed with compartments if either exists. The reaction reversibility is determined
     * by the enumeration value {@see Reversibility} enumeration. Example representation:
     * <code>(1)A [e] + (1)B [c] <=?=> (1)C [c] + (1)A [c]</code>     *
     *
     * @return textual representation of the reaction
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(40);

        sb.append(Joiner.on(" + ").join(reactants)).append(' ').append(direction).append(' ').
                append(Joiner.on(" + ").join(products));


        return sb.toString();
    }


    public void clear() {
        reactants.clear();
        products.clear();
    }
}
