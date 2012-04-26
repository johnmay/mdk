/**
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.ebi.annotation.chemical;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.base.AbstractFloatAnnotation;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.resource.DefaultLoader;


/**
 * @name    SMILESAnnotation
 * @date    2012.02.03
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class metaInfo...
 *
 */
@Context(Metabolite.class)
@Brief("Exact Mass")
@Description("The exact mass is the sum of the masses of the atoms in a molecule using the most abundant isotope for each element")
public class ExactMass extends AbstractFloatAnnotation {

    private static final Logger LOGGER = Logger.getLogger(ExactMass.class);

    private static uk.ac.ebi.core.MetaInfo metaInfo = DefaultLoader.getInstance().getMetaInfo(
            ExactMass.class);


    public ExactMass() {
    }


    public ExactMass(Float exactMass) {
        super.setValue(exactMass);
    }


    public Annotation newInstance() {
        return new ExactMass();
    }


    public Annotation getInstance(Float exactMass) {
        return new ExactMass(exactMass);
    }


    @Override
    public String getShortDescription() {
        return metaInfo.brief;
    }


    @Override
    public String getLongDescription() {
        return metaInfo.description;
    }
}
