/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.domain;

import junit.framework.Assert;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.identifier.HMDBIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.IdentifierSet;
import uk.ac.ebi.mdk.domain.identifier.SwissProtIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicProteinIdentifier;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;


/**
 * @author johnmay
 */
public class DefaultIdentifierFactoryTest {




    @Test public void testOfName(){
        DefaultIdentifierFactory factory    = DefaultIdentifierFactory.getInstance();
        Identifier        identifier = factory.ofName("ChEBI identifier", "ChEBI:12");
        System.out.println(identifier.getSummary());
    }


    @Test
    public void testSynonymLoading() {
        DefaultIdentifierFactory factory = DefaultIdentifierFactory.getInstance();
        Assert.assertEquals(ECNumber.class, factory.ofSynonym("EC").getClass());
        Assert.assertEquals(SwissProtIdentifier.class, factory.ofSynonym("Sprot").getClass());
        Assert.assertEquals(HMDBIdentifier.class, factory.ofSynonym("HMDB").getClass());
    }



    @Test
    public void testMapping() {

        DefaultIdentifierFactory factory = DefaultIdentifierFactory.getInstance();
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
        IdentifierSet ids = DefaultIdentifierFactory.getInstance().resolveSequenceHeader(sequenceHeader);
        Assert.assertTrue(ids.contains(new SwissProtIdentifier("Q197F8")));
        Assert.assertTrue(ids.contains(new SwissProtIdentifier("Q6GZX1")));
        Assert.assertTrue(ids.contains(new ECNumber("1.1.1.1")));
        Assert.assertTrue(ids.contains(new BasicProteinIdentifier("chemet-id")));

    }


    @Test
    public void sequenceHeaderUnsupportedIdentifier() throws Exception {

        // basic features
        String sequenceHeader = "gi|2010202|sp|Q197F8|002R_IIV3|sp|Q6GZX1|004R_FRG3G|gnl|ec|1.1.1.1";
        IdentifierSet ids = DefaultIdentifierFactory.getInstance().resolveSequenceHeader(sequenceHeader);

        Assert.assertTrue(ids.contains(new SwissProtIdentifier("Q197F8")));
        Assert.assertTrue(ids.contains(new SwissProtIdentifier("Q6GZX1")));
        Assert.assertTrue(ids.contains(new ECNumber("1.1.1.1")));

    }





}
