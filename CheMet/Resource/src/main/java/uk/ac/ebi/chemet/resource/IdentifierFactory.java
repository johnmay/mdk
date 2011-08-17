/**
 * IdentifierFactory.java
 *
 * 2011.08.16
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
package uk.ac.ebi.chemet.resource;

import org.apache.log4j.Logger;
import uk.ac.ebi.metabolomes.identifier.AbstractIdentifier;
import uk.ac.ebi.metabolomes.identifier.GenericIdentifier;

/**
 * @name    IdentifierFactory – 2011.08.16
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class IdentifierFactory {

    private static final Logger LOGGER = Logger.getLogger( IdentifierFactory.class );

    private IdentifierFactory() {
    }

    public static IdentifierFactory getInstance() {
        return IdentifierFactoryHolder.INSTANCE;
    }

    private static class IdentifierFactoryHolder {

        public static IdentifierFactory INSTANCE = new IdentifierFactory();
    }

    /**
     * Builds an identifier from a MIRIAM Registry
     * @param urn
     * @return
     */
    public AbstractIdentifier getIdentifier(MIRIAMURN urn){
        throw new UnsupportedOperationException("TODO");
    }


}
