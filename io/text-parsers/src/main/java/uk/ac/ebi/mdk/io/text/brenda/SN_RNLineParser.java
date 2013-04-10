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

/**
 *
 * @author pmoreno
 */
public class SN_RNLineParser extends LineParser{

    private String systematicName;
    private String recommendedName;

    public SN_RNLineParser(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public String parse(String line) throws IOException {
        if(line.startsWith("SN")) {
            String[] tokens = line.split("\t");
            if(tokens.length==2)
                this.systematicName=tokens[1];
        } else if(line.startsWith("RN")) {
            String[] tokens = line.split("\t");
            if(tokens.length==2)
                this.recommendedName=tokens[1];
        }
        return this.reader.readLine();
    }

    @Override
    public void reset() {
        this.systematicName = null;
        this.recommendedName = null;
    }

    /**
     * @return the systematicName
     */
    public String getSystematicName() {
        return systematicName;
    }

    /**
     * @return the recommendedName
     */
    public String getRecommendedName() {
        return recommendedName;
    }

}
