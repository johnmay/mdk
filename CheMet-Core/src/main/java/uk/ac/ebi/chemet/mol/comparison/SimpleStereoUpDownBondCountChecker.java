/**
 * SimpleStereoUpDownBondCountChecker.java
 *
 * 2012.03.11
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
package uk.ac.ebi.chemet.mol.comparison;

import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

/**
 * @name    SimpleStereoUpDownBondCountChecker
 * @date    2012.03.11
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   Compares up and down stereo bond counts, allowing for inversion of ups down in case the molecules are drawn up side down.
 *
 */
public class SimpleStereoUpDownBondCountChecker {
    
    private static final Logger LOGGER = Logger.getLogger(SimpleStereoUpDownBondCountChecker.class);
    
    public Integer getUpBonds(IAtomContainer mol) {
        Integer count=0;
        for (IBond iBond : mol.bonds()) {
            if(iBond.getStereo().equals(IBond.Stereo.UP) ||
                    iBond.getStereo().equals(IBond.Stereo.UP_INVERTED))
                count++;
        }
        return count;
    }
    
    public Integer getDownBonds(IAtomContainer mol) {
        Integer count = 0;
        for (IBond iBond : mol.bonds()) {
            if(iBond.getStereo().equals(IBond.Stereo.DOWN) ||
                    iBond.getStereo().equals(IBond.Stereo.DOWN_INVERTED))
                count++;
        }
        return count;
    }
    
    public boolean equals(IAtomContainer molA, IAtomContainer molB) {
        Integer upA = getUpBonds(molA);
        Integer doA = getDownBonds(molA);
        
        Integer upB = getUpBonds(molB);
        Integer doB = getDownBonds(molB);
        
        // if the number of up and downs bonds and different, they are clearly different.
        if((upA + doA) != (upB + doB))
            return false;
            
        return ((upA == upB) && (doA == doB)) || ((upA == doB) && (doA == upB));
    }
}
