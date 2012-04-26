/**
 * MetaboliteMassComparator.java
 *
 * 2012.02.05
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

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.chemical.ExactMass;
import uk.ac.ebi.mdk.domain.entity.Metabolite;

/**
 * @name    MetaboliteMassComparator
 * @date    2012.02.05
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class MetaboliteMassComparator extends AbstractLinkedComparator<Metabolite> {
    
    private static final Logger LOGGER = Logger.getLogger(MetaboliteMassComparator.class);

    @Override
    protected int internalComparator(Metabolite s, Metabolite s1) {
        Float mass = getMass(s);
        Float mass1 = getMass(s1);
        
        return mass.compareTo(mass1);
    }

    private Float getMass(Metabolite s) {
        List<ExactMass> massAnnots = new ArrayList<ExactMass>(s.getAnnotations(ExactMass.class));
        if(massAnnots.size()>0 && massAnnots.get(0).getValue()!=null)
            return massAnnots.get(0).getValue();
        return -1.0f;
    }
}
