/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.entities.reaction.participant;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static uk.ac.ebi.chemet.TestMoleculeFactory.*;

/**
 *
 * @author johnmay
 */
public class GenericParticipantTest {

    public GenericParticipantTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testPrimaryAlochol() {

        Participant primaryAlochol = new GenericParticipant( primary_alcohol_no_r() );
        Participant alcoholImpl = new AtomContainerParticipant( _5bcholestane3a7a26triol() );

        assertEquals( true , primaryAlochol.equals( alcoholImpl ) );
        assertEquals( true , alcoholImpl.equals( primaryAlochol ) );

        Participant primaryAlocholR = new GenericParticipant( primary_alcohol() );

        assertEquals( true , primaryAlochol.equals( alcoholImpl ) );
        assertEquals( true , primaryAlocholR.equals( alcoholImpl ) );
        assertEquals( true , alcoholImpl.equals( primaryAlochol ) );

    }

    @Test
    public void testAldehyde() {

        Participant aldehyde = new GenericParticipant( aldehyde_no_r() );
        Participant aldehydeImpl = new AtomContainerParticipant( _3a7adihydroxy5Bcholestan26al() );

        assertEquals( true , aldehyde.equals( aldehydeImpl ) );
        assertEquals( true , aldehydeImpl.equals( aldehyde ) );

    }

    @Test
    public void testGenericEquals() {

        Participant aldehyde = new GenericParticipant( aldehyde_no_r() );
        Participant aldehyde2 = new GenericParticipant( aldehyde_no_r() );

        assertEquals( true , aldehyde.equals( aldehyde2 ) );
        assertEquals( true , aldehyde2.equals( aldehyde ) );

    }

    @Test
    public void testGenericNotEquals() {

        Participant aldehyde = new GenericParticipant( aldehyde_no_r() );
        Participant primaryAlochol = new GenericParticipant( primary_alcohol_no_r() );

        assertEquals( false , aldehyde.equals( primaryAlochol ) );
        assertEquals( false , primaryAlochol.equals( aldehyde ) );

    }
}
