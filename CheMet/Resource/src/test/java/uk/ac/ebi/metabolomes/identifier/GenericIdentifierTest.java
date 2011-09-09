/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.identifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import junit.framework.TestCase;


/**
 *
 * @author johnmay
 */
public class GenericIdentifierTest extends TestCase {

    public GenericIdentifierTest(String testName) {
        super(testName);
    }


    /**
     * Test of parse method, of class GenericIdentifier.
     */
    public void testParse() {
    }


    /**
     * Test of readExternal method, of class GenericIdentifier.
     */
    public void testObjectWriting() throws Exception {
        GenericIdentifier id = new GenericIdentifier("RXN-100");
        File tmpFile = File.createTempFile("test", ".jx");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tmpFile));
        id.writeExternal(oos);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tmpFile));
        GenericIdentifier readId = new GenericIdentifier("");
        readId.readExternal(ois);
        ois.close();

        System.out.println(readId);

    }


}

