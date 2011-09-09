/*
 *     This file is part of Metabolic Network Builder
 *
 *     Metabolic Network Builder is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.metabolomes.core.compound;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.openscience.cdk.interfaces.IMolecule;
import uk.ac.ebi.metabolomes.core.metabolite.MetabolicEntity;
import uk.ac.ebi.metabolomes.descriptor.annotation.GeneralAccessList;


/**
 *
 * @author johnmay
 * @date May 15, 2011
 */
public class MetaboliteCollection
  extends GeneralAccessList<MetabolicEntity>
  implements Serializable {

    private static final org.apache.log4j.Logger logger =
                                                 org.apache.log4j.Logger.getLogger(
      MetaboliteCollection.class);
    private static final long serialVersionUID = -694434528337274752L;
    HashSet<MetabolicEntity> unique = new HashSet<MetabolicEntity>();


    @Override
    public boolean add(MetabolicEntity e) {
        if( unique.contains(e) ) {
            return false;
        }
        unique.add(e);
        return super.add(e);
    }


    @Override
    public boolean addAll(Collection<? extends MetabolicEntity> c) {
        throw new UnsupportedOperationException();
    }


}

