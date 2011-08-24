
/**
 * MissingStructureException.java
 *
 * 2011.08.23
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
package uk.ac.ebi.chemet.ws.exceptions;

import org.apache.log4j.Logger;


/**
 *          MissingStructureException – 2011.08.23 <br>
 *          Class should be thrown if a web service returns an entity but is unable to access the
 *          structural information (i.e. InChI/SMILES/MDL)
 *
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MissingStructureException extends Exception {

    private static final Logger LOGGER = Logger.getLogger( MissingStructureException.class );
    private final String id;


    public MissingStructureException( String id ) {
        this.id = id;
    }


    @Override
    public String getMessage() {
        return "Unable to retrieve structural infomation for id: " + id;
    }


}

