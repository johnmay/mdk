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
import uk.ac.ebi.annotation.util.AnnotationLoader;
import uk.ac.ebi.interfaces.Observation;
import uk.ac.ebi.interfaces.annotation.Context;
import uk.ac.ebi.interfaces.annotation.MetaInfo;
import uk.ac.ebi.interfaces.entities.Metabolite;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;


/**
 *          ChEBICrossReference – 2011.09.14 <br>
 *          Class metaInfo
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@Context(Metabolite.class)
@MetaInfo(brief       = "ChEBI Crossreference",
            description = "A crossreference specific to the ChEBI database")
public class ChEBICrossReference<O extends Observation>
        extends CrossReference<ChEBIIdentifier, O> {

    private static final Logger LOGGER = Logger.getLogger(ChEBICrossReference.class);

    private static uk.ac.ebi.core.MetaInfo metaInfo = AnnotationLoader.getInstance().getMetaInfo(
            ChEBICrossReference.class);


    public ChEBICrossReference() {
    }


    public ChEBICrossReference(ChEBIIdentifier identifier) {
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
    public Byte getIndex() {
        return metaInfo.index;
    }


    /**
     * @inheritDoc
     */
    @Override
    public ChEBICrossReference newInstance() {
        return new ChEBICrossReference();
    }
}
