/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.domain.annotation;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.domain.entity.Reaction;


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
@Brief("Flux Lower Bound")
@Description("A lower bound for reaction flux")
public class FluxLowerBound extends FluxBound {

    private static final Logger LOGGER = Logger.getLogger(FluxLowerBound.class);


    public FluxLowerBound() {
    }


    public FluxLowerBound(Double value) {
        super(value);
    }


    public FluxLowerBound newInstance() {
        return new FluxLowerBound();
    }

    @Override public FluxLowerBound forValue(double value) {
        return new FluxLowerBound(value);
    }
}
