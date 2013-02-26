/**
 * AtomCountComparator.java
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
package uk.ac.ebi.mdk.tool.comparator;

import java.util.Comparator;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * @name    AtomCountComparator
 * @date    2012.01.23
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class AtomCountComparator implements Comparator<IAtomContainer> {

    public int compare(IAtomContainer t, IAtomContainer t1) {
        if (t == null && t1 == null) {
            return 0;
        } else if (t == null && t1 != null) {
            return -1;
        } else if (t != null && t1 == null) {
            return 1;
        }
        return -1 * ((Integer) t.getAtomCount()).compareTo(((Integer) t1.getAtomCount()));
    }
}
