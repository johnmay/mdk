/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.io.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import uk.ac.ebi.metabolomes.core.reaction.matrix.BasicStoichiometricMatrix;


/**
 *
 * @author johnmay
 */
public class ReactionMatrixIOTest {

    public ReactionMatrixIOTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testCompressedMatrix() throws IOException {

        System.out.printf("[TEST] %-50s\n", "Compressed matrix read/write");

        File tmp = File.createTempFile("tmp", ".bin");

        Double[][] expected;
        Double[][] actual;

        {
            BasicStoichiometricMatrix s = BasicStoichiometricMatrix.create();

            s.addReaction(new String[0], new String[]{"A"});
            s.addReaction("A => B");
            s.addReaction("B => C");
            s.addReaction("C => D");
            s.addReaction(new String[]{"D"}, new String[0]);

            expected = s.getFixedMatrix();

            ReactionMatrixIO.writeCompressedBasicStoichiometricMatrix(s,
                                                                      new FileOutputStream(tmp));
        }

        {
            BasicStoichiometricMatrix s = ReactionMatrixIO.readCompressedBasicStoichiometricMatrix(new FileInputStream(tmp));
            actual = s.getFixedMatrix();
        }

        assertArrayEquals(expected, actual);

    }
}
