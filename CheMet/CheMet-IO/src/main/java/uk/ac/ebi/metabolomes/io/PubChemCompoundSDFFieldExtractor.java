/**
 * PubChemSDFFieldExtractor.java
 *
 * 2011.10.12
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

import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * @name    PubChemSDFFieldExtractor
 * @date    2011.10.12
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class PubChemCompoundSDFFieldExtractor extends AbstractSDFFieldExtractor {
    
    private static final Logger LOGGER = Logger.getLogger(PubChemCompoundSDFFieldExtractor.class);

    private boolean nextLineID=false;
    private boolean nextLineName=false;
    private boolean nextLineSyn=false;
    private boolean nextLineInChI=false;

    
    public PubChemCompoundSDFFieldExtractor() {
    }

    private Pattern pubchemSyns = Pattern.compile("> <PUBCHEM_.+_NAME>");
    
    public void feedLine(String line) {
        if(nextLineID) { 
            this.currentRecord.setId(line);
            nextLineID=false;
        } else if(nextLineName) {
            this.currentRecord.setName(line);
            nextLineName=false;
        } else if(nextLineSyn) {
            this.currentRecord.addSynonym(line);
            nextLineSyn=false;
        } else if(nextLineInChI) {
            this.currentRecord.setInChI(line);
            nextLineInChI=false;
        } else {
            
            if(line.startsWith("> <PUBCHEM_COMPOUND_CID>"))
                nextLineID=true;
            else if(line.startsWith("> <PUBCHEM_IUPAC_TRADITIONAL_NAME>"))
                nextLineName=true;
            else if(line.startsWith("> <PUBCHEM_IUPAC_INCHI>"))
                nextLineInChI=true;
            else if(pubchemSyns.matcher(line).find())
                nextLineSyn=true;
            
        }
    }

    public boolean proposeIdentifier() {
        return false;
    }

    public String getProposedIdentifier() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
