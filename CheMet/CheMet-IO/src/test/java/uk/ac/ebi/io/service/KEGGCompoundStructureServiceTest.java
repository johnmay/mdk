/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.io.service;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.resource.chemical.KEGGCompoundIdentifier;
import static org.junit.Assert.*;

/**
 *
 * @author johnmay
 */
public class KEGGCompoundStructureServiceTest {

    public KEGGCompoundStructureServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testGetStructure() {
        System.out.println(KEGGCompoundStructureService.getInstance().getStructure(new KEGGCompoundIdentifier("C00009")));

    }

    @Test
    public void testGetMDL() {
        System.out.println(KEGGCompoundStructureService.getInstance().getMDL(new KEGGCompoundIdentifier("C00009")));
    }
}
