/**
 * GenericReaction.java
 *
 * 2011.08.08
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.chemet.entities.reaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IMolecule;
import uk.ac.ebi.metabolomes.core.ObjectDescriptor;

/**
 * @name    GenericReaction
 *          2011.08.08
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @param <M> The molecule class type (IAtomContainer, String, etc. ) should overide hashCode/equals
 * @param <S> The stoichiometry class type (normally int or double) should overide hashCode/equals
 * @param <C> The compartment class type (can be a string e.g. [e] or Enumeration) overide hashCode/equals
 *          Generic reaction class allows the the specification of a reaction
 *          storing the molecule (M), stoichiometry (S) and compartment (C).
 *
 * Note that the molecule comparator is given in the constructor, this is because common molecule
 * object (i.e. IAtomContainer) does not implement comparable. We extends MetabolicReconstructionObject so
 * we can add annotations/observations to the reaction
 *
 */
public class GenericReaction<M , S extends Comparable , C extends Comparable>
        extends ObjectDescriptor implements Serializable {

    private static final Logger LOGGER = Logger.getLogger( GenericReaction.class );
    private static final long serialVersionUID = 8309040049214143031L;
    protected List<M> reactants;
    protected List<M> products;
    protected List<S> reactantStoichiometries;
    protected List<S> productStoichiometries;
    protected List<C> reactantCompartments;
    protected List<C> productCompartments;
    // revesibility
    protected ReactionReversibility reversibility = ReactionReversibility.UNKNOWN;
    // comparator used to compare
    private Comparator<M> moleculeComparator;
    //private boolean sorted = false;
    // flatterned arrays of all components
//    protected List<M> reactionParticipants;
//    protected List<S> reactionParticipantStoichiometries;
//    protected List<C> reactionParticipantCompartments;
    // should not persit between VM instances
    transient private Integer cachedHash = null;

    /**
     * Constructor for a generic reaction. The constructor must provide a comparator for
     * class of molecule used in the generic reaction. Ideally this should be a singleton class.
     * @param moleculeComparator
     */
    public GenericReaction( Comparator<M> moleculeComparator ) {
        this.moleculeComparator = moleculeComparator;
        reactants = new ArrayList<M>();
        products = new ArrayList<M>();
        reactantStoichiometries = new ArrayList<S>();
        productStoichiometries = new ArrayList<S>();
        reactantCompartments = new ArrayList<C>();
        productCompartments = new ArrayList<C>();
    }

    /**
     * Sets the reversibility of the reaction. By default the reaction reversibility is unknown.
     * The reversibility is not tested in the {@see equals(M} method as this method treats reactions
     * with same products (coefficients and compartments), different sides as being the same.
     * @param reversibility The reaction reversibility
     */
    public void setReversibility( ReactionReversibility reversibility ) {
        this.reversibility = reversibility;
    }

    /**
     * Accessor for the reversibility of the reaction
     * @return Reaction reversibility enumeration
     */
    public ReactionReversibility getReversibility() {
        return reversibility;
    }

    /**
     * Accessor for all the reactant compartments of the reaction
     * @return Unmodifiable collection of reactant compartments
     */
    public Collection<C> getReactantCompartments() {
        return Collections.unmodifiableList( reactantCompartments );
    }

    protected void addReactantCompartment( C reactantCompartment ) {
        this.reactantCompartments.add( reactantCompartment );
        this.cachedHash = null;
    }

    /**
     * Accessor for all the reactant coefficients of the reaction
     * @return Unmodifiable collection of reactant coefficients
     */
    public Collection<S> getReactantStoichiometries() {
        return Collections.unmodifiableList( reactantStoichiometries );
    }

    protected void addReactantStoichiometry( S reactantStoichiometry ) {
        this.reactantStoichiometries.add( reactantStoichiometry );
        this.cachedHash = null;
    }

    /**
     * Accessor for all the reactants of the reaction
     * @return Unmodifiable collection of reactants of class 'M'
     */
    public Collection<M> getReactantMolecules() {
        return Collections.unmodifiableList( reactants );
    }

    /**
     * Add a reactant (left side) to the reaction
     * @param product The reactant to add
     */
    public void addReactant( M reactant ) {
        this.reactants.add( reactant );
        this.cachedHash = null;
    }

    /**
     * Add a reactant (left side) and compartment to the reaction
     * @param product The reactant to add
     * @param coefficient The stoichiometric coefficient of the molecule
     */
    public void addReactant( M reactant , S stoichiometry ) {
        this.addReactant( reactant );
        this.addReactantStoichiometry( stoichiometry );
    }

    /**
     * Add a reactant (left side), stoichiometry and compartment to the reaction
     * @param product The reactant to add
     * @param coefficient The stoichiometric coefficient of the molecule
     * @param compartment The compart the molecule resides in
     */
    public void addReactant( M reactant , S stoichiometry , C compartment ) {
        this.addReactant( reactant );
        this.addReactantStoichiometry( stoichiometry );
        this.addReactantCompartment( compartment );
    }

    /**
     * Accessor for all the product compartments of the reaction
     * @return Unmodifiable collection of compartments
     */
    public Collection<C> getProductCompartments() {
        return Collections.unmodifiableList( productCompartments );
    }

    protected void addProductCompartment( C productCompartment ) {
        this.productCompartments.add( productCompartment );
        this.cachedHash = null;
    }

    /**
     * Accessor for all the product coefficients of the reaction
     * @return Unmodifiable collection of coefficients
     */
    public Collection<S> getProductStoichiometries() {
        return Collections.unmodifiableList( productStoichiometries );
    }

    protected void addProductStoichiometry( S productStoichiometry ) {
        this.productStoichiometries.add( productStoichiometry );
        this.cachedHash = null;
    }

    /**
     * Accessor for all the products of the reaction
     * @return Unmodifiable collection of products of class 'M'
     */
    public Collection<M> getProductMolecules() {
        return Collections.unmodifiableList( products );
    }

    /**
     * Add a product (right side) to the reaction
     * @param product The product to add
     */
    public void addProduct( M product ) {
        this.products.add( product );
        this.cachedHash = null;
    }

    /**
     * Add a product (right side) and it's stoichiometry to the reaction
     * @param product The product to add
     * @param coefficient The stoichiometric coefficient of the molecule
     */
    public void addProduct( M product , S coefficient ) {
        this.addProduct( product );
        this.addProductStoichiometry( coefficient );
    }

    /**
     * Add a product (right side), stoichiometry and compartment to the reaction
     * @param product The product to add
     * @param coefficient The stoichiometric coefficient of the molecule
     * @param compartment The compart the molecule resides in
     */
    public void addProduct( M product , S coefficient , C compartment ) {
        this.addProduct( product );
        this.addProductCompartment( compartment );
        this.addProductStoichiometry( coefficient );
    }

    /**
     * Accessor to all the reaction participants (molecules)
     * @return shallow copy combined list of all products (ordered reactant, product)
     */
    public List<M> getAllReactionParticipants() {
        List<M> allMolecules = new ArrayList<M>( getReactantMolecules() );
        allMolecules.addAll( getProductMolecules() );
        return allMolecules;
    }

    /**
     * Accessor to all the reaction compartments
     * @return shallow copy combined list of all coefficients (ordered reactant, product)
     */
    public List<S> getAllReactionCoefficients() {
        List<S> allMolecules = new ArrayList<S>( getReactantStoichiometries() );
        allMolecules.addAll( getProductStoichiometries() );
        return allMolecules;
    }

    /**
     * Accessor to all the reaction compartments
     * @return shallow copy combined list of all compartments (ordered reactant, product)
     */
    public List<C> getAllReactionCompartments() {
        List<C> allMolecules = new ArrayList<C>( getReactantCompartments() );
        allMolecules.addAll( getProductCompartments() );
        return allMolecules;
    }

    public int getReactantCount() {
        return getReactantMolecules().size();
    }

    public int getProductCount() {
        return getProductMolecules().size();
    }

    public S getReactantCoefficient( M m ) {
        return reactantStoichiometries.get( reactants.indexOf( m ) );
    }

    public S getProductCoefficient( M m ) {
        return productStoichiometries.get( products.indexOf( m ) );
    }

    /**
     * Calculates the hash code for the reaction in it's current state. As the molecules need to be sorted
     * this operation is more expensive and thus non-optimal. The hash is therefore cached in a the variable
     * this.cachedHash which is 'nullified' if the state of the object changes.
     * Warning: This hash code will not persist between different virtual machines if enumerations are used. However
     * as the hash code is meant to be calculated when needed this should not be a problem
     * @return hash code the reaction, note – there is no guarantee of unique hash code and the equals method should
     *         be called if the the matching hashCodes are found
     */
    @Override
    public int hashCode() {

        int hash = 5;

        // (re)calculate the hash if needed (i.e. there has been a state-change)
        if ( this.cachedHash == null ) {

            List<M> reacMols = getAllReactionParticipants();
            List<S> reacCoef = getAllReactionCoefficients();
            List<C> reacComp = getAllReactionCompartments();

            Collections.sort( reacMols , moleculeComparator );
            Collections.sort( reacCoef );
            Collections.sort( reacComp );

            /* default hashCode method is used the Coefficents and Compartments but
            the molecule has a specialise hashCode that allows it to be overridden
            in sub classes */

            hash = 59 * hash + ( !reacMols.isEmpty() ? hashCode( reacMols ) : 0 );
            hash = 59 * hash + ( !reacCoef.isEmpty() ? reacCoef.hashCode() : 0 );
            hash = 59 * hash + ( !reacComp.isEmpty() ? reacComp.hashCode() : 0 );

            // TODO: reduce collisions by calculating per-side then add then ordering the two hashes by size

            this.cachedHash = hash;

        }

        return this.cachedHash;

    }

    /**
     * Check equality of reactions based on state. Reactions are considered equal if their reactants and products
     * (coefficients and compartments) are equal regardless of the side they reside on. For example A + B <-> C + D is
     * considered equal to C + D <-> A + B. The participant stoichiometric coefficients and compartments are also checked.
     *
     * To accomplish this the reactionParticipants and copied, sorted and then a hash for each side (four total) is made.
     * Duplicate hashes are then removed via the {@See Set} interface and the query (this) and the other (obj) sides are
     * tested for coefficient, compartment and finally molecule similarity
     *
     * @param obj The other object to test equality with
     * @return Whether the objects are considered equal
     */
    @Override
    public boolean equals( Object obj ) {

        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final GenericReaction<M , S , C> other =
                                         ( GenericReaction<M , S , C> ) obj;


        /* Make shallow copies of compounds, coeffcients and compartments and sort */
        // query compounds
        List queryReactants = new ArrayList( this.reactants );
        List queryProducts = new ArrayList( this.products );
        Collections.sort( queryReactants , moleculeComparator );
        Collections.sort( queryProducts , moleculeComparator );
        // query coefs
        List queryReacCoef = new ArrayList( this.reactantStoichiometries );
        List queryProdCoef = new ArrayList( this.productStoichiometries );
        Collections.sort( queryReacCoef );
        Collections.sort( queryProdCoef );
        // query compartments
        List queryReacComp = new ArrayList( this.reactantCompartments );
        List queryProdComp = new ArrayList( this.productCompartments );
        Collections.sort( queryReacComp );
        Collections.sort( queryProdComp );

        // other compounds
        List otherReactants = new ArrayList( other.reactants );
        List otherProducts = new ArrayList( other.products );
        Collections.sort( otherReactants , moleculeComparator );
        Collections.sort( otherProducts , moleculeComparator );
        // other coefs
        List otherReacCoef = new ArrayList( other.reactantStoichiometries );
        List otherProdCoef = new ArrayList( other.productStoichiometries );
        Collections.sort( otherReacCoef );
        Collections.sort( otherProdCoef );
        // other compartments
        List otherReacComp = new ArrayList( other.reactantCompartments );
        List otherProdComp = new ArrayList( other.productCompartments );
        Collections.sort( otherReacComp );
        Collections.sort( otherProdComp );

        /* calculate the hashes for these all the reaction sides and flattern into a hashset to test the length */
        Integer queryReactantHash = hashCode( queryReactants ) + queryReacCoef.hashCode() + queryReacComp.hashCode();
        Integer queryProductHash = hashCode( queryProducts ) + queryProdCoef.hashCode() + queryProdComp.hashCode();
        Integer otherReactantHash = hashCode( otherReactants ) + otherReacCoef.hashCode() + otherReacComp.hashCode();
        Integer otherProductHash = hashCode( otherProducts ) + otherProdCoef.hashCode() + otherProdComp.hashCode();

        Set hashes = new HashSet<Integer>( Arrays.asList( queryReactantHash , queryProductHash ,
                                                          otherReactantHash , otherProductHash ) );

        /* Check the correct number of side */
        if ( hashes.size() != 2 ) {
            // not handling cases where reactants and products are the same.. could just be same hashcode
            if ( hashes.size() == 1 ) {
                for ( C c : this.getAllReactionCompartments() ) {
                        System.out.println( c + ":" + c.hashCode() );
                }

                throw new UnsupportedOperationException( "this.reactants == this.products " +
                                                         "&& other.reactants == other.products " +
                                                         "&& this.reactants == other.reactants" );
            }
            return false;
        }


        /* check the sides match */
        try {
            // this.reactants == other.reactants
            if ( queryReactantHash.equals( otherReactantHash ) ) {

                // checks the coef and compartments
                if ( queryReacCoef.retainAll( otherReacCoef ) ) {
                    return false;
                }
                if ( queryProdCoef.retainAll( otherProdCoef ) ) {
                    return false;
                }
                if ( queryReacComp.retainAll( otherReacComp ) ) {
                    return false;
                }
                if ( queryProdComp.retainAll( otherProdComp ) ) {
                    return false;
                }

                // checks the molecules
                return checkMolecules( queryReactants , otherReactants ) && checkMolecules( queryProducts ,
                                                                                            otherProducts );
            } // this.reactants == other.products and other.reactants == this.products
            else if ( queryReactantHash.equals( otherProductHash ) ) {

                // checks the coef and compartments
                if ( queryReacCoef.retainAll( otherProdCoef ) ) {
                    return false;
                }
                if ( queryProdCoef.retainAll( otherReacCoef ) ) {
                    return false;
                }
                if ( queryReacComp.retainAll( otherProdComp ) ) {
                    return false;
                }
                if ( queryProdComp.retainAll( otherReacComp ) ) {
                    return false;
                }

                // checks the molecules
                return checkMolecules( queryReactants , otherProducts ) && checkMolecules( queryProducts ,
                                                                                           otherReactants );
            } else {
                // shouldn't happen qReactants != oReactants && qReactants != oProducts... then hashes.size() > 2
                // can only be when qReactants == qProducts && oReactants == oProducts
                throw new UnsupportedOperationException(
                        "queryReactants != otherReactants && queryReactants != otherProducts ?" );
            }

        } catch ( Exception ex ) {
            LOGGER.error( "There was a problem checking individual molecule equality – assumbing reactions are different : " +
                          ex.getMessage() );
            return false;
        }

    }

    /**
     * Determines whether all the reference participants and equal to the query participants. This method iterates over
     * the sort participants and uses {@see moleculeEqual(M, M)} to determine equality.
     * @param queryParticipants
     * @param otherParticipants
     * @return Whether the participants are equal or not
     * @throws Exception
     */
    private boolean checkMolecules( List<M> queryParticipants ,
                                    List<M> otherParticipants ) throws Exception {

        if ( queryParticipants.size() != otherParticipants.size() ) {
            return false;
        }

        // these are sorted prior to call
        for ( int i = 0; i < queryParticipants.size(); i++ ) {

            // so if there is one not equal then we can return false
            if ( !moleculeEqual( queryParticipants.get( i ) , otherParticipants.get( i ) ) ) {
                return false;
            }
        }

        return true;
    }

    public boolean moleculeEqual( M query , M reference ) throws Exception {
        return query.equals( reference );
    }

    /**
     * Calculates the hash code for a molecule. By default this molecule calls the provided
     * molecules {@code hashCode()} method. This method is intended to be overridden by sub
     * classes that use an 'M' object that do not override the default {@code hashCode}
     * (e.g. CDK AtomContainer).
     * @param  molecule The molecule to calculate the hashCode for
     * @return hash code for the provided molecule
     */
    public int moleculeHashCode( M molecule ) {
        return molecule.hashCode();
    }

    /**
     * Private method that calls the {@see moleculeHashCode} on all molecules provided
     * @param molecules Molecules to calculate the hash code for
     * @return the combined hash
     */
    private int hashCode( List<M> molecules ) {
        int hash = 5;
        for ( M molecule : molecules ) {
            hash = 31 * hash + moleculeHashCode( molecule );
        }
        return hash;
    }

    /**
     * Displays the objects stored in the reaction in string form prefixed with the stoichiometric
     * coefficients and post fixed with compartments if either exists. The reaction reversibility is determined
     * by the enumeration value {@see ReactionReversibility} enumeration. Example representation:
     * <code>(1)A [e] + (1)B [c] <=?=> (1)C [c] + (1)A [c]</code>
     *
     *
     * @return textual representation of the reaction
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder( 40 );

        Iterator reit = reactants.iterator();
        Iterator rsit = reactantStoichiometries.iterator();
        Iterator rcit = reactantCompartments.iterator();

        while ( reit.hasNext() ) {
            if ( rsit.hasNext() ) {
                sb.append( '(' ).append( rsit.next() ).append( ')' );
            }
            sb.append( reit.next() );
            if ( rcit.hasNext() ) {
                sb.append( " [" ).append( rcit.next() ).append( "]" );
            }
            if ( reit.hasNext() ) {
                sb.append( " + " );
            }
        }

        sb.append( ' ' ).append( reversibility ).append( ' ' );

        Iterator prit = products.iterator();
        Iterator psit = productStoichiometries.iterator();
        Iterator pcit = productCompartments.iterator();

        while ( prit.hasNext() ) {
            if ( psit.hasNext() ) {
                sb.append( '(' ).append( psit.next() ).append( ')' );
            }
            sb.append( prit.next() );
            if ( pcit.hasNext() ) {
                sb.append( " [" ).append( pcit.next() ).append( "]" );
            }
            if ( prit.hasNext() ) {
                sb.append( " + " );
            }
        }

        return sb.toString();
    }
}
