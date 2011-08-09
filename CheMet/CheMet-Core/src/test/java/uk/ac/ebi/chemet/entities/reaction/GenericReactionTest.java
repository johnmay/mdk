/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.entities.reaction;

import java.util.Comparator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author johnmay
 */
public class GenericReactionTest {

    public GenericReactionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of equals method, of class GenericReaction.
     */
    @Test
    public void testEquals() {
    }

    /**
     * Test of hashCode method, of class GenericReaction.
     */
    @Test
    public void testHashCode() {
        GenericReaction r1 = new GenericReaction<String , Integer , String>( new Comparator<String>() {

            public int compare( String o1 , String o2 ) {
                return o1.compareTo( o2 );
            }
        } );

        r1.addReactant( "A" , 1 );
        r1.addReactant( "B" , 2 );
        r1.addProduct( "C" , 2 );
        r1.addProduct( "D" , 1 );
        GenericReaction r2 = new GenericReaction<String , Integer , String>( new Comparator<String>() {

            public int compare( String o1 , String o2 ) {
                return o1.compareTo( o2 );
            }
        } );

        r2.addReactant( "B" , 2 );
        r2.addReactant( "A" , 1 );
        r2.addProduct( "D" , 1 );
        r2.addProduct( "C" , 2 );

        assertEquals( r1.hashCode() , r2.hashCode() );

    }
}
