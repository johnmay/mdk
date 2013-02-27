/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.parser.brenda;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author pmoreno
 */
public class PRLineParser extends LineParser {

    private List<String> citations;
    private HashMap<String, String> id2DbRef;
    private String protNumber;
    private HashMap<String, String> idExt2ProtNumber;
    private Pattern proteinNumPattern;
    private Pattern citationListPattern;
    private Pattern dbRefPattern;

    public PRLineParser(BufferedReader reader, String specie) {
        this.specie = specie;
        this.reader = reader;
        //this.protNumber = new ArrayList<String>();
        this.citations = new ArrayList<String>();
        this.idExt2ProtNumber = new HashMap<String, String>();
        this.proteinNumPattern = Pattern.compile("#(\\d+)#");
        this.citationListPattern = Pattern.compile("(\\t|<)([\\d,]+)(>|$)");
        this.dbRefPattern = Pattern.compile("(\\S+) (SwissProt|UniProt|TrEMBL)");
        this.id2DbRef = new HashMap<String, String>();
    }

    /**
     * The contents are organised in 40 information fields as given
     * below. Protein information is included in '#'...#', literature
     * citations are in '<...>', commentaries in '(...)' and field-
     * special information in '{...}'.
     * @param line
     * @return
     */
    public String parse(String line) throws IOException {
        boolean keepParsing = true;
        while (keepParsing) {
            if (line.startsWith("PR\t") || line.startsWith("\t")) {
                //if(line.contains(this.specie)) {
                Matcher protNumberInLineMatcher = this.proteinNumPattern.matcher(line);
                Matcher citationListMatcher = this.citationListPattern.matcher(line);
                Matcher dbRefMatcher = this.dbRefPattern.matcher(line);
                
                if (protNumberInLineMatcher.find() && line.startsWith("PR\t")) {
                    //protNumber.add(protNumberInLineMatcher.group(1));
                    protNumber = protNumberInLineMatcher.group(1);
                }
                if ((line.contains("<") || line.contains(">")) && citationListMatcher.find()) {
                    this.citations.addAll(Arrays.asList(citationListMatcher.group(2).split(",")));
                }
                if (dbRefMatcher.find()) {
                    this.id2DbRef.put(dbRefMatcher.group(1), dbRefMatcher.group(2));
                }
                //} else {
                //    return this.reader.readLine();
                //}
                line = reader.readLine();
                if(line.startsWith("PR\t") || line.length()<2)
                    return line;
            } else {
                return line;
            }
        }
        return null;
    }

    public List<String> getCitations() {
        return this.citations;
    }

    public void reset() {
        this.citations.clear();
        this.id2DbRef.clear();
        this.idExt2ProtNumber.clear();
        this.protNumber=null;
    }

    /**
     * @return the id2DbRef
     */
    public HashMap<String, String> getId2DbRef() {
        return id2DbRef;
    }

    public String getDummyProteinEntryIdentifier() {
        return this.protNumber;
    }
}
