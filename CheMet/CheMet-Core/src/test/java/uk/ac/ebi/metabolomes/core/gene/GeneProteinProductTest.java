/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.core.gene;

import com.sun.org.apache.bcel.internal.generic.LNEG;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.annotation.crossreference.ChEBICrossReference;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.annotation.crossreference.EnzymeClassification;
import uk.ac.ebi.resource.classification.ECNumber;
import uk.ac.ebi.metabolomes.identifier.GenericIdentifier;


/**
 *
 * @author johnmay
 */
public class GeneProteinProductTest extends TestCase {

    public GeneProteinProductTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testAnnotationRetrieval() throws Exception {

        GeneProteinProduct product = new GeneProteinProduct(new GenericIdentifier("ProteinA"),
                                                            "MTFG", "A protein");

        product.addAnnotation(new EnzymeClassification());
        product.addAnnotation(new ChEBICrossReference());
        product.addAnnotation(new ChEBICrossReference());

        // 3 annotations total
        assertEquals(3, product.getAnnotations().size());
        // 2 instances of ChEBICrossReference
        assertEquals(2, product.getAnnotations(ChEBICrossReference.class).size());
        // no direct instances of cross references
        assertEquals(0, product.getAnnotations(CrossReference.class).size());
        // 3 instances that 'are' sub classes of Crossreference
        assertEquals(3, product.getAnnotationsExtending(CrossReference.class).size());

    }


    @Test
    public void testExternalization() throws Exception {

        File tmp = File.createTempFile("test", "");


        ObjectOutput oo = new ObjectOutputStream(new FileOutputStream(tmp));
        for( int i = 0 ; i < 100 ; i++ ) {
            GeneProteinProduct product = new GeneProteinProduct(new GenericIdentifier("ProteinA"),
                                                                "MTFG", "A protein");

            product.addAnnotation(new ChEBICrossReference());
            product.addAnnotation(new EnzymeClassification(new ECNumber("1.1.1.1")));
            product.writeExternal(oo);
        }
        oo.close();
        System.out.println(tmp);
        System.out.println("File length:" + tmp.length()/1024 + " kb");
        long start = System.currentTimeMillis();
        ObjectInput oi = new ObjectInputStream(new FileInputStream(tmp));
        for( int i = 0 ; i < 100 ; i++ ) {
            GeneProteinProduct product2 = new GeneProteinProduct();
            product2.readExternal(oi);
            assertEquals(1, product2.getAnnotations(ChEBICrossReference.class).size());
            assertEquals(1, product2.getAnnotations(EnzymeClassification.class).size());
        }
        oi.close();

        long end = System.currentTimeMillis();
        System.out.println("Time: " + (end - start) + "(ms)");

    }


}

