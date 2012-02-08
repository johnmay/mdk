package uk.ac.ebi.chemet;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 *
 * @author johnmay
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({uk.ac.ebi.core.DefaultEntityFactoryTest.class,
                     uk.ac.ebi.core.MetaboliteImplementationTest.class})
public class CoreTestSuite {


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
}
