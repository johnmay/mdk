/**
 * SMILESAnnotation.java
 *
 * 2012.02.03
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
package uk.ac.ebi.annotation.chemical;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.base.AbstractFloatAnnotation;
import uk.ac.ebi.annotation.util.AnnotationLoader;
import uk.ac.ebi.core.Description;
import uk.ac.ebi.interfaces.Annotation;
import uk.ac.ebi.interfaces.annotation.Context;
import uk.ac.ebi.interfaces.entities.Metabolite;


/**
 * @name    SMILESAnnotation
 * @date    2012.02.03
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
@Context(Metabolite.class)
public class ExactMass extends AbstractFloatAnnotation {

    private static final Logger LOGGER = Logger.getLogger(ExactMass.class);

    private static Description description = AnnotationLoader.getInstance().getMetaInfo(
            ExactMass.class);


    public ExactMass() {
    }


    public ExactMass(Float exactMass) {
        super.setValue(exactMass);
    }


    public Annotation getInstance() {
        return new ExactMass();
    }


    public Annotation getInstance(Float exactMass) {
        return new ExactMass(exactMass);
    }


    @Override
    public String getShortDescription() {
        return description.shortDescription;
    }


    @Override
    public String getLongDescription() {
        return description.longDescription;
    }
}
