/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.parser.brenda;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import uk.ac.ebi.metabolomes.bioObjects.Reaction;

/**
 *
 * @author pmoreno
 */
public class NSPLineParser extends AbstractReactionLineParser{

    private List<String> protNumsForSpecie;
    private List<String> foundProtNumsForSp;

    public NSPLineParser(BufferedReader reader, List<String> protNumsForSpecie) {
        this.reader = reader;
        this.rxn = new Reaction();
        this.startingPrefix = "NSP\t";
        this.protNumsForSpecie = protNumsForSpecie;
        this.foundProtNumsForSp = new ArrayList<String>();
    }
    
    protected Pattern initialPattern = Pattern.compile("^NSP\\t#([\\d|,]+)[#|$]");
    
    public String parseSelect(String line) throws IOException {
        Matcher nspInitialMatcher = initialPattern.matcher(line);
        if(nspInitialMatcher.find()) {
            String protNums[] = nspInitialMatcher.group(1).split(",");
            boolean parseForSpecie=false;
            for (String protNum : protNums) {
                if(this.protNumsForSpecie.contains(protNum)) {
                    this.foundProtNumsForSp.add(protNum);
                    parseForSpecie=true;
                    break;
                }
            }
            if(parseForSpecie)
                return this.parse(nspInitialMatcher.replaceAll(startingPrefix));
            else
                return reader.readLine();
        }
        return reader.readLine();
    }

    public List<String> getFoundDummyIdentifersOfProteins() {
        return this.foundProtNumsForSp;
    }

}
