package uk.ac.ebi.mdk.io.text.brenda;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 29/5/13
 * Time: 11:58
 * To change this template use File | Settings | File Templates.
 */
public class BrendaParserTest {


    @Test
    public void testNext() throws Exception {
        BrendaParser parser = new BrendaParser(BrendaParserTest.class.getResourceAsStream("brenda_v1111_initial_entries.txt"),"Homo sapiens");

        BrendaEntryEnzyme enzyme = parser.next();

        List<String> ecNumbers = new ArrayList<String>();
        List<String> proteins = new ArrayList<String>();
        while (enzyme!=null) {
            //System.out.println(enzyme.getEcNumber());
            ecNumbers.add(enzyme.getEcNumber());

            for (BrendaEntryEnzyme.BrendaProtein prot : enzyme.getBrendaProteinsForEntry()) {
                proteins.add(prot.toString());
                //System.out.println(prot.toString());
            }

            for(Reaction rxn : enzyme.getReactions()) {
                //System.out.println(rxn.toString());
            }

            //System.out.println();

            enzyme = parser.next();
        }
        assertTrue(ecNumbers.size()==7);
        assertTrue(ecNumbers.get(0).equals("1.1.1.1"));
        assertTrue(proteins.size()==16);
    }

    @Test
    public void testCase2017424() throws IOException {
        BrendaParser parser = new BrendaParser(BrendaParserTest.class.
                getResourceAsStream("brenda_v1111_conflictive_2016511_2017424.txt"),"Homo sapiens");

        BrendaEntryEnzyme enzyme = parser.next();

        List<String> ecNumbers = new ArrayList<String>();
        List<String> proteins = new ArrayList<String>();
        while (enzyme!=null) {
            System.out.println(enzyme.getEcNumber());
            ecNumbers.add(enzyme.getEcNumber());

            for (BrendaEntryEnzyme.BrendaProtein prot : enzyme.getBrendaProteinsForEntry()) {
                proteins.add(prot.toString());
                //System.out.println(prot.toString());
            }

            for(Reaction rxn : enzyme.getReactions()) {
                //System.out.println(rxn.toString());
            }

            //System.out.println();

            enzyme = parser.next();
        }
    }
}
