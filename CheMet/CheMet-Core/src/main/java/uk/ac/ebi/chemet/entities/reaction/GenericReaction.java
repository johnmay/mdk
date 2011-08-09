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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
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

            hash = 59 * hash + ( reacMols != null ? hashCode( reacMols ) : 0 );
            hash = 59 * hash + ( reacCoef != null ? reacCoef.hashCode() : 0 );
            hash = 59 * hash + ( reacComp != null ? reacComp.hashCode() : 0 );

            this.cachedHash = hash;

        }

        return this.cachedHash;

    }

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

        // just need shallow copies
        // this compounds
        List thisReactantCopy = new ArrayList( this.reactants );
        List thisProductCopy = new ArrayList( this.products );
        Collections.sort( thisReactantCopy , moleculeComparator );
        Collections.sort( thisProductCopy , moleculeComparator );
        // this coefs
        List thisReStCopy = new ArrayList( this.reactantStoichiometries );
        List thisPrStCopy = new ArrayList( this.productStoichiometries );
        Collections.sort( thisReStCopy );
        Collections.sort( thisPrStCopy );

        //other compounds
        List otherReactantCopy = new ArrayList( other.reactants );
        List otherProductCopy = new ArrayList( other.products );
        Collections.sort( otherReactantCopy , moleculeComparator );
        Collections.sort( otherProductCopy , moleculeComparator );
        //other coefs
        List otherReStCopy = new ArrayList( other.reactantStoichiometries );
        List otherPrStCopy = new ArrayList( other.productStoichiometries );
        Collections.sort( otherReStCopy );
        Collections.sort( otherPrStCopy );

        Integer[] thisMolHashes =
                  new Integer[]{ hashCode( thisReactantCopy ) + ( thisReStCopy != null ?
                                                                  thisReStCopy.hashCode() : 0 ) ,
                                 hashCode( thisProductCopy ) + ( thisPrStCopy != null ?
                                                                 thisPrStCopy.hashCode() : 0 )
        };

        Integer[] otherMolHashes =
                  new Integer[]{ hashCode( otherReactantCopy ) +
                                 ( otherReStCopy != null ? otherReStCopy.hashCode() : 0 ) ,
                                 hashCode( otherProductCopy ) + ( otherPrStCopy != null ?
                                                                  otherPrStCopy.hashCode() : 0 )
        };

        HashSet<Integer> collapsed = new HashSet<Integer>();
        collapsed.add( thisMolHashes[0] );
        collapsed.add( thisMolHashes[1] );
        collapsed.add( otherMolHashes[0] );
        collapsed.add( otherMolHashes[1] );

        // there are more then 2 sides
        if ( collapsed.size() > 2 ) {
            System.out.println( thisMolHashes[1] + " " + otherMolHashes[0] );
            return false;
        } else if ( collapsed.size() == 1 ) {
            // left and right side are the same? in both...
            throw new UnsupportedOperationException( "Reaction sides are the same!, this is not handled yet" );
        }


        try {

            // probably only need to check that [0] == [0] once but just be safe we check all
            // check for actual mathces
            // this.reactants == other.reactants
            if ( thisMolHashes[0].equals( otherMolHashes[0] ) ) {
                return checkMolecules( thisReactantCopy , otherReactantCopy ) && checkMolecules( thisProductCopy ,
                                                                                                 otherProductCopy );
            } // this.reactants == other.products and other.reactants == this.products
            else if ( thisMolHashes[0].equals( otherMolHashes[1] ) ) {

                return checkMolecules( thisReactantCopy , otherProductCopy ) && checkMolecules( thisProductCopy ,
                                                                                                otherReactantCopy );
            } else {
                LOGGER.error( "There was a problem! when checking equality" );
            }

        } catch ( Exception ex ) {
            LOGGER.error( "There was a problem check individual molecule equality – assumbing reactions are different" );
            return false;
        }


        return true;
    }

    private boolean checkMolecules( List<M> side1 ,
                                    List<M> side2 ) throws Exception {

        if ( side1.size() != side2.size() ) {
            return false;
        }

        int count = 0;

        // these are sorted prior to callage
        for ( int i = 0; i < side1.size(); i++ ) {
            if ( moleculeEqual( side1.get( i ) , side2.get( i ) ) ) {
                count++;
            }
        }

        return count == side1.size();
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
