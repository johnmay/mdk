
/**
 * UnfetchableEntry.java
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
 *          UnfetchableEntry – 2011.08.23 <br>
 *          Class should be thrown if a web service could not retrieve a record for the given id
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class UnfetchableEntry extends RuntimeException {

    private static final Logger LOGGER = Logger.getLogger(UnfetchableEntry.class);
    public static final String NO_MATCH_FOUND = "no match was found";
    public static final String CLIENT_EXCEPTION = "the client threw an exception";


    public UnfetchableEntry() {
    }


    public UnfetchableEntry(String accession, String ws, String reason) {
       super( new StringBuilder(60).append("Unable to fetch ").append(accession).
          append(" from ").append(ws).
          append(" because ").append(reason).toString());
    }

    


}

