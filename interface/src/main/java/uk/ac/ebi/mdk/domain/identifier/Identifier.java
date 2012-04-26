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
package uk.ac.ebi.mdk.domain.identifier;

import java.net.URL;
import java.util.Collection;

import uk.ac.ebi.mdk.domain.Descriptor;

/**
 * Identifier – 2011.09.15 <br>
 * Interface for an identifier object
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public interface Identifier extends Descriptor {

    /**
     * Mutator for the identifier accession.
     *
     * @param accession
     */
    public void setAccession(String accession);

    /**
     * Access the accession value (as a string) for this identifier type
     *
     * @return The string accession
     */
    public String getAccession();

    /**
     * Returns a new empty instance of the identifier object. This is primarily used
     * in factories for object creation without reflection.
     *
     * @return new instance of the identifier
     */
    public Identifier newInstance();

    /**
     * Access a URL for the identifier item.
     *
     * @return URL for the identifier
     */
    public URL getURL();

    /**
     * Access the URN for use in RDF annotations, in particular the
     * Systems Biology Markup Language (SBML).
     *
     * @return String urn for the identifier (note some accessions
     *         may have special characters substituted)
     */
    public String getURN();

    /**
     * Provides a list of synonyms commonly used to describe the database of this
     * identifier. For example Enzyme Nomenclature is more frequently referred to
     * as EC. The synonyms are specified in the IdentifierDescription.properites
     * file. If no synonyms are present an empty collection is returned
     *
     * @return
     */
    public Collection<String> getSynonyms();

    public Resource getResource();

    /**
     * Access a summary of the identifier. The summary provides the
     * name of the identifier concatenated to the accession.
     *
     * @return summary
     */
    public String getSummary();


}
