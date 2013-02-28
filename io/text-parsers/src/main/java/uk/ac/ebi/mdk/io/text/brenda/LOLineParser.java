/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.mdk.io.text.brenda;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author pmoreno
 */
public class LOLineParser {

    private STLineParser sTLineParser;

    public LOLineParser(BufferedReader reader,List<String> numIdentifersOfProteins) {
        this.sTLineParser = new STLineParser(reader, numIdentifersOfProteins);
        this.sTLineParser.setStartingPrefix("LO\t");
    }

    public String parse(String line) throws IOException {
        return this.sTLineParser.parse(line);
    }

    public String getLocation() {
        return this.sTLineParser.getTissue();
    }

    public List<String> getFoundDummyIdentifersOfProteins() {
        return this.sTLineParser.getFoundDummyIdentifersOfProteins();
    }
}
