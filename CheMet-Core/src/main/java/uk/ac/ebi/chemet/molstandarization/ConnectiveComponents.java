/**
 * ConnectiveComponents.java
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
import java.util.List;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMoleculeSet;
import uk.ac.ebi.chemet.mol.comparison.AtomCountComparator;

/**
 * @name    ConnectiveComponents
 * @date    2012.01.23
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class ConnectiveComponents {

    List<IAtomContainer> components = new ArrayList<IAtomContainer>();
    AtomCountComparator comparator = new AtomCountComparator();

    public ConnectiveComponents(IMoleculeSet mols) {
        for (IAtomContainer mol : mols.atomContainers()) {
            if (mol == null) {
                continue;
            }
            if (mol.getAtomCount() == 0) {
                continue;
            }
            components.add(mol);
        }
        Collections.sort(components, comparator);
    }

    /**
     * Returns the number of connective components stored.
     * @return 
     */
    public int componentsCount() {
        return components.size();
    }

    /**
     * Checks whether the maximum size (atom count) within the components is achieved by one component only or if there
     * are more components with the same size.
     * 
     * @return 
     */
    public boolean hasSingleLargestComponent() {
        if (components.size() <= 1) {
            return true;
        }
        // more than one component
        return comparator.compare(components.get(0), components.get(1)) < 0;
    }

    /**
     * Returns the first largest connective component, the first element of the ConnectiveComponents. This is returned
     * regardless of whether there might be other following components with the same amount of atoms.
     * 
     * @return 
     */
    public IAtomContainer getFirstLargestComponent() {
        return components.get(0);
    }
}
