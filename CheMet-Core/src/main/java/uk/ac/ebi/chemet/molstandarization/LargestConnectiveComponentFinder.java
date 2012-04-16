/**
 * LargestConnectiveComponentFinder.java
 *
 * 2012.01.23
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
package uk.ac.ebi.chemet.molstandarization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.log4j.Logger;
import org.openscience.cdk.MoleculeSet;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMoleculeSet;

/**
 * @name    LargestConnectiveComponentFinder
 * @date    2012.01.23
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   Receives an IAtomContainer, separates it into its connective components and allows to retrieve any of them,
 *          ordering them by size (number of atoms).
 *
 */
public class LargestConnectiveComponentFinder {
    
    private static final Logger LOGGER = Logger.getLogger(LargestConnectiveComponentFinder.class);
    
    public ConnectiveComponents processMolecule(IAtomContainer mol) {
        if(!ConnectivityChecker.isConnected(mol)) {
            IMoleculeSet comps = ConnectivityChecker.partitionIntoMolecules(mol);
            ConnectiveComponents res = new ConnectiveComponents(comps);
            return res;
        }
        IMoleculeSet molSetSingleton = new MoleculeSet();
        molSetSingleton.addAtomContainer(mol);
        return new ConnectiveComponents(molSetSingleton);
    }
}
