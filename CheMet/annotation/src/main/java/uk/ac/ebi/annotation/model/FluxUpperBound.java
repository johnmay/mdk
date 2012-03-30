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
import uk.ac.ebi.interfaces.annotation.Context;
import uk.ac.ebi.interfaces.annotation.MetaInfo;
import uk.ac.ebi.interfaces.entities.Reaction;


/**
 *
 * FluxUpperBound 2012.01.12
 *
 * @version $Rev$ : Last Changed $Date$
 * @author johnmay
 * @author $Author$ (this version)
 *
 * Class description
 *
 */
@Context(Reaction.class)
@MetaInfo(brief = "Flux Upper Bound",
            description = "An upper bound for reaction flux")
public class FluxUpperBound extends FluxBound {

    private static final Logger LOGGER = Logger.getLogger(FluxUpperBound.class);


    public FluxUpperBound() {
    }


    public FluxUpperBound(Double value) {
        super(value);
    }


    public FluxUpperBound newInstance() {
        return new FluxUpperBound();
    }
}
