/**
 * BasicStringAnnotation.java
 *
 * 2011.12.08
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
package uk.ac.ebi.interfaces.annotation;

import uk.ac.ebi.interfaces.Annotation;

/**
 *          BooleanAnnotation - 2011.12.08 <br>
 *
 * @version $Rev: 1341 $ : Last Changed $Date: 2012-02-03 22:28:14 +0000 (Fri, 03 Feb 2012) $
 * @author  johnmay
 * @author  $Author: pmoreno $ (this version)
 */
public interface BooleanAnnotation extends Annotation {

    public Boolean getValue();

    public void setValue(Boolean value);

    public Annotation getInstance(Boolean value);

}
