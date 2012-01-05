/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.core.reaction.matrix;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author johnmay
 */
public class StoichiometricMatrixTest {

    public StoichiometricMatrixTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of merge method, of class StoichiometricMatrix.
     */
    @Test
    public void testMerge() {
        System.out.println("merge");
        BasicStoichiometricMatrix first = new BasicStoichiometricMatrix();
        BasicStoichiometricMatrix second = new BasicStoichiometricMatrix();

        first.addReactionWithName("v1", "A + B => C + D");
        first.addReactionWithName("v2", "C => E + F");


        second.addReactionWithName("v3", "D => F + G");
        second.addReactionWithName("v1", "A + B => C + D");

        first.display(System.out);
        second.display(System.out);

        first.merge(second).display(System.out);


    }
}
