/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.entities.reaction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author johnmay
 */
public class ReactionParticipantTest {

    public ReactionParticipantTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testSomeMethod() {
        try {
            ReactionParticipant rp = new ReactionParticipant<String , Integer , String>();
            rp.setMolecule( "A Molecule" );
            rp.setCompartment( "[a]" );
            File tmpFile = File.createTempFile( "ReactionParticipant" , ".javaobject" );
            System.out.println( "Testing Object writing: " + tmpFile );
            ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( tmpFile ) );
            oos.writeObject( rp );
            oos.close();

            ObjectInputStream ois = new ObjectInputStream( new FileInputStream( tmpFile ) );
            ReactionParticipant rp2 = ( ReactionParticipant ) ois.readObject();
            ois.close();

            System.out.println( rp2 );

        } catch ( ClassNotFoundException ex ) {
            ex.printStackTrace();
        } catch ( IOException ex ) {
            ex.printStackTrace();
        }
    }
}
