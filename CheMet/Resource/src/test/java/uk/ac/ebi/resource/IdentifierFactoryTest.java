/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.resource;

import java.util.Set;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;
import uk.ac.ebi.core.IdentifierSet;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.resource.chemical.HMDBIdentifier;
import uk.ac.ebi.resource.classification.ECNumber;
import uk.ac.ebi.resource.protein.BasicProteinIdentifier;
import uk.ac.ebi.resource.protein.SwissProtIdentifier;

/**
 *
 * @author johnmay
 */
public class IdentifierFactoryTest extends TestCase {

    public IdentifierFactoryTest(String testName) {
        super(testName);
    }

    @Test
    public void testIndexLoading() {
        IdentifierFactory factory = IdentifierFactory.getInstance();

        System.out.println("Testing factory load times using index (10,000 objects);");
        for (Identifier id : factory.getSupportedIdentifiers()) {
            long start = System.currentTimeMillis();
            Byte index = id.getIndex();
            for (int j = 0; j < 10000; j++) {
                factory.ofIndex(index);
            }
            long end = System.currentTimeMillis();
            long time = end - start;
            System.out.printf("%3s %-25s: %d (ms)\n", id.getIndex(), id.getClass().getSimpleName(),
                              time);

            // fail test on slow creation
            assertTrue(time < 500);

        }
    }

    @Test
    public void testSynonymLoading() {
        IdentifierFactory factory = IdentifierFactory.getInstance();
        assertEquals(ECNumber.class, factory.ofSynonym("EC").getClass());
        assertEquals(SwissProtIdentifier.class, factory.ofSynonym("Sprot").getClass());
        assertEquals(HMDBIdentifier.class, factory.ofSynonym("HMDB").getClass());
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
            assertTrue(time < 500);

        }
    }

    @Test
    public void sequenceHeaderResolution() {
        System.out.println("testSequenceHeaderResolution");

        // basic features
        String sequenceHeader = "sp|Q197F8|002R_IIV3|sp|Q6GZX1|004R_FRG3G|gnl|ec|1.1.1.1";
        IdentifierSet ids = IdentifierFactory.getInstance().resolveSequenceHeader(sequenceHeader);
        Assert.assertTrue(ids.contains(new SwissProtIdentifier("Q197F8")));
        Assert.assertTrue(ids.contains(new SwissProtIdentifier("Q6GZX1")));
        Assert.assertTrue(ids.contains(new BasicProteinIdentifier("1.1.1.1")));

    }

    @Test
    public void sequenceHeaderUnsupportedIdentifier() {
        System.out.println("testSequenceHeaderResolution");

        // basic features
        String sequenceHeader = "gi|2010202|sp|Q197F8|002R_IIV3|sp|Q6GZX1|004R_FRG3G|gnl|ec|1.1.1.1";
        IdentifierSet ids = IdentifierFactory.getInstance().resolveSequenceHeader(sequenceHeader);
        Assert.assertTrue(ids.contains(new SwissProtIdentifier("Q197F8")));
        Assert.assertTrue(ids.contains(new SwissProtIdentifier("Q6GZX1")));
        Assert.assertTrue(ids.contains(new BasicProteinIdentifier("1.1.1.1")));

    }

    @Test
    public void testGetSupportedIdentifiers() {
    }

    @Test
    public void testGetInstance() {
    }

    @Test
    public void testResolveSequenceHeader() {
    }

    @Test
    public void testGetIdentifier() {
    }

    @Test
    public void testGetUncheckedIdentifier() {
    }

    @Test
    public void testGetIdentifiers() {
    }

    @Test
    public void testGetResouce() {
    }

    @Test
    public void testOfClass() {
    }

    @Test
    public void testOfIndex() {
    }

    @Test
    public void testRead() throws Exception {
    }

    @Test
    public void testWrite() throws Exception {
    }
}
