/**
 * AtomContainerReaction.java
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
import java.util.*;
import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.*;
import org.openscience.cdk.smsd.Isomorphism;
import org.openscience.cdk.smsd.interfaces.Algorithm;
import org.openscience.cdk.tools.manipulator.AtomContainerComparator;
import uk.ac.ebi.chemet.entities.Compartment;

/**
 * @name    AtomContainerReaction
 * @date    2011.08.08
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   Reaction using AtomContainers from CDK. The class provides basic implementation of getting/setting
 *          reactants, products and their coefficients of the IReaction class.
 *
 */
public class AtomContainerReaction
        extends Reaction<IMolecule , Double , Compartment>
        implements IReaction , Serializable {

    private static final Logger LOGGER = Logger.getLogger( AtomContainerReaction.class );
    private static final long serialVersionUID = -3032876353022267689L;

    public AtomContainerReaction() {
        super(new MoleculeComparator());
    }

    private static class MoleculeComparator
            implements Comparator<IMolecule> {

        private AtomContainerComparator comparator = new AtomContainerComparator();

        public int compare( IMolecule o1 , IMolecule o2 ) {
            return comparator.compare( o1 , o2 );
        }
    }

    @Override
    public int moleculeHashCode( IMolecule mol ) {
        int hash = 7;

        hash = 67 * hash + mol.getBondCount();
        hash = 67 * hash + mol.getAtomCount();
        // hash atoms
        for ( int i = 0; i < mol.getAtomCount(); i++ ) {
            IAtom atom = mol.getAtom( i );
            hash = 67 * hash + ( atom.getCharge() != null ? atom.getCharge().hashCode() : 0 );
            hash = 67 * hash + ( atom.getAtomicNumber() != null ? atom.getAtomicNumber().hashCode() : 0 );
            hash = 67 * hash + ( atom.getMassNumber() != null ? atom.getMassNumber().hashCode() : 0 );
            hash = 67 * hash + ( atom.getExactMass() != null ? atom.getExactMass().hashCode() : 0 );
        }
        // don't check the bonds this is simply for a quicker hashCode
        return hash;
    }

    @Override
    public boolean moleculeEqual( IMolecule m1 , IMolecule m2 ) throws Exception {
        Isomorphism isoChecker = new Isomorphism( Algorithm.DEFAULT , true );
        isoChecker.init( m1 , m2 , false , true );
        return isoChecker.getTanimotoSimilarity() == 1;
    }

    /**
     * Create an instance IReaction object for use with CDK
     * TODO: Write unit test...
     * @return IReaction
     */
    public IReaction getCDKReaction() {

        IReaction reaction = DefaultChemObjectBuilder.getInstance().newInstance( IReaction.class );

        // add the reactants
        Iterator<IMolecule> reIt = reactants.iterator();
        Iterator<Double> rsIt = reactantStoichiometries.iterator();
        while ( reIt.hasNext() ) {
            if ( rsIt.hasNext() ) {
                reaction.addReactant( new Molecule( reIt.next() ) , rsIt.next() );
            } else {
                reaction.addReactant( new Molecule( reIt.next() ) );
            }
        }
        // add the product
        Iterator<IMolecule> prIt = products.iterator();
        Iterator<Double> psIt = productStoichiometries.iterator();
        while ( reIt.hasNext() ) {
            if ( rsIt.hasNext() ) {
                reaction.addProduct( new Molecule( reIt.next() ) , rsIt.next() );
            } else {
                reaction.addProduct( new Molecule( reIt.next() ) );
            }
        }
        return reaction;
    }

    /* IMPLEMENTATION OF IREACTION INTERFACE */
    public IMoleculeSet getReactants() {
        IMoleculeSet molSet = DefaultChemObjectBuilder.getInstance().newInstance( IMoleculeSet.class );
        Iterator<IMolecule> reIt = super.reactants.iterator();
        while ( reIt.hasNext() ) {
            molSet.addAtomContainer( new Molecule( reIt.next() ) );
        }
        return molSet;
    }

    public void setReactants( IMoleculeSet ims ) {
        for ( int i = 0; i < ims.getAtomContainerCount(); i++ ) {
            super.addReactant( ims.getMolecule( i ) );
        }
    }

    public IMoleculeSet getProducts() {
        IMoleculeSet molSet = DefaultChemObjectBuilder.getInstance().newInstance( IMoleculeSet.class );
        Iterator<IMolecule> reIt = super.products.iterator();
        while ( reIt.hasNext() ) {
            molSet.addAtomContainer( new Molecule( reIt.next() ) );
        }
        return molSet;
    }

    public void setProducts( IMoleculeSet ims ) {
        for ( int i = 0; i < ims.getAtomContainerCount(); i++ ) {
            super.addProduct( ims.getMolecule( i ) );
        }
    }

    public IMoleculeSet getAgents() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public Iterable<IMapping> mappings() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public boolean setReactantCoefficient( IMolecule im , Double d ) {
        super.reactantStoichiometries.set( super.reactants.indexOf( im ) , d );
        return true;
    }

    public boolean setProductCoefficient( IMolecule im , Double d ) {
        super.productStoichiometries.set( super.products.indexOf( im ) , d );
        return true;
    }

    public Double[] getReactantCoefficients() {
        return super.reactantStoichiometries.toArray( new Double[ 0 ] );
    }

    public Double[] getProductCoefficients() {
        return super.productStoichiometries.toArray( new Double[ 0 ] );
    }

    public boolean setReactantCoefficients( Double[] doubles ) {
        super.reactantStoichiometries = Arrays.asList( doubles );
        return true;
    }

    public boolean setProductCoefficients( Double[] doubles ) {
        super.productStoichiometries = Arrays.asList( doubles );
        return true;
    }

    public void setDirection( Direction drctn ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public Direction getDirection() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void addMapping( IMapping im ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void removeMapping( int i ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public IMapping getMapping( int i ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public int getMappingCount() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void addListener( IChemObjectListener il ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public int getListenerCount() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void removeListener( IChemObjectListener il ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void setNotification( boolean bln ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public boolean getNotification() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void notifyChanged() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void notifyChanged( IChemObjectChangeEvent icoce ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void setProperty( Object o , Object o1 ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void removeProperty( Object o ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public Object getProperty( Object o ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public Map<Object , Object> getProperties() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public String getID() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void setID( String string ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void setFlag( int i , boolean bln ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public boolean getFlag( int i ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void setProperties( Map<Object , Object> map ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void setFlags( boolean[] blns ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public boolean[] getFlags() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public IChemObjectBuilder getBuilder() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void addAgent( IMolecule im ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}
