/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.core.reaction.matrix;

import java.security.InvalidParameterException;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.ebi.core.util.Util;


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


    @Test
    public void testAssign() {
        BasicStoichiometricMatrix os = BasicStoichiometricMatrix.create();

        os.addReaction("A => B", true);
        os.addReaction("C => D", false);

        BasicStoichiometricMatrix ns = BasicStoichiometricMatrix.create();

        ns.addReaction("A => D", false);

        ns.assign(os);

        assertFalse(ns.isReversible(0));
        assertTrue(ns.isReversible(1));
        assertFalse(ns.isReversible(2));

    }


    @Test
    public void testDuplicate() {
        System.out.printf("[TEST] %-50s \n", "duplicate");
        BasicStoichiometricMatrix s = BasicStoichiometricMatrix.create();
        assertEquals(0, s.addReaction("A + B => C + I"));
        assertEquals(0, s.addReaction("A + B => C + I"));
        assertEquals(1, s.addReaction("B + C => D")); // intersect molecules but no matching reaction
        System.out.println("PASSED");
    }


    @Test
    public void testRemoveColumn() {

        System.out.printf("[TEST] %-50s \n", "removeColumn");
        BasicStoichiometricMatrix s = BasicStoichiometricMatrix.create(1, 1);
        s.addReaction("A + B => C + I");
        s.addReaction("A + B => C + J");
        s.addReaction("C + D => G");

        String name = s.getReaction(0);

        // remove first column        
        s.removeColumn(0);

        // should throw InvalidParameterException
        {
            assertTrue(s.getColumns(name).isEmpty());
        }

        {
            Object[][] expecteds = new Object[][]{{-1.0, null},
                                                  {-1.0, null},
                                                  {1.0, -1.0},
                                                  {null, null},
                                                  {1.0, null},
                                                  {null, -1.0},
                                                  {null, 1.0}};

            Object[][] actuals = s.getFixedMatrix();

            assertArrayEquals(expecteds, actuals);

        }

    }


    @Test
    public void testRemoveRow() {
        System.out.printf("[TEST] %-50s \n", "removeRow");
        BasicStoichiometricMatrix s = BasicStoichiometricMatrix.create();
        s.addReaction("A + B => C + I");
        s.addReaction("B + C => D");

        // remove C
        s.removeRow(2);


        // should throw InvalidParameterException
        {
            try {
                s.getRow("C");
            } catch (Exception e) {
                assertEquals(InvalidParameterException.class, e.getClass());
            }
        }

        {

            s.display(System.out);
            Object[][] expecteds = new Object[][]{
                {-1.0, null},
                {-1.0, -1.0},
                {1.0, null},
                {null, 1.0}
            };

            Object[][] actuals = s.getFixedMatrix();

            assertArrayEquals(expecteds, actuals);
        }

        {
            Object[] expecteds = new Object[]{"A", "B", "I", "D"};
            Object[] actuals = s.getMolecules();

            assertArrayEquals(expecteds, actuals);

        }

        System.out.println("PASSED");
    }


    /**
     * Test of merge method, of class StoichiometricMatrixImpl.
     */
    @Test
    public void testMerge() {
        System.out.printf("[TEST] %-50s \n", "merge");
        BasicStoichiometricMatrix first = BasicStoichiometricMatrix.create();
        BasicStoichiometricMatrix second = BasicStoichiometricMatrix.create();

        first.addReactionWithName("v1", "A + B => C + D");
        first.addReactionWithName("v1", "A + B => C + D");
        first.addReactionWithName("v2", "C => E + F");

        second.addReactionWithName("v3", "D => F + G");
        second.addReactionWithName("v1", "A + B => C + D");

        first.display(System.out);
        second.display(System.out);

        first.merge(first, second).display();

        System.out.println("PASSED");

    }


    @Test
    public void testAssign2() {
        System.out.printf("[TEST] %-50s \n", "assign2");
        BasicStoichiometricMatrix first = BasicStoichiometricMatrix.create();
        BasicStoichiometricMatrix second = BasicStoichiometricMatrix.create();

        first.addReactionWithName("v1", "A + B => C + D");
        first.addReactionWithName("v2", "C => E + F");

        second.addReactionWithName("v3", "D => F + G");
        second.addReactionWithName("v1", "A + B => C + D");

        first.display(System.out);
        second.display(System.out);

        Object[] expecteds = new Object[]{2, 0};
        
        assertArrayEquals(expecteds, first.assign(second).values().toArray());

        System.out.println("PASSED");

    }
}
