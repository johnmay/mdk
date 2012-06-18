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
 */
package uk.ac.ebi.mdk.domain.annotation;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.annotation.primitive.AbstractDoubleAnnotation;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.lang.annotation.Unique;
import uk.ac.ebi.mdk.domain.entity.Metabolite;


/**
 *          ChargeAnnotation 2012.02.14 <br/>
 *          Describes the charge on a metabolite
 * 
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 */
@Unique
@Context(Metabolite.class)
@Brief("Charge")
@Description("The charge of this metabolite")
public class Charge
        extends AbstractDoubleAnnotation {

    private static final Logger LOGGER = Logger.getLogger(Charge.class);


    public Charge() {
    }


    public Charge(Double value) {
        super(value);
    }


    public Annotation newInstance() {
        return new Charge();
    }
}
