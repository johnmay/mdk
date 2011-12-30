/**
 * SDFFieldReader.java
 *
 * 2011.12.30
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
package uk.ac.ebi.metabolomes.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * @name    SDFFieldReader
 * @date    2011.12.30
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   Parses an SDF file producing records for the mols with data taken from the comments.
 *
 */
public class SDFFieldReader {
    
    private static final Logger LOGGER = Logger.getLogger(SDFFieldReader.class);
    
    private final FieldExtractor fieldExtractor;
    private InputStream incomingSDFStream;
    
    public SDFFieldReader(InputStream input, FieldExtractor extractor) {
        this.incomingSDFStream = input;
        this.fieldExtractor = extractor;
    }
    
    /**
     * This method writes the incoming SDF stream into separate MDL Mol files at the desired destination, and using
     * the database to which the ids belong to construct the path. For an individual identifer, the path can be reconstructed
     * using 
     * 
     * @param identifiers
     * @return the number of written files.
     * @throws IOException 
     */
    public Iterator<SDFRecord> readSDFFields() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.incomingSDFStream));
        
        String line;
        
        while ((line = reader.readLine()) != null) {
            if (line.contains("$$$$")) {
                if(fieldExtractor!=null) {
                    fieldExtractor.shiftRecord();
                }
            } else {
                if(fieldExtractor!=null)
                    fieldExtractor.feedLine(line);
            }
        }
        reader.close();
        
        return fieldExtractor.getSDFRecordsIterator();
    }
    
}
