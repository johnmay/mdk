/**
 * FingerPrintWriter.java
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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openscience.cdk.fingerprint.IFingerprinter;

/**
 * @name    FingerPrintWriter
 * @date    2011.11.03
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   This class prints fingerprints in their extended forms (not avoiding zeros), so that it can be used, for instance
 *          for classification purposes.
 *
 */
public class VectorFingerprintWriter extends FingerprintWriter {
    
    private static final Logger LOGGER = Logger.getLogger(VectorFingerprintWriter.class);
    private String fingerPrinterClassName;
    private IFingerprinter fingerprinter;
    private String separator="";

    public VectorFingerprintWriter(Writer writer, Class<? extends IFingerprinter> fingerprinter, Integer fingerprintSize) throws IOException {
        super(writer,fingerprinter,fingerprintSize);
    }

    public VectorFingerprintWriter(Writer writer, int i, Class<? extends IFingerprinter> fingerprinter, Integer fingerprintSize) throws IOException {
        super(writer, i, fingerprinter, fingerprintSize);
    }
    
    @Override
    public void write(BitSet bitSet) throws IOException {
        
        List<String> line = new ArrayList<String>(this.fingerprintSize);
        for (int i = 0; i < this.fingerprintSize; i++) {
            if(bitSet.get(i))
                line.add("1");
            else
                line.add("0");
        }
        super.write(StringUtils.join(line, this.separator)+"\n");
        //super.write(bitSet.toString()+"\n");
    }

    /**
     * @return the separator
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * @param separator the separator to set
     */
    public void setSeparator(String separator) {
        this.separator = separator;
    }
    
    
}
