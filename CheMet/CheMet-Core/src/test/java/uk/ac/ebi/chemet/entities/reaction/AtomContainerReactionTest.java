/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.entities.reaction;

import uk.ac.ebi.chemet.entities.Compartment;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.templates.MoleculeFactory;
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
        System.out.printf( "%-120s" , "[TEST] Reaction with transposed direction" );

        AtomContainerReaction r = new AtomContainerReaction();
        r.addReactant( benzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r.addReactant( cyclohexane() , 1d , Compartment.CYTOPLASM );
        r.addReactant( butan1ol() , 2d , Compartment.CYTOPLASM );
        r.addProduct( _123Triazole() , 1d , Compartment.EXTRACELLULA );
        r.addProduct( adenine() , 2d , Compartment.CYTOPLASM );

        // System.out.printf( "\t\tq: %10s %s\n" , q1.hashCode() , q1 );
        // System.out.printf( "\t\tr: %10s %s\n" , r2.hashCode() , r2 );

        assertEquals( q1.hashCode() , r.hashCode() );
        assertEquals( q1 , r );

        System.out.println( "PASSED" );
    }

    @Test
    public void testEqualsDifferentOrganisation() {

        System.out.printf( "%-120s" , "[TEST] Different participant organisation" );

        // same total molecules but on different sides
        AtomContainerReaction r = new AtomContainerReaction();
        r.addReactant( benzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r.addProduct( cyclohexane() , 1d , Compartment.CYTOPLASM );
        r.addReactant( _123Triazole() , 1d , Compartment.EXTRACELLULA );
        r.addProduct( adenine() , 2d , Compartment.CYTOPLASM );
        r.addProduct( butan1ol() , 2d , Compartment.CYTOPLASM );

        //  System.out.printf( "\t\tq: %10s %s\n" , q1.hashCode() , q1 );
        //  System.out.printf( "\t\tr: %10s %s\n" , r3.hashCode() , r3 );

        assertNotSame( q1 , r );

        System.out.println( "PASSED" );

    }

    @Test
    public void testEqualsDifferentOrg2() {

        System.out.printf( "%-120s" , "[TEST] Different participant organisation" );

        // different sides one molecule stays the same side
        AtomContainerReaction r = new AtomContainerReaction();
        r.addReactant( benzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r.addReactant( cyclohexane() , 1d , Compartment.CYTOPLASM );
        r.addProduct( _123Triazole() , 1d , Compartment.EXTRACELLULA );
        r.addProduct( adenine() , 2d , Compartment.CYTOPLASM );
        r.addProduct( butan1ol() , 2d , Compartment.CYTOPLASM ); // stays same but others move

        // System.out.printf( "\t\tq: %10s %s\n" , q1.hashCode() , q1 );
        // System.out.printf( "\t\tr: %10s %s\n" , r.hashCode() , r );


        // hash code doesn't need to be the same
        assertNotSame( q1 , r );

        System.out.println( "PASSED" );



    }

    @Test
    public void testEqualsOneDifferentMol() {

        System.out.printf( "%-120s" , "[TEST] Different participants" );

        // one different mol
        AtomContainerReaction r = new AtomContainerReaction();
        r.addProduct( benzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r.addProduct( cyclohexane() , 1d , Compartment.CYTOPLASM );
        r.addProduct( butan1ol() , 2d , Compartment.CYTOPLASM );
        r.addReactant( _124Triazole() , 1d , Compartment.EXTRACELLULA ); // difference
        r.addReactant( adenine() , 2d , Compartment.CYTOPLASM );

//        System.out.printf( "\tvq: %10s %s\n" , q1.hashCode() , q1 );
//        System.out.printf( "\t\tr: %10s %s\n" , r.hashCode() , r );


        // different sides one molecule stays the same side
        assertNotSame( q1 , r );

        System.out.println( "PASSED" );

    }

    @Test
    public void testEqualsOneDiffIsomer() {

        System.out.printf( "%-120s" , "[TEST] Different participants (Isomer butan-1-ol ≠ butan-2ol)" );

        // one different mol (isomer)
        AtomContainerReaction r = new AtomContainerReaction();
        r.addReactant( _123Triazole() , 1d , Compartment.EXTRACELLULA ); // difference
        r.addReactant( adenine() , 2d , Compartment.CYTOPLASM );
        r.addProduct( benzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r.addProduct( cyclohexane() , 1d , Compartment.CYTOPLASM );
        r.addProduct( butan2ol() , 2d , Compartment.CYTOPLASM ); // butan-2-ol ≠ butan-1-ol

//        System.out.printf( "\t\tq: %10s %s\n" , q1.hashCode() , q1 );
//        System.out.printf( "\t\tr: %10s %s\n" , r.hashCode() , r );


        // hash code doesn't need to be the same
        assertNotSame( q1 , r );

        System.out.println( "PASSED" );

    }

    @Test
    public void testEqualsDiffCoef() {

        System.out.printf( "%-120s" , "[TEST] Different participants (coefficients)" );

        // one molecule with different stoichiometry
        AtomContainerReaction r = new AtomContainerReaction();
        r.addReactant( MoleculeFactory.make123Triazole() , 2d , Compartment.EXTRACELLULA ); // different stoichiometry
        r.addReactant( MoleculeFactory.makeAdenine() , 2d , Compartment.CYTOPLASM );
        r.addProduct( MoleculeFactory.makeBenzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r.addProduct( MoleculeFactory.makeCyclohexane() , 1d , Compartment.CYTOPLASM );
        r.addProduct( butan1ol() , 2d , Compartment.CYTOPLASM );

//        System.out.printf( "\t\tq: %10s %s\n" , q1.hashCode() , q1 );
//        System.out.printf( "\t\tr: %10s %s\n" , r.hashCode() , r );

        // hash code doesn't need to be the same
        assertNotSame( q1 , r );

        System.out.println( "PASSED" );
    }

    @Test
    public void testEqualsDiffComp() {

        System.out.printf( "%-120s" , "[TEST] Different participants (compartments)" );

        // one molecule with different compartment
        AtomContainerReaction r = new AtomContainerReaction();
        r.addReactant( _123Triazole() , 2d , Compartment.CYTOPLASM ); // different compartment
        r.addReactant( adenine() , 2d , Compartment.CYTOPLASM );
        r.addProduct( benzene() , 2d , Compartment.MITOCHONDRIAL_MEMBRANE );
        r.addProduct( cyclohexane() , 1d , Compartment.CYTOPLASM );
        r.addProduct( butan1ol() , 2d , Compartment.CYTOPLASM );

        //  System.out.printf( "\t\tq: %10s %s\n" , q1.hashCode() , q1 );
        //  System.out.printf( "\t\tr: %10s %s\n" , r.hashCode() , r );

        // hash code doesn't need to be the same
        assertNotSame( q1 , r );

        System.out.println( "PASSED" );

    }

    @Test
    public void testEqualsTransComp() {

        System.out.printf( "%-120s" , "[TEST] Transposed compartments" );

        AtomContainerReaction r = new AtomContainerReaction();
        r.addReactant( adenine() , null , Compartment.CYTOPLASM );
        r.addReactant( butan1ol() , null , Compartment.EXTRACELLULA );
        r.addProduct( adenine() , null , Compartment.EXTRACELLULA );
        r.addProduct( butan1ol() , null , Compartment.CYTOPLASM );

//        System.out.printf( "\t\treactans: %10s %s\n" , r.getReactantParticipants().hashCode() , StringUtils.join( r.
//                getReactantParticipants() , " + " ) );
//        System.out.printf( "\t\tproducts: %10s %s\n" , r.getProductParticipants().hashCode() , StringUtils.join( r.
//                getProductParticipants() , " + " ) );

        // Make sure the hashCode for reactants and products is different
        assertNotSame( r.getReactantParticipants().hashCode() , r.getProductParticipants().hashCode() );

        System.out.println( "PASSED" );

    }

    @Test
    public void testEqualsMissingCoefComp() {
         System.out.printf( "%-120s" , "[TEST] Testing absent coefficients/compartments" );

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

        //     System.out.printf( "\t\tq: %10s %s\n" , q2.hashCode() , q2 );
        //     System.out.printf( "\t\tr: %10s %s\n" , r.hashCode() , r );


        assertEquals( q2.hashCode() , r.hashCode() );
        assertEquals( q2 , r );

        System.out.println( "PASSED" );
    }

    @Test
    public void testEqualsDiffBonds() {

        System.out.printf( "%-120s" , "[TEST] Testing same connectivity diff bonds (but-1-ene ≠ butane)" );

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

        //   System.out.printf( "\t\tq: %10s %s\n" , q2.hashCode() , q2 );
        //   System.out.printf( "\t\tr: %10s %s\n" , r.hashCode() , r );

        assertNotSame( q2 , r );

        System.out.println( "PASSED" );
    }

    @Test
    public void testGenericADH() {
        System.out.printf( "%-120s" , "[TEST] Testing generic reaction Alcohol Dehydrogenase" );

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

//        System.out.printf( "\t\tq: %10s %s\n" , generic.hashCode() , generic );
//        System.out.printf( "\t\tr: %10s %s\n" , r.hashCode() , r );

        assertEquals( "FAILED", generic , r );

        System.out.println( "PASSED" );
    }
}
