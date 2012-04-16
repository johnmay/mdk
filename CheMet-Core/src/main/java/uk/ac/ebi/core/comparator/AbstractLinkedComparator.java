/**
 * AbstractLinkedComparator.java
 *
 * 2012.02.04
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
package uk.ac.ebi.core.comparator;

import java.util.Comparator;

/**
 * @name    AbstractLinkedComparator
 * @date    2012.02.04
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public abstract class AbstractLinkedComparator<S> implements Comparator<S> {
    
    Comparator<S> secondary;

    public AbstractLinkedComparator() {
    }

    public void setSecondary(Comparator<S> secondary) {
        this.secondary = secondary;
    }
    
    public int compare(S s, S s1) {
        int comp = internalComparator(s, s1);
        return comp == 0 && secondary!=null ? secondary.compare(s, s1) : comp;
    }
    
    protected abstract int internalComparator(S s, S s1);
}
