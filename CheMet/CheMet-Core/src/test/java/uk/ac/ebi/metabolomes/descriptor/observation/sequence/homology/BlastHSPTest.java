/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.descriptor.observation.sequence.homology;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.metabolomes.descriptor.observation.AbstractObservation;


/**
 *
 * @author johnmay
 */
public class BlastHSPTest {

    public BlastHSPTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testExternalization() throws IOException, ClassNotFoundException {

        File file = File.createTempFile("testExternalization",
                                        "");

        System.out.println("testExternalization()");

        System.out.println(file);

        BlastHSP hsp = new BlastHSP();
        hsp.setExpectedValue(0.02d);
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(hsp);
        out.close();

        // read-in
        ObjectInput in = new ObjectInputStream(new FileInputStream(file));
        BlastHSP hsp2 = new BlastHSP();
        AbstractObservation obs = (AbstractObservation) in.readObject();
        in.close();



    }


}

