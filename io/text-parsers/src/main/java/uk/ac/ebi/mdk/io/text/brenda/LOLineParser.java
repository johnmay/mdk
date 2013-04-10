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
import java.util.List;

/**
 *
 * @author pmoreno
 */
public class LOLineParser {

    private STLineParser sTLineParser;

    public LOLineParser(BufferedReader reader,List<String> numIdentifersOfProteins) {
        this.sTLineParser = new STLineParser(reader, numIdentifersOfProteins);
        this.sTLineParser.setStartingPrefix("LO\t");
    }

    public String parse(String line) throws IOException {
        return this.sTLineParser.parse(line);
    }

    public String getLocation() {
        return this.sTLineParser.getTissue();
    }

    public List<String> getFoundDummyIdentifersOfProteins() {
        return this.sTLineParser.getFoundDummyIdentifersOfProteins();
    }
}
