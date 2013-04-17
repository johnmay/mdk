/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.mdk.io.text.brenda;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for parsing the SourceTissue entry from a Brenda file. It requires the
 * specie key number.
 *
 * @author pmoreno
 */
public class STLineParser extends LineParser {

    private List<String> numIdentifersOfProteins;
    private List<String> foundNumIdentifersOfProteins;
    private Pattern proteinNumListPattern;
    private Pattern tissuePattern;
    private String tissue;

    protected String startingPrefix;

    public STLineParser(BufferedReader reader,List<String> numIdentifersOfProteins) {
        this.reader = reader;
        this.numIdentifersOfProteins = numIdentifersOfProteins;
        this.proteinNumListPattern = Pattern.compile("(\\t|#)([\\d,]+)(#|$)");
        this.tissuePattern = Pattern.compile("# (.+) (\\(#|<\\d)");
        this.startingPrefix = "ST\t";
        this.foundNumIdentifersOfProteins = new ArrayList<String>();
    }

    @Override
    public String parse(String line) throws IOException {
        boolean keepParsing=true;
        String fragmentProteinNumbers="";
        boolean seenST=false;
        while(keepParsing) {
            if((line.startsWith(this.startingPrefix) && seenST) || line.length() < 2)
                keepParsing = false;
            else if(line.startsWith(this.startingPrefix)) {
                seenST=true;
                fragmentProteinNumbers = line.replace(this.startingPrefix, "");
            } else if(line.startsWith("\t")) {
                if(Pattern.compile("\\d$").matcher(fragmentProteinNumbers).find()
                        && Pattern.compile("^\\t\\d").matcher(line).find())
                    fragmentProteinNumbers += ","+line.replace("\t", "");
                else
                    fragmentProteinNumbers += " "+line.replace("\t", "");
            }
            line = reader.readLine();
        }

        this.parseJointString(fragmentProteinNumbers);
        if(this.tissue!=null && this.tissue.contains(" (#"))
            this.tissue = this.tissue.substring(0, this.tissue.indexOf(" (#"));
        return line;
    }

    private void parseJointString(String line) {
        Matcher proteinNumListMatcher = proteinNumListPattern.matcher(line);
            if(proteinNumListMatcher.find()) {
                if(proteinNumListMatcher.group(1).contains("#") && proteinNumListMatcher.group(3).contains("#")) {

                    for (String protNumInEntry : proteinNumListMatcher.group(2).split(",")) {
                        if(this.numIdentifersOfProteins.contains(protNumInEntry)) {
                            this.foundNumIdentifersOfProteins.add(protNumInEntry);
                            Matcher tissueMatcher = this.tissuePattern.matcher(line);
                            if(tissueMatcher.find())
                                this.tissue = tissueMatcher.group(1);

                        }
                    }
                }
            }
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the tissue
     */
    public String getTissue() {
        return tissue;
    }

    /**
     * @param startingPrefix the startingPrefix to set
     */
    public void setStartingPrefix(String startingPrefix) {
        this.startingPrefix = startingPrefix;
    }

    /**
     * @return the foundNumIdentifersOfProteins
     */
    public List<String> getFoundDummyIdentifersOfProteins() {
        return foundNumIdentifersOfProteins;
    }


}
