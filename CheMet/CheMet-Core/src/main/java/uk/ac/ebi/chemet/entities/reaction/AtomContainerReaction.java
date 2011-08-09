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

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IChemObjectChangeEvent;
import org.openscience.cdk.interfaces.IChemObjectListener;
import org.openscience.cdk.interfaces.IMapping;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.smsd.Isomorphism;
import org.openscience.cdk.smsd.interfaces.Algorithm;
import org.openscience.cdk.tools.manipulator.AtomContainerComparator;
import uk.ac.ebi.chemet.util.CDKMoleculeBuilder;
import uk.ac.ebi.metabolomes.identifier.InChI;

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
        extends GenericReaction<IAtomContainer , Double , Compartment>
        implements IReaction {

    private static final Logger LOGGER = Logger.getLogger( AtomContainerReaction.class );

    public AtomContainerReaction() {
        super( new AtomContainerComparator() );
    }

    @Override
    public int getMoleculeHashCode( IAtomContainer mol ) {
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
    public boolean checkMoleculeEquality( IAtomContainer m1 , IAtomContainer m2 ) throws Exception {
        Isomorphism isoChecker = new Isomorphism( Algorithm.DEFAULT , true );
        isoChecker.init( m1 , m2 , true , true );
        return isoChecker.getTanimotoSimilarity() == 1;
    }

    public static void main( String[] args ) {

        String atp =
               "InChI=1S/C10H16N5O13P3/c11-8-5-9(13-2-12-8)15(3-14-5)10-7(17)6(16)4(26-10)1-25-30(21,22)28-31(23,24)27-29(18,19)20/h2-4,6-7,10,16-17H,1H2,(H,21,22)(H,23,24)(H2,11,12,13)(H2,18,19,20)/t4-,6-,7-,10-/m1/s1";
        String adp =
               "InChI=1S/C10H15N5O10P2/c11-8-5-9(13-2-12-8)15(3-14-5)10-7(17)6(16)4(24-10)1-23-27(21,22)25-26(18,19)20/h2-4,6-7,10,16-17H,1H2,(H,21,22)(H2,11,12,13)(H2,18,19,20)/t4-,6-,7-,10-/m1/s1";
        String water = "InChI=1S/H2O/h1H2";
        String nad =
               "InChI=1S/C21H27N7O14P2/c22-17-12-19(25-7-24-17)28(8-26-12)21-16(32)14(30)11(41-21)6-39-44(36,37)42-43(34,35)38-5-10-13(29)15(31)20(40-10)27-3-1-2-9(4-27)18(23)33/h1-4,7-8,10-11,13-16,20-21,29-32H,5-6H2,(H5-,22,23,24,25,33,34,35,36,37)/p+1/t10-,11-,13-,14-,15-,16-,20-,21-/m1/s1";

        String butan1ol = "InChI=1S/C4H10O/c1-2-3-4-5/h5H,2-4H2,1H3";
        String butan2ol = "InChI=1S/C4H10O/c1-3-4(2)5/h4-5H,3H2,1-2H3";

        IMolecule mol_atp = CDKMoleculeBuilder.getInstance().buildFromInChI( new InChI( atp ) );
        IMolecule mol_adp = CDKMoleculeBuilder.getInstance().buildFromInChI( new InChI( adp ) );
        IMolecule mol_h2o = CDKMoleculeBuilder.getInstance().buildFromInChI( new InChI( water ) );
        IMolecule mol_nad = CDKMoleculeBuilder.getInstance().buildFromInChI( new InChI( nad ) );

        IMolecule mol_atp2 = CDKMoleculeBuilder.getInstance().buildFromInChI( new InChI( atp ) );
        IMolecule mol_adp2 = CDKMoleculeBuilder.getInstance().buildFromInChI( new InChI( adp ) );
        IMolecule mol_h2o2 = CDKMoleculeBuilder.getInstance().buildFromInChI( new InChI( water ) );
        IMolecule mol_nad2 = CDKMoleculeBuilder.getInstance().buildFromInChI( new InChI( nad ) );

        IMolecule mol_but1ol = CDKMoleculeBuilder.getInstance().buildFromInChI( new InChI( butan1ol ) );
        IMolecule mol_but1ol_1 = CDKMoleculeBuilder.getInstance().buildFromInChI( new InChI( butan1ol ) );
        IMolecule mol_but2ol = CDKMoleculeBuilder.getInstance().buildFromInChI( new InChI( butan2ol ) );

        AtomContainerReaction r1 = new AtomContainerReaction();
        r1.addReactant( mol_adp , 1d , Compartment.EXTRACELLULA );
        r1.addReactant( mol_atp , 2d , Compartment.CYTOPLASM );
        r1.addProduct( mol_h2o , 2d , Compartment.CYTOPLASM );
        r1.addProduct( mol_nad , 1d , Compartment.CYTOPLASM );
        r1.addProduct( mol_but1ol , 1d , Compartment.CYTOPLASM );

        AtomContainerReaction r2 = new AtomContainerReaction();
        r2.addProduct( mol_adp2 , 1d , Compartment.EXTRACELLULA );
        r2.addProduct( mol_atp2 , 2d , Compartment.CYTOPLASM );
        r2.addReactant( mol_h2o2 , 2d , Compartment.CYTOPLASM );
        r2.addReactant( mol_nad2 , 1d , Compartment.CYTOPLASM );
        r2.addReactant( mol_but1ol_1 , 1d , Compartment.CYTOPLASM );

        //System.out.println( r1.equals( r2 ) );
        // System.out.println( r1.hashCode() );

    }

    /**
     * Create an instance IReaction object for use with CDK
     * TODO: Write unit test...
     * @return IReaction
     */
    public IReaction getCDKReaction() {

        IReaction reaction = DefaultChemObjectBuilder.getInstance().newInstance( IReaction.class );

        // add the reactants
        Iterator<IAtomContainer> reIt = reactants.iterator();
        Iterator<Double> rsIt = reactantStoichiometries.iterator();
        while ( reIt.hasNext() ) {
            if ( rsIt.hasNext() ) {
                reaction.addReactant( new Molecule( reIt.next() ) , rsIt.next() );
            } else {
                reaction.addReactant( new Molecule( reIt.next() ) );
            }
        }
        // add the product
        Iterator<IAtomContainer> prIt = products.iterator();
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
    public int getReactantCount() {
        return super.getReactantMolecules().size();
    }

    public int getProductCount() {
        return super.getProductMolecules().size();
    }

    public IMoleculeSet getReactants() {
        IMoleculeSet molSet = DefaultChemObjectBuilder.getInstance().newInstance( IMoleculeSet.class );
        Iterator<IAtomContainer> reIt = super.reactants.iterator();
        while ( reIt.hasNext() ) {
            molSet.addAtomContainer( new Molecule( reIt.next() ) );
        }
        return molSet;
    }

    public void setReactants( IMoleculeSet ims ) {
        for ( int i = 0; i < ims.getAtomContainerCount(); i++ ) {
            super.addReactant( ims.getAtomContainer( i ) );
        }
    }

    public IMoleculeSet getProducts() {
        IMoleculeSet molSet = DefaultChemObjectBuilder.getInstance().newInstance( IMoleculeSet.class );
        Iterator<IAtomContainer> reIt = super.products.iterator();
        while ( reIt.hasNext() ) {
            molSet.addAtomContainer( new Molecule( reIt.next() ) );
        }
        return molSet;
    }

    public void setProducts( IMoleculeSet ims ) {
        for ( int i = 0; i < ims.getAtomContainerCount(); i++ ) {
            super.addProduct( ims.getAtomContainer( i ) );
        }
    }

    public IMoleculeSet getAgents() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public Iterable<IMapping> mappings() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void addReactant( IMolecule im ) {
        super.addReactant( im );
    }

    public void addAgent( IMolecule im ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void addReactant( IMolecule im , Double d ) {
        super.addReactant( im , d );
    }

    public void addProduct( IMolecule im ) {
        super.addProduct( im );
    }

    public void addProduct( IMolecule im , Double d ) {
        super.addProduct( im , d );
    }

    public Double getReactantCoefficient( IMolecule im ) {
        return super.reactantStoichiometries.get( super.reactants.indexOf( im ) );
    }

    public Double getProductCoefficient( IMolecule im ) {
        return super.productStoichiometries.get( super.products.indexOf( im ) );
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
}
