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

/**
 * @name    PubChemCompoundESummaryResult
 * @date    2011.10.30
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   Eutils ESummary request result for PubChem Compounds
 *
 */
public class MeSHESummaryResult extends ESummaryResult {

    private static final Logger LOGGER = Logger.getLogger(MeSHESummaryResult.class);
    
    String meshTermName;
    String id;
    String parent;
    List<String> children;

    private void setMeSHTerm(List<String> parseList) {
        if(parseList.size()>0)
            meshTermName=parseList.get(0);
    }
    
    public String getMeSHTermName() {
        return meshTermName;
    }

    private void setChildren(List<String> parseList) {
        this.children = new ArrayList<String>(parseList);
    }
    
    public List<String> getChildren() {
        return this.children;
    }

    private void setId(String followingCharacters) {
        id = followingCharacters;
    }
    
    public String getID() {
        return id;
    }

    private void setParent(String followingCharacters) {
        parent = followingCharacters;
    }
    
    public String getParent() {
        return parent;
    }

    public enum MeSHESummarySingleFields {
        Id,Parent;
    }
    
    public enum MeSHESummaryListFields {
        DS_MeshTerms, Children;
    }

    public List<MeSHESummarySingleFields> getScalarKeywords() {
        return Arrays.asList(MeSHESummarySingleFields.values());
    }

    public List<MeSHESummaryListFields> getListKeywords() {
        return Arrays.asList(MeSHESummaryListFields.values());
    }

    public void addListForKeyword(Enum keyword, List<String> parseList) {
        if (keyword instanceof MeSHESummaryListFields) {
            switch ((MeSHESummaryListFields) keyword) {
                case DS_MeshTerms:
                    this.setMeSHTerm(parseList);
                    break;
                case Children:
                    this.setChildren(parseList);
                    break;
            }
        }
    }

    public void addScalarForKeyword(Enum keyword, String followingCharacters) {
        if (keyword instanceof MeSHESummarySingleFields) {
            switch ((MeSHESummarySingleFields) keyword) {
                case Id:
                    this.setId(followingCharacters);
                    break;
                case Parent:
                    this.setParent(followingCharacters);
                    break;
            }
        }
    }

   
}
