/**
 * ChEBIIdentifier.java
 *
 * 2011.08.05
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
package uk.ac.ebi.metabolomes.identifier;

import org.apache.log4j.Logger;

/**
 * @name    ChEBIIdentifier
 * @date    2011.08.05
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class ChEBIIdentifier extends AbstractIdentifier{

    private static final Logger LOGGER = Logger.getLogger( ChEBIIdentifier.class );

    protected ChEBIIdentifier(String identifier ) {

    }

    @Override
    public String parse( String identifier ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }



}
