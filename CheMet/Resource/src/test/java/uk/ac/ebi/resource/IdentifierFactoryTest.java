/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.resource;

import junit.framework.TestCase;
import org.junit.Test;
import uk.ac.ebi.chemet.interfaces.entities.Identifier;
import uk.ac.ebi.resource.chemical.KEGGCompoundIdentifier;


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
        for( Identifier id : factory.getSupportedIdentifiers() ) {
            long start = System.currentTimeMillis();
            for( int j = 0 ; j < 10000 ; j++ ) {
                factory.ofIndex(id.getIndex());
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
    public void testClassLoading() {
        IdentifierFactory factory = IdentifierFactory.getInstance();

        System.out.println("Testing factory load times using Class (10,000 objects);");
        for( Identifier id : factory.getSupportedIdentifiers() ) {
            long start = System.currentTimeMillis();

            for( int j = 0 ; j < 10000 ; j++ ) {
                factory.ofClass(id.getClass());
            }
            long end = System.currentTimeMillis();
            long time = end - start;

            System.out.printf("%3s %-25s: %d (ms)\n", id.getIndex(), id.getClass().getSimpleName(),
                              time);

            // fail test on slow creation
            assertTrue(time < 500);

        }
    }


}

