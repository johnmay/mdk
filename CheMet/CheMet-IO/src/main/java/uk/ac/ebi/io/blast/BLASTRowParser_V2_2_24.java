/**
 * BLASTRowParser_V2_2_25.java
 *
 * 2011.10.06
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
package uk.ac.ebi.io.blast;

import org.apache.log4j.Logger;
import uk.ac.ebi.observation.sequence.LocalAlignment;

/**
 * @name    BLASTRowParser_V2_2_25 - 2011.10.06 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class BLASTRowParser_V2_2_24 implements BLASTRowParser {

    private static final Logger LOGGER = Logger.getLogger(BLASTRowParser_V2_2_24.class);

    public LocalAlignment parse(String[] row) {

        String query = row[0];
        String subject = row[1];

        float percentage = Float.parseFloat(row[2]);
        int length = Integer.parseInt(row[3]);

        int mismatch = Integer.parseInt(row[4]);
        int gaps = Integer.parseInt(row[5]);

        int queryStart = Integer.parseInt(row[6]);
        int queryEnd = Integer.parseInt(row[7]);
        int subjectStart = Integer.parseInt(row[8]);
        int subjectEnd = Integer.parseInt(row[9]);
        
        double exepected = Double.parseDouble(row[10]);
        double bit = Double.parseDouble(row[11]);

        return new LocalAlignment(query, subject, percentage, length, queryStart, queryEnd, subjectStart, subjectEnd, exepected, bit);

    }
}
