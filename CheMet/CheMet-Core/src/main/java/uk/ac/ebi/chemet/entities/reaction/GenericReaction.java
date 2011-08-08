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

import java.lang.String;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ebi.metabolomes.core.MetabolicReconstructionObject;

/**
 * @name    GenericReaction
 * @date    2011.08.08
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   Generic reaction class allows the the specification of a reaction
 *          storing the molecule (M), stoichiometry (S) and compartment (C).
 * @param <M> The molecule class type (IAtomContainer, String, etc. ) should overide hashCode/equals
 * @param <S> The stoichiometry class type (normally int or double) should overide hashCode/equals
 * @param <C> The compartment class type (can be a string e.g. [e] or Enumeration) overide hashCode/equals
 *
 * Note that the molecule comparator is given in the constructor, this is because common molecule
 * object (i.e. IAtomContainer) does not implement comparable. We extends MetabolicReconstructionObject so
 * we can add annotations/observations to the reaction
 *
 */
public class GenericReaction<M , S extends Comparable , C extends Comparable>
        extends MetabolicReconstructionObject {

    private static final Logger LOGGER = Logger.getLogger( GenericReaction.class );
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
    private boolean sorted = false;
    // flatterned arrays of all components
    protected List<M> reactionParticipants;
    protected List<S> reactionParticipantStoichiometries;
    protected List<C> reactionParticipantCompartments;

    public GenericReaction( Comparator<M> moleculeComparator ) {
        this.moleculeComparator = moleculeComparator;
        reactionParticipants = new ArrayList<M>();
        reactionParticipantStoichiometries = new ArrayList<S>();
        reactionParticipantCompartments = new ArrayList<C>();
        reactants = new ArrayList<M>();
        products = new ArrayList<M>();
        reactantStoichiometries = new ArrayList<S>();
        productStoichiometries = new ArrayList<S>();
        reactantCompartments = new ArrayList<C>();
        productCompartments = new ArrayList<C>();

    }

    public void setReversibility( ReactionReversibility reversibility ) {
        this.reversibility = reversibility;
    }

    public ReactionReversibility getReversibility() {
        return reversibility;
    }

    public Collection<C> getReactantCompartments() {
        return Collections.unmodifiableList( reactantCompartments );
    }

    protected void addReactantCompartment( C reactantCompartment ) {
        this.reactantCompartments.add( reactantCompartment );
        this.reactionParticipantCompartments.add( reactantCompartment );
        sorted = false;

    }

    public Collection<S> getReactantStoichiometries() {
        return Collections.unmodifiableList( reactantStoichiometries );
    }

    protected void addReactantStoichiometry( S reactantStoichiometry ) {
        this.reactantStoichiometries.add( reactantStoichiometry );
        this.reactionParticipantStoichiometries.add( reactantStoichiometry );
        sorted = false;
    }

    public Collection<M> getReactants() {
        return Collections.unmodifiableList( reactants );
    }

    public void addReactant( M reactant ) {
        this.reactants.add( reactant );
        reactionParticipants.add( reactant );
        sorted = false;
    }

    public void addReactant( M reactant , S stoichiometry ) {
        this.addReactant( reactant );
        this.addReactantStoichiometry( stoichiometry );
    }

    public void addReactant( M reactant , S stoichiometry , C compartment ) {
        this.addReactant( reactant );
        this.addReactantStoichiometry( stoichiometry );
        this.addReactantCompartment( compartment );
    }

    public Collection<C> getProductCompartments() {
        return Collections.unmodifiableList( productCompartments );
    }

    protected void addProductCompartment( C productCompartment ) {
        this.productCompartments.add( productCompartment );
        this.reactionParticipantCompartments.add( productCompartment );
        sorted = false;

    }

    public Collection<S> getProductStoichiometries() {
        return Collections.unmodifiableList( productStoichiometries );
    }

    protected void addProductStoichiometry( S productStoichiometry ) {
        this.productStoichiometries.add( productStoichiometry );
        this.reactionParticipantStoichiometries.add( productStoichiometry );
        sorted = false;

    }

    public Collection<M> getProducts() {
        return Collections.unmodifiableList( products );
    }

    public void addProduct( M product ) {
        this.products.add( product );
        reactionParticipants.add( product );
        sorted = false;
    }

    public void addProduct( M product , S stoichiometry ) {
        this.addProduct( product );
        this.addProductStoichiometry( stoichiometry );
    }

    public void addProduct( M product , S stoichiometry , C compartment ) {
        this.addProduct( product );
        this.addProductCompartment( compartment );
        this.addProductStoichiometry( stoichiometry );
    }

    @Override
    public boolean equals( Object obj ) {
        // todo
        return super.equals( obj );
    }

    @Override
    public int hashCode() {
        int hash = 5;

        // sort if needed
        if ( sorted == Boolean.FALSE ) {
            Collections.sort( reactionParticipants , moleculeComparator );
            Collections.sort( reactionParticipantStoichiometries );
            Collections.sort( reactionParticipantCompartments );
            sorted = true;
        }

        hash = 59 * hash + ( this.reactantStoichiometries != null ? this.reactantStoichiometries.hashCode() : 0 );
        hash = 59 * hash + ( this.reactionParticipants != null ? this.reactionParticipants.hashCode() : 0 );
        hash =
        59 * hash +
        ( this.reactionParticipantStoichiometries != null ? this.reactionParticipantStoichiometries.hashCode() : 0 );
        hash =
        59 * hash +
        ( this.reactionParticipantCompartments != null ? this.reactionParticipantCompartments.hashCode() : 0 );
        return hash;
    }

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

    public static void main( String[] args ) {
        GenericReaction<String , Integer , String> r =
                                                   new GenericReaction<String , Integer , String>( new Comparator<String>() {

            public int compare( String o1 , String o2 ) {
                return o1.compareTo( o2 );
            }
        } );

        r.addReactant( "A" , 1 , "e" );
        r.addReactant( "B" , 1 , "c" );
        r.addProduct( "A" , 1 , "c" );
        r.addProduct( "C" , 1 , "c" );

        System.out.println( r );
    }
}
