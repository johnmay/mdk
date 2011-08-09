/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.entities.reaction;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.iupac.parser.MoleculeBuilder;
import org.openscience.cdk.templates.MoleculeFactory;
import uk.ac.ebi.chemet.util.CDKMoleculeBuilder;
import uk.ac.ebi.metabolomes.identifier.InChI;
import static org.junit.Assert.*;

/**
 *
 * @author johnmay
 */
public class AtomContainerReactionTest {

    private static final InChI butan1olInChI = new InChI( "InChI=1S/C4H10O/c1-2-3-4-5/h5H,2-4H2,1H3" );
    private static final InChI butan2olInChI = new InChI( "InChI=1S/C4H10O/c1-3-4(2)5/h4-5H,3H2,1-2H3" );
    private static AtomContainerReaction q1;

    public AtomContainerReactionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        // query
        q1 = new AtomContainerReaction();
        q1.addReactant( MoleculeFactory.make123Triazole() , 1d , Compartment.EXTRACELLULA );
        q1.addReactant( MoleculeFactory.makeAdenine() , 2d , Compartment.CYTOPLASM );
        q1.addProduct( MoleculeFactory.makeBenzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        q1.addProduct( MoleculeFactory.makeCyclohexane() , 1d , Compartment.CYTOPLASM );
        q1.addProduct( getButan1ol() , 2d , Compartment.CYTOPLASM );
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testSomeMethod() {
    }

    public static IAtomContainer getButan1ol() {
        return CDKMoleculeBuilder.getInstance().buildFromInChI( butan1olInChI );
    }

    public static IAtomContainer getButan2ol() {
        return CDKMoleculeBuilder.getInstance().buildFromInChI( butan2olInChI );
    }

    @Test
    public void testEqualsDifferentSides() {

        // different sides
        AtomContainerReaction r2 = new AtomContainerReaction();
        r2.addReactant( MoleculeFactory.makeBenzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r2.addReactant( MoleculeFactory.makeCyclohexane() , 1d , Compartment.CYTOPLASM );
        r2.addReactant( getButan1ol() , 2d , Compartment.CYTOPLASM );
        r2.addProduct( MoleculeFactory.make123Triazole() , 1d , Compartment.EXTRACELLULA );
        r2.addProduct( MoleculeFactory.makeAdenine() , 2d , Compartment.CYTOPLASM );

        assertEquals( q1.hashCode() , r2.hashCode() );
        assertEquals( true , q1.equals( r2 ) );
    }

    @Test
    public void testEqualsDifferentOrganisation() {
        // same total molecules but on different sides
        AtomContainerReaction r3 = new AtomContainerReaction();
        r3.addReactant( MoleculeFactory.makeBenzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r3.addProduct( MoleculeFactory.makeCyclohexane() , 1d , Compartment.CYTOPLASM );
        r3.addReactant( MoleculeFactory.make123Triazole() , 1d , Compartment.EXTRACELLULA );
        r3.addProduct( MoleculeFactory.makeAdenine() , 2d , Compartment.CYTOPLASM );
        r3.addProduct( getButan1ol() , 2d , Compartment.CYTOPLASM );

        assertEquals( false , q1.equals( r3 ) );
    }

    @Test
    public void testEqualsOneDifferentMol() {

        // one different mol
        AtomContainerReaction r4 = new AtomContainerReaction();
        r4.addReactant( MoleculeFactory.makeBenzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r4.addReactant( MoleculeFactory.makeCyclohexane() , 1d , Compartment.CYTOPLASM );
        r4.addProduct( MoleculeFactory.make124Triazole() , 1d , Compartment.EXTRACELLULA ); // difference
        r4.addProduct( MoleculeFactory.makeAdenine() , 2d , Compartment.CYTOPLASM );
        r4.addProduct( getButan1ol() , 2d , Compartment.CYTOPLASM );

        // different sides one molecule stays the same side
        assertEquals( false , q1.equals( r4 ) );
    }

    @Test
    public void testEqualsOneDiffIsomer() {
        // one different mol (isomer)
        AtomContainerReaction r5 = new AtomContainerReaction();
        r5.addReactant( MoleculeFactory.makeBenzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r5.addReactant( MoleculeFactory.makeCyclohexane() , 1d , Compartment.CYTOPLASM );
        r5.addProduct( MoleculeFactory.make123Triazole() , 1d , Compartment.EXTRACELLULA ); // difference
        r5.addProduct( MoleculeFactory.makeAdenine() , 2d , Compartment.CYTOPLASM );
        r5.addProduct( getButan2ol() , 2d , Compartment.CYTOPLASM ); // butan-2-ol ≠ butan-1-ol

        // hash code doesn't need to be the same
        assertEquals( false , q1.equals( r5 ) );
    }

    @Test
    public void testEqualsDifferentOrg2() {
        // different sides one molecule stays the same side
        AtomContainerReaction r6 = new AtomContainerReaction();
        r6.addReactant( MoleculeFactory.makeBenzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r6.addReactant( MoleculeFactory.makeCyclohexane() , 1d , Compartment.CYTOPLASM );
        r6.addProduct( MoleculeFactory.make123Triazole() , 1d , Compartment.EXTRACELLULA );
        r6.addProduct( MoleculeFactory.makeAdenine() , 2d , Compartment.CYTOPLASM );
        r6.addProduct( getButan1ol() , 2d , Compartment.CYTOPLASM ); // stays same but others move

        // hash code doesn't need to be the same
        assertEquals( false , q1.equals( r6 ) );
    }

    @Test
    public void testEqualsDiffCoef() {

        // one molecule with different stoichiometry
        AtomContainerReaction r7 = new AtomContainerReaction();
        r7.addReactant( MoleculeFactory.make123Triazole() , 2d , Compartment.EXTRACELLULA ); // different stoichiometry
        r7.addReactant( MoleculeFactory.makeAdenine() , 2d , Compartment.CYTOPLASM );
        r7.addProduct( MoleculeFactory.makeBenzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r7.addProduct( MoleculeFactory.makeCyclohexane() , 1d , Compartment.CYTOPLASM );
        r7.addProduct( getButan1ol() , 2d , Compartment.CYTOPLASM );

        // hash code doesn't need to be the same
        assertEquals( false , q1.equals( r7 ) );
    }

    @Test
    public void testEqualsDiffComp() {

        // one molecule with different compartment
        AtomContainerReaction r8 = new AtomContainerReaction();
        r8.addReactant( MoleculeFactory.make123Triazole() , 2d , Compartment.CYTOPLASM ); // different compartment
        r8.addReactant( MoleculeFactory.makeAdenine() , 2d , Compartment.CYTOPLASM );
        r8.addProduct( MoleculeFactory.makeBenzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r8.addProduct( MoleculeFactory.makeCyclohexane() , 1d , Compartment.CYTOPLASM );
        r8.addProduct( getButan1ol() , 2d , Compartment.CYTOPLASM );

        // hash code doesn't need to be the same
        assertEquals( false , q1.equals( r8 ) );
    }

    @Test
    public void testEqualsMissingCoefComp() {
        /** only with products no coef or compartments **/
        // query
        AtomContainerReaction q2 = new AtomContainerReaction();
        q2.addReactant( MoleculeFactory.make123Triazole() );
        q2.addReactant( MoleculeFactory.makeAdenine() );
        q2.addProduct( MoleculeFactory.makeBenzene() );
        q2.addProduct( MoleculeFactory.makeCyclohexane() );
        q2.addProduct( getButan1ol() );

        // different sides
        AtomContainerReaction r9 = new AtomContainerReaction();
        r9.addReactant( MoleculeFactory.makeBenzene() );
        r9.addReactant( MoleculeFactory.makeCyclohexane() );
        r9.addReactant( getButan1ol() );
        r9.addProduct( MoleculeFactory.make123Triazole() );
        r9.addProduct( MoleculeFactory.makeAdenine() );

        assertEquals( q2.hashCode() , r9.hashCode() );
        assertEquals( true , q2.equals( r9 ) );

    }
}
