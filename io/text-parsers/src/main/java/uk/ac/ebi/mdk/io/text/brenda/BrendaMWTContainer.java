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
        reader.close();
    }

    /**
     * Produces a list of identifiers for the given free text name of the tissue or location.
     *
     * @param query
     * @return
     */
    public List<String> getIDsForName(String query) {
        if(name2BrendaTissueOntID.containsKey(query.toLowerCase().trim()))
            return name2BrendaTissueOntID.get(query.toLowerCase().trim());
        else
            return new ArrayList<String>();
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
