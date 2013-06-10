package uk.ac.ebi.mdk.service.query;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 10/6/13
 * Time: 12:01
 * To change this template use File | Settings | File Templates.
 */
public class BRENDAWebServiceTest {
    @Test
    public void testGetLigandStructureID() throws Exception {
        BRENDAWebService service = new BRENDAWebService();
        String id = service.getLigandStructureID("pyruvate");
        assertNotNull(id);
        System.out.println("Pyruvate BRENDA id : "+id);
        Integer idAdInteger = Integer.parseInt(id);
        assertTrue(idAdInteger > 0);
    }
}
