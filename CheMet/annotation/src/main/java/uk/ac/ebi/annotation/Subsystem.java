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
package uk.ac.ebi.annotation;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.base.AbstractStringAnnotation;
import uk.ac.ebi.chemet.Brief;
import uk.ac.ebi.chemet.Description;
import uk.ac.ebi.interfaces.annotation.Context;
import uk.ac.ebi.interfaces.entities.Reaction;


/**
 *          Subsystem – 2011.09.26 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@Context(Reaction.class)
@Brief("Subsystem")
@Description("Abstract functional role of this reaction")
public class Subsystem
        extends AbstractStringAnnotation {

    private static final Logger LOGGER = Logger.getLogger(Subsystem.class);


    public Subsystem() {
    }


    public Subsystem(String subsystem) {
        super(subsystem);
    }


    public Subsystem newInstance() {
        return new Subsystem();
    }


    public Subsystem getInstance(String subsystem) {
        return new Subsystem(subsystem);
    }
}
