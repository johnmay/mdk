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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import uk.ac.ebi.metabolomes.bioObjects.Reaction;

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
