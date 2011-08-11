/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.entities.reaction.generic;

import java.util.HashMap;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.metabolomes.util.CDKAtomTyper;
import static org.junit.Assert.*;

/**
 *
 * @author johnmay
 */
public class GenericReactionTest {

    public GenericReactionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Tests generic reaction comparison with Alcohol Dehydrogenase (EC: 1.1.1.1)
     */
    @Test
    public void testAlocholDehydrogenase() {
        HashMap<String , IMolecule> chebi = new HashMap<String , IMolecule>() {

            {
                put( "NAD+" , loadMol( "ChEBI_57540.mol" , "NAD+" ) );
                put( "primary alchol" , loadMol( "ChEBI_15734_edit.mol" , "primary alcohol" ) );
                put( "sedondary alchol" , loadMol( "ChEBI_35681.mol" , "secondary alcohol" ) );
                put( "H+" , loadMol( "ChEBI_15378.mol" , "H+" ) );
                put( "aldehyde" , loadMol( "ChEBI_17478_edit.mol" , "aldehyde" ) );
                put( "ketone" , loadMol( "ChEBI_17087.mol" , "ketone" ) );
                put( "NADH" , loadMol( "ChEBI_57945.mol" , "NADH" ) );
                put( "5beta-cholestane-3alpha,7alpha,26-triol" , loadMol( "ChEBI_28540.mol" ,
                                                                          "5B-cholestane-3a,7a,26-triol" , true ) );
                put( "3alpha,7alpha-dihydroxy-5beta-cholestan-26-al" , loadMol( "ChEBI_27428.mol" ,
                                                                                "3a,7a-dihydroxy-5B-cholestan-26-al" ,
                                                                                true ) );
            }
        };

        GenericReaction r1 = new GenericReaction();
        r1.addReactant( chebi.get( "NAD+" ) );
        r1.addReactant( chebi.get( "primary alchol" ) );
        r1.addProduct( chebi.get( "H+" ) );
        r1.addProduct( chebi.get( "aldehyde" ) );
        r1.addProduct( chebi.get( "NADH" ) );

        //   System.out.println( r1.hashCode() );

        GenericReaction r2 = new GenericReaction();
        r2.addReactant( chebi.get( "NAD+" ) );
        r2.addReactant( chebi.get( "5beta-cholestane-3alpha,7alpha,26-triol" ) );
        r2.addProduct( chebi.get( "H+" ) );
        r2.addProduct( chebi.get( "3alpha,7alpha-dihydroxy-5beta-cholestan-26-al" ) );
        r2.addProduct( chebi.get( "NADH" ) );

        assertEquals( true , r1.equals( r2 ) );


        // compounds matching generics are switched... should be false
        GenericReaction r3 = new GenericReaction();
        r3.addReactant( chebi.get( "NAD+" ) );
        r3.addReactant( chebi.get( "3alpha,7alpha-dihydroxy-5beta-cholestan-26-al" ) );
        r3.addProduct( chebi.get( "H+" ) );
        r3.addProduct( chebi.get( "5beta-cholestane-3alpha,7alpha,26-triol" ) );
        r3.addProduct( chebi.get( "NADH" ) );

        //  System.out.println( r2.hashCode() );

        assertEquals( false , r1.equals( r3 ) );

    }

    public IMolecule loadMol( String resource , String name ) {
        return loadMol( resource , name , false );
    }

    public IMolecule loadMol( String resource , String name , Boolean addH ) {

        MDLV2000Reader mol2Reader = new MDLV2000Reader( getClass().getResourceAsStream( resource ) );
        IMolecule molecule = DefaultChemObjectBuilder.getInstance().newInstance( IMolecule.class );
        try {
            mol2Reader.read( molecule );
            molecule.setProperty( "name" , name );
            CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance( DefaultChemObjectBuilder.getInstance() );
            if ( addH ) {
                CDKAtomTyper.typeAtoms( molecule );
                adder.addImplicitHydrogens( molecule );
                AtomContainerManipulator.convertImplicitToExplicitHydrogens( molecule );
            }
        } catch ( CDKException ex ) {
            // fail
            System.err.println( "Fatal Error – Cannot load molecule" );
            System.out.println( ex.getMessage() );
        }
        return molecule;
    }
}
