/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.mdk.io.text.brenda;

import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author pmoreno
 */
public class SN_RNLineParser extends LineParser{

    private String systematicName;
    private String recommendedName;

    public SN_RNLineParser(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public String parse(String line) throws IOException {
        if(line.startsWith("SN")) {
            String[] tokens = line.split("\t");
            if(tokens.length==2)
                this.systematicName=tokens[1];
        } else if(line.startsWith("RN")) {
            String[] tokens = line.split("\t");
            if(tokens.length==2)
                this.recommendedName=tokens[1];
        }
        return this.reader.readLine();
    }

    @Override
    public void reset() {
        this.systematicName = null;
        this.recommendedName = null;
    }

    /**
     * @return the systematicName
     */
    public String getSystematicName() {
        return systematicName;
    }

    /**
     * @return the recommendedName
     */
    public String getRecommendedName() {
        return recommendedName;
    }

}
