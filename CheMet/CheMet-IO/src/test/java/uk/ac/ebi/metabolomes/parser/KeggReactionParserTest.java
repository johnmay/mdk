/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.parser;

import java.io.IOException;
import java.io.InputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.ebi.metabolomes.parser.KeggReactionParser.KeggReactionEntry;

/**
 *
 * @author pmoreno
 */
public class KeggReactionParserTest {

    private KeggReactionParser parser;
    private InputStream stream;

    public KeggReactionParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        // stream of file reopened to test the methods on fresh stream
        stream = ClassLoader.getSystemResourceAsStream( "uk/ac/ebi/metabolomes/parser/KEGGReaction_R00001-R00017.txt" );
        parser = new KeggReactionParser( stream );
    }

    @After
    public void tearDown() {
        try {
            stream.close();
        } catch ( IOException ex ) {
            System.out.println( "Error closing stream: " + ex.getMessage() );
        }
    }

    /**
     * Test of getNextEntry method, of class KeggReactionParser.
     */
    @Test
    public void testGetNextEntry() {
        System.out.println( "getNextEntry" );
        KeggReactionParser instance = parser;
        while ( instance.hasNextEntry() ) {
            KeggReactionEntry result = instance.getNextEntry();
            assertNotNull( result );
            System.out.println( result.getReactionID() + " parsed!" );
            System.out.println( "Reactants: " + result.numberOfReactants() );
            System.out.println( "Products: " + result.numberOfProducts() );
            System.out.println( "Pathways: " + result.numberOfPathways() );
        }
    }
}