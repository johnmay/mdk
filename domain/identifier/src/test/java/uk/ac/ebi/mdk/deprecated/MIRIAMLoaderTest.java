/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.deprecated;

import org.apache.log4j.BasicConfigurator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.deprecated.MIRIAMLoader;

import static org.junit.Assert.*;

/**
 *
 * @author johnmay
 */
public class MIRIAMLoaderTest {

    private MIRIAMLoader loader = MIRIAMLoader.getInstance();

    public MIRIAMLoaderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        BasicConfigurator.configure();
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
        assertEquals(chebiId, loader.getIdentifier("urn:miriam:chebi:CHEBI%3A12"));
    }

    @Test
    public void testGetIdentifier_deprecated() {
        ChEBIIdentifier chebiId = new ChEBIIdentifier(12);
        assertEquals(chebiId, loader.getIdentifier("urn:miriam:obo.chebi:CHEBI%3A12"));
    }

    @Test
    public void testMain() {
    }
}
