/**
 * Resource.java
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
package uk.ac.ebi.interfaces;

import java.net.URL;
import java.util.regex.Pattern;

/**
 *          Resource – 2011.09.15 <br>
 * 
 *          Identifier resource description of an identifier. Provides basic
 *          functionality such as Name, Definition and URL link. The internal
 *          URL should provide the address of an entry with the provided accession.
 *          The compiled pattern determines whether the accession patches the pattern
 *          for this resources identifier
 *
 *          This class primarily serves as a mapping for a
 *          <a href="http://www.ebi.ac.uk/miriam">MIRIAM Registry</a> entry
 *
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface Resource {

    /**
     * Name of the resource
     * @return
     */
    public String getName();

    /**
     * Description of the resource
     */
    public String getDescription();

    /**
     * Persist URN of resource for provided
     * accession (if available)
     * @param accession
     * @return
     */
    public String getURN(String accession);

    /**
     * Accessible URL of resource for the provided
     * accession
     * @param accession
     * @return
     */
    public URL getURL(String accession);

    /**
     * Access the required pattern for
     * this resource
     * @return
     */
    public Pattern getCompiledPattern();
}
