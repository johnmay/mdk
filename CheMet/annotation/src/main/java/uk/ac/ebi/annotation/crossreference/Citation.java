/**
 * Citation.java
 *
 * 2011.10.28
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
package uk.ac.ebi.annotation.crossreference;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.util.AnnotationLoader;
import uk.ac.ebi.core.Description;
import uk.ac.ebi.interfaces.identifiers.Identifier;

/**
 *          Citation - 2011.10.28 <br>
 *          A citation refers to a publication identifier (e.g. PubMedIdentifier
 *          or DOI)
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class Citation extends CrossReference<Identifier> {

    private static final Logger LOGGER = Logger.getLogger(Citation.class);
    private static Description description = AnnotationLoader.getInstance().getMetaInfo(
            Citation.class);

    public Citation() {
    }

    public Citation(Identifier identifier) {
        super(identifier);
    }


    /**
     * @inheritDoc
     */
    @Override
    public Byte getIndex() {
        return description.index;
    }

    @Override
    public Citation getInstance() {
        return new Citation();
    }
}
