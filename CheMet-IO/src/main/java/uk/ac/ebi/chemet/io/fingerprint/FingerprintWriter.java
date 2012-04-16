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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.BitSet;
import org.apache.log4j.Logger;
import org.openscience.cdk.fingerprint.IFingerprinter;

/**
 * @name    FingerPrintWriter
 * @date    2011.11.03
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   Class for writing finger prints.
 *
 */
public class FingerprintWriter extends BufferedWriter {
    
    private static final Logger LOGGER = Logger.getLogger(FingerprintWriter.class);
    private String fingerPrinterClassName;
    private IFingerprinter fingerprinter;
    Integer fingerprintSize;

    public FingerprintWriter(Writer writer, Class<? extends IFingerprinter> fingerprinter, Integer fingerprintSize) throws IOException {
        super(writer);
        init(fingerprinter, fingerprintSize);
    }

    public FingerprintWriter(Writer writer, int i, Class<? extends IFingerprinter> fingerprinter, Integer fingerprintSize) throws IOException {
        super(writer, i);
        init(fingerprinter, fingerprintSize);
    }
    
    private void init(Class<? extends IFingerprinter> fingerprinter, Integer fingerprintSize) throws IOException {
        fingerPrinterClassName = fingerprinter.getSimpleName();
        this.fingerprintSize = fingerprintSize;
        super.write(fingerPrinterClassName+"\n");
        super.write(fingerprintSize+"\n");
    }
    
    public void write(BitSet bitSet) throws IOException {
        super.write(bitSet.toString()+"\n");
    }
}
