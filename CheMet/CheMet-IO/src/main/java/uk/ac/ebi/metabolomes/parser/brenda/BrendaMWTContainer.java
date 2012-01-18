/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.parser.brenda;

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
 * Class for handling curated mappings of names to Brenda Tissue Ontology identifiers. Normal usage is to load the mappings
 * file and then retrieve mappings by using the {@link #getIDsForName(java.lang.String) } method.
 *
 * @author pmoreno
 */
public class BrendaMWTContainer {

    private BufferedReader reader;
    private HashMap<String,List<String>> name2BrendaTissueOntID;
    private Pattern idNamePattern;
    private Pattern idTabNameSimplePattern;

    public BrendaMWTContainer(InputStream stream) {
        reader = new BufferedReader(new InputStreamReader(stream));
        this.init();
    }

    public BrendaMWTContainer(String path) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(path));
        this.init();
    }

    private void init() {
        name2BrendaTissueOntID = new HashMap<String, List<String>>();
        idNamePattern = Pattern.compile("p1=\\\"(\\d+)\\\">(.+)</t>");
        idTabNameSimplePattern = Pattern.compile("^(.+:\\d+)\\t([^\\t]+)");
    }

    public void load() throws IOException {
        String line = reader.readLine();
        while(line!=null) {
            Matcher idNameMatcher = idNamePattern.matcher(line);
            Matcher idTabNameMatcher = idTabNameSimplePattern.matcher(line);
            if(idNameMatcher.find()) {
                this.addToHash(idNameMatcher.group(2).toLowerCase().trim(), idNameMatcher.group(1));
            } else if(idTabNameMatcher.find()) {
                this.addToHash(idTabNameMatcher.group(2).toLowerCase().trim(), idTabNameMatcher.group(1));
            }
            line = reader.readLine();
        }
    }

    public List<String> getIDsForName(String query) {
        if(name2BrendaTissueOntID.containsKey(query.toLowerCase().trim()))
            return name2BrendaTissueOntID.get(query.toLowerCase().trim());
        else
            return null;
    }

    private void addToHash(String key, String value) {
        List<String> toAdd;
        if(this.name2BrendaTissueOntID.containsKey(key))
            toAdd = this.name2BrendaTissueOntID.get(key);
        else {
            toAdd = new ArrayList<String>();
            this.name2BrendaTissueOntID.put(key, toAdd);
        }
        toAdd.add(value);
    }

}
