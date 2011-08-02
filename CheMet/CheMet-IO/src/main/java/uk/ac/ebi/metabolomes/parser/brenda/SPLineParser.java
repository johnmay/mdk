/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.parser.brenda;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import uk.ac.ebi.metabolomes.bioObjects.Reaction;

/**
 *
 * @author pmoreno
 */
public class SPLineParser extends NSPLineParser{


    public SPLineParser(BufferedReader reader, List<String> numsOfProts) {
        super(reader,numsOfProts);
        this.reader = reader;
        this.startingPrefix = "SP\t";
        this.initialPattern = Pattern.compile("^SP\\t#([\\d|,]+)[#|$]");
    }
    /*
    public String parseSelect(String line) throws IOException{
        String res=line;
        if(line.startsWith("SP\t")) {
            return this.nspParser.parseSelect(line.replaceFirst("SP\t", "NSP\t"));
        }
        return reader.readLine();
    }*/
}
