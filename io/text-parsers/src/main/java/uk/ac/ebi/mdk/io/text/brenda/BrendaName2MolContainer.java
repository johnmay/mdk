/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.mdk.io.text.brenda;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author pmoreno
 */
public class BrendaName2MolContainer {

    private BufferedReader reader;
    private HashMap<String,String> name2MolId;

    public BrendaName2MolContainer(String path) throws IOException {
        reader = new BufferedReader(new FileReader(path));
        name2MolId  = new HashMap<String, String>();
    }

    public void load() throws IOException {
        String line = reader.readLine();
        while(line!=null) {
            String[] tokens = line.split("\t");
            if(tokens.length == 2)
                name2MolId.put(tokens[0].toLowerCase().trim(), tokens[1]);
            line = reader.readLine();
        }
    }

    public String getMolID(String name) {
        if(name2MolId.containsKey(name.toLowerCase().trim()))
            return name2MolId.get(name.toLowerCase().trim());
        else
            return null;
    }



}
