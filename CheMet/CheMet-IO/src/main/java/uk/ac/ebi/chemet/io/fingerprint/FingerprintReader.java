/**
 * FingerPrintReader.java
 *
 * 2011.11.03
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
package uk.ac.ebi.chemet.io.fingerprint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.BitSet;
import org.apache.log4j.Logger;
import org.openscience.cdk.fingerprint.IFingerprinter;

/**
 * @name    FingerPrintReader
 * @date    2011.11.03
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class FingerprintReader extends BufferedReader {
    
    private static final Logger LOGGER = Logger.getLogger(FingerprintReader.class);
    private String fingerPrintClassName;
    private Integer fingerprintLength;

    public FingerprintReader(Reader reader, Class<? extends IFingerprinter> fingerPrinter) throws IOException, FingerprintIOException {
        super(reader);
        fingerPrintClassName = fingerPrinter.getSimpleName();
        String line = super.readLine();
        if(!line.equals(fingerPrintClassName)) {
            throw new FingerprintIOException("Writing and reading fingerprint classes don't match.");
        }
        line = super.readLine();
        try {
            this.fingerprintLength = Integer.parseInt(line);
        } catch(NumberFormatException e) {
            throw new FingerprintIOException("Could not find fingerprint length in the file... this might not be a compliant file.");
        }
    }
    
    public BitSet readBitSet() throws IOException {
        String line = super.readLine();
        if(line==null)
            return null;
        BitSet bitSet = new BitSet(this.fingerprintLength);
        line = line.replace("{", "").replace("}", "").trim();
        for (String position : line.split(", ")) {
            try {
            Integer pos = Integer.parseInt(position);
            bitSet.set(pos);
            } catch(NumberFormatException e) {
                continue;
            }
        }
        return bitSet;
    }
    
    
}
