/**
 * PubChemSubstanceESummaryResult.java
 *
 * 2011.10.30
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
package uk.ac.ebi.metabolomes.webservices.eutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ebi.resource.IdentifierFactory;

/**
 * @name    PubChemCompoundESummaryResult
 * @date    2011.10.30
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   Eutils ESummary request result for PubChem Compounds
 *
 */
public class PubChemCompoundESummaryResult extends ESummaryResult {

    private static final Logger LOGGER = Logger.getLogger(PubChemCompoundESummaryResult.class);
    private static final IdentifierFactory FACTORY = IdentifierFactory.getInstance();

    /**
     * @return the preferredName
     */
    public String getPreferredName() {
        return preferredName;
    }

    /**
     * @param preferredName the preferredName to set
     */
    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    /**
     * This enum holds the fields that are lists (more than one value)
     * that are currently parsed by the XML parser. If more list fields are desired for parsing (that are present in the
     * response, they should be added here).
     */
    public enum PubChemCompoundESummaryListFields {

        SynonymList;
    }

    /**
     * This enum holds the fields that are scalars (just one value) that are currently parsed by the XML parser. If more
     * scalar fields are desired for parsing, they should be added here.
     */
    public enum PubChemCompoundESummarySingleFields {

        CID, IUPACName;
    }
    
    /**
     * This is all internal representation.
     */
    private String id;
    private String iupacName;
    private String preferredName; // normally the first synonym.
    private final List<String> synonyms = new ArrayList<String>();

    public List<PubChemCompoundESummarySingleFields> getScalarKeywords() {
        return Arrays.asList(PubChemCompoundESummarySingleFields.values());
    }

    public List<PubChemCompoundESummaryListFields> getListKeywords() {
        return Arrays.asList(PubChemCompoundESummaryListFields.values());
    }

    public void addListForKeyword(Enum keyword, List<String> parseList) {
        if (keyword instanceof PubChemCompoundESummaryListFields) {
            switch ((PubChemCompoundESummaryListFields) keyword) {
                case SynonymList:
                    this.setSynonyms(parseList);
                    break;
            }
        }
    }

    public void addScalarForKeyword(Enum keyword, String followingCharacters) {
        if (keyword instanceof PubChemCompoundESummarySingleFields) {
            switch ((PubChemCompoundESummarySingleFields) keyword) {
                case CID:
                    this.setId(followingCharacters);
                    break;
                case IUPACName:
                    this.setIupacName(followingCharacters);
                    break;
            }
        }
    }

    @Override
    public void wrap() {
        super.wrap();
        // here we could set the preferred name to either the first synonym or the iupac name.
        if(this.synonyms.size()>0)
            this.setPreferredName(this.synonyms.get(0));
        else
            this.setPreferredName(this.iupacName);
    }
    
    private void setIupacName(String iupacName) {
        this.iupacName = iupacName;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the synonyms
     */
    public List<String> getSynonyms() {
        return synonyms;
    }

    /**
     * @param synonyms the synonyms to set
     */
    public void setSynonyms(List<String> synonyms) {
        this.synonyms.addAll(synonyms);
    }

    public void addSynonym(String synonym) {
        this.synonyms.add(synonym);
    }

}
