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
package uk.ac.ebi.mdk.domain.entity.collection;

import uk.ac.ebi.mdk.domain.entity.Metabolite;

import java.util.ArrayList;
import java.util.Collection;


/**
 * @author johnmay
 * @date May 15, 2011
 */
public class MetabolomeImpl
        extends EntitySet<Metabolite> implements Metabolome {

    /**
     * Retrieves the metabolites that match the specified name.
     * Note. this is not a search over the list (as the name can change)
     *
     * @param name
     *
     * @return
     */
    public Collection<Metabolite> get(String name) {

        String clean = name.trim().toLowerCase();

        Collection<Metabolite> metabolites = new ArrayList<Metabolite>();

        for (Metabolite metabolite : this) {
            String cleanOther = metabolite.getName().trim().toLowerCase();
            if (cleanOther.equals(clean)) {
                metabolites.add(metabolite);
            }
        }

        return metabolites;

    }
}
