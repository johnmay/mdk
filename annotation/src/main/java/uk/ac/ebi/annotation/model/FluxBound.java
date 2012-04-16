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

package uk.ac.ebi.annotation.model;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.base.AbstractDoubleAnnotation;
import uk.ac.ebi.interfaces.Annotation;


/**
 *
 * FluxBound 2012.01.12
 *
 * @version $Rev$ : Last Changed $Date$
 * @author johnmay
 * @author $Author$ (this version)
 *
 * Class description
 *
 */
public abstract class FluxBound
        extends AbstractDoubleAnnotation {

    private static final Logger LOGGER = Logger.getLogger(FluxBound.class);


    public FluxBound() {
    }


    public FluxBound(Double value) {
        super(value);
    }
    
    
    
}
