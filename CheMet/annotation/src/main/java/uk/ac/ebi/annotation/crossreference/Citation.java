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
import uk.ac.ebi.interfaces.identifiers.Identifier;


/**
 *          Citation - 2011.10.28 <br>
 *          A citation refers to a publication identifier (e.g. PubMedIdentifier
 *          or DOI)
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@Context
@MetaInfo(brief = "Citation",
            description = "A citation refering to an non-descript resource")
public class Citation extends CrossReference<Identifier, Observation> {

    private static final Logger LOGGER = Logger.getLogger(Citation.class);

    private static uk.ac.ebi.core.MetaInfo metaInfo = AnnotationLoader.getInstance().getMetaInfo(
            Citation.class);


    public Citation() {
    }


    public Citation(Identifier identifier) {
        super(identifier);
    }


    @Override
    public String getShortDescription() {
        return metaInfo.brief;
    }


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


    @Override
    public Citation newInstance() {
        return new Citation();
    }
}
