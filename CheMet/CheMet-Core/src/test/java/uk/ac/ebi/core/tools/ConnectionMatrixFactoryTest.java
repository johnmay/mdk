/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.core.tools;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
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

        byte[][] bem = factory.getBondElectronMatrix(formaldehyde);

        System.out.printf("%3s", "-");
        for (int i = 0; i < bem.length; i++) {
            System.out.printf(" %3s ", formaldehyde.getAtom(i).getSymbol());
        }
        System.out.println("");


        for (int i = 0; i < bem.length; i++) {
            System.out.printf("%3s", formaldehyde.getAtom(i).getSymbol());
            for (int j = 0; j < bem[i].length; j++) {
                System.out.printf(" %3s ", bem[i][j]);
            }
            System.out.println("");
        }

    }
}
