/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.util;

import uk.ac.ebi.metabolomes.util.inchi.StdInChIGenerator;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.graph.matrix.ConnectionMatrix;
import org.openscience.cdk.interfaces.IAtom;
import static org.junit.Assert.*;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import uk.ac.ebi.metabolomes.webservices.ChEBIWebServiceConnection;

/**
 *
 * @author pmoreno
 */
public class StdInChIGeneratorTest {

    private ChEBIWebServiceConnection cwsc;
    private ConnectionMatrix m;

    public StdInChIGeneratorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        cwsc = new ChEBIWebServiceConnection();
        m = new ConnectionMatrix();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getCDKMolFromInChI method, of class StdInChIGenerator.
     */
    @Test
    public void testGetCDKMolFromInChI() throws Exception {
        System.out.println("getCDKMolFromInChI");
        String inchi = "InChI=1S/C6H4O3/c7-4-1-2-5(8)6(9)3-4/h1-3,9H\n";
        IAtomContainer expResult = readMolFromString(getBenzeneMolString());
        IAtomContainer result = StdInChIGenerator.getCDKMolFromInChI(inchi);
        double[][] connExpResult = ConnectionMatrix.getMatrix(expResult);
        double[][] connResult = ConnectionMatrix.getMatrix(result);
        assertEquals(connExpResult.length,connResult.length );
        if(connExpResult.length == connResult.length && connResult.length > 0)
            assertEquals(connExpResult[0].length, connResult[0].length);
        for(int i=0;i<connExpResult.length;i++) {
            if(connExpResult.length > 0)
                for(int j=0;j<i;j++)
                    assertEquals(connExpResult[i][j],connResult[i][j]);
        }
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getInChIAndKeyFromCDKMol method, of class StdInChIGenerator.
     */
    @Test
    public void testGetInChIAndKeyFromCDKMol() throws Exception {
        System.out.println("getInChIAndKeyFromCDKMol");
        IAtomContainer mol = readBenzeneFromChebi();
        talkAboutMol(mol);
        String[] expResult = {"InChI=1S/C6H6/c1-2-4-6-5-3-1/h1-6H","InChIKey=UHOVQNZJYSORNB-UHFFFAOYSA-N"};
        String[] result = StdInChIGenerator.getInChIAndKeyFromCDKMol(mol);
        assertEquals(expResult, result);
    }

    private String getBenzeneMolString() {
        String molString = "Structure #1\n" + "  InChIV10\n\n" + "  9  9  0  0  0  0  0  0  0  0  1 V2000\n" + "   0.0000    0.0000    0.0000 C   0  0  0     0  0  0  0  0  0\n" + "   0.0000    0.0000    0.0000 C   0  0  0     0  0  0  0  0  0\n" + "   0.0000    0.0000    0.0000 C   0  0  0     0  0  0  0  0  0\n" + "   0.0000    0.0000    0.0000 C   0  0  0     0  0  0  0  0  0\n" + "   0.0000    0.0000    0.0000 C   0  0  0     0  0  0  0  0  0\n" + "   0.0000    0.0000    0.0000 C   0  0  0     0  0  0  0  0  0\n" + "   0.0000    0.0000    0.0000 O   0  0  0     0  0  0  0  0  0\n" + "   0.0000    0.0000    0.0000 O   0  0  0     0  0  0  0  0  0\n" + "   0.0000    0.0000    0.0000 O   0  0  0     0  0  0  0  0  0\n" + "  1  2  2  0  0  0  0\n" + "  1  4  1  0  0  0  0\n" + "  2  5  1  0  0  0  0\n" + "  3  4  1  0  0  0  0\n" + "  3  6  2  0  0  0  0\n" + "  4  7  2  0  0  0  0\n" + "  5  6  1  0  0  0  0\n" + "  5  8  2  0  0  0  0\n" + "  6  9  1  0  0  0  0\n" + "M  END\n" + "$$$$";
        return molString;
    }

    private IAtomContainer readMolFromString(String molString) throws IOException {
        IAtomContainer mol = null;
        MDLV2000Reader r = new MDLV2000Reader(new StringReader(molString));
        try {
            mol = (IMolecule) r.read(DefaultChemObjectBuilder.getInstance().newInstance(Molecule.class));
            r.close();
        } catch (CDKException ex) {
            fail("The test cannot read mol file from string");
        }
        talkAboutMol(mol);
        return mol;
    }


    private IAtomContainer readBenzeneFromChebi() {
        List<IAtomContainer> mols = cwsc.downloadMolsToCDKObject(new String[] {"16716"});
        talkAboutMol(mols.get(0));
        return mols.get(0);
    }

    private void talkAboutMol(IAtomContainer mol) {
        System.out.println("Mol loaded has " + mol.getAtomCount() + " atoms, " + mol.getBondCount() + " bonds.");
        int counter = 1;
        for (IAtom atom : mol.atoms()) {
            System.out.println("Atom " + counter + ": " + atom.getSymbol());
            counter++;
        }
    }

}