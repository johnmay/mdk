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
package uk.ac.ebi.annotation.crossreference;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.Brief;
import uk.ac.ebi.chemet.Description;
import uk.ac.ebi.interfaces.Observation;
import uk.ac.ebi.interfaces.annotation.Context;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.resource.DefaultLoader;


/**
 * Classification – 2011.09.14 <br>
 * Base class for classification cross references such as EC, TCDB codes and GO Terms
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
@Context
@Brief("Classification")
@Description("A cross-reference that specifically links to a classifcation identifier")
public class Classification<E extends Identifier, O extends Observation>
        extends CrossReference<E, O> {

    private static final Logger LOGGER = Logger.getLogger(Classification.class);

    private static uk.ac.ebi.core.MetaInfo metaInfo = DefaultLoader.getInstance().getMetaInfo(
            Classification.class);


    public Classification() {
    }


    public Classification(E identifier) {
        super(identifier);
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getShortDescription() {
        return metaInfo.brief;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getLongDescription() {
        return metaInfo.description;
    }


    /**
     * @inheritDoc
     */
    @Override
    public Classification newInstance() {
        return new Classification();
    }
}
