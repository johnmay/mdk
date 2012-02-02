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

package uk.ac.ebi.core.metabolite;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import uk.ac.ebi.core.EntityList;


/**
 *
 * @author johnmay
 * @date May 15, 2011
 */
public class MetaboliteCollection
  extends EntityList<uk.ac.ebi.interfaces.entities.Metabolite>
  implements Serializable {

    private static final org.apache.log4j.Logger logger =
                                                 org.apache.log4j.Logger.getLogger(
      MetaboliteCollection.class);
    HashSet<uk.ac.ebi.interfaces.entities.Metabolite> unique = new HashSet<uk.ac.ebi.interfaces.entities.Metabolite>();


    @Override
    public boolean add(uk.ac.ebi.interfaces.entities.Metabolite e) {
        if( unique.contains(e) ) {
            return false;
        }
        unique.add(e);
        return super.add(e);
    }


    @Override
    public boolean addAll(Collection<? extends uk.ac.ebi.interfaces.entities.Metabolite> c) {
        for( uk.ac.ebi.interfaces.entities.Metabolite metabolite : c ) {
            boolean results = add(metabolite);
        }
        return true;
    }

}

