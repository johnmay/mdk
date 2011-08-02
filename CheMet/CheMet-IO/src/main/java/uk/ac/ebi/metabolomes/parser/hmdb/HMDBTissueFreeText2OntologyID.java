/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.parser.hmdb;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author pmoreno
 */
public class HMDBTissueFreeText2OntologyID {

    private BufferedReader reader;
    private HashMap<String,List<String>> name2TissueOntID;
    private Pattern idNamePattern;
    private Pattern idTabNameSimplePattern;

    public HMDBTissueFreeText2OntologyID(InputStream stream) {
        reader = new BufferedReader(new InputStreamReader(stream));
        this.init();
    }

    public HMDBTissueFreeText2OntologyID(String path) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(path));
        this.init();
    }
    
    public HMDBTissueFreeText2OntologyID() {
        this(HMDBTissueFreeText2OntologyID.class.getResourceAsStream("data/HMDB_Tissues2BTO_EFO_curated.txt"));
    }
    
    private void init() {
        name2TissueOntID = new HashMap<String, List<String>>();
        idNamePattern = Pattern.compile("p1=\\\"(\\d+)\\\">(.+)</t>");
        idTabNameSimplePattern = Pattern.compile("^([^\\t]+)\\t(.+:\\d+)\\t");
    }

    public void load() throws IOException {
        String line = reader.readLine();
        while(line!=null) {
            Matcher idNameMatcher = idNamePattern.matcher(line);
            Matcher idTabNameMatcher = idTabNameSimplePattern.matcher(line);
            if(idNameMatcher.find()) {
                this.addToHash(idNameMatcher.group(2).toLowerCase().trim(), idNameMatcher.group(1));
            } else if(idTabNameMatcher.find()) {
                this.addToHash(idTabNameMatcher.group(1).toLowerCase().trim(), idTabNameMatcher.group(2));
            }
            line = reader.readLine();
        }
    }

    public List<String> getIDsForName(String query) {
        if(name2TissueOntID.containsKey(query.toLowerCase().trim()))
            return name2TissueOntID.get(query.toLowerCase().trim());
        else
            return null;
    }

    private void addToHash(String key, String value) {
        List<String> toAdd;
        if(this.name2TissueOntID.containsKey(key))
            toAdd = this.name2TissueOntID.get(key);
        else {
            toAdd = new ArrayList<String>();
            this.name2TissueOntID.put(key, toAdd);
        }
        toAdd.add(value);
    }

}
