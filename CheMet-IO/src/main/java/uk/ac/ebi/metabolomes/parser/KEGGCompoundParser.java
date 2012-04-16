/**
 * KEGGCompoundParser.java
 *
 * 2011.10.27
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.metabolomes.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *          KEGGCompoundParser - 2011.10.27 <br>
 *          Parser the the ligand/compound file from kegg
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class KEGGCompoundParser {

    private static final Logger LOGGER = Logger.getLogger(KEGGCompoundParser.class);
    private BufferedReader reader;
    private EnumSet<KEGGField> filter = EnumSet.noneOf(KEGGField.class);

    public KEGGCompoundParser(InputStream stream, KEGGField... field) {
        this.reader = new BufferedReader(new InputStreamReader(stream));
        for (KEGGField f : field) {
            filter.add(f);
        }
    }

    public Map<KEGGField, StringBuilder> readNext() throws IOException {

        StringBuilder sb = new StringBuilder(1000);

        Map<KEGGField, StringBuilder> map = new EnumMap(KEGGField.class);

        String line;
        KEGGField field = null;
        while ((line = reader.readLine()) != null && !line.equals("///")) {

            String key = line.substring(0, Math.min(line.length(), 12)).trim();
            field = key.isEmpty() ? field : KEGGField.valueOf(key);

            if (filter.contains(field)) {
                if (!map.containsKey(field)) {
                    map.put(field, new StringBuilder(500));
                }
                map.get(field).append(line.substring(12));
            }

        }
        return line == null ? null : map;
    }

    public enum KEGGField {

        ENTRY, NAME,
        REACTION, FORMULA, MASS,
        REMARK, PATHWAY,
        ENZYME, DBLINKS,
        ATOM, BOND,
        COMMENT, BRACKET,
        ORIGINAL, REPEAT,
        SEQUENCE, ORGANISM,
        GENE, REFERENCE,
        // Glycan fields
        COMPOSITION, NODE, EDGE, CLASS, ORTHOLOGY
    };
}
