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
import java.util.List;
import java.util.regex.Pattern;

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
