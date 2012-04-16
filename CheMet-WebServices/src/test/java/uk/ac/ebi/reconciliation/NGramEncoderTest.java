/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.reconciliation;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author johnmay
 */
public class NGramEncoderTest {

    public NGramEncoderTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testEncode() {
        StringEncoder encoder = new NGramEncoder(3);
        System.out.println(encoder.encode("onetwothreefour"));
    }


}

