/**
 * Source.java
 *
 * 2012.02.01
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
package uk.ac.ebi.annotation;

import uk.ac.ebi.annotation.base.AbstractStringAnnotation;
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.Annotation;
import uk.ac.ebi.interfaces.annotation.Context;
import uk.ac.ebi.interfaces.annotation.Descriptor;


/**
 *
 *          Source 2012.02.01
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Source annotation describes where the entity came from (e.g. ChEBI Database)
 *
 */
@Context
@Descriptor(brief = "Soruce",
            description = "Non-semantic description of where the entity has come from")
public class Source extends AbstractStringAnnotation {

    private static final Logger LOGGER = Logger.getLogger(Source.class);


    public Source() {
    }


    public Source(String value) {
        super(value);
    }


    public Source getInstance() {
        return new Source();
    }


    public Annotation getInstance(String value) {
        return new Source(value);
    }
}
