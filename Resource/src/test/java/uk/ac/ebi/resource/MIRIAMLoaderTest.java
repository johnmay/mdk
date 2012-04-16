/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.resource;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.chemet.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.chemet.resource.util.MIRIAMLoader;

import static org.junit.Assert.*;

/**
 *
 * @author johnmay
 */
public class MIRIAMLoaderTest {

    public MIRIAMLoaderTest() {
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
    public void testGetEntry_String() {
    }

    @Test
    public void testGetEntry_Short() {
    }

    @Test
    public void testGetAccession() {
    }

    @Test
    public void testGetIdentifier() {
        ChEBIIdentifier chebiId = new ChEBIIdentifier(12);
        String urn = chebiId.getURN();
        assertEquals(chebiId, MIRIAMLoader.getInstance().getIdentifier(urn));
    }

    @Test
    public void testMain() {
    }
}
