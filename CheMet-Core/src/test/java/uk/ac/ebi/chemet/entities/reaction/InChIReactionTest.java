/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.entities.reaction;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.entity.reaction.InChIReaction;
import uk.ac.ebi.mdk.domain.entity.reaction.compartment.Organelle;
import uk.ac.ebi.mdk.domain.identifier.InChI;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author johnmay
 */
public class InChIReactionTest {

    public InChIReactionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testTransposedCompartments() {
        /**
         * This test test the premise that reactants/products can switch compartments:
         * (1.0)InChI=1S/p+1 [e] + (1.0)InChI=1S/HNO2/c2-1-3/h(H,2,3)/p-1 [c] <=?=> (1.0)InChI=1S/p+1 [c] + (1.0)InChI=1S/HNO2/c2-1-3/h(H,2,3)/p-1 [e]
         * Previously this was a bug and would generate the same hashcode for left and right side the coef and compartments were sorted separately to the molecules. This has not be
         * rectified by sorting by compound order
         */
        InChIReaction r = new InChIReaction();
        r.addReactant( new InChI( "InChI=1S/p+1" ) , 1d , Organelle.EXTRACELLULA );
        r.addReactant( new InChI( "InChI=1S/HNO2/c2-1-3/h(H,2,3)/p-1" ) , 1d , Organelle.CYTOPLASM );
        r.addProduct( new InChI( "InChI=1S/p+1" ) , 1d , Organelle.CYTOPLASM );
        r.addProduct( new InChI( "InChI=1S/HNO2/c2-1-3/h(H,2,3)/p-1" ) , 1d , Organelle.EXTRACELLULA );

        // copy
        InChIReaction r1 = new InChIReaction();
        r1.addReactant( new InChI( "InChI=1S/p+1" ) , 1d , Organelle.EXTRACELLULA );
        r1.addReactant( new InChI( "InChI=1S/HNO2/c2-1-3/h(H,2,3)/p-1" ) , 1d , Organelle.CYTOPLASM );
        r1.addProduct( new InChI( "InChI=1S/p+1" ) , 1d , Organelle.CYTOPLASM );
        r1.addProduct( new InChI( "InChI=1S/HNO2/c2-1-3/h(H,2,3)/p-1" ) , 1d , Organelle.EXTRACELLULA );

        // all compartments switched (different direction)
        InChIReaction r2 = new InChIReaction();
        r2.addReactant( new InChI( "InChI=1S/p+1" ) , 1d , Organelle.CYTOPLASM );
        r2.addReactant( new InChI( "InChI=1S/HNO2/c2-1-3/h(H,2,3)/p-1" ) , 1d , Organelle.EXTRACELLULA );
        r2.addProduct( new InChI( "InChI=1S/p+1" ) , 1d , Organelle.EXTRACELLULA );
        r2.addProduct( new InChI( "InChI=1S/HNO2/c2-1-3/h(H,2,3)/p-1" ) , 1d , Organelle.CYTOPLASM );

        assertEquals( r.hashCode() , r1.hashCode() );
        assertEquals( true , r.equals( r1 ) );
        assertEquals( r.hashCode() , r2.hashCode() );
        assertEquals( true , r.equals( r2 ) );
    }

    @Test
    public void testTransposedStoichiometries() {
        InChIReaction r = new InChIReaction();
        r.addReactant( new InChI( "InChI=1S/p+1" ) , 2d , Organelle.CYTOPLASM );
        r.addReactant( new InChI( "InChI=1S/HNO2/c2-1-3/h(H,2,3)/p-1" ) , 1d , Organelle.CYTOPLASM );
        r.addProduct( new InChI( "InChI=1S/p+1" ) , 1d , Organelle.CYTOPLASM );
        r.addProduct( new InChI( "InChI=1S/HNO2/c2-1-3/h(H,2,3)/p-1" ) , 2d , Organelle.CYTOPLASM );

        // copy
        InChIReaction r1 = new InChIReaction();
        r1.addProduct( new InChI( "InChI=1S/p+1" ) , 2d , Organelle.CYTOPLASM );
        r1.addProduct( new InChI( "InChI=1S/HNO2/c2-1-3/h(H,2,3)/p-1" ) , 1d , Organelle.CYTOPLASM );
        r1.addReactant( new InChI( "InChI=1S/p+1" ) , 1d , Organelle.CYTOPLASM );
        r1.addReactant( new InChI( "InChI=1S/HNO2/c2-1-3/h(H,2,3)/p-1" ) , 2d , Organelle.CYTOPLASM );

        assertEquals( r.hashCode() , r1.hashCode() );
        assertEquals( true , r.equals( r1 ) );

    }
}
