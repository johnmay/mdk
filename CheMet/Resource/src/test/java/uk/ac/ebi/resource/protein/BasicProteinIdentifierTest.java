/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.resource.protein;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.interfaces.identifiers.ProteinIdentifier;

/**
 *
 * @author johnmay
 */
public class BasicProteinIdentifierTest {

    public BasicProteinIdentifierTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testNewInstance() {
    }

    @Test
    public void testResolve() {
        ProteinIdentifier id = new BasicProteinIdentifier();
        Assert.assertEquals(new ArrayList(), id.resolve(new LinkedList(Arrays.asList("gnl", "db", "1234"))));
        Assert.assertEquals("1234", id.getAccession());
    }

    @Test
    public void testResolve2() {
        ProteinIdentifier id = new BasicProteinIdentifier();
        Assert.assertEquals(Arrays.asList("sp", "Q32", "DNAA"), id.resolve(new LinkedList(Arrays.asList("gnl", "db", "1234", "sp", "Q32", "DNAA"))));
        Assert.assertEquals("1234", id.getAccession());
    }
}
