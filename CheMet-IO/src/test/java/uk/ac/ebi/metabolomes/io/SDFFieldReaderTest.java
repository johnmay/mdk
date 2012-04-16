/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.io;

import java.net.URL;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pmoreno
 */
public class SDFFieldReaderTest {
    
    public SDFFieldReaderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of readSDFFields method, of class SDFFieldReader.
     */
    @Test
    public void testReadSDFFields_PubChem() throws Exception {
        System.out.println("readSDFFields");
        String location = "ftp://ftp.ncbi.nih.gov/pubchem/Compound/CURRENT-Full/SDF/Compound_000075001_000100000.sdf.gz";
        Long milis = System.currentTimeMillis();
        URL remote = new URL(location);
        GZIPInputStream gZIPInputStream = new GZIPInputStream(remote.openStream());
        SDFFieldReader instance = new SDFFieldReader(gZIPInputStream, new PubChemCompoundSDFFieldExtractor());
        Iterator<SDFRecord> result = instance.readSDFFields();
        assertNotNull(result);
        System.out.println("Elapsed downloading and parse: "+(System.currentTimeMillis()-milis));
        while (result.hasNext()) {            
            SDFRecord rec = result.next();
            System.out.println(rec.getId()+"\t"+rec.getName()+"\t"+rec.getInChI());
        }
        System.out.println("Elapsed printing: "+(System.currentTimeMillis()-milis));
    }
}
