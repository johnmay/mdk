/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.resource;

import junit.framework.Assert;
import org.junit.Test;
import uk.ac.ebi.chemet.resource.classification.ECNumber;
import uk.ac.ebi.chemet.resource.protein.SwissProtIdentifier;
import uk.ac.ebi.core.IdentifierSet;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.interfaces.identifiers.ProteinIdentifier;
import uk.ac.ebi.resource.chemical.HMDBIdentifier;
import uk.ac.ebi.chemet.resource.basic.*;

import java.io.*;


/**
 * @author johnmay
 */
public class IdentifierFactoryTest {







    @Test
    public void testSynonymLoading() {
        IdentifierFactory factory = IdentifierFactory.getInstance();
        Assert.assertEquals(ECNumber.class, factory.ofSynonym("EC").getClass());
        Assert.assertEquals(SwissProtIdentifier.class, factory.ofSynonym("Sprot").getClass());
        Assert.assertEquals(HMDBIdentifier.class, factory.ofSynonym("HMDB").getClass());
    }


    @Test
    public void testClassLoading() {
        IdentifierFactory factory = IdentifierFactory.getInstance();

        System.out.println("Testing factory load times using Class (10,000 objects);");
        for (Identifier id : factory.getSupportedIdentifiers()) {
            long start = System.currentTimeMillis();
            Class clazz = id.getClass();
            for (int j = 0; j < 10000; j++) {
                factory.ofClass(clazz);
            }
            long end = System.currentTimeMillis();
            long time = end - start;

            System.out.printf("%3s %-25s: %d (ms)\n", id.getIndex(), id.getClass().getSimpleName(),
                              time);

            // fail test on slow creation
            Assert.assertTrue(time < 500);

        }
    }


    @Test
    public void testMapping() {

        IdentifierFactory factory = IdentifierFactory.getInstance();
        System.out.printf("%35s %-35s\n", "Class Name", "Mapped MetaInfo");

        for (Identifier id : factory.getSupportedIdentifiers()) {
            long start = System.currentTimeMillis();
            Class c = id.getClass();

            int mir = IdentifierLoader.getInstance().getMIR(c);

            if (mir != 0) {
                System.out.printf("%35s %-35s\n", c.getSimpleName(), id.getShortDescription());
            }


        }
    }

    @Test
    public void sequenceHeaderResolution() throws Exception {

        System.out.println("testSequenceHeaderResolution");

        // basic features
        String sequenceHeader = "sp|Q197F8|002R_IIV3|sp|Q6GZX1|004R_FRG3G|gnl|ec|1.1.1.1|lcl|chemet-id";
        IdentifierSet ids = IdentifierFactory.getInstance().resolveSequenceHeader(sequenceHeader);
        Assert.assertTrue(ids.contains(new SwissProtIdentifier("Q197F8")));
        Assert.assertTrue(ids.contains(new SwissProtIdentifier("Q6GZX1")));
        Assert.assertTrue(ids.contains(new ECNumber("1.1.1.1")));
        Assert.assertTrue(ids.contains(new BasicProteinIdentifier("chemet-id")));

    }


    @Test
    public void sequenceHeaderUnsupportedIdentifier() throws Exception {

        // basic features
        String sequenceHeader = "gi|2010202|sp|Q197F8|002R_IIV3|sp|Q6GZX1|004R_FRG3G|gnl|ec|1.1.1.1";
        IdentifierSet ids = IdentifierFactory.getInstance().resolveSequenceHeader(sequenceHeader);

        Assert.assertTrue(ids.contains(new SwissProtIdentifier("Q197F8")));
        Assert.assertTrue(ids.contains(new SwissProtIdentifier("Q6GZX1")));
        Assert.assertTrue(ids.contains(new ECNumber("1.1.1.1")));

    }





}
