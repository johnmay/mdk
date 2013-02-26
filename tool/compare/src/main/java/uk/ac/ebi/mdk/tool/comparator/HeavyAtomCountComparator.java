/**
 * HeavyAtomCountComparator.java
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
package uk.ac.ebi.mdk.tool.comparator;

import java.util.Comparator;
import java.util.List;
import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IElement;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

/**
 * @name    HeavyAtomCountComparator
 * @date    2012.03.11
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class HeavyAtomCountComparator implements Comparator<IAtomContainer> {
    
    private static final Logger LOGGER = Logger.getLogger(HeavyAtomCountComparator.class);
    
    @Override
    public int compare(IAtomContainer molA, IAtomContainer molB) {
        IMolecularFormula formA = MolecularFormulaManipulator.getMolecularFormula(molA);
        IMolecularFormula formB = MolecularFormulaManipulator.getMolecularFormula(molB);
        
        List<IElement> heavyElementsA = MolecularFormulaManipulator.getHeavyElements(formA);
        List<IElement> heavyElementsB = MolecularFormulaManipulator.getHeavyElements(formB);
        
        return ((Integer)heavyElementsA.size()).compareTo(heavyElementsB.size());
    }
}
