/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package uk.ac.ebi.metabolomes.core.reaction;

import com.google.common.base.Joiner;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.tools.manipulator.AtomContainerComparator;
import org.openscience.cdk.tools.manipulator.MoleculeSetManipulator;
import uk.ac.ebi.metabolomes.core.gene.OldGeneProduct;
import uk.ac.ebi.metabolomes.identifier.InChI;

/**
 * BiochemicalReaction.java
 * A class to represent a biochemical reaction. This class extends the CDK (1.3.8) reaction class
 * but includes the gene product which is carrying out the reaction.
 * <br>
 * The associated product is kept abstract as the BiochemicalReactions as enzymes can be
 * purely proteins (Enzyme), RNA (Ribozyme) or proteins+RNA (e.g. Ribosome)
 *
 * There could also be more then one different gene products carrying out the same reaction
 * and so the class handles <i>n</i> gene products which can be added at or after instantiation
 *
 * @author johnmay
 * @date Apr 6, 2011
 */
@Deprecated
public class BiochemicalReaction
        extends ProxyReaction
        implements Serializable {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( BiochemicalReaction.class );
    private static final long serialVersionUID = 1218989374829810919L;
    private ArrayList<OldGeneProduct> geneProducts = new ArrayList<OldGeneProduct>();

    /**
     * Create a biochemical reaction with no associated gene products
     */
    public BiochemicalReaction() {
        super();
    }

    /**
     * Create a new gene product with one associate gene product
     * @param geneProduct
     */
    public BiochemicalReaction( OldGeneProduct geneProduct ) {
        this();
        this.geneProducts.add( geneProduct );
    }

    public BiochemicalReaction( ArrayList<OldGeneProduct> geneProducts ) {
        this();
        this.geneProducts = geneProducts;
    }

    /**
     * Adds a gene product to the biochemical reaction
     * @param geneProduct
     */
    public void addModifier( OldGeneProduct geneProduct ) {
        this.geneProducts.add( geneProduct );
    }

    /**
     * Accessor for the first gene product in the array list
     * note. warning if array list contains more then one element
     */
    public OldGeneProduct getFirstModifier() {
        if ( geneProducts.size() > 1 ) {
            logger.warn( "more then one assoicated gene product in biochemical reaction" );
        }
        return geneProducts.get( 0 );
    }

    /**
     * Set the associated gene product(s)
     * @param geneProducts
     */
    public void setModifiers( ArrayList<OldGeneProduct> geneProducts ) {
        this.geneProducts = geneProducts;
    }

    /**
     * Accessor for the gene products
     * @return geneProducts
     */
    public ArrayList<OldGeneProduct> getModifiers() {
        return geneProducts;
    }

    /**
     * Accessor for the associated gene product(s)
     * @return array of gene products
     */
    public OldGeneProduct[] getFixedAssociatedGeneProducts() {
        return geneProducts.toArray( new OldGeneProduct[ 0 ] );
    }

    @Override
    public String toString() {
        String leftSide = buildList( getReactants() , getInchiReactants() );
        String rightSide = buildList( getProducts() , getInchiProducts() );
        return leftSide + " <?> " + rightSide;
    }

    public String buildList( IMoleculeSet molSet , List<InChI> inchis ) {
        List<String> ids = new ArrayList<String>();
        for ( int i = 0; i < molSet.getAtomContainerCount(); i++ ) {
            IMolecule molecule = molSet.getMolecule( i );
            ids.add( molecule.getID() );
        }
        if ( ids.isEmpty() ) {
            for ( int i = 0; i < inchis.size(); i++ ) {
                ids.add( inchis.get( i ).getName() );
            }
        }
        return Joiner.on(" + ").join(ids);
    }

    @Override
    public int hashCode() {

        // super.hashCode works on InChI reactants and products
        int hash = super.hashCode();

        hash = 67 * hash + ( this.geneProducts != null ? this.geneProducts.hashCode() : 0 );

        // todo stoichiometry
        return hash;
    }

    @Override
    public boolean equals( Object obj ) {

        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final BiochemicalReaction other = ( BiochemicalReaction ) obj;
        if ( this.getProductCount() != other.getProductCount() ) {

            return false;
        }
        if ( this.getReactantCount() != other.getReactantCount() ) {
            return false;
        }

        if ( MoleculeSetManipulator.getAtomCount( this.products ) !=
             MoleculeSetManipulator.getAtomCount( other.products ) ) {
            return false;
        }

        if ( MoleculeSetManipulator.getAtomCount( this.reactants ) != MoleculeSetManipulator.getAtomCount(
                other.reactants ) ) {
            return false;
        }



        // check the products

        AtomContainerComparator comparator = new AtomContainerComparator();

        // there probably is a faster way to do it
        int productCount = 0;
        for ( int i = 0; i < products.getMoleculeCount(); i++ ) {
            IAtomContainer query = products.getAtomContainer( i );
            for ( int j = 0; j < other.products.getMoleculeCount(); j++ ) {
                IAtomContainer ref = other.products.getAtomContainer( j );
                if ( comparator.compare( query , ref ) == 0 ) {
                    productCount++;
                }
            }
        }

        if ( productCount != this.products.getMoleculeCount() ) {
            return false;
        }

        // check reactants
        int reactantCount = 0;
        for ( int i = 0; i < reactants.getMoleculeCount(); i++ ) {
            IAtomContainer query = this.reactants.getAtomContainer( i );
            for ( int j = 0; j < other.reactants.getMoleculeCount(); j++ ) {
                IAtomContainer ref = other.reactants.getAtomContainer( j );
                if ( comparator.compare( query , ref ) == 0 ) {
                    reactantCount++;
                }
            }
        }

        if ( reactantCount != this.reactants.getMoleculeCount() ) {
            return false;
        }

        // bi directional



        return true;
    }
}
