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
