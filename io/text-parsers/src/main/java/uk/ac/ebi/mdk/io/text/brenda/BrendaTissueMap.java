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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for handling curated mappings of names to Brenda Tissue Ontology identifiers. Normal usage is to load the mappings
 * file and then retrieve mappings by using the {@link #getIDsForName(java.lang.String) } method.
 *
 * @author pmoreno
 */
public class BrendaTissueMap {

    public static final String NOMAPPING = "NOMAPPING";

    private BufferedReader reader;
    private Multimap<String,String> name2BrendaTissueOntID;
    private Pattern idTabNameSimplePattern;
    private Pattern idTabNameNotMappedPattern;
    private Boolean useNotMappped = false;

    /**
     * Loads the tissue/location curated map from a stream.
     *
     * @param stream
     */
    public BrendaTissueMap(InputStream stream) {
        reader = new BufferedReader(new InputStreamReader(stream));
        this.init();
    }

    /**
     * Set to true to use NOMAPPING entries in the tissue/location curated map. This is useful for the preloading, but
     * not the actual loading to the database.
     *
     * @param useNotMappped
     */
    public void setUseNotMapped(Boolean useNotMappped) {
        this.useNotMappped = useNotMappped;
    }

    /**
     * Loads the tissue/location curated map from a file in the the specified path.
     *
     * @param path
     * @throws FileNotFoundException
     */
    public BrendaTissueMap(String path) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(path));
        this.init();
    }

    private void init() {
        name2BrendaTissueOntID = HashMultimap.create();
        // this patterns ignores those that don't have a mapping, which is useful for loading to the database.
        idTabNameSimplePattern = Pattern.compile("^(.+:\\d+)\\t([^\\t]+)");
        idTabNameNotMappedPattern = Pattern.compile("^"+NOMAPPING+"\\t([^\\t]+)");
    }

    public void load() throws IOException {
        String line = reader.readLine();
        while(line!=null) {
            Matcher idTabNameMatcher = idTabNameSimplePattern.matcher(line);
            if(idTabNameMatcher.find()) {
                this.name2BrendaTissueOntID.put(idTabNameMatcher.group(2).toLowerCase().trim(), idTabNameMatcher.group(1));
            } else if(useNotMappped) {
                Matcher notMappedMatcher = idTabNameNotMappedPattern.matcher(line);
                if(notMappedMatcher.find())
                    this.name2BrendaTissueOntID.put(notMappedMatcher.group(1),NOMAPPING);
            }
            line = reader.readLine();
        }
        reader.close();
    }

    /**
     * Produces a list of identifiers for the given free text name of the tissue or location.
     *
     * @param query the tissue or location free text as it appears in the BRENDA files
     * @return a list of BTO or other ontologies identifiers matching that free text, or an empty list with no results.
     */
    public List<String> getIDsForName(String query) {
        if(name2BrendaTissueOntID.containsKey(query.toLowerCase().trim()))
            return new ArrayList<String>(name2BrendaTissueOntID.get(query.toLowerCase().trim()));
        else
            return new ArrayList<String>();
    }

}
