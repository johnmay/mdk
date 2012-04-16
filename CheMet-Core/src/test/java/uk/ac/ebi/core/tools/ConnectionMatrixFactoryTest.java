/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.core.tools;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smsd.Isomorphism;
import org.openscience.cdk.smsd.interfaces.Algorithm;
import uk.ac.ebi.chemet.TestMoleculeFactory;
import static org.junit.Assert.*;

/**
 *
 * @author johnmay
 */
public class ConnectionMatrixFactoryTest {

    public ConnectionMatrixFactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testGetInstance() {
    }

    @Test
    public void testGetBondElectronMatrix() {
        ConnectionMatrixFactory factory = ConnectionMatrixFactory.getInstance();

        IAtomContainer formaldehyde = TestMoleculeFactory.formaldehyde();

        byte[][] actual = factory.getBondElectronMatrix(formaldehyde);

        System.out.printf("%3s", "-");
        for (int i = 0; i < actual.length; i++) {
            System.out.printf(" %3s ", formaldehyde.getAtom(i).getSymbol());
        }
        System.out.println("");




        for (int i = 0; i < actual.length; i++) {
            System.out.printf("%3s", formaldehyde.getAtom(i).getSymbol());
            for (int j = 0; j < actual[i].length; j++) {
                System.out.printf(" %3s ", actual[i][j]);
            }
            System.out.println("");
        }

        byte[][] expected = new byte[][]{
            {0, 1, 2, 1},
            {1, 0, 0, 0},
            {2, 0, 4, 0},
            {1, 0, 0, 0},};

        assertArrayEquals(expected, actual);
    }
}
