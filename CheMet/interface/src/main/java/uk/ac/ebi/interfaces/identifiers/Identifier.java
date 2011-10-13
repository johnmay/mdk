
/**
 * Identifier.java
 *
 * 2011.09.15
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
package uk.ac.ebi.interfaces.identifiers;

import java.net.URL;
import uk.ac.ebi.interfaces.Descriptor;
import uk.ac.ebi.interfaces.Resource;


/**
 *          Identifier – 2011.09.15 <br>
 *          Interface for an identifier object
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface Identifier extends Descriptor {


    /**
     *
     * Access the stored accession
     *
     * @param accession
     *
     */
    public void setAccession(String accession);


    /**
     *
     * Returns the accession of the identifier
     *
     * @return The string accession
     *
     */
    public String getAccession();


    /**
     *
     * Returns a new empty instance of the identifier object. Primarily used in factory methods
     *
     * @return new instance of the identifier
     *
     */
    public Identifier newInstance();


    /**
     *
     * Return the URL of the database item
     *
     */
    public URL getURL();


    /**
     *
     * Return the URN for use in Systems Biology Markup Language
     *
     */
    public String getURN();


    public Resource getResource();


}

