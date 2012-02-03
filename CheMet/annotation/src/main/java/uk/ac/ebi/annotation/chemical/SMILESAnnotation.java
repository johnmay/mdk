/**
 * SMILESAnnotation.java
 *
 * 2012.02.03
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
package uk.ac.ebi.annotation.chemical;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.AbstractStringAnnotation;
import uk.ac.ebi.interfaces.Annotation;

/**
 * @name    SMILESAnnotation
 * @date    2012.02.03
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class SMILESAnnotation extends AbstractStringAnnotation {
    
    private static final Logger LOGGER = Logger.getLogger(SMILESAnnotation.class);
    

    public SMILESAnnotation() {
    }
    
    public SMILESAnnotation(String smiles) {
        super.setValue(smiles);
    }
    
    public Annotation getInstance() {
        return new SMILESAnnotation();
    }

    public Annotation getInstance(String value) {
        return new SMILESAnnotation(value);
    }

    @Override
    public String getShortDescription() {
        return "SMILES";
    }

    @Override
    public String getLongDescription() {
        return "The simplified molecular-input line-entry specification";
    }
    
    
}
