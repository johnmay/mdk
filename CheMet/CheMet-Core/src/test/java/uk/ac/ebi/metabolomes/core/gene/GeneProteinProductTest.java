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
import java.util.ArrayList;
import java.util.Collection;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.annotation.crossreference.ChEBICrossReference;
import uk.ac.ebi.annotation.crossreference.Classification;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.annotation.crossreference.EnzymeClassification;
import uk.ac.ebi.resource.classification.ECNumber;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.resource.protein.BasicProteinIdentifier;


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

        GeneProteinProduct product = new GeneProteinProduct(new BasicProteinIdentifier("ProteinA"),
                                                            "MTFG", "A protein");

        product.addAnnotation(new EnzymeClassification(new ECNumber("1.1.1.1")));
        product.addAnnotation(new EnzymeClassification(new ECNumber("1.1.2.1")));
        product.addAnnotation(new ChEBICrossReference(new ChEBIIdentifier("ChEBI:12342")));
        product.addAnnotation(new ChEBICrossReference(new ChEBIIdentifier("ChEBI:12344")));

        // 3 annotations total
        assertEquals(4, product.getAnnotations().size());
        // 2 instances of ChEBICrossReference
        assertEquals(2, product.getAnnotations(ChEBICrossReference.class).size());
        // no direct instances of cross references
        assertEquals(0, product.getAnnotations(CrossReference.class).size());
        // 3 instances that 'are' sub classes of Crossreference
        assertEquals(4, product.getAnnotationsExtending(CrossReference.class).size());

        Collection<Classification> annotations = product.getAnnotationsExtending(Classification.class);
        System.out.println(new ArrayList(annotations));
    }


    @Test
    public void testExternalization() throws Exception {

        File tmp = File.createTempFile("test", "");


        ObjectOutput out = new ObjectOutputStream(new FileOutputStream(tmp));
        for( int i = 0 ; i < 100 ; i++ ) {
            GeneProteinProduct product = new GeneProteinProduct(new BasicProteinIdentifier("ProteinA"),
                                                                "MTFG", "A protein");

            product.addAnnotation(new ChEBICrossReference(new ChEBIIdentifier("ChEBI:12")));
            product.addAnnotation(new EnzymeClassification(new ECNumber("1.1.1.1")));
            product.writeExternal(out);
        }
        out.close();
        System.out.println(tmp);
        System.out.println("File length:" + tmp.length() / 1024 + " kb");
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

