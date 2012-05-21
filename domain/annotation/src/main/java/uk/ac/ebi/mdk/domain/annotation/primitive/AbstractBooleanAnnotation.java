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
package uk.ac.ebi.mdk.domain.annotation.primitive;

import org.apache.log4j.Logger;


/**
 * BooleanAnnotation 2012.01.12 <br/>
 * Provides an abstract layer for boolean annotations to build upon
 * 
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public abstract class AbstractBooleanAnnotation
        extends AbstractValueAnnotation<Boolean>
        implements BooleanAnnotation {

    private static final Logger LOGGER = Logger.getLogger(AbstractBooleanAnnotation.class);


    public AbstractBooleanAnnotation() {
    }


    public AbstractBooleanAnnotation(Boolean value) {
        super(value);
    }


    @Override
    public String toString() {
        return getValue() ? "Yes" : "No";
    }
}
