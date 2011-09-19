/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.resource.chemical.ChemicalIdentifier;
import uk.ac.ebi.resource.chemical.KEGGCompoundIdentifier;


/**
 *
 * @author johnmay
 */
public class IdentifierSetTest {

    public IdentifierSetTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void textExternalization() {
        try {
            IdentifierSet ids = new IdentifierSet();
            ids.add(new ChEBIIdentifier("ChEBI:16353"));
            ids.add(new ChEBIIdentifier("ChEBI:16355"));
            ids.add(new KEGGCompoundIdentifier("C00005"));


            File tmpFile = File.createTempFile("test", "extern");
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(tmpFile));
            ids.writeExternal(out);
            out.close();

            ObjectInput in = new ObjectInputStream(new FileInputStream(tmpFile));
            IdentifierSet readIds = new IdentifierSet();
            readIds.readExternal(in);
            in.close();


            assertEquals(3, readIds.getIdentifiers().size());
            assertEquals(2, readIds.getIdentifiers(ChEBIIdentifier.class).size());
            assertEquals(1, readIds.getIdentifiers(KEGGCompoundIdentifier.class).size());
            assertEquals(3, readIds.getSubIdentifiers(ChemicalIdentifier.class).size());




        } catch( IOException ex ) {
            fail();

        } catch( ClassNotFoundException ex ) {
            fail();
        }
    }


}

