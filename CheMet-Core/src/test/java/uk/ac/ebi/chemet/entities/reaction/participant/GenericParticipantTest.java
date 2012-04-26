/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.entities.reaction.participant;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.entity.reaction.AtomContainerParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.GenericParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.ParticipantImplementation;

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

        System.out.printf( "%-120s" , "[TEST] Primary Alchol (removed R) equals 5ß-cholestane-3α,7α,26-triol" );

        ParticipantImplementation primaryAlochol = new GenericParticipant( primary_alcohol_no_r() );
        ParticipantImplementation alcoholImpl = new AtomContainerParticipant( _5bcholestane3a7a26triol() );

        assertEquals( true , primaryAlochol.equals( alcoholImpl ) );
        assertEquals( true , alcoholImpl.equals( primaryAlochol ) );

        ParticipantImplementation primaryAlocholR = new GenericParticipant( primary_alcohol() );

        assertEquals( true , primaryAlochol.equals( alcoholImpl ) );
        assertEquals( true , primaryAlocholR.equals( alcoholImpl ) );
        assertEquals( true , alcoholImpl.equals( primaryAlochol ) );


        System.out.println( "PASSED" );
    }

    @Test
    public void testAldehyde() {

        System.out.printf( "%-120s" , "[TEST] Aldehyde (removed R) equals  _3a7adihydroxy5Bcholestan26al" );


        ParticipantImplementation aldehyde = new GenericParticipant( aldehyde_no_r() );
        ParticipantImplementation aldehydeImpl = new AtomContainerParticipant( _3a7adihydroxy5Bcholestan26al() );

        assertEquals( true , aldehyde.equals( aldehydeImpl ) );
        assertEquals( true , aldehydeImpl.equals( aldehyde ) );
        System.out.println( "PASSED" );

    }

    @Test
    public void testGenericEquals() {

        System.out.printf( "%-120s" , "[TEST] Aldehyde (removed R) equals Aldehyde (removed R)" );

        ParticipantImplementation aldehyde = new GenericParticipant( aldehyde_no_r() );
        ParticipantImplementation aldehyde2 = new GenericParticipant( aldehyde_no_r() );

        assertEquals( aldehyde , aldehyde2 );
        assertEquals( aldehyde2 , aldehyde );

        System.out.println( "PASSED" );


    }

    @Test
    public void testGenericNotEquals() {

        System.out.printf( "%-120s" , "[TEST] Aldehyde (removed R) does not equal Primary Alcohol (removed R)" );

        ParticipantImplementation aldehyde = new GenericParticipant( aldehyde_no_r() );
        ParticipantImplementation primaryAlochol = new GenericParticipant( primary_alcohol_no_r() );

        assertNotSame( aldehyde , primaryAlochol );
        assertNotSame( primaryAlochol , aldehyde );

        System.out.println( "PASSED" );


    }
}
