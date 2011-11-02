/**
 * SynonymCandidateEntry.java
 *
 * 2011.09.23
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
package uk.ac.ebi.metabolomes.webservices.util;

import java.util.Collection;
import java.util.HashSet;
import org.apache.log4j.Logger;

/**
 *          SynonymCandidateEntry – 2011.09.23 <br>
 *          Similar to candidate entry but maintains a collection of descriptions for a single
 *          identifier. Only the best distance is stored
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class SynonymCandidateEntry
        extends CandidateEntry {

    private static final Logger LOGGER = Logger.getLogger(SynonymCandidateEntry.class);
    private Collection<String> synonyms;

    public SynonymCandidateEntry(String accession,
                                 String description,
                                 Collection<String> synonyms,
                                 int distance,
                                 int index,
                                 String comment) {
        super(accession, description, distance, comment);
        this.synonyms = synonyms;
    }

    public SynonymCandidateEntry(String accession,
                                 String description,
                                 Collection<String> synonyms,
                                 int distance,
                                 int index) {
        this(accession, description, synonyms, distance, index, "Synonym Candidate");
    }

    public SynonymCandidateEntry(String accession,
                                 String description) {
        super(accession, description, 10, "Synonym Candidate");
        this.synonyms = new HashSet<String>();
    }

    public boolean addSynonym(String synonym) {
        return this.synonyms.add(synonym);
    }

    public Collection<String> getSynonyms() {
        return synonyms;
    }
}
