/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.core.gene;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.metabolomes.identifier.GenericIdentifier;


/**
 *
 * @author johnmay
 */
public class GeneProteinProductTest {

    public GeneProteinProductTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testExternalization() throws Exception {

        File tmp = File.createTempFile("test", "");

        GeneProteinProduct product = new GeneProteinProduct(new GenericIdentifier("ProteinA"),
                                                            "MTFG", "A protein");

        System.out.println(product);

        ObjectOutput oo = new ObjectOutputStream(new FileOutputStream(tmp));
        product.writeExternal(oo);
        oo.close();
        GeneProteinProduct product2 = new GeneProteinProduct();
        ObjectInput oi = new ObjectInputStream(new FileInputStream(tmp));
        product2.readExternal(oi);
        oi.close();
        System.out.println(product2);

    }


}

