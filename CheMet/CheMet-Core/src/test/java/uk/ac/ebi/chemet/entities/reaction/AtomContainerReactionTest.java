/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.entities.reaction;

import org.apache.commons.lang.StringUtils;
import uk.ac.ebi.chemet.entities.Compartment;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.templates.MoleculeFactory;
import uk.ac.ebi.chemet.entities.reaction.participant.GenericParticipant;
import static uk.ac.ebi.chemet.TestMoleculeFactory.*;
import static org.junit.Assert.*;

/**
 *
 * @author johnmay
 */
public class AtomContainerReactionTest {

    private static AtomContainerReaction q1;
    private static Integer i = 1;

    public AtomContainerReactionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        // query
        q1 = new AtomContainerReaction();
        q1.addReactant( _123Triazole() , 1d , Compartment.EXTRACELLULA );
        q1.addReactant( adenine() , 2d , Compartment.CYTOPLASM );
        q1.addProduct( benzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        q1.addProduct( cyclohexane() , 1d , Compartment.CYTOPLASM );
        q1.addProduct( butan1ol() , 2d , Compartment.CYTOPLASM );
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testSomeMethod() {
    }

    @Test
    public void testEqualsDifferentSides() {
// TODO
        // different sides
        System.out.println( "[" + i++ + "] Testing Transposed Reaction Sides" );

        AtomContainerReaction r2 = new AtomContainerReaction();
        r2.addReactant( benzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r2.addReactant( cyclohexane() , 1d , Compartment.CYTOPLASM );
        r2.addReactant( butan1ol() , 2d , Compartment.CYTOPLASM );
        r2.addProduct( _123Triazole() , 1d , Compartment.EXTRACELLULA );
        r2.addProduct( adenine() , 2d , Compartment.CYTOPLASM );

        System.out.printf( "\t\tq: %10s %s\n" , q1.hashCode() , q1 );
        System.out.printf( "\t\tr: %10s %s\n" , r2.hashCode() , r2 );

        assertEquals( q1.hashCode() , r2.hashCode() );
        assertEquals( true , q1.equals( r2 ) );

        System.out.println( "    Test passed!" );
    }

    @Test
    public void testEqualsDifferentOrganisation() {

        System.out.println( "[" + i++ + "] Testing Different Organisation" );

        // same total molecules but on different sides
        AtomContainerReaction r3 = new AtomContainerReaction();
        r3.addReactant( benzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r3.addProduct( cyclohexane() , 1d , Compartment.CYTOPLASM );
        r3.addReactant( _123Triazole() , 1d , Compartment.EXTRACELLULA );
        r3.addProduct( adenine() , 2d , Compartment.CYTOPLASM );
        r3.addProduct( butan1ol() , 2d , Compartment.CYTOPLASM );

        System.out.printf( "\t\tq: %10s %s\n" , q1.hashCode() , q1 );
        System.out.printf( "\t\tr: %10s %s\n" , r3.hashCode() , r3 );

        assertEquals( false , q1.equals( r3 ) );

        System.out.println( "    Test passed!\n" );

    }

    @Test
    public void testEqualsDifferentOrg2() {

        System.out.println( "[" + i++ + "] Testing Different Organisation 2" );

        // different sides one molecule stays the same side
        AtomContainerReaction r = new AtomContainerReaction();
        r.addReactant( benzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r.addReactant( cyclohexane() , 1d , Compartment.CYTOPLASM );
        r.addProduct( _123Triazole() , 1d , Compartment.EXTRACELLULA );
        r.addProduct( adenine() , 2d , Compartment.CYTOPLASM );
        r.addProduct( butan1ol() , 2d , Compartment.CYTOPLASM ); // stays same but others move

        System.out.printf( "\t\tq: %10s %s\n" , q1.hashCode() , q1 );
        System.out.printf( "\t\tr: %10s %s\n" , r.hashCode() , r );


        // hash code doesn't need to be the same
        assertEquals( false , q1.equals( r ) );

        System.out.println( "    Test Passed!\n" );



    }

    @Test
    public void testEqualsOneDifferentMol() {

        System.out.println( "[" + i++ + "] Testing One Molecule Different" );

        // one different mol
        AtomContainerReaction r = new AtomContainerReaction();
        r.addProduct( benzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r.addProduct( cyclohexane() , 1d , Compartment.CYTOPLASM );
        r.addProduct( butan1ol() , 2d , Compartment.CYTOPLASM );
        r.addReactant( _124Triazole() , 1d , Compartment.EXTRACELLULA ); // difference
        r.addReactant( adenine() , 2d , Compartment.CYTOPLASM );

        System.out.printf( "\tvq: %10s %s\n" , q1.hashCode() , q1 );
        System.out.printf( "\t\tr: %10s %s\n" , r.hashCode() , r );


        // different sides one molecule stays the same side
        assertEquals( false , q1.equals( r ) );

        System.out.println( "    Test Passed!\n" );

    }

    @Test
    public void testEqualsOneDiffIsomer() {

        System.out.println( "[" + i++ + "] Testing One Molecule Different (Isomer)" );

        // one different mol (isomer)
        AtomContainerReaction r = new AtomContainerReaction();
        r.addReactant( _123Triazole() , 1d , Compartment.EXTRACELLULA ); // difference
        r.addReactant( adenine() , 2d , Compartment.CYTOPLASM );
        r.addProduct( benzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r.addProduct( cyclohexane() , 1d , Compartment.CYTOPLASM );
        r.addProduct( butan2ol() , 2d , Compartment.CYTOPLASM ); // butan-2-ol ≠ butan-1-ol

        System.out.printf( "\t\tq: %10s %s\n" , q1.hashCode() , q1 );
        System.out.printf( "\t\tr: %10s %s\n" , r.hashCode() , r );


        // hash code doesn't need to be the same
        assertEquals( false , q1.equals( r ) );

        System.out.println( "    Test Passed!\n" );

    }

    @Test
    public void testEqualsDiffCoef() {

        System.out.println( "[" + i++ + "] Testing Different Coefficients" );

        // one molecule with different stoichiometry
        AtomContainerReaction r = new AtomContainerReaction();
        r.addReactant( MoleculeFactory.make123Triazole() , 2d , Compartment.EXTRACELLULA ); // different stoichiometry
        r.addReactant( MoleculeFactory.makeAdenine() , 2d , Compartment.CYTOPLASM );
        r.addProduct( MoleculeFactory.makeBenzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r.addProduct( MoleculeFactory.makeCyclohexane() , 1d , Compartment.CYTOPLASM );
        r.addProduct( butan1ol() , 2d , Compartment.CYTOPLASM );

        System.out.printf( "\t\tq: %10s %s\n" , q1.hashCode() , q1 );
        System.out.printf( "\t\tr: %10s %s\n" , r.hashCode() , r );

        // hash code doesn't need to be the same
        assertEquals( false , q1.equals( r ) );

        System.out.println( "    Test Passed!\n" );
    }

    @Test
    public void testEqualsDiffComp() {

        System.out.println( "[" + i++ + "] Testing Different Compartments" );

        // one molecule with different compartment
        AtomContainerReaction r = new AtomContainerReaction();
        r.addReactant( _123Triazole() , 2d , Compartment.CYTOPLASM ); // different compartment
        r.addReactant( adenine() , 2d , Compartment.CYTOPLASM );
        r.addProduct( benzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r.addProduct( cyclohexane() , 1d , Compartment.CYTOPLASM );
        r.addProduct( butan1ol() , 2d , Compartment.CYTOPLASM );

        System.out.printf( "\t\tq: %10s %s\n" , q1.hashCode() , q1 );
        System.out.printf( "\t\tr: %10s %s\n" , r.hashCode() , r );

        // hash code doesn't need to be the same
        assertEquals( false , q1.equals( r ) );

        System.out.println( "    Test Passed!\n" );

    }

    @Test
    public void testEqualsTransComp() {

        System.out.println( "[" + i++ + "] Testing Transposed Compartments" );

        AtomContainerReaction r = new AtomContainerReaction();
        r.addReactant( adenine() , null , Compartment.CYTOPLASM );
        r.addReactant( butan1ol() , null , Compartment.EXTRACELLULA );
        r.addProduct( adenine() , null , Compartment.EXTRACELLULA );
        r.addProduct( butan1ol() , null , Compartment.CYTOPLASM );

        System.out.printf( "\t\treactans: %10s %s\n" , r.reactants.hashCode() , StringUtils.join( r.reactants , " + " ) );
        System.out.printf( "\t\tproducts: %10s %s\n" , r.products.hashCode() , StringUtils.join( r.products , " + " ) );

        // Make sure the hashCode for reactants and products is different
        assertEquals( false , r.reactants.hashCode() == r.products.hashCode() );

        System.out.println( "    Test Passed!\n" );

    }

    @Test
    public void testEqualsMissingCoefComp() {
        System.out.println( "[" + i++ + "] Testing Null Coef + Compartments" );

        /** only with products no coef or compartments **/
        // query
        AtomContainerReaction q2 = new AtomContainerReaction();
        q2.addReactant( _123Triazole() , null , null );
        q2.addReactant( adenine() , null , null );
        q2.addProduct( benzene() , null , null );
        q2.addProduct( cyclohexane() , null , null );
        q2.addProduct( butan1ol() , null , null );

        // different sides
        AtomContainerReaction r = new AtomContainerReaction();
        r.addReactant( benzene() , null , null );
        r.addReactant( cyclohexane() , null , null );
        r.addReactant( butan1ol() , null , null );
        r.addProduct( _123Triazole() , null , null );
        r.addProduct( adenine() , null , null );

        System.out.printf( "\t\tq: %10s %s\n" , q2.hashCode() , q2 );
        System.out.printf( "\t\tr: %10s %s\n" , r.hashCode() , r );


        assertEquals( q2.hashCode() , r.hashCode() );
        assertEquals( true , q2.equals( r ) );

        System.out.println( "    Test Passed!\n" );
    }

    @Test
    public void testEqualsDiffBonds() {
        System.out.println( "[" + i++ + "] Testing same connection diff bonds" );

        /** only with products no coef or compartments **/
        // query
        AtomContainerReaction q2 = new AtomContainerReaction();
        q2.addReactant( _123Triazole() , null , null );
        q2.addReactant( but1ene() , null , null );
        q2.addProduct( benzene() , null , null );
        q2.addProduct( cyclohexane() , null , null );

        // different sides
        AtomContainerReaction r = new AtomContainerReaction();
        r.addReactant( _123Triazole() , null , null );
        r.addReactant( butane() , null , null );
        r.addReactant( benzene() , null , null );
        r.addProduct( cyclohexane() , null , null );

        System.out.printf( "\t\tq: %10s %s\n" , q2.hashCode() , q2 );
        System.out.printf( "\t\tr: %10s %s\n" , r.hashCode() , r );

        assertEquals( false , q2.equals( r ) );

        System.out.println( "     Test Passed!\n" );
    }

    @Test
    public void testGenericADH() {
        System.out.println( "[" + i++ + "] Testing Generic" );

        /** only with products no coef or compartments **/
        // query
        AtomContainerReaction generic = new AtomContainerReaction();
        generic.addReactant( primary_alcohol() );
        generic.addReactant( nad_plus() );
        generic.addProduct( aldehyde() );
        generic.addProduct( hydron() );
        generic.addProduct( nadh() );

        // different sides
        AtomContainerReaction r = new AtomContainerReaction();
        r.addReactant( _5bcholestane3a7a26triol() );
        r.addReactant( nad_plus() );
        r.addProduct( _3a7adihydroxy5Bcholestan26al() );
        r.addProduct( hydron() );
        r.addProduct( nadh() );

        System.out.printf( "\t\tq: %10s %s\n" , generic.hashCode() , generic );
        System.out.printf( "\t\tr: %10s %s\n" , r.hashCode() , r );

        assertEquals( true , generic.equals( r ) );

        System.out.println( "     Test Passed!\n" );
    }
}
